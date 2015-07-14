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

module.exports = /*@ngInject*/ function($timeout, $q, $scope, $rootScope, $http,
    $location, $stateParams, $state, AuthenticationProvider, $translate) {
    AuthenticationProvider.update();
    $scope.showAdministratorApprovalRequiredMessage = ($stateParams.showAdministratorApprovalRequiredMessage === 'true');

    $http.get("/api/security/userStatus").success(function(data) {
        if (data != null) {
            $state.go("home");
        } else {
            $scope.logInFormVisible = true;
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

    $scope.login = function(user) {
        var deferred = $q.defer();
        AuthenticationProvider.login(user.username, user.password, user.recaptchaResponse)
            .success(function(data) {
                if (data.auth === "succeded") {
                    deferred.resolve();
                    AuthenticationProvider.update();
                    $state.go("home");
                    return;
                } else if (data.auth === "failed") {
                    var key = 'login.form.invalidUsernameOrPassword';
                    $translate([key]).then(
                        (translations) => {
                            deferred.reject({
                                "username": translations[key],
                                "password": translations[key]
                            })
                        }
                    );
                    $scope.$broadcast("formSubmissionRejected")
                    return;
                }
                if (data.recaptchaErrorCodes) {
                    $translate(data.recaptchaErrorCodes).then((translations) => {
                        deferred.reject({
                            recaptchaResponse: _.chain(data.recaptchaErrorCodes)
                                .map(((key) => "login.form.recaptchaErrors." + translations[key]))
                                .join("\n")
                                .value()
                        });
                    });
                    $scope.$broadcast("formSubmissionRejected");
                    return;
                }
                throw {
                    name: "UnknownLoginResultException",
                    message: "Unknown login result",
                    obj: data
                };
            });
        return deferred.promise;
    };
};