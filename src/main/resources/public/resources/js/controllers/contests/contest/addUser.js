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

var angular = require("angular");
var _ = require("lodash");

const controller = /*@ngInject*/ function($scope, $rootScope, $stateParams, $http, FormDefaultHelperService) {
	$scope.getUserSuggestions = function(name) {
		return $http.get("/api/userCompletion", {
			params: {
				"term": name
			}
		}).then(function(response) {
			return response.data;
		});
	};

	class ContestAddUserForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "contest.addUser");
			this.submitUrl = "/api/contest/" + $stateParams.contestId + "/addUser";
		}

		transformDataForServer(data) {
			return {
				username: angular.isString(data.user) && data.user || data.user.username
			};
		}

		transformFailureResponse(response) {
			let x = _.mapKeys(response, (value, key) => {
				if (key === "username") {
					return "user";
				}
				return key;
			});
			return x;
		}

		preSubmit() {
			super.preSubmit();
			this.lastUserAdded = null;
		}

		postSubmit(response) {
			super.postSubmit(response);
			this.lastUserAdded = {
				username: response.data.username
			};
			$rootScope.$emit("userAddedToContest");
		}
	}

	$scope.form = new ContestAddUserForm();
};

module.exports = {
	"parent": "contestView",
	"name": "contestView.addUser",
	"templateUrl": "/partials/contests/contest/addUser.html",
	"controller": controller,
	"backdrop": true
};
