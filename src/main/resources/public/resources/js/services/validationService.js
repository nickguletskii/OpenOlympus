/*
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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
var Util = require("oolutil");
var _ = require("lodash");
var angular = require("angular");
var app = require("app");

function ValidationException(message, data) {
	this.message = message;
	this.data = data;
	this.name = "ValidationException";
}

angular.module("ool.services").factory("ValidationService", /*@ngInject*/ function($injector, $q, $translate) {
	return {
		report: function(reporter, form, errors) {
			_.map(_.filter(form, function(element) {
				return _.has(element, "$setValidity");
			}), function(element) {
				return element.$name;
			}).forEach(function(field) {
				reporter.report(form[field], field, errors);
			});
		},
		transformBindingResultsIntoFormForMap: function(errors) {
			var deferred = $q.defer();

			var fieldMessageMap = _.chain(errors)
				.groupBy("field")
				.indexBy((group) => group[0].field)
				.mapValues((group) =>
					_.chain(group).map(
						(validationResult) => validationResult.defaultMessage
					).value())
				.value();

			var shortKeyToLongKey = x => "validation.failed." + x;

			var messages = _.chain(fieldMessageMap).values().flatten().map(shortKeyToLongKey).value();
			$translate(messages).then(function(translations) {
				deferred.resolve(
					_.chain(fieldMessageMap).mapValues(
						(group) =>
						_.chain(group)
						.map((message) => translations[shortKeyToLongKey(message)])
						.join("\n")
						.value()
					)
					.value()
				);
			});
			return deferred.promise;
		},
		postToServer: function(path, data, progressListener) {
			var deferred = $q.defer();
			var formData = new FormData();
			_.forEach(data, (value, key) => {
				if (value instanceof FileList) {
					console.log(value.length);
					if (value.length !== 1 && value.length !== 0) {
						throw new Error("TODO: multiple file upload support");
					}
					formData.append(key, value[0]); // TODO: Multiple file upload support
				} else {
					formData.append(key, value);
				}
			});
			$injector.invoke(function($rootScope, Upload, ValidationService) {
				Upload.http({
					method: "POST",
					url: path,
					headers: {
						"X-Auth-Token": $rootScope.authToken,
						"Content-Type": undefined
					},
					data: formData,
					transformRequest: angular.identity
				}).progress(function(evt) {
					progressListener(evt);
				}).success(function(response) {
					if (response.status === "BINDING_ERROR") {
						ValidationService.transformBindingResultsIntoFormForMap(response.fieldErrors).then(function(msg) {
							deferred.reject(
								msg
							);
						});
					} else if (response.status === "OK") {
						deferred.resolve(response);
					} else {
						throw new ValidationException("Validation failed unexpectedly due to an unknown response", data);
					}
				}).error(function(errorData) {
					throw new ValidationException("Validation failed unexpectedly", errorData);
				});
			});
			return deferred.promise;
		}
	};
});
