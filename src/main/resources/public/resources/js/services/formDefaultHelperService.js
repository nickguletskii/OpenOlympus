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
var angular = require("angular");


angular.module("ool.services").factory("FormDefaultHelperService", /*@ngInject*/ function() {
	return {
		setup: function($scope, id, submittedMessageId) {
			let formId = id + "Form";
			let formControllerId = id + "FormController";
			if (angular.isUndefined($scope[id])) {
				$scope[id] = {};
			}

			let origValue = angular.copy($scope[id]);

			if (angular.isUndefined($scope[formControllerId])) {
				$scope[formControllerId] = {};
			}

			$scope.submitting = false;
			$scope.uploadProgress = null;
			$scope[submittedMessageId] = null;

			$scope.$watch(() => $scope[formId].$dirty, function() {
				if ($scope[formId].$dirty) {
					$scope[submittedMessageId] = null;
				}
			});

			$scope.formHelper = {
				startUpload: function() {
					$scope.submitting = true;
					$scope.uploadProgress = null;
					$scope[submittedMessageId] = null;
				},
				finishUpload: function(message) {
					$scope[submittedMessageId] = message;
					$scope.uploadProgress = null;
					$scope.submitting = false;
					$scope[id] = angular.copy(origValue);
					$scope[formId].$setPristine();
					$scope[formControllerId].resetFields();
				},
				progressReporter: function(progress) {
					$scope.progress = progress;
				}
			};
		}
	};
});
