/*
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import {
	map as _map,
	each as _each,
	filter as _filter,
	flow as _flow,
	has as _has
} from "lodash";
import flow from "lodash/fp/flow";
import groupBy from "lodash/fp/groupBy";
import flatten from "lodash/fp/flatten";
import values from "lodash/fp/values";
import mapValues from "lodash/fp/mapValues";
import map from "lodash/fp/map";
import join from "lodash/fp/join";

import { services } from "app";

function ValidationException(message, data) {
	this.message = message;
	this.data = data;
	this.name = "ValidationException";
}

class ValidationService {
	constructor($injector, $q, $translate) {
		this.$injector = $injector;
		this.$q = $q;
		this.$translate = $translate;
	}

	toTranslationPromise(func) {
		return _flow(func, (key) => {
			const deferred = this.$q.defer();
			if (key) {
				this.$translate(key)
					.then(translation =>
						deferred.reject(translation), (msg) =>
						deferred.reject(`No translation available: ${msg}`));
			} else {
				deferred.resolve();
			}
			return deferred.promise;
		});
	}

	report(reporter, form, errors) {
		const fields = _map(_filter(form, (element) => _has(element, "$setValidity")),
			"name");
		_each(fields, (field) => {
			reporter.report(form[field], field, errors);
		});
	}

	transformBindingResultsIntoFormForMap(errors) {
		const deferred = this.$q.defer();

		const fieldMessageMap =
			flow(
				groupBy("field"),
				mapValues(fieldObj => _map(fieldObj, "defaultMessage"))
			)(errors);

		const shortKeyToLongKey = x => `validation.failed.${x}`;

		const messages = flow(
			values,
			flatten,
			map(shortKeyToLongKey)
		)(fieldMessageMap);

		this.$translate(messages)
			.then((translations) => {
				deferred.resolve(
					mapValues(
						flow(
							map((message) => translations[shortKeyToLongKey(message)]),
							join("\n")
						)
					)(fieldMessageMap)
				);
			});
		return deferred.promise;
	}

	postToServer(path, data, progressListener, fieldTransformationMap) {
		const deferred = this.$q.defer();
		const formData = new FormData();
		_each(data, (value, key) => {
			if (value instanceof FileList) {
				if (value.length !== 1 && value.length !== 0) {
					throw new Error("TODO: multiple file upload support");
				}
				formData.append(key, value[0]); // TODO: Multiple file upload support
			} else {
				formData.append(key, value);
			}
		});
		this.$injector.invoke(($rootScope, Upload) => {
			Upload.http({
				method: "POST",
				url: path,
				headers: {
					"X-Auth-Token": $rootScope.authToken,
					"Content-Type": undefined
				},
				data: formData,
				transformRequest: angular.identity
			})
				.progress((evt) => {
					progressListener(evt);
				})
				.success((response) => {
					if (response.status === "BINDING_ERROR") {
						if (fieldTransformationMap) {
							_each(response.fieldErrors, (error) => {
								if (fieldTransformationMap[error.field]) {
									error.field = fieldTransformationMap[error.field];
								}
							});
						}

						this.transformBindingResultsIntoFormForMap(
								response.fieldErrors)
							.then((msg) => {
								deferred.reject(
									msg
								);
							});
					} else if (response.status === "OK") {
						deferred.resolve(response);
					} else {
						throw new ValidationException(
							"Validation failed unexpectedly due to an unknown response",
							data);
					}
				})
				.error((errorData) => {
					throw new ValidationException("Validation failed unexpectedly",
						errorData);
				});
		});
		return deferred.promise;
	}
}

services
	.service("ValidationService", ValidationService);
