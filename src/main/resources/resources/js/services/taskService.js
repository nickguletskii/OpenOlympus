define(requirements, function(angular, app, _) {
    return function(app) {
        app.factory('TaskService', function($http) {
            return {
                getTask: function(taskId) {
                    return $http.get("/api/task/" + taskId).then(_.property("data"));
                },
                getTaskEditData: function(taskId) {
                    return $http.get("/api/task/" + taskId + "/edit").then(_.property("data"));
                },
                getArchiveTasksPage: function(page) {
                    return $http.get('api/archive/tasks', {
                        params: {
                            page: page
                        }
                    }).then(_.property("data"));
                },
                countArchiveTasks: function() {
                    return $http.get('api/archive/tasksCount').then(_.property("data"));
                }
            };
        });
    };
});
