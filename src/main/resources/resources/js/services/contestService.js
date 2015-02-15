define(requirements, function(angular, app, _) {
    return function(app) {
        app.factory('ContestService', function($http) {
            return {
                getContestsPage: function(page) {
                    return $http.get('/api/contests', {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
                countContests: function() {
                    return $http.get('/api/contestsCount').then(_.property("data"));
                },
                getContestTasks: function(contestId) {
                    return $http.get('/api/contest/' + contestId).then(_.property("data"));
                },
                countContestParticipants: function(contestId) {
                    return $http.get('api/contest/' + contestId + "/participantsCount").then(_.property("data"));
                },
                getContestParticipantsPage: function(contestId, page) {
                    return $http.get('api/contest/' + contestId + "/participants", {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
                getContestResultsPage: function(contestId, page) {
                    return $http.get('api/contest/' + contestId + "/results", {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
                getContestEditData: function(contestId) {
                    return $http.get("/api/contest/" + contestId + "/edit").then(_.property("data"));
                }
            };
        });
    };
});
