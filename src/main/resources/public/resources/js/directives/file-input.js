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
var $ = require("jquery");
var angular = require("angular");

angular.module("ool.directives").directive("fileInput", /*@ngInject*/ function(FieldHelper) {
	return {
		restrict: "E",
		require: "^formFor",
		template: require("ng-cache!directives/file-input.html"),
		scope: {
			attribute: "@",
			label: "@",
			multiple: "@"
		},
		link: function($scope, $element, $attributes, formForController) {
			FieldHelper.manageFieldRegistration($scope, $attributes, formForController);
			let bindableFix = $scope.$watch(() => $scope.model.bindable, function() {
				Object.defineProperty($scope.model, "bindable", {
					set: (value) => {
						//Hide bindable from Angular's shitty built-in validation that somehow casts the files to string.
						if (angular.isString(value)) {
							return;
						}
						this.hideYoBindable = value;
					},
					get: () => this.hideYoBindable
				});
			});
			let watcher = $scope.$watch($attributes.multiple, function(val) {
				$($element[0]).find(".directive-main-element").attr("multiple", val);
			});
			$scope.$on("$destroy", () => {
				watcher();
				bindableFix();
			});
		}
	};
});
