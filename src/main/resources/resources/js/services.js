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
'use strict';

define(['angular', 'app', 'lodash'], function(angular, app, _) {

    /* Services */

    // Demonstrate how to register services
    // In this case it is a simple value service.
    angular.module('ool.services', []).factory('AuthenticationProvider', function($rootScope, $http, $timeout) {
        var data = {
            loggedIn: false,
            user: null,
            roles: []
        };
        var poller = function() {
            $http.get('/api/security/userStatus').then(function(r) {
                data = r.data;
                $rootScope.$broadcast('securityInfoChanged');
                $timeout(poller, 60000);
            });
        };
        poller();

        var transform = function(data) {
            return $.param(data);
        };

        var update = function() {
            $http.get('/api/security/userStatus').then(function(r) {
                data = r.data;
                $rootScope.$broadcast('securityInfoChanged');
            });
        };

        return {
            data: data,
            isLoggedIn: function() {
                return data.loggedIn;
            },
            getUsername: function() {
                if (!data.user)
                    return null;
                return data.user.username;
            },
            getUser: function() {
                return data.user;
            },
            isUser: function() {
                return _.contains(data.roles, "USER");
            },
            isAdmin: function() {
                return _.contains(data.roles, "SUPERUSER");
            },
            update: update,
            login: function(username, password, recaptchaResponse) {
                return $http({
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    },
                    method: 'POST',
                    url: '/login',
                    transformRequest: transform,
                    data: {
                        'username': username,
                        'password': password,
                        'recaptchaResponse': recaptchaResponse
                    }
                });
            },
            logout: function() {
                $http({
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    },
                    method: 'POST',
                    url: '/logout',
                    transformRequest: transform,
                    data: {}
                }).success(function(x) {
                    update();
                });
            }
        };
    }).factory('ServersideFormErrorReporter', function() {

        return function(fieldMap) {
            return {
                existingErrorKeys: {},
                report: function(ctrl, field, errors) {
                    if (!ctrl) {
                        return;
                    }
                    ctrl.forceValidation = true;
                    var self = this;
                    if (!_.has(self.existingErrorKeys, field))
                        self.existingErrorKeys[field] = [];
                    var errorsToAdd = [];
                    _.forEach(errors, function(error) {
                        if (error.field === field || (!!fieldMap ? _.contains(fieldMap[error.field], field) : false)) {
                            errorsToAdd.push(error);
                        }
                    });
                    _.forEach(self.existingErrorKeys[field], function(errorKey) {
                        if (_(errorsToAdd).map(function(error) {
                                return error.defaultMessage;
                            }).contains(errorKey))
                            return;
                        ctrl.$setValidity(errorKey, true);
                    });

                    self.existingErrorKeys[field].length = 0;

                    _.forEach(errorsToAdd, function(error) {
                        ctrl.$setValidity(error.defaultMessage, false);
                        self.existingErrorKeys[field].push(error.defaultMessage);
                    });
                    ctrl.$setDirty();
                }
            };
        };
    }).factory('ValidationService', function($injector) {
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
    }).provider('modalState', function($stateProvider) {
        var provider = this;
        this.$get = function() {
            return provider;
        };
        this.state = function(stateName, options) {
            var modalInstance;
            $stateProvider.state(stateName, {
                url: options.url,
                onEnter: function($modal, $state) {
                    modalInstance = $modal.open(options);
                    modalInstance.result['finally'](function() {
                        modalInstance = null;
                        if ($state.$current.name === stateName) {
                            $state.go('^');
                        }
                    });
                },
                onExit: function() {
                    if (modalInstance) {
                        modalInstance.close();
                    }
                }
            });
        };
    });

});
