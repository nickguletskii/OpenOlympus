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

import angular from "angular";
import {
	directives
} from "app";

directives.directive("typeaheadBootstrap",
		/* @ngInject*/
		(FieldHelper) => ({
			require: "^formFor",
			restrict: "E",
			transclude: true,

			template: require("ng-cache!directives/typeahead/typeahead.html"),
			scope: {
				attribute: "@",
				debounce: "@?",
				disable: "=",
				help: "@?",
				tooltip: "@?",
				buttonText: "@?"
			},
			compile: (_scope, element) =>
				({
					pre: ($scope, $element, $attributes, formForController,
						$transclude) => {
						FieldHelper.manageFieldRegistration($scope, $attributes,
							formForController);
						formForController.registerSubmitButton($scope);
						$scope.selectItem = (item) => {
							$scope.model.bindable = item;
						};
						$transclude((clone, scope) => {
							scope.$$oolDirectiveIsolatedScope = $scope;
							$element.append(clone);
						});
					}
				})
		}))
	.directive("typeaheadBootstrapModel", /* @ngInject*/ ($compile) => ({
		restrict: "A",
		priority: 100000,
		terminal: true,
		compile: () => ({
			post: ($scope, $element, $attributes) => {
				$attributes.$set("ngModel",
					"$$oolDirectiveIsolatedScope.model.bindable");
				$attributes.$set("typeaheadOnSelect",
					"$$oolDirectiveIsolatedScope.selectItem($item, $model, $label)");
				$compile($element, null, 100000)($scope);
			}
		})
	}));
