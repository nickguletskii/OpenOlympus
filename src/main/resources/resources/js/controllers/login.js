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
        return function($timeout, $q, $scope, $rootScope, $http,
            $location, $stateParams, Restangular, $state, AuthenticationProvider) {
            $scope.$apply(function() {
                $http.get("/api/security/userStatus").success(function(response) {
                    if (response.loggedIn) {
                        $state.go("home");
                    } else {
                        $scope.logInFormVisible = true;
                    }
                });

                function changeTooltips(authStatus) {
                    if (authStatus == "failed") {
                        $scope.usernameTooltip = getTranslation("login.wrongUsernameOrPassword");
                        $scope.passwordTooltip = getTranslation("login.wrongUsernameOrPassword");
                    } else {
                        $scope.usernameTooltip = null;
                        $scope.passwordTooltip = null;
                    }
                }

                $scope.login = function() {

                    AuthenticationProvider.login($scope.username, $scope.password).success(function(response) {
                        if (response.auth === "succeded") {
                            AuthenticationProvider.update();
                            $state.go("home");
                        } else if (response.auth === "failed") {
                            $scope.authError = true;
                            changeTooltips(response.auth);
                        }
                    });

                };
                $scope.username = "";
                $scope.password = "";
                changeTooltips("none");
            });
        };
    });
