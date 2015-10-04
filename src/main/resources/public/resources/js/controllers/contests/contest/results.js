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

var _ = require("lodash");

const controller = /*@ngInject*/ function($scope, $stateParams, users, userCount) {
	$scope.page = $stateParams.page;

	$scope.users = users;
	$scope.tasks = _.map(users[0].taskScores, function(taskScore) {
		return taskScore.first;
	});
	$scope.userCount = userCount;
};
module.exports = {
	"name": "contestResults",
	"url": "/contest/{contestId:[0-9]+}/results",
	"templateUrl": "/partials/contests/contest/results.html",
	"controller": controller,
	"customWidth": "wide",
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
	},
	"data": {
		canAccess: /*@ngInject*/ function($refStateParams, PromiseUtils, SecurityService) {
			return PromiseUtils.and(
				SecurityService.hasPermission("approved"),
				PromiseUtils.or(
					SecurityService.hasContestPermission($refStateParams.contestId, "view_results_during_contest"),
					PromiseUtils.and(SecurityService.noCurrentContest(),
						SecurityService.isContestOver($refStateParams.contestId),
						SecurityService.hasContestPermission($refStateParams.contestId, "view_results_after_contest"))
				)
			);
		}
	}
};
