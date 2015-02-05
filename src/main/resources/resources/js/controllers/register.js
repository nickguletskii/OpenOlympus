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
define(['oolutil', 'lodash'],
    function(Util, _) {
        return function($timeout, $q, $scope, $rootScope, $http, googleGrecaptcha,
            $location, $stateParams, Restangular, $state, AuthenticationProvider, ServersideFormErrorReporter, ValidationService) {
            $scope.$apply(function() {
                $http.get("/api/security/userStatus").success(function(response) {
                    if (response.loggedIn) {
                        $state.go("home");
                    } else {
                        $scope.logInFormVisible = true;
                    }
                });
                $scope.serverErrorReporter = new ServersideFormErrorReporter();
                $scope.userForm.forceValidation = true;
                $scope.user = {};
                $http.get("/api/recaptchaPublicKey").success(function(recaptchaPublicKey) {
                    console.log(recaptchaPublicKey);
                    googleGrecaptcha.then(function() {
                        widgetId = grecaptcha.render(
                            document.getElementById("no-captcha"), {
                                "theme": "light",
                                "sitekey": recaptchaPublicKey,
                                "callback": function(r) {
                                    $scope.$apply(function() {
                                        $scope.user.recaptchaResponse = r;
                                        $scope.captchaErrors = null;
                                    });
                                }
                            }
                        );
                        $scope.resetCaptcha = function() {
                            grecaptcha.reset(widgetId);
                            $scope.user.recaptchaResponse = null;
                        };
                        $scope.loaded = true;
                    });
                });

                $scope.register = function(user) {
                    $http({
                        method: 'POST',
                        url: '/api/user/register',
                        data: user
                    }).then(function(response) {
                        if (response.data.status === "BINDING_ERROR") {
                            ValidationService.report($scope.serverErrorReporter, $scope.userForm, response.data.fieldErrors);
                        } else if (response.data.status === "RECAPTCHA_ERROR") {
                            $scope.captchaErrors = response.data.recaptchaErrorCodes;
                        } else {
                            $state.go("login");
                        }
                    });
                };

            });
        };
    });
