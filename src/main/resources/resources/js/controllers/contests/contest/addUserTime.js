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
            $location, $stateParams, $state, AuthenticationProvider, ServersideFormErrorReporter, ValidationService, $upload, $translate) {
            $scope.$apply(function() {
                $scope.serverErrorReporter = new ServersideFormErrorReporter();
                $scope.userTimeAdditionForm.forceValidation = true;
                $scope.task = {};
                $scope.uploadProgressBarColour = function() {
                    if ($scope.uploadFailure)
                        return "danger";
                    if ($scope.uploadSuccess)
                        return "success";
                    return "info";
                };
                $scope.isFormVisible = true;

                function success(response) {
                    $scope.isSubmitting = false;
                    $scope.uploadSuccess = true;
                    $scope.uploadFailure = false;
                    $scope.processing = false;
                    $scope.$dismiss('close');
                }

                function failure() {
                    $scope.isSubmitting = false;
                    $scope.uploadSuccess = false;
                    $scope.uploadFailure = true;
                    $scope.processing = false;
                }

                function reset() {
                    $scope.isSubmitting = false;
                    $scope.uploadSuccess = false;
                    $scope.uploadFailure = false;
                    $scope.processing = false;
                }

                $scope.addUserTime = function(time) {
                    $scope.isSubmitting = true;

                    try {
                        var fd = new FormData();
                        fd.append("user", $stateParams.userId);
                        if (time)
                            fd.append("time", time * (60 * 1000));

                        ValidationService.postToServer($scope, '/api/contest/' + $stateParams.contestId + '/addUserTime', $scope.userTimeAdditionForm, fd, success, failure, reset);
                    } catch (err) {
                        reset();
                    }
                };

            });
        };
    });
