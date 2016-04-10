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
import flow from "lodash/fp/flow";
import map from "lodash/fp/map";
import join from "lodash/fp/join";

class UnknownLoginResultException extends Error {
	constructor(data) {
		super("Unknown login result");
		this.obj = data;
	}
}

const controller = /* @ngInject*/ ($q, $scope, $stateParams,
	$state, SecurityService, $translate) => {
	$scope.showAdministratorApprovalRequiredMessage =
		(String($stateParams.showAdministratorApprovalRequiredMessage) === "true");
	SecurityService.update();
	SecurityService.update()
		.then(() => {
			if (SecurityService.isLoggedIn) {
				$state.go("home");
			} else {
				$scope.loginRequired = true;
			}
		});

	$scope.validationRules = {
		username: {
			required: true
		},
		password: {
			required: true
		}
	};

	$scope.user = {};

	$scope.login = (user) => {
		const deferred = $q.defer();
		SecurityService.login(user.username, user.password, user.recaptchaResponse)
			.then((response) => {
				const data = response.data;
				if (data.auth === "succeded") {
					deferred.resolve();
					SecurityService.update();
					$state.go("home");
					return;
				} else if (data.auth === "failed") {
					const key = "login.form.invalidUsernameOrPassword";
					$translate([key])
						.then(
							(translations) => {
								deferred.reject({
									"username": translations[key],
									"password": translations[key]
								});
							}
						);
					$scope.$broadcast("formSubmissionRejected");
					return;
				}
				if (data.recaptchaErrorCodes) {
					$translate(data.recaptchaErrorCodes)
						.then((translations) => {
							deferred.reject({
								recaptchaResponse: flow(
									map((key) =>
										`login.form.recaptchaErrors.${translations[key]}`),
									join("\n")
								)(data.recaptchaErrorCodes)
							});
						});
					$scope.$broadcast("formSubmissionRejected");
					return;
				}
				throw new UnknownLoginResultException(data);
			});
		return deferred.promise;
	};
};

export default {
	"name": "login",
	"url": "/login?failure&showAdministratorApprovalRequiredMessage",
	"templateUrl": "/partials/login.html",
	controller,
	"customWidth": "narrow",
	"params": {
		"failure": {
			value: "false"
		},
		"showAdministratorApprovalRequiredMessage": {
			value: "false",
			squash: true
		}
	}
};
