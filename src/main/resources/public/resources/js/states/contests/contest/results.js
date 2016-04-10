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

import {
	map as _map
} from "lodash";

const controller =
	/* @ngInject*/
	($scope, $stateParams, users, userCount) => {
		$scope.page = $stateParams.page;

		$scope.users = users;
		$scope.tasks = _map(users[0].taskScores, (taskScore) => taskScore.first);
		$scope.userCount = userCount;
	};
export default {
	"name": "contestResults",
	"url": "/contest/{contestId:[0-9]+}/results",
	"templateUrl": "/partials/contests/contest/results.html",
	controller,
	"customWidth": "wide",
	"fluidContainer": true,
	"params": {
		"page": "1"
	},
	"resolve": {
		"users": /* @ngInject*/ (ContestService, $stateParams) =>
			ContestService.getContestResultsPage($stateParams.contestId,
				$stateParams.page),
		"userCount": (ContestService, $stateParams) =>
			ContestService.countContestParticipants($stateParams.contestId)
	},
	"data": {
		canAccess: /* @ngInject*/ ($refStateParams, PromiseUtils, SecurityService) =>
			PromiseUtils.and(
				SecurityService.hasPermission("approved"),
				PromiseUtils.or(
					SecurityService.hasContestPermission($refStateParams.contestId,
						"view_results_during_contest"),
					PromiseUtils.and(SecurityService.noCurrentContest(),
						SecurityService.isContestOver($refStateParams.contestId),
						SecurityService.hasContestPermission($refStateParams.contestId,
							"view_results_after_contest"))
				)
			)
	}
};
