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

import {
	directives
} from "app";

directives.directive("formAutoconfig", /* @ngInject*/
		($compile) => ({
			restrict: "A",
			priority: 100000,
			terminal: true,
			scope: false,
			compile: () => ({
				post: ($scope, $element, $attributes) => {
					const root = $attributes.formAutoconfig;
					$attributes.$set("name", `${root}.form`);
					$attributes.$set("controller", `${root}.formController`);
					$attributes.$set("formFor", `${root}.data`);
					$attributes.$set("validationRules", `${root}.validationRules`);
					$attributes.$set("submitWith", `${root}.submit()`);
					$attributes.$set("disable", `${root}.submitting`);
					$attributes.$set("disable", `${root}.submitting`);
					$compile($element, null, 100000)($scope);
				}
			})
		})
	)
	.directive("formProgressPanel", /* @ngInject*/ () => ({
		restrict: "E",
		template: require("ng-cache!directives/form/formProgressPanel.html"),
		scope: {
			localisationNamespace: "@",
			form: "="
		},
		link: () => {}
	}))
	.directive("labelTranslate", /* @ngInject*/ ($compile, $rootScope, $translate) =>
		({
			restrict: "A",
			priority: -100000,
			scope: false,
			link: ($scope, $element, $attributes) => {
				$rootScope.$on("$translateChangeSuccess",
					() => {
						$attributes.$set("label",
							$translate.instant($attributes.labelTranslate));
					});
				$attributes.$set("label",
					$translate.instant($attributes.labelTranslate));
			}
		}));
