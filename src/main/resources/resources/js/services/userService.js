define(['oolutil', 'angular', 'app', 'lodash'], function(Util, angular, app, _) {
    return function(app) {
        app.factory('UserService', function($http) {
            return {
                patchUser: function(user, userId) {
                    var personalInfoPatchUrl = !userId ? "/api/user/personalInfo" : "/api/admin/user/" + userId + "/personalInfo";
                    return $http({
                        method: 'PATCH',
                        url: personalInfoPatchUrl,
                        data: Util.emptyToNull(user)
                    }).then(_.property("data"));
                },
                changePassword: function(passwordObj, userId) {
                    var passwordPatchUrl = !userId ? "/api/user/changePassword" : "/api/admin/user/" + userId + "/changePassword";
                    return $http({
                        method: 'PATCH',
                        url: passwordPatchUrl,
                        data: Util.emptyToNull(passwordObj)
                    }).then(_.property("data"));
                },
                countPendingUsers: function() {
                    return $http.get('api/admin/pendingUsersCount').then(_.property("data"));
                },
                getPendingUsersPage: function(page) {
                    return $http.get('api/admin/pendingUsers', {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
                countUsers: function() {
                    return $http.get('api/admin/usersCount').then(_.property("data"));
                },
                getUsersPage: function(page) {
                    return $http.get('api/admin/users', {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
                approveUsers: function(users) {
                    return $http.post("/api/admin/users/approve", users).then(_.property("data"));
                },
                deleteUsers: function(users) {
                    return $http.post("/api/admin/users/deleteUsers", users).then(_.property("data"));
                },
                countArchiveUsers: function() {
                    return $http.get('api/archive/rankCount').then(_.property("data"));
                },
                getArchiveRankPage: function(page) {
                    return $http.get('api/archive/rank', {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
            };
        });
    };
});
