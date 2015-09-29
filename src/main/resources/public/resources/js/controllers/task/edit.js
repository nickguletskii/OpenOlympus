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

const controller = /*@ngInject*/ function($scope, $stateParams, FormDefaultHelperService, editTaskData) {
	$scope.taskId = $stateParams.taskId;
	const validationRules = {
		name: {
			required: true
		}
	};

	class TaskModificationForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "task.editForm");
			this.submitUrl = "/api/task/" + $scope.taskId + "/edit";
		}

		setUpData() {
			if (this.data) {
				return;
			}
			this.data = editTaskData;
		}

		get validationRules() {
			return validationRules;
		}
	}

	$scope.form = new TaskModificationForm();
};

module.exports = {
	"name": "taskModificationView",
	"url": "/task/{taskId:[0-9]+}/edit?contestId",
	"templateUrl": "/partials/task/edit.html",
	"controller": controller,
	"fluidContainer": true,
	"resolve": {
		"editTaskData": function(TaskService, $stateParams) {
			return TaskService.getTaskEditData($stateParams.taskId);
		}
	},
	"data": {
		canAccess: /*@ngInject*/ function(PromiseUtils, SecurityService, $refStateParams) {
			return PromiseUtils.and(
				SecurityService.hasPermission("approved"),
				SecurityService.hasGroupPermission($refStateParams.groupId, "modify")
			);
		}
	}
};
