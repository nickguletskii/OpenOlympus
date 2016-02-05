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


angular.module("ool.directives").directive("asyncIf", /*@ngInject*/ function($animate, $state, $injector, $rootScope, PromiseUtils) {
	return {
		multiElement: true,
		transclude: "element",
		priority: 601,
		terminal: true,
		restrict: "A",
		$$tlb: true,
		link: function($scope, $element, $attr, ctrl, $transclude) {
			let ngIf = require("directives/angularInternal/ngIf")($element, $attr, $transclude, $animate);

			function ngIfWatchAction() {

				let allowed = $scope.$eval($attr.asyncIf, {
					PromiseUtils: PromiseUtils
				});

				if (!allowed) {
					ngIf(false);
				} else if (allowed.then) {
					allowed.then((val) => ngIf(val));
				} else {
					ngIf(allowed);
				}
			}

			$rootScope.$on("securityInfoChanged", ngIfWatchAction);
			$scope.$watch($attr.uiSref, ngIfWatchAction);
		}
	};
});
