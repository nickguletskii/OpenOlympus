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

const controller = /*@ngInject*/ function($scope,
	$stateParams, groups, groupsCount) {

	$scope.page = $stateParams.page;

	$scope.groups = groups;
	$scope.groupsCount = groupsCount;
};
module.exports = {
	"name": "groupList",
	"url": "/groups?page",
	"templateUrl": "/partials/groups.html",
	"controller": controller,
	"params": {
		"page": "1"
	},
	"resolve": {
		"groups": function(GroupService, $stateParams) {
			return GroupService.getGroupsPage($stateParams.page);
		},
		"groupsCount": function(GroupService) {
			return GroupService.countGroups();
		}
	},
	"data": {
		canAccess: /*@ngInject*/ function(PromiseUtils, SecurityService) {
			return SecurityService.hasPermission("list_groups");
		}
	}
};
