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
    "name": "taskView",
    "url": "/task/{taskId:[0-9]+}?contestId",
    "templateUrl": "/partials/task",
    "controllerPath": "controllers/task",
    "controller": "TaskViewController"
}, {
    "name": "taskModificationView",
    "url": "/task/{taskId:[0-9]+}/edit?contestId",
    "templateUrl": "/partials/task/edit",
    "controllerPath": "controllers/task/edit",
    "controller": "TaskModificationController"
}, {
    "name": "archiveTaskList",
    "url": "/archive/tasks?page?taskId",
    "templateUrl": "/partials/archive/tasks",
    "controllerPath": "controllers/archive/tasks",
    "controller": "ArchiveTaskListController",
    "params": {
        "page": 1,
        "taskId": null
    }
}, {
    "name": "archiveRank",
    "url": "/archive/users?page",
    "templateUrl": "/partials/archive/users",
    "controllerPath": "controllers/archive/users",

    "controller": "ArchiveRankController",
    "params": {
        "page": 1
    }
}, {
    "name": "contestList",
    "url": "/contests?page",
    "templateUrl": "/partials/contests",
    "controllerPath": "controllers/contests",
    "controller": "ContestListController",
    "params": {
        "page": 1
    }
}, {
    "name": "contestView",
    "url": "/contest/{contestId:[0-9]+}?taskId",
    "templateUrl": "/partials/contests/contest",
    "controllerPath": "controllers/contests/contest",
    "controller": "ContestViewController",
    "params": {
        "taskId": null
    }
}, {
    "name": "contestParticipantsList",
    "url": "/contest/{contestId:[0-9]+}/participants",
    "templateUrl": "/partials/contests/contest/participants",
    "controllerPath": "controllers/contests/contest/participants",
    "controller": "ContestParticipantsListController",
    "params": {
        "page": 1
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
    }
}, {
    "name": "userSolutionList",
    "url": "/user/solutions?page",
    "templateUrl": "/partials/user/solutions",
    "controllerPath": "controllers/user/solutions",
    "controller": "UserSolutionListController",
    "params": {
        "page": 1
    }
}, {
    "name": "solutionView",
    "url": "/solution/{solutionId:[0-9]+}",
    "templateUrl": "/partials/solution",
    "controllerPath": "controllers/solution",
    "controller": "SolutionController"
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
    }
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
}];

var required = ['require', 'angular', 'lodash', 'services'];
stateList.forEach(function(state) {
    required.push(state.controllerPath);
});


define(required, function(require, angular, _) {

    var getControllerInjector = function(controllerPath) {
        return ['$scope', '$stateParams', '$injector',
            function($scope, $stateParams, $injector) {
                require([controllerPath], function(controller) {
                    $injector.invoke(controller, this, {
                        '$scope': $scope,
                        '$stateParams': $stateParams
                    });
                });
            }
        ];
    };

    var controllers = angular.module('ool.controllers', ['ool.services']);

    function processState(state) {
        controllers.controller(state.controller, getControllerInjector(state.controllerPath));
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
