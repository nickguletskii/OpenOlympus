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
"use strict";

var _ = require("lodash");

module.exports = /*@ngInject*/ function($timeout, $q, $translate, $scope, $rootScope, $http,
	$location, $stateParams, $state, AuthenticationProvider, ServersideFormErrorReporter, ValidationService) {

	$http.get("/api/security/userStatus").success(function(response) {
		if (response) {
			$state.go("home");
		} else {
			$scope.logInFormVisible = true;
		}
	});
	$scope.serverErrorReporter = new ServersideFormErrorReporter();

	$scope.user = {};
	$scope.validationRules = {
		username: {
			required: true,
			pattern: /[a-zA-Z0-9_-]+/,
			minlength: 4,
			maxlength: 16
		},
		password: {
			required: true
		},
		passwordConfirmation: {
			required: true,
			custom: ValidationService.toTranslationPromise(function(value, model) {
				if (value !== model.password) {
					return "register.form.validation.passwordsDontMatch";
				}
			})
		},
		firstNameMain: {
			required: true,
			minlength: 1
		},
		middleNameMain: {
			required: true,
			minlength: 1
		},
		lastNameMain: {
			required: true,
			minlength: 1
		},
		landline: {
			pattern: /^(\\+?[0-9]*)?$/
		},
		mobile: {
			pattern: /^(\\+?[0-9]*)?$/
		}
	};

	$scope.setRecaptchaWidgetId = function(widgetId) {
		$scope.recaptchaWidgetId = widgetId;
	};

	$scope.register = function(user) {
		var deferred = $q.defer();
		$http({
			method: "POST",
			url: "/api/user/register",
			data: user
		}).success(function(data) {
			if (data.status === "BINDING_ERROR") {
				ValidationService.transformBindingResultsIntoFormForMap(data.fieldErrors).then(function(msg) {
					deferred.reject(
						msg
					);
				});
			} else if (data.status === "RECAPTCHA_ERROR") {
				$translate(data.recaptchaErrorCodes).then((translations) => {
					deferred.reject({
						recaptchaResponse: _.chain(data.recaptchaErrorCodes)
							.map(((key) => "register.form.recaptchaErrors." + translations[key]))
							.join("\n")
							.value()
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
