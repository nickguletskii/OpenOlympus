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
        return ['$timeout', '$q', '$scope', '$rootScope', '$http', '$location',
            '$stateParams', 'Restangular',
            function($timeout, $q, $scope, $rootScope, $http, $location,
                $stateParams, Restangular) {

                $scope.$apply(function() {
                    var page = $stateParams.page;

                    $scope.page = $stateParams.page;

                    function updateUsers() {
                        Restangular.all('api/admin/pendingUsers').getList({
                            page: page
                        }).then(function(users) {
                            $scope.users = users;
                        });
                        $http.get('api/admin/pendingUsersCount').then(function(response) {
                            $scope.userCount = response.data;
                        });
                    }
                    updateUsers();

                    function handleApprovalResponse(userApprovals) {
                        Restangular.all('api/admin/pendingUsers').getList({
                            page: page
                        }).then(function(users) {
                            $scope.users = _.map(users, function(user) {
                                var oldUser = _.find($scope.users, function(oldUser) {
                                    return oldUser.id == user.id;
                                });
                                if (!!oldUser)
                                    user = oldUser;
                                var userResponse = _.find(userApprovals, function(userResponse) {
                                    return userResponse.id == user.id;
                                });
                                if (!userResponse)
                                    return user;
                                return _.assign(user, {
                                    "checked": true,
                                    "error": userResponse.statusMessage,
                                });
                            });
                            $scope.loading = false;
                        });
                        $http.get('api/admin/pendingUsersCount').then(function(response) {
                            $scope.userCount = response.data;
                        });
                    }
                    $scope.approveUsers = function() {
                        $scope.loading = true;
                        $http.post("/api/admin/users/approve", _($scope.users).filter(function(user) {
                            return user.checked;
                        }).map(function(user) {
                            return user.id;
                        }).value()).success(handleApprovalResponse);
                    };


                    $scope.retryApprovingFailedUsers = function() {
                        $scope.loading = true;
                        $http.post("/api/admin/users/approve", _($scope.users).filter(function(user) {
                            return !!user.error;
                        }).map(function(user) {
                            return user.id;
                        }).value()).success(handleApprovalResponse);
                    };


                    $scope.deleteUsersWithErrors = function() {
                        $http.post("/api/admin/users/deleteUsers", _($scope.users).filter(function(user) {
                            return user.checked && !!user.error;
                        }).map(function(user) {
                            return user.id;
                        }).value()).success(function() {
                            updateUsers();
                        });
                    };
                });
            }
        ];
    });
