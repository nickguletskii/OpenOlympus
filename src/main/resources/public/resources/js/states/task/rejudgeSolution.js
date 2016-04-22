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
const controller =
	/* @ngInject*/
	($scope, $http, $stateParams, $state, taskName, FormDefaultHelperService) => {
		$scope.taskName = taskName;
		$scope.solutionId = $stateParams.solutionId;

		class SolutionRejudgementForm extends FormDefaultHelperService.FormClass {
			constructor() {
				super($scope, "task.rejudgeForm");
			}

			submit() {
				this.preSubmit();
				$http.post(
						`/api/task/${$stateParams.taskId}/rejudgeSolution/${$stateParams.solutionId}`
					)
					.then(() => {
						this.resetProgress();
						this.form.$setPristine();
						this.formController.resetFields();
						this.justSubmitted = true;
					});
			}

		}
		$scope.form = new SolutionRejudgementForm();
	};

export default {
	"parent": "adminSolutionList",
	"name": "adminSolutionList.rejudgeSolution",
	"url": "/task/{taskId}/rejudgeSolution/{solutionId}",
	"templateUrl": "/partials/task/rejudge.html",
	controller,
	"backdrop": true,
	"modal": true,
	"resolve": {
		"taskName": (TaskService, $stateParams) =>
			TaskService.getTaskName($stateParams.taskId)
	},
	"data": {
		canAccess: /* @ngInject*/ ($refStateParams, SecurityService) =>
			SecurityService.hasTaskPermission($refStateParams.taskId, "rejudge")
	},
	params: {
		taskId: {
			squash: false
		}
	}
};
