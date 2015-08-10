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

var moment = require("moment");
module.exports = /*@ngInject*/ function($timeout, ValidationService, $scope, $rootScope, $http,
	$location, $stateParams, $state, AuthenticationProvider, FormDefaultHelperService) {

	function getSuggestedContestStartTime() {
		let timeSuggestion = moment().startOf("minute");
		let upperMinutes = Math.floor((timeSuggestion.minute() + 10) / 10) * 10;
		timeSuggestion = timeSuggestion.add("minutes",
			upperMinutes - timeSuggestion.minute());
		return timeSuggestion;
	}

	const validationRules = require("controllers/contests/contestValidation")(ValidationService, moment);

	class ContestCreationForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "contest.createForm");
			this.submitUrl = "/api/contests/create";
		}

		transformDataForServer(data) {
			data.duration *= (60 * 1000);
			return data;
		}

		preSubmit() {
			super.preSubmit();
			this.lastContestId = null;
		}

		postSubmit(response) {
			super.postSubmit(response);
			this.lastContestId = response.data.id;
		}

		setUpData() {
			this.data = {
				startTime: getSuggestedContestStartTime().format("YYYY-MM-DDTHH:mm:ss.SSSZ")
			};
		}

		get validationRules() {
			return validationRules;
		}
	}

	$scope.form = new ContestCreationForm();
};
