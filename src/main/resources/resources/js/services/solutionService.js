define(requirements, function(angular, app, _) {
    return function(app) {
        app.factory('SolutionService', function($http) {
            return {
                countUserSolutions: function() {
                    return $http.get('api/user/solutionsCount').then(_.property("data"));
                },
                getUserSolutionsPage: function(page) {
                    return $http.get('api/user/solutions', {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
                countSolutions: function() {
                    return $http.get('api/admin/solutionsCount').then(_.property("data"));
                },
                getSolutionsPage: function(page) {
                    return $http.get('api/admin/solutions', {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
                getVerdicts: function(solutionId) {
                    return $http.get("/api/solution/" + solutionId).then(_.property("data"));
                }
            };
        });
    };
});
