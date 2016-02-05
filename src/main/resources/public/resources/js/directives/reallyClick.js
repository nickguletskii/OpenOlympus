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
"use strict";

var angular = require("angular");

angular.module("ool.directives").directive("ngReallyClick", /*@ngInject*/ function($modal) {

	var ModalInstanceCtrl = function($scope, $modalInstance) {
		$scope.ok = function() {
			$modalInstance.close();
		};

		$scope.cancel = function() {
			$modalInstance.dismiss("cancel");
		};
	};

	return {
		restrict: "A",
		scope: {
			ngReallyClick: "&",
			item: "="
		},
		link: function(scope, element, attrs) {
			element.bind("click", function() {
				var message = attrs.ngReallyMessage;

				var modalHtml = "<div class=\"modal-body\">" + message + "</div>"; //TODO: replace with proper templating
				modalHtml += "<div class=\"modal-footer\"><button class=\"btn btn-danger\" ng-click=\"ok()\">" + attrs.ngReallyYesButton + "</button><button class=\"btn btn-default\" ng-click=\"cancel()\">{{'confirmationDialog.cancel' | translate}}</button></div>";

				var modalInstance = $modal.open({
					template: modalHtml,
					controller: ModalInstanceCtrl
				});

				modalInstance.result.then(function() {
					scope.ngReallyClick({
						item: scope.item
					});
				}, function() {});

			});

		}
	};
});
