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

var NARROW = 'col-md-6 col-md-offset-3';
var WIDE = 'col-md-12 col-md-offset-0';

var stateList = [{
    "name": "forbidden",
    "url": "/forbidden",
    "templateUrl": "/partials/forbidden"
}, {
    "name": "taskView",
    "url": "/task/{taskId:[0-9]+}?contestId",
    "templateUrl": "/partials/task",
    "controllerPath": "controllers/task",
    "controller": "TaskViewController",
    "resolve": {
        "task": function(TaskService, $stateParams) {
            return TaskService.getTask($stateParams.taskId);
        }
    }
}, {
    "name": "taskModificationView",
    "url": "/task/{taskId:[0-9]+}/edit?contestId",
    "templateUrl": "/partials/task/edit",
    "controllerPath": "controllers/task/edit",
    "controller": "TaskModificationController",
    "resolve": {
        "editTaskData": function(TaskService, $stateParams) {
            return TaskService.getTaskEditData($stateParams.taskId);
        }
    }
}, {
    "name": "archiveTaskList",
    "url": "/archive/tasks?page?taskId",
    "templateUrl": "/partials/archive/tasks",
    "controllerPath": "controllers/archive/tasks",
    "controller": "ArchiveTaskListController",
    "params": {
        "page": 1,
        "taskId": null
    },
    "resolve": {
        "tasks": function(TaskService, $stateParams) {
            return TaskService.getArchiveTasksPage($stateParams.page);
        },
        "taskCount": function(TaskService, $stateParams) {
            return TaskService.countArchiveTasks();
        }
    }
}, {
    "name": "archiveRank",
    "url": "/archive/users?page",
    "templateUrl": "/partials/archive/users",
    "controllerPath": "controllers/archive/users",

    "controller": "ArchiveRankController",
    "params": {
        "page": 1
    },
    "resolve": {
        "users": function(UserService, $stateParams) {
            return UserService.getArchiveRankPage($stateParams.page);
        },
        "userCount": function(UserService, $stateParams) {
            return UserService.countArchiveUsers();
        }
    }
}, {
    "name": "contestList",
    "url": "/contests?page",
    "templateUrl": "/partials/contests",
    "controllerPath": "controllers/contests",
    "controller": "ContestListController",
    "params": {
        "page": 1
    },
    "resolve": {
        "contests": function(ContestService, $stateParams) {
            return ContestService.getContestsPage($stateParams.page);
        },
        "contestsCount": function(ContestService, $stateParams) {
            return ContestService.countContests();
        }
    }
}, {
    "name": "contestView",
    "url": "/contest/{contestId:[0-9]+}?taskId",
    "templateUrl": "/partials/contests/contest",
    "controllerPath": "controllers/contests/contest",
    "controller": "ContestViewController",
    "params": {
        "taskId": null
    },
    "resolve": {
        "tasks": function(ContestService, $stateParams) {
            return ContestService.getContestTasks($stateParams.contestId);
        }
    }
}, {
    "name": "contestParticipantsList",
    "url": "/contest/{contestId:[0-9]+}/participants?userId",
    "templateUrl": "/partials/contests/contest/participants",
    "controllerPath": "controllers/contests/contest/participants",
    "controller": "ContestParticipantsListController",
    "params": {
        "page": 1
    },
    "resolve": {
        "users": function(ContestService, $stateParams) {
            return ContestService.getContestParticipantsPage($stateParams.contestId, $stateParams.page);
        },
        "userCount": function(ContestService, $stateParams) {
            return ContestService.countContestParticipants($stateParams.contestId);
        }
    }
}, {
    "name": "contestResults",
    "url": "/contest/{contestId:[0-9]+}/results",
    "templateUrl": "/partials/contests/contest/results",
    "controllerPath": "controllers/contests/contest/results",
    "customWidth": WIDE,
    "fluidContainer": true,
    "controller": "ContestResultsController",
    "params": {
        "page": 1
    },
    "resolve": {
        "users": function(ContestService, $stateParams) {
            return ContestService.getContestResultsPage($stateParams.contestId, $stateParams.page);
        },
        "userCount": function(ContestService, $stateParams) {
            return ContestService.countContestParticipants($stateParams.contestId);
        }
    }
}, {
    "name": "userSolutionList",
    "url": "/user/solutions?page",
    "templateUrl": "/partials/user/solutions",
    "controllerPath": "controllers/user/solutions",
    "controller": "UserSolutionListController",
    "params": {
        "page": 1
    },
    "resolve": {
        "solutions": function(SolutionService, $stateParams) {
            return SolutionService.getUserSolutionsPage($stateParams.page);
        },
        "solutionCount": function(SolutionService, $stateParams) {
            return SolutionService.countUserSolutions();
        }
    }
}, {
    "name": "adminSolutionList",
    "url": "/admin/solutions?page",
    "templateUrl": "/partials/admin/solutions",
    "controllerPath": "controllers/admin/solutions",
    "controller": "AdminSolutionListController",
    "params": {
        "page": 1
    },
    "resolve": {
        "solutions": function(SolutionService, $stateParams) {
            return SolutionService.getSolutionsPage($stateParams.page);
        },
        "solutionCount": function(SolutionService, $stateParams) {
            return SolutionService.countSolutions();
        }
    }
}, {
    "name": "solutionView",
    "url": "/solution/{solutionId:[0-9]+}",
    "templateUrl": "/partials/solution",
    "controllerPath": "controllers/solution",
    "controller": "SolutionController",
    "resolve": {
        "data": function(SolutionService, $stateParams) {
            return SolutionService.getVerdicts($stateParams.solutionId);
        }
    }
}, {
    "name": "createTask",
    "url": "/archive/tasks/add",
    "templateUrl": "/partials/archive/tasks/add",
    "controllerPath": "controllers/archive/tasks/add",
    "controller": "TaskCreationController"
}, {
    "name": "createContest",
    "url": "/contests/add",
    "templateUrl": "/partials/contests/add",
    "controllerPath": "controllers/contests/add",
    "controller": "ContestCreationController"
}, {
    "name": "editContest",
    "url": "/contest/{contestId:[0-9]+}/edit",
    "templateUrl": "/partials/contests/contest/edit",
    "controllerPath": "controllers/contests/contest/edit",
    "controller": "ContestModificationController",
    "resolve": {
        "contest": function(ContestService, $stateParams) {
            return ContestService.getContestEditData($stateParams.contestId);
        }
    }
}, {
    "name": "login",
    "url": "/login?failure",
    "templateUrl": "/partials/login",
    "controllerPath": "controllers/login",
    "controller": "LoginController",
    "customWidth": NARROW,
    "params": {
        "failure": false
    }
}, {
    "name": "register",
    "url": "/register",
    "templateUrl": "/partials/register",
    "controllerPath": "controllers/register",
    "controller": "RegistrationController",
    "customWidth": NARROW
}, {
    "name": "personalInfoModificationView",
    "url": "/user/personalInfo",
    "templateUrl": "/partials/user/personalInfo",
    "controllerPath": "controllers/user/personalInfo",
    "controller": "PersonalInfoModificationController",
    "customWidth": NARROW,
    "resolve": {
        personalInfoPatchUrl: function() {
            return '/api/user/personalInfo';
        },
        passwordPatchUrl: function() {
            return '/api/user/changePassword';
        },
        requireExistingPassword: function() {
            return true;
        }
    }
}, {
    "name": "administrativePersonalInfoModificationView",
    "url": "/admin/user/{userId:[0-9]+}/personalInfo",
    "templateUrl": "/partials/user/personalInfo",
    "controllerPath": "controllers/user/personalInfo",
    "controller": "AdministrativePersonalInfoModificationController",
    "customWidth": NARROW,
    "resolve": {
        personalInfoPatchUrl: function($stateParams) {
            return '/api/admin/user/' + $stateParams.userId + '/personalInfo';
        },
        passwordPatchUrl: function($stateParams) {
            return '/api/admin/user/' + $stateParams.userId + '/changePassword';
        },
        requireExistingPassword: function() {
            return false;
        }
    }
}, {
    "name": "home",
    "url": "/",
    "templateUrl": "/partials/home",
    "controllerPath": "controllers/home",
    "controller": "HomeController",
    "type": "requireController",
    "customWidth": NARROW
}, {
    "name": "pendingUsersList",
    "url": "/admin/pendingUsers?page",
    "templateUrl": "/partials/admin/pendingUsers",
    "customWidth": WIDE,
    "controllerPath": "controllers/admin/pendingUsers",
    "controller": "PendingUsersController",
    "params": {
        "page": 1
    },
    "resolve": {
        "users": function(UserService, $stateParams) {
            return UserService.getPendingUsersPage($stateParams.page);
        },
        "userCount": function(UserService, $stateParams) {
            return UserService.countPendingUsers();
        }
    }
}, {
    "name": "adminUsersList",
    "url": "/admin/users?page",
    "templateUrl": "/partials/admin/users",
    "customWidth": WIDE,
    "controllerPath": "controllers/admin/users",
    "controller": "AdministrativeUsersController",
    "params": {
        "page": 1
    },
    "resolve": {
        "users": function(UserService, $stateParams) {
            return UserService.getUsersPage($stateParams.page);
        },
        "userCount": function(UserService, $stateParams) {
            return UserService.countUsers();
        }
    }
}, {
    "name": "administrationHub",
    "url": "/admin",
    "templateUrl": "/partials/admin",
    "controllerPath": "controllers/admin",
    "controller": "AdministrationHubController"
}];

var modalStateList = [{
    "parent": "archiveTaskList",
    "name": "archiveTaskList.rejudgeTaskConfirmation",
    "templateUrl": "/partials/archive/tasks/rejudgeTask/confirmation",
    "backdrop": true
}, {
    "parent": "archiveTaskList",
    "name": "archiveTaskList.rejudgeTaskWorking",
    "templateUrl": "/partials/archive/tasks/rejudgeTask/working",
    "controllerPath": "controllers/archive/tasks/rejudgeWorker",
    "controller": "TaskRejudgementWorkerController",
    "backdrop": true
}, {
    "parent": "archiveTaskList",
    "name": "archiveTaskList.rejudgeTaskSuccess",
    "templateUrl": "/partials/archive/tasks/rejudgeTask/success",
    "backdrop": true
}, {
    "parent": "contestView",
    "name": "contestView.rejudgeTaskConfirmation",
    "templateUrl": "/partials/archive/tasks/rejudgeTask/confirmation",
    "backdrop": true
}, {
    "parent": "contestView",
    "name": "contestView.rejudgeTaskWorking",
    "templateUrl": "/partials/archive/tasks/rejudgeTask/working",
    "controllerPath": "controllers/archive/tasks/rejudgeWorker",
    "controller": "TaskRejudgementWorkerController",
    "backdrop": true
}, {
    "parent": "contestView",
    "name": "contestView.addUser",
    "templateUrl": "/partials/contests/contest/addUser",
    "controllerPath": "controllers/contests/contest/addUser",
    "controller": "ContestUserAdditionController",
    "backdrop": true
}, {
    "parent": "contestView",
    "name": "contestView.addTask",
    "templateUrl": "/partials/contests/contest/addTask",
    "controllerPath": "controllers/contests/contest/addTask",
    "controller": "ContestTaskAdditionController",
    "backdrop": true
}, {
    "parent": "contestParticipantsList",
    "name": "contestParticipantsList.addUserTime",
    "templateUrl": "/partials/contests/contest/addUserTime",
    "controllerPath": "controllers/contests/contest/addUserTime",
    "controller": "ContestUserTimeAdditionController",
    "backdrop": true
}];

var required = ['require', 'angular', 'lodash', 'services'];
stateList.forEach(function(state) {
    required.push(state.controllerPath);
});


define(required, function(require, angular, _) {

    var getControllerInjector = function(controllerPath, resolve) {
        if (!resolve)
            resolve = [];
        return _.union(['$scope', '$stateParams', '$injector'], resolve, [
            function($scope, $stateParams, $injector) {
                var args = arguments;
                require([controllerPath], function(controller) {
                    var routerArgs = _.assign({
                        '$scope': $scope,
                        '$stateParams': $stateParams,
                    }, _.zipObject(resolve,
                        _.takeRight(args, resolve.length)
                    ));
                    $injector.invoke(controller, this, routerArgs);
                });
            }
        ]);
    };

    var controllers = angular.module('ool.controllers', ['ool.services']);

    function processState(state) {
        controllers.controller(state.controller, getControllerInjector(state.controllerPath, _.keys(state.resolve)));
    }

    _.each(stateList, processState);
    _.each(modalStateList, processState);

    controllers.config(function($stateProvider, modalStateProvider) {

        _.each(stateList, function(state) {
            $stateProvider.state(state);
        });
        _.each(modalStateList, function(state) {
            modalStateProvider.state(state.name, state);
        });
    });

});
