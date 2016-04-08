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
require("font-awesome-webpack");

import angular from "angular";
import _ from "lodash";

require("angular-ui-router");
require("angular-translate");
require("angular-translate-loader-url");
require("angular-ui-codemirror");
require("angular-recaptcha");
require("angular-animate");
require("angular-cookies");
require("angular-form-for");
require("angular-form-for-bootstrap");
require("ng-file-upload");

require("app");

angular.module("ool")
	.factory("missingTranslationHandler", function() {
		return function(translationId) {
			let orig = angular.fromJson(localStorage.getItem("missingTranslations") || "[]");
			let unified = _.union(orig, [translationId]);
			localStorage.setItem("missingTranslations", angular.toJson(unified));
		};
	})
	.config( /*@ngInject*/ function($translateProvider) {
		$translateProvider.useSanitizeValueStrategy("escape");
		$translateProvider.useUrlLoader("/translation");
		$translateProvider.preferredLanguage("ru");
		// $translateProvider.translationNotFoundIndicator("$$");
		$translateProvider.useMissingTranslationHandler("missingTranslationHandler");
	})
	.config( /*@ngInject*/ function($httpProvider, $locationProvider) {

		$locationProvider.html5Mode(true);

		$httpProvider.interceptors.push( /*@ngInject*/ function($q, $rootScope, $location, $cookies) {
			return {
				"request": function(config) {
					config.headers["X-CSRF-TOKEN"] = $cookies.get("X-CSRF-TOKEN");

					if (angular.isDefined($rootScope.authToken)) {
						config.headers["X-Auth-Token"] = $rootScope.authToken;
					}
					return config || $q.when(config);
				},
				"response": function(response) {
					if (response.status === 401) {
						$location.path("/api/login");
						return $q.reject(response);
					}

					return response;
				},
				"responseError": function(response) {
					if (!_.has(response, "config.method")) {
						console.error("No response method for response", response);
					}
					if (response.status === 500 || !response.config.method) {
						errorHandler.showUnknownError();
						$rootScope.$destroy();
					}
					var status = response.status;
					var config = response.config;
					var method = config.method;
					var url = config.url;
					if (_.includes(config.acceptableFailureCodes, status)) {
						return $q.reject(response);
					}
					switch (status) {
						case -1:
							errorHandler.showConnectionLostError();
							$rootScope.$destroy();
							return null;
						case 403:
							$location.path("/forbidden");
							$rootScope.forbidden = true;
							break;
						case 401:
							$location.path("/login");
							break;
						default:
							throw new Error(method + " on " + url + " failed with status " + status);
					}

					return $q.reject(response);
				}
			};
		});

	}).factory("$exceptionHandler", /*@ngInject*/ function($injector) {
		return function(exception) {
			var $rootScope = $injector.get("$rootScope");
			if (!$rootScope.forbidden) {
				errorHandler.showUnknownError();
			}
			console.error(exception);
			//	throw exception;
			$rootScope.$destroy();
		};
	})
	.run( /*@ngInject*/ function($rootScope, SecurityService) {
		$rootScope.security = SecurityService;

		$rootScope.$on("securityInfoChanged", function() {
			$rootScope.security = SecurityService;
		});
	});
require("ui-setup");
