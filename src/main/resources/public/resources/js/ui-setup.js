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
var moment = require("moment");
require("app");
require("angular-animate");
require("angular-ui-bootstrap");

angular
	.module("template/pagination/pagination.html", [])
	.run(
		/*@ngInject*/
		function($templateCache) {
			$templateCache
				.put(
					"template/pagination/pagination.html",
					require("ng-cache!directives/pagination.html"));
		}
	);

angular.module("ool")
	.run(function($rootScope, $timeout) {
		$timeout(function() {
			$rootScope.hideResourcesLoading = true;
		}, 0);
	})
	.run( /*@ngInject*/ function($rootScope, $stateParams, AuthenticationProvider, $location, $timeout, $state, $http, $translate, $window, FormForConfiguration) {
		// Make state parameters easily accessible
		$rootScope.stateParams = $stateParams;

		// Make sure that locales are synchronised across libraries
		$rootScope.$on("$translateChangeSuccess",
			function(event, language) {
				$rootScope.currentLanguage = language.language;
				moment.locale(language.language);
			});

		$rootScope.availableLanguages = ["en", "ru"]; // TODO: Make this dynamic
		$rootScope.changeLanguage = $translate.use; // Expose $translate.use to the language change menu


		$rootScope.$on("$stateNotFound",
			function(event, unfoundState, fromState, fromParams) {
				console.error("Couldn't find state: ", unfoundState);
				throw new Error("Couldn't find state: " + unfoundState.to);
			});
		$rootScope.$on("$stateChangeStart",
			function(event, toState, toParams, fromState, fromParams) {
				if ($rootScope.showErrorModal) {
					event.preventDefault();
					console.log("Trying to recover from failure state by reloading!");
					$window.location.reload(true); // Refresh the page to prevent broken templates getting stuck in the page
					$state.go(toState, toParams, {
						reload: true,
						notify: false,
						inherit: false
					});
				} else {
					$rootScope.stateParams = toParams; // Make state parameters easily accessible
				}
			});
		$rootScope.$on("$stateChangeSuccess",
			function(event, toState, toParams, fromState, fromParams) {
				$rootScope.stateConfig = toState;
			});
		$rootScope.$on("$stateChangeError",
			function(event, toState, toParams, fromState, fromParams, error) {
				console.error(error);
				if (error.status === 403) {
					event.preventDefault();
					$location.path("/forbidden");
				} else {
					$rootScope.showErrorModal = true;
				}
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




angular.bootstrap(document, ["ool"]);
