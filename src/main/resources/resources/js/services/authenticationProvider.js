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
        app.factory('AuthenticationProvider', function($rootScope, $http, $timeout) {
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
        });
    };
});
