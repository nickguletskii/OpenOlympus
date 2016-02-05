/*
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
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

const controller = /*@ngInject*/ function($scope, $rootScope, $stateParams, $http, FormDefaultHelperService) {
	$scope.getTaskSuggestions = function(name) {
		return $http.get("/api/taskCompletion", {
			params: {
				"term": name
			}
		}).then(function(response) {
			return response.data;
		});
	};

	class ContestAddTaskForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "contest.addTask");
			this.submitUrl = "/api/contest/" + $stateParams.contestId + "/addTask";
		}

		preSubmit() {
			super.preSubmit();
			this.lastTaskAdded = null;
		}

		postSubmit(response) {
			super.postSubmit(response);
			this.lastTaskAdded = {
				name: response.data.taskName
			};
			$rootScope.$emit("taskAddedToContest");
		}
	}

	$scope.form = new ContestAddTaskForm();
};

module.exports = {
	"parent": "contestView",
	"name": "contestView.addTask",
	"templateUrl": "/partials/contests/contest/addTask.html",
	"controller": controller,
	"backdrop": true,
	"data": {
		canAccess: /*@ngInject*/ function($refStateParams, PromiseUtils, SecurityService) {
			return PromiseUtils.and(
				SecurityService.hasPermission("approved"),
				SecurityService.hasContestPermission($refStateParams.contestId, "add_task"));
		}
	}
};
