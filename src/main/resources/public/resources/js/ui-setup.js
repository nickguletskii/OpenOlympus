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
var app = require("app");
var Util = require("oolutil");
var moment = require("moment");
require("angular-animate");
require("angular-ui-bootstrap");

app
	.run(function($rootScope, $timeout) {
		$timeout(function() {
			$rootScope.hideResourcesLoading = true;
		}, 0);
	});

angular
	.module("template/pagination/pagination.html", [])
	.run(
		[
			"$templateCache",
			function($templateCache) {
				$templateCache
					.put(
						"template/pagination/pagination.html",
						"<div class=\"pagination-centered\"><ul class=\"pagination\">\n" +
						"  <li ng-repeat=\"page in pages\" ng-class=\"{arrow: $first || $last, current: page.active, unavailable: page.disabled}\"><a ui-sref-opts=\"reload\" ui-sref=\"{page: page.number}\">{{page.text}}</a></li>\n" +
						"</ul>\n" + "</div>");
			}
		]);

angular.module("template/pagination/pagination.html", []).run(["$templateCache", function($templateCache) {
	$templateCache.put("template/pagination/pagination.html",
		"<div class=\"pagination-centered hidden-print\">\n" +
		"  <ul class=\"pagination pagination-md\">\n" +
		"    <li ng-if=\"boundaryLinks\" ng-class=\"{disabled: noPrevious()}\"><a eat-click-if=\"noPrevious()\" ui-sref=\"{page: 1}\">{{getText('first')}}</a></li>\n" +
		"    <li ng-if=\"directionLinks\" ng-class=\"{disabled: noPrevious()}\"><a eat-click-if=\"noPrevious()\" ui-sref=\"{page: page-1}\">{{getText('previous')}}</a></li>\n" +
		"    <li ng-repeat=\"page in pages track by $index\" ng-class=\"{active: page.active}\"><a ui-sref=\"{page: page.number}\">{{page.text}}</a></li>\n" +
		"    <li ng-if=\"directionLinks\" ng-class=\"{disabled: noNext()}\"><a eat-click-if=\"noNext()\" ui-sref=\"{page: page+1}\">{{getText('next')}}</a></li>\n" +
		"    <li ng-if=\"boundaryLinks\" ng-class=\"{disabled: noNext()}\"><a eat-click-if=\"noNext()\" ui-sref=\"{page: totalPages}\">{{getText('last')}}</a></li>\n" +
		"  </ul>\n" +
		"</div>");
}]);

app.run( /*@ngInject*/ function($rootScope, $stateParams, AuthenticationProvider, $location, $timeout, $state, $http, $translate, FormForConfiguration) {
	// Make state parameters easily accessible
	$rootScope.stateParams = $stateParams;

	// Make sure that locales are synchronised across libraries
	$rootScope.$on('$translateChangeSuccess',
		function(event, language) {
			$rootScope.currentLanguage = language.language;
			moment.locale(language.language);
		});

	$rootScope.availableLanguages = ["en", "ru"]; // TODO: Make this dynamic
	$rootScope.changeLanguage = $translate.use; // Expose $translate.use to the language change menu


	$rootScope.$on('$stateNotFound',
		function(event, unfoundState, fromState, fromParams) {
			console.error("Couldn't find state: " + unfoundState);
		});
	$rootScope.$on('$stateChangeStart',
		function(event, toState, toParams, fromState, fromParams) {
			if ($rootScope.showErrorModal) {
				event.preventDefault();
				console.log("Trying to recover from failure state by reloading!");
				window.location.reload(true); // Refresh the page to prevent broken templates getting stuck in the page
				$state.go(toState, toParams, {
					reload: true,
					notify: false,
					inherit: false
				});
			} else {
				$rootScope.stateParams = toParams; // Make state parameters easily accessible
			}
		});
	$rootScope.$on('$stateChangeSuccess',
		function(event, toState, toParams, fromState, fromParams) {
			$rootScope.stateConfig = toState;
		});
	$rootScope.$on('$stateChangeError',
		function(event, toState, toParams, fromState, fromParams, error) {
			console.error(error);
			if (error.status == 403) {
				event.preventDefault();
				$location.path("/forbidden");
			} else
				$rootScope.showErrorModal = true;
		});

	$rootScope.toggleFullscreen = function(fullScreen) {
		$rootScope.fullScreen = fullScreen;
	};

	$translate(["validation.failed.custom", "validation.failed.email", "validation.failed.integer",
		"validation.failed.maxCollectionSize", "validation.failed.maximum", "validation.failed.maxLength",
		"validation.failed.minimum", "validation.failed.minCollectionSize", "validation.failed.minLength",
		"validation.failed.negative", "validation.failed.nonNegative", "validation.failed.numeric",
		"validation.failed.pattern", "validation.failed.positive", "validation.failed.empty"
	]).then(function(translations) {
		FormForConfiguration.setValidationFailedForCustomMessage(translations["validation.failed.custom"]);
		FormForConfiguration.setValidationFailedForEmailTypeMessage(translations["validation.failed.email"]);
		FormForConfiguration.setValidationFailedForIntegerTypeMessage(translations["validation.failed.integer"]);
		FormForConfiguration.setValidationFailedForMaxCollectionSizeMessage(translations["validation.failed.maxCollectionSize"]);
		FormForConfiguration.setValidationFailedForMaximumMessage(translations["validation.failed.maximum"]);
		FormForConfiguration.setValidationFailedForMaxLengthMessage(translations["validation.failed.maxLength"]);
		FormForConfiguration.setValidationFailedForMinimumMessage(translations["validation.failed.minimum"]);
		FormForConfiguration.setValidationFailedForMinCollectionSizeMessage(translations["validation.failed.minCollectionSize"]);
		FormForConfiguration.setValidationFailedForMinLengthMessage(translations["validation.failed.minLength"]);
		FormForConfiguration.setValidationFailedForNegativeTypeMessage(translations["validation.failed.negative"]);
		FormForConfiguration.setValidationFailedForNonNegativeTypeMessage(translations["validation.failed.nonNegative"]);
		FormForConfiguration.setValidationFailedForNumericTypeMessage(translations["validation.failed.numeric"]);
		FormForConfiguration.setValidationFailedForPatternMessage(translations["validation.failed.pattern"]);
		FormForConfiguration.setValidationFailedForPositiveTypeMessage(translations["validation.failed.positive"]);
		FormForConfiguration.setValidationFailedForRequiredMessage(translations["validation.failed.empty"]);
		FormForConfiguration.disableAutoTrimValues();
	});

});




angular.bootstrap(document, ['ool']);
