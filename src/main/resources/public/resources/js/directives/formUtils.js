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
angular.module("ool.directives").directive("formAutoconfig", /*@ngInject*/ function($compile) {
	return {
		restrict: "A",
		priority: 100000,
		terminal: true,
		scope: false,
		compile: function() {
			return {
				post: function($scope, $element, $attributes) {
					let root = $attributes.formAutoconfig;
					$attributes.$set("name", root + ".form");
					$attributes.$set("controller", root + ".formController");
					$attributes.$set("formFor", root + ".data");
					$attributes.$set("validationRules", root + ".validationRules");
					$attributes.$set("submitWith", root + ".submit()");
					$attributes.$set("disable", root + ".submitting");
					$attributes.$set("disable", root + ".submitting");
					$compile($element, null, 100000)($scope);
				}
			};
		}
	};
}).directive("formProgressPanel", /*@ngInject*/ function() {
	return {
		restrict: "E",
		template: require("ng-cache!directives/form-progress-bar.html"),
		scope: {
			localisationNamespace: "@",
			form: "="
		},
		link: function() {}
	};
}).directive("labelTranslate", /*@ngInject*/ function($compile) {
	return {
		restrict: "A",
		priority: 100000,
		terminal: true,
		scope: false,
		compile: function() {
			return {
				post: function($scope, $element, $attributes) {
					$attributes.$set("label", $attributes.labelTranslate);
					$compile($element, null, 100000)($scope);
				}
			};
		}
	};
});
