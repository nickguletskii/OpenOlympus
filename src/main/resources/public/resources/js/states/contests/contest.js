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

import moment from "moment";

const controller =
	/* @ngInject*/
	($timeout, $scope, $rootScope, $stateParams, contest, datetime, ContestService) => {
		function updateTasks() {
			ContestService.getContestInfo($stateParams.contestId)
				.then((contestInfo) => {
					$scope.contest = contestInfo;
				});
		}
		$scope.contestId = $stateParams.contestId;
		$scope.contest = contest;

		$scope.removeTask = (task) => {
			ContestService.removeTask($stateParams.contestId, task.id)
				.then(updateTasks);
		};

		let prevValue = null;

		const tick = () => {
			$scope.$apply(() => {
				const end = moment(contest.timings.endTimeIncludingTimeExtensions || contest.timings.endTime);
				console.log(datetime.currentServerTime(), end);
				if (datetime.currentServerTime()
					.isBefore(moment(contest.timings.startTime))) {
					$scope.contest.countdown = datetime.timeTo(contest.timings.startTime);
					$scope.contest.countdownKey = "contest.timeToStart";
					prevValue = "timeToStart";
				} else if (datetime.currentServerTime()
					.isBefore(end)) {
					$scope.contest.countdown =
						datetime.timeTo(end);
					$scope.contest.countdownKey = "contest.timeToEnd";
					if (prevValue === "timeToStart") {
						updateTasks();
					}
					prevValue = "timeToEnd";
				} else {
					$scope.contest.countdownKey = "contest.ended";
				}
			});
			$timeout(tick, 500);
		};
		$timeout(tick, 0);
		$rootScope.$on("taskAddedToContest", () => {
			updateTasks();
		});
	};

export default {
	"name": "contestView",
	"url": "/contest/{contestId:[0-9]+}?taskId",
	"templateUrl": "/partials/contests/contest.html",
	controller,
	"params": {
		"taskId": null
	},
	"resolve": {
		"contest": /* @ngInject*/ (ContestService, $stateParams) =>
			ContestService.getContestInfo($stateParams.contestId)
	},
	"data": {
		canAccess: /* @ngInject*/ (PromiseUtils, SecurityService, $refStateParams) =>
			PromiseUtils.and(
				SecurityService.hasPermission("approved"),
				SecurityService.hasContestPermission($refStateParams.contestId,
					"participate"))
	}
};
