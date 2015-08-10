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

const controller = /*@ngInject*/ function($scope, $stateParams, UserService, users, userCount) {

	var page = $stateParams.page;

	$scope.page = $stateParams.page;
	$scope.users = users;
	$scope.userCount = userCount;

	$scope.deleteUser = function(user) {
		UserService.deleteUsers([user.id]).then(updateUsers);
	};

	function updateUsers() {
		UserService.getUsersPage(page).then(function(users) {
			$scope.users = users;
		});
		UserService.countUsers().then(function(count) {
			$scope.userCount = count;
		});
	}
};

module.exports = {
	"name": "adminUsersList",
	"url": "/admin/users?page",
	"templateUrl": "/partials/admin/users.html",
	"customWidth": "wide",
	"controller": controller,
	"params": {
		"page": "1"
	},
	"resolve": {
		"users": function(UserService, $stateParams) {
			return UserService.getUsersPage($stateParams.page);
		},
		"userCount": function(UserService) {
			return UserService.countUsers();
		}
	}
};
