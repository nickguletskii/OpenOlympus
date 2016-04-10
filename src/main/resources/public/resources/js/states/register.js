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
	defaults as _defaults
} from "lodash";
import userValidation from "states/user/userInfoValidation";
import map from "lodash/fp/map";
import flow from "lodash/fp/flow";
import join from "lodash/fp/join";

const controller = /* @ngInject*/ ($q, $translate, $scope, $http, $state,
	ServersideFormErrorReporter, ValidationService, SecurityService) => {
	SecurityService.update()
		.then(() => {
			if (SecurityService.isLoggedIn) {
				$state.go("home");
			} else {
				$scope.logInFormVisible = true;
			}
		});
	$scope.serverErrorReporter = new ServersideFormErrorReporter();

	$scope.user = {};
	$scope.validationRules = _defaults({
		password: {
			required: true
		},
		passwordConfirmation: {
			required: true,
			custom: ValidationService.toTranslationPromise((value, model) => {
				if (value !== model.password) {
					return "register.form.validation.passwordsDontMatch";
				}
				return null;
			})
		}
	}, userValidation);

	$scope.setRecaptchaWidgetId = (widgetId) => {
		$scope.recaptchaWidgetId = widgetId;
	};

	$scope.register = (user) => {
		const deferred = $q.defer();
		$http({
			method: "POST",
			url: "/api/user/register",
			data: user
		})
			.then((response) => {
				const data = response.data;

				if (data.status === "BINDING_ERROR") {
					ValidationService.transformBindingResultsIntoFormForMap(data.fieldErrors)
						.then((msg) => {
							deferred.reject(
								msg
							);
						});
				} else if (data.status === "RECAPTCHA_ERROR") {
					$translate(data.recaptchaErrorCodes)
						.then((translations) => {
							deferred.reject({
								recaptchaResponse: flow(
									map((key) =>
										`register.form.recaptchaErrors.${translations[key]}`),
									join("\n")
								)(data.recaptchaErrorCodes)
							});
						});
				} else {
					deferred.resolve();
					$state.go("login", {
						showAdministratorApprovalRequiredMessage: true
					});
					return;
				}
				$scope.$broadcast("formSubmissionRejected");
			});

		return deferred.promise;
	};
};

export default {
	"name": "register",
	"url": "/register",
	"templateUrl": "/partials/register.html",
	controller,
	"customWidth": "narrow"
};
