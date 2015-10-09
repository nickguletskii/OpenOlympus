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
var angular = require("angular");
angular.module("ool.services").factory("TaskService", /*@ngInject*/ function($http, $rootScope, $q) {
	return {
		getTaskIndexPage: function(taskId) {
			let deferred = $q.defer();
			$http.get(
					"/api/task/" + taskId + "/data/localisation.json", {
						acceptableFailureCodes: [404]
					}
				)
				.then((response) => {
					let path = response.data[$rootScope.currentLanguage] ||
						response.data.default;
					if (!path) {
						deferred.reject(404);
					} else {
						deferred.resolve(path);
					}
				}, (response) => deferred.reject(response.status));
			return deferred.promise;
		},
		getTaskName: function(taskId) {
			return $http.get("/api/task/" + taskId + "/name").then(_.property("data"));
		},
		getTaskEditData: function(taskId) {
			return $http.get("/api/task/" + taskId + "/edit").then(_.property("data"));
		},
		getArchiveTasksPage: function(page) {
			return $http.get("api/archive/tasks", {
				params: {
					page: page
				}
			}).then(_.property("data"));
		},
		countArchiveTasks: function() {
			return $http.get("api/archive/tasksCount").then(_.property("data"));
		}
	};
});
