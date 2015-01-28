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
            $location, $stateParams, Restangular, $state, AuthenticationProvider, ServersideFormErrorReporter, ValidationService, $upload) {
            $scope.$apply(function() {
                $scope.editorOptions = {
                    lineWrapping: true,
                    lineNumbers: true,
                    mode: 'markdown'
                };
                $http.get("/api/task/" + $stateParams.taskId + "/edit").success(
                    function(response) {
                        $scope.task.name = response.name;
                        $scope.task.published = response.published;
                        $scope.task.descriptionText = response.descriptionText;
                    });
                $scope.serverErrorReporter = new ServersideFormErrorReporter();
                $scope.taskModificationForm.forceValidation = true;
                $scope.task = {};
                $scope.uploadProgressBarColour = function() {
                    if ($scope.uploadFailure)
                        return "danger";
                    if ($scope.uploadSuccess)
                        return "success";
                    return "info";
                };
                $scope.isFormVisible = true;

                function success() {
                    $scope.isFormVisible = false;
                    $scope.uploadSuccess = true;
                    $scope.uploadFailure = false;
                    $scope.processing = false;
                }

                function failure() {
                    $scope.isFormVisible = false;
                    $scope.uploadSuccess = false;
                    $scope.uploadFailure = true;
                    $scope.processing = false;
                }

                function reset() {
                    $scope.isFormVisible = true;
                    $scope.uploadSuccess = false;
                    $scope.uploadFailure = false;
                    $scope.processing = false;
                }

                $scope.modifyTask = function(task) {
                    $scope.isFormVisible = false;

                    try {
                        var fd = new FormData();

                        _.forEach(task, function(value, key) {
                            fd.append(key, value);
                        });

                        if (!!task.judgeFile)
                            fd.append("judgeFile", task.judgeFile[0]);
                        if (!!task.descriptionFile)
                            fd.append("descriptionFile", task.descriptionFile[0]);
                        ValidationService.postToServer($scope, '/api/task/' + $stateParams.taskId + '/edit', fd, success, failure, reset);
                    } catch (err) {
                        reset();
                    }
                };
            });
        };
    });
