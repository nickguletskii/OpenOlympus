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
        return function($timeout, $q, $scope, $rootScope, $http, $location,
            $stateParams, UserService, users, userCount) {

            $scope.$apply(function() {
                var page = $stateParams.page;

                $scope.page = $stateParams.page;

                $scope.users = users;
                $scope.userCount = userCount;

                function updateUsers() {
                    UserService.getPendingUsersPage(page).then(function(users) {
                        $scope.users = users;
                    });
                    UserService.countPendingUsers().then(function(count) {
                        $scope.userCount = count;
                    });
                }

                function handleApprovalResponse(userApprovals) {
                    UserService.getPendingUsersPage(page).then(function(users) {
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
                    UserService.countPendingUsers().then(function(count) {
                        $scope.userCount = count;
                    });
                }
                $scope.approveUsers = function() {
                    $scope.loading = true;
                    UserService.approveUsers(_($scope.users).filter(function(user) {
                        return user.checked;
                    }).map(function(user) {
                        return user.id;
                    }).value()).then(handleApprovalResponse);
                };


                $scope.retryApprovingFailedUsers = function() {
                    $scope.loading = true;
                    UserService.approveUsers(_($scope.users).filter(function(user) {
                        return !!user.error;
                    }).map(function(user) {
                        return user.id;
                    }).value()).then(handleApprovalResponse);
                };


                $scope.deleteUsersWithErrors = function() {
                    UserService.deleteUsers(_($scope.users).filter(function(user) {
                        return user.checked && !!user.error;
                    }).map(function(user) {
                        return user.id;
                    }).value()).then(function() {
                        updateUsers();
                    });
                };
            });
        };
    });
