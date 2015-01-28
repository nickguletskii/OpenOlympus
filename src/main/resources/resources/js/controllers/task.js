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
            $location, $stateParams, Restangular, $state, AuthenticationProvider, ServersideFormErrorReporter, ValidationService, $upload, $sce) {
            $scope.$apply(function() {
                $http.get("/api/task/" + $stateParams.taskId).success(function(response) {
                    console.log(response);
                    $scope.name = response.name;
                    $scope.description = $sce.trustAsHtml(response.description);
                });
                $scope.serverErrorReporter = new ServersideFormErrorReporter();
                $scope.solutionSubmissionForm.forceValidation = true;
                $scope.task = {};
                $scope.isFormVisible = true;

                $scope.uploadProgressBarColour = function() {
                    if ($scope.uploadFailure)
                        return "danger";
                    if ($scope.uploadSuccess)
                        return "success";
                    return "info";
                };
                $scope.isFormVisible = true;

                function success() {
                    $state.go("solutionView", {
                        'solutionId': response.data.id
                    });
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
                $scope.reset = reset;

                $scope.submitSolution = function(solution) {
                    $scope.isFormVisible = false;
                    try {
                        var fd = new FormData();

                        _.forEach(solution, function(value, key) {
                            fd.append(key, value);
                        });

                        if (!!solution.solutionFile)
                            fd.append("solutionFile", solution.solutionFile[0]);

                       ValidationService. ValidationService.postToServer($scope, '/api/task/' + $stateParams.taskId + '/submitSolution', fd, success, failure, reset);
                    } catch (err) {
                        reset();
                    }
                };

            });
        };
    });
