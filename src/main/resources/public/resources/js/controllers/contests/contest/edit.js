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

const controller = /*@ngInject*/ function($scope, $stateParams, FormDefaultHelperService, ValidationService, contest) {
	$scope.contestId = $stateParams.contestId;
	const validationRules = require("controllers/contests/contestValidation")(ValidationService, moment);

	class ContestModificationForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "contest.editForm");
			this.submitUrl = "/api/contest/" + $stateParams.contestId + "/edit";
		}

		transformDataForServer(data) {
			data.duration *= (60 * 1000);
			return data;
		}

		preSubmit() {
			super.preSubmit();
		}

		postSubmit(response) {
			super.postSubmit(response);
		}

		setUpData() {
			if (this.data) {
				return;
			}
			this.data = {
				name: contest.name,
				duration: contest.duration / (60 * 1000),
				startTime: moment(contest.startTime).format("YYYY-MM-DDTHH:mm:ss.SSSZ")
			};
		}

		get validationRules() {
			return validationRules;
		}
	}

	$scope.form = new ContestModificationForm();
};

module.exports = {
	"name": "editContest",
	"url": "/contest/{contestId:[0-9]+}/edit",
	"templateUrl": "/partials/contests/contest/edit.html",
	"controller": controller,
	"resolve": {
		"contest": function(ContestService, $stateParams) {
			return ContestService.getContestEditData($stateParams.contestId);
		}
	},
	"data": {
		canAccess: /*@ngInject*/ function($refStateParams, SecurityService){
			return SecurityService.hasContestPermission($refStateParams.contestId, "edit");
		}
	}
};
