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
