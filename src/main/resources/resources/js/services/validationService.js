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
define(['oolutil', 'angular', 'app', 'lodash'], function(Util, angular, app, _) {
    return function(app) {
        app.factory('ValidationService', function($injector) {
            return {
                report: function(reporter, form, errors) {
                    _.map(_.filter(form, function(element) {
                        return _.has(element, "$setValidity");
                    }), function(element) {
                        return element.$name;
                    }).forEach(function(field) {
                        reporter.report(form[field], field, errors);
                    });
                },
                postToServer: function($scope, path, form, fd, success, failure, reset) {
                    $injector.invoke(function($rootScope, $upload, ValidationService) {
                        $upload.http({
                            method: 'POST',
                            url: path,
                            headers: {
                                'X-Auth-Token': $rootScope.authToken,
                                'Content-Type': undefined
                            },
                            data: fd,
                            transformRequest: angular.identity
                        }).progress(function(evt) {
                            $scope.uploadProgress = Math.round(1000.0 * evt.loaded / evt.total) / 10.0;
                            if (evt.loaded === evt.total) {
                                $scope.processing = true;
                            }
                        }).success(function(response) {
                            if (response.status === "BINDING_ERROR") {
                                ValidationService.report($scope.serverErrorReporter, form, response.fieldErrors);
                                reset(response);
                            } else if (response.status === "OK") {
                                success(response);
                            } else {
                                failure(response);
                            }
                        }).error(function(data) {
                            reset();
                        });
                    });
                }
            };
        });
    };
});
