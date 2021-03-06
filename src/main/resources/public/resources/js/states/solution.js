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

import Util from "oolutil";
import {
	every as _every
} from "lodash";
const controller =
	/* @ngInject*/
	($timeout, $scope, $stateParams, SolutionService, data) => {
		const DELAY_MIN = 100;
		const DELAY_STEP = 100;
		const DELAY_MAX = 1000;
		const FINISHED_MULTIPLIER = 10;
		let delay = DELAY_MIN;

		const setData = (solution) => {
			if (!Util.equalsWithoutAngular($scope.solution, solution)) {
				$scope.solution = solution;
				delay = DELAY_MIN;
			}
			return solution;
		};

		const update = (callback) => {
			SolutionService.getVerdicts($stateParams.solutionId)
				.then(setData)
				.then(callback);
		};

		$scope.isTested = (verdict) =>
			(verdict.status !== "being_tested" &&
				verdict.status !== "waiting");
		const multiplier = () => {
			if (!$scope.solution) {
				return 1;
			}
			if (_every($scope.solution.verdicts, $scope.isTested)) {
				return FINISHED_MULTIPLIER;
			}
			return 1;
		};
		setData(data);

		let promise;

		const poller = () => {
			delay = Math.min(delay + multiplier() * DELAY_STEP, multiplier() *
				DELAY_MAX);
			update(() => {
				promise = $timeout(poller, delay);
			});
		};

		promise = $timeout(poller, delay);
		$scope.$on("$destroy", () => {
			$timeout.cancel(promise);
		});

		// TODO: Websocket integration: server should push verdict updates.
	};

export default {
	"name": "solutionView",
	"url": "/solution/{solutionId:[0-9]+}",
	"templateUrl": "/partials/solution.html",
	controller,
	"resolve": {
		"data": /* @ngInject*/ (SolutionService, $stateParams) =>
			SolutionService.getVerdicts($stateParams.solutionId)
	}
};
