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

var angular = require("angular");
var _ = require("lodash");
require("services");

var NARROW = 'col-md-6 col-md-offset-3';
var WIDE = 'col-md-12 col-md-offset-0';

var stateList = [{
    "name": "forbidden",
    "url": "/forbidden",
    "templateUrl": "/partials/forbidden"
}, {
    "name": "eula",
    "url": "/eula",
    "templateUrl": "/partials/eula"
}, {
    "name": "taskView",
    "url": "/task/{taskId:[0-9]+}?contestId",
    "templateUrl": "/partials/task",
    "controller": require("controllers/task"),
    "resolve": {
        "task": function(TaskService, $stateParams) {
            return TaskService.getTask($stateParams.taskId);
        }
    }
}, {
    "name": "taskModificationView",
    "url": "/task/{taskId:[0-9]+}/edit?contestId",
    "templateUrl": "/partials/task/edit",
    "controller": require("controllers/task/edit"),
    "fluidContainer": true,
    "resolve": {
        "editTaskData": function(TaskService, $stateParams) {
            return TaskService.getTaskEditData($stateParams.taskId);
        }
    }
}, {
    "name": "archiveTaskList",
    "url": "/archive/tasks?page?taskId",
    "templateUrl": "/partials/archive/tasks",
    "controller": require("controllers/archive/tasks"),
    "params": {
        "page": "1",
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
    "controller": require("controllers/archive/users"),

    "params": {
        "page": "1"
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
    "controller": require("controllers/contests"),
    "params": {
        "page": "1"
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
    "controller": require("controllers/contests/contest"),
    "params": {
        "taskId": null
    },
    "resolve": {
        "contest": function(ContestService, $stateParams) {
            return ContestService.getContestInfo($stateParams.contestId);
        }
    }
}, {
    "name": "contestParticipantsList",
    "url": "/contest/{contestId:[0-9]+}/participants?userId",
    "templateUrl": "/partials/contests/contest/participants",
    "controller": require("controllers/contests/contest/participants"),
    "params": {
        "page": "1"
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
    "controller": require("controllers/contests/contest/results"),
    "customWidth": WIDE,
    "fluidContainer": true,
    "params": {
        "page": "1"
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
    "controller": require("controllers/user/solutions"),
    "params": {
        "page": "1"
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
    "controller": require("controllers/admin/solutions"),
    "params": {
        "page": "1"
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
    "controller": require("controllers/solution"),
    "resolve": {
        "data": function(SolutionService, $stateParams) {
            return SolutionService.getVerdicts($stateParams.solutionId);
        }
    }
}, {
    "name": "createTask",
    "url": "/archive/tasks/add",
    "templateUrl": "/partials/archive/tasks/add",
    "controller": require("controllers/archive/tasks/add")
}, {
    "name": "createContest",
    "url": "/contests/add",
    "templateUrl": "/partials/contests/add",
    "controller": require("controllers/contests/add")
}, {
    "name": "editContest",
    "url": "/contest/{contestId:[0-9]+}/edit",
    "templateUrl": "/partials/contests/contest/edit",
    "controller": require("controllers/contests/contest/edit"),
    "resolve": {
        "contest": function(ContestService, $stateParams) {
            return ContestService.getContestEditData($stateParams.contestId);
        }
    }
}, {
    "name": "login",
    "url": "/login?failure",
    "templateUrl": "/partials/login",
    "controller": require("controllers/login"),
    "customWidth": NARROW,
    "params": {
        "failure": "false",
        "showAdministratorApprovalRequiredMessage": "false"
    }
}, {
    "name": "register",
    "url": "/register",
    "templateUrl": "/partials/register",
    "controller": require("controllers/register"),
    "customWidth": NARROW
}, {
    "name": "personalInfoModificationView",
    "url": "/user/personalInfo",
    "templateUrl": "/partials/user/personalInfo",
    "controller": require("controllers/user/personalInfo"),
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
    "controller": require("controllers/user/personalInfo"),
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
    "controller": require("controllers/home"),
    "type": "requireController",
    "customWidth": NARROW
}, {
    "name": "pendingUsersList",
    "url": "/admin/pendingUsers?page",
    "templateUrl": "/partials/admin/pendingUsers",
    "customWidth": WIDE,
    "controller": require("controllers/admin/pendingUsers"),
    "params": {
        "page": "1"
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
    "controller": require("controllers/admin/users"),
    "params": {
        "page": "1"
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
    "controller": require("controllers/admin")
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
    "controller": require("controllers/archive/tasks/rejudgeWorker"),
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
    "controller": require("controllers/archive/tasks/rejudgeWorker"),
    "backdrop": true
}, {
    "parent": "contestView",
    "name": "contestView.addUser",
    "templateUrl": "/partials/contests/contest/addUser",
    "controller": require("controllers/contests/contest/addUser"),
    "backdrop": true
}, {
    "parent": "contestView",
    "name": "contestView.addTask",
    "templateUrl": "/partials/contests/contest/addTask",
    "controller": require("controllers/contests/contest/addTask"),
    "backdrop": true
}, {
    "parent": "contestParticipantsList",
    "name": "contestParticipantsList.addUserTime",
    "templateUrl": "/partials/contests/contest/addUserTime",
    "controller": require("controllers/contests/contest/addUserTime"),
    "backdrop": true
}];

var controllers = angular.module('ool.controllers', ['ool.services']);

controllers.config( /*@ngInject*/ function($stateProvider, modalStateProvider) {

    _.each(stateList, function(state) {
        $stateProvider.state(state);
    });
    _.each(modalStateList, function(state) {
        modalStateProvider.state(state.name, state);
    });
});

module.exports = controllers;
