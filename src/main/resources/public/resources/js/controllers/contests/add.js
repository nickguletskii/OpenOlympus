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
var moment = require("moment");
var angular = require("angular");
module.exports = /*@ngInject*/ function($timeout, $q, $scope, $rootScope, $http,
	$location, $stateParams, $state, AuthenticationProvider, ServersideFormErrorReporter, ValidationService) {

	function getSuggestedContestStartTime() {
		let timeSuggestion = moment().startOf("minute");
		let upperMinutes = Math.floor((timeSuggestion.minute() + 10) / 10) * 10;
		timeSuggestion = timeSuggestion.add("minutes",
			upperMinutes - timeSuggestion.minute());
		return timeSuggestion;
	}

	$scope.contest = {
		startTime: getSuggestedContestStartTime().format("YYYY-MM-DDTHH:mm:ss.SSSZ")
	};
	$scope.lastContestId = null;
	$scope.progress = {};
	$scope.validationRules = require("controllers/contests/contestValidation")($q, moment);
	$scope.createContest = function(newContest_) {
		var newContest = angular.copy(newContest_);
		newContest.duration *= (60 * 1000);
		$scope.lastContestId = null;
		$scope.submitting = true;
		$scope.uploadProgress = null;
		return ValidationService.postToServer("/api/contests/create", newContest, (progress) => $scope.progress = progress)
			.then((response) => {
				$scope.uploadProgress = null;
				$scope.submitting = false;
				$scope.contest = {};
				$scope.lastContestId = response.data.id;
			});
	};
};
