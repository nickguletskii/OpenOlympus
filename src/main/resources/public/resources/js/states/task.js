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
	($scope, $state, $stateParams, $location, FormDefaultHelperService, taskName,
		taskIndexPage) => {
		$scope.name = taskName;

		$scope.baseUrl =
			`${$location.protocol()}://${$location.host()}:${$location.port()}`;

		$scope.indexPath = taskIndexPage;

		$scope.taskId = $stateParams.taskId;

		class SolutionSubmissionForm extends FormDefaultHelperService.FormClass {
			constructor() {
				super($scope, "task.createForm");
				this.submitUrl = `/api/task/${$stateParams.taskId}/submitSolution`;
			}

			postSubmit(response) {
				super.postSubmit(response);
				$state.go("solutionView", {
					"solutionId": response.data.id
				});
			}

		}

		$scope.solutionSubmissionForm = new SolutionSubmissionForm();
	};

export default {
	"name": "taskView",
	"url": "/task/{taskId:[0-9]+}?contestId",
	"templateUrl": "/partials/task.html",
	controller,
	"resolve": {
		"taskIndexPage": /* @ngInject*/ (TaskService, $stateParams, $q) => {
			const deferred = $q.defer();
			TaskService.getTaskIndexPage($stateParams.taskId)
				.then(x => deferred.resolve(x), (error) => {
					if (error === 404) {
						deferred.resolve(null);
					}
					deferred.reject(error);
				});
			return deferred.promise;
		},
		"taskName": /* @ngInject*/ (TaskService, $stateParams) =>
			TaskService.getTaskName($stateParams.taskId)
	}
};
