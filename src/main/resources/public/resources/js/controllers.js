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
"use strict";

var angular = require("angular");
var _ = require("lodash");
require("services");

const WIDTH_CLASS_MAP = {
	"narrow": "col-md-6 col-md-offset-3",
	"wide": "col-md-12 col-md-offset-0"
};

var stateList = [{
		"name": "forbidden",
		"url": "/forbidden",
		"templateUrl": "/partials/forbidden.html"
	}, {
		"name": "eula",
		"url": "/eula",
		"templateUrl": "/partials/eula.html"
	}, {
		"name": "home",
		"url": "/",
		"templateUrl": "/partials/home.html"
	}, {
		"name": "administrationHub",
		"url": "/admin",
		"templateUrl": "/partials/admin.html"
	},
	require("controllers/task"),
	require("controllers/task/edit"),
	require("controllers/archive/tasks"),
	require("controllers/archive/users"),
	require("controllers/contests"),
	require("controllers/contests/contest"),
	require("controllers/contests/contest/participants"),
	require("controllers/contests/contest/results"),
	require("controllers/user/solutions"),
	require("controllers/admin/solutions"),
	require("controllers/solution"),
	require("controllers/archive/tasks/add"),
	require("controllers/contests/add"),
	require("controllers/contests/contest/edit"),
	require("controllers/login"),
	require("controllers/register"),
	require("controllers/user/personalInfo"),
	require("controllers/admin/pendingUsers"),
	require("controllers/admin/users")
];

var modalStateList = [{
		"parent": "archiveTaskList",
		"name": "archiveTaskList.rejudgeTaskConfirmation",
		"templateUrl": "/partials/archive/tasks/rejudgeTask/confirmation.html",
		"backdrop": true
	}, {
		"parent": "archiveTaskList",
		"name": "archiveTaskList.rejudgeTaskSuccess",
		"templateUrl": "/partials/archive/tasks/rejudgeTask/success.html",
		"backdrop": true
	}, {
		"parent": "contestView",
		"name": "contestView.rejudgeTaskConfirmation",
		"templateUrl": "/partials/archive/tasks/rejudgeTask/confirmation.html",
		"backdrop": true
	},
	require("controllers/archive/tasks/rejudgeWorker"),
	require("controllers/contests/contest/addUser"),
	require("controllers/contests/contest/addTask"),
	require("controllers/contests/contest/addUserTime")
];

var controllers = angular.module("ool.controllers", ["ool.services"]);

controllers.config( /*@ngInject*/ function($stateProvider, modalStateProvider) {

	_.each(stateList, function(state) {
		var stateConfig = _.clone(state);
		if (state.width && WIDTH_CLASS_MAP[state.width]) {
			stateConfig.width = WIDTH_CLASS_MAP[state.width];
		}
		$stateProvider.state(stateConfig);
	});
	_.each(modalStateList, function(state) {
		var stateConfig = _.clone(state);
		if (state.width && WIDTH_CLASS_MAP[state.width]) {
			stateConfig.width = WIDTH_CLASS_MAP[state.width];
		}
		modalStateProvider.state(stateConfig.name, stateConfig);
	});
});

module.exports = controllers;
