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
