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

var $ = require("jquery");
var _ = require("lodash");
var angular = require("angular");
angular.module("ool.directives").directive("recaptcha", /*@ngInject*/ function(FieldHelper, $http, $compile, vcRecaptchaService) {
	return {
		restrict: "E",
		require: "^formFor",
		template: require("ng-cache!directives/captcha/captcha.html"),
		scope: {
			attribute: "@",
			label: "@",
			onCreate: "&?"
		},
		link: function($scope, $element, $attributes, formForController) {
			FieldHelper.manageFieldRegistration($scope, $attributes, formForController);

			$scope.onCreateAndSet = function(widgetId) {
				$scope.widgetId = widgetId;

				if ($scope.onCreate) {
					$scope.onCreate();
				}
			};
			$http.get("/api/recaptchaPublicKey").then(function(response) {
				let recaptchaPublicKey = response.data;
				if (_.isEmpty(recaptchaPublicKey)) {
					$element.remove();

					if ($scope.onCreate) {
						$scope.onCreate();
					}
					return;
				}
				$scope.recaptchaPublicKey = recaptchaPublicKey;
				$.getScript("//www.google.com/recaptcha/api.js?onload=vcRecaptchaApiLoaded&render=explicit");
				$($element[0]).find(".captchaLoading").replaceWith($compile("<div vc-recaptcha key=\"recaptchaPublicKey\" ng-model=\"model.bindable\" on-create=\"onCreateAndSet(widgetId)\"></div>")($scope));
			});

			if ($attributes.resetOn) {
				$scope.$parent.$on($attributes.resetOn, () => {
					if ($scope.recaptchaPublicKey) {
						vcRecaptchaService.reload($scope.widgetId);
					}
				});
			}
		}
	};
});
