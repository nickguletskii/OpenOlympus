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

module.exports = function($timeout, $q, $scope, $rootScope, $http, googleGrecaptcha,
    $location, $stateParams, $state, AuthenticationProvider, ServersideFormErrorReporter, ValidationService) {
    $http.get("/api/security/userStatus").success(function(response) {
        if (response.loggedIn) {
            $state.go("home");
        } else {
            $scope.logInFormVisible = true;
        }
    });
    $scope.serverErrorReporter = new ServersideFormErrorReporter();
    ;
    $scope.user = {};
    $http.get("/api/recaptchaPublicKey").success(function(recaptchaPublicKey) {
        if (_.isEmpty(recaptchaPublicKey)) {
            $scope.captchaDisabled = true;
            $scope.loaded = true;
            return;
        }
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
        if (!($scope.eulaAccepted))
            return;
        $http({
            method: 'POST',
            url: '/api/user/register',
            data: user
        }).success(function(data) {
            if (data.status === "BINDING_ERROR") {
                ValidationService.report($scope.serverErrorReporter, $scope.userForm, data.fieldErrors);
            } else if (data.status === "RECAPTCHA_ERROR") {
                $scope.captchaErrors = data.recaptchaErrorCodes;
            } else {
                $state.go("login", {
                    showAdministratorApprovalRequiredMessage: true
                });
            }
        });
    };
};
