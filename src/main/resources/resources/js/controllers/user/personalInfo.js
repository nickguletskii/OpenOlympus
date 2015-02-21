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
            $location, $stateParams, $state, AuthenticationProvider, ServersideFormErrorReporter, ValidationService,
            personalInfoPatchUrl, passwordPatchUrl, requireExistingPassword, UserService) {
            $scope.$apply(function() {
                $scope.serverErrorReporter = new ServersideFormErrorReporter();
                $scope.userForm.forceValidation = true;
                $scope.user = {};
                $scope.password = {};
                $scope.requireExistingPassword = requireExistingPassword;
                $scope.$watch("userForm.$pristine", function(newValue, oldValue) {
                    if (!newValue)
                        $scope.updatedUser = false;
                });

                $scope.$watch("passwordForm.$pristine", function(newValue, oldValue) {
                    if (!newValue)
                        $scope.updatedPassword = false;
                });

                $http.get(personalInfoPatchUrl).success(function(user) {
                    $scope.user = user;
                    $scope.loaded = true;
                });

                $scope.patchUser = function(user) {
                    UserService.patchUser(user, $stateParams.userId).then(function(data) {
                        if (data.status === "BINDING_ERROR") {
                            ValidationService.report($scope.serverErrorReporter, $scope.userForm, data.fieldErrors);
                        } else {
                            $scope.updatedUser = true;
                            $scope.userForm.$setPristine();
                        }
                    });
                };

                $scope.changePassword = function(passwordObj) {
                    UserService.changePassword(passwordObj, $stateParams.userId).then(function(data) {
                        if (data.status === "BINDING_ERROR") {
                            ValidationService.report($scope.serverErrorReporter, $scope.passwordForm, data.fieldErrors);
                        } else {
                            $scope.updatedPassword = true;
                            $scope.passwordForm.$setPristine();
                        }
                    });
                };

            });
        };
    });
