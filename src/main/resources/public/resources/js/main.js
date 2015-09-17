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
require("font-awesome-webpack");

var angular = require("angular");
require("angular-ui-router");
require("angular-ui-bootstrap");
require("angular-ui-bootstrap-tpls");
require("angular-translate");
require("angular-translate-loader-url");
require("angular-ui-codemirror");
require("angular-recaptcha");
require("angular-animate");
require("angular-form-for");
require("angular-form-for-bootstrap");
require("ng-file-upload");

require("app");

angular.module("ool")
	.config( /*@ngInject*/ function($translateProvider) {
		$translateProvider.useSanitizeValueStrategy("escape");
		$translateProvider.useUrlLoader("/translation");
		$translateProvider.preferredLanguage("ru");
	})
	.config( /*@ngInject*/ function($httpProvider, $locationProvider) {

		$locationProvider.html5Mode(true);

		$httpProvider.interceptors.push( /*@ngInject*/ function($q, $rootScope, $location) {
			return {
				"request": function(config) {
					if (angular.isDefined($rootScope.authToken)) {
						config.headers["X-Auth-Token"] = $rootScope.authToken;
					}
					return config || $q.when(config);
				},
				"response": function(response) {
					if (response.status === 401) {
						$location.path("/login");
						return $q.reject(response);
					}

					return response;
				},
				"responseError": function(response) {
					if (response.status === 500) {
						$rootScope.showErrorModal = true;
						return $q.reject(response);
					}
					var status = response.status;
					var config = response.config;
					var method = config.method;
					var url = config.url;

					if (status === 403) {
						$location.path("/forbidden");
						$rootScope.forbidden = true;
						return $q.reject(response);
					} else if (response.status === 401) {
						$location.path("/login");
					} else {
						throw new Error(method + " on " + url + " failed with status " + status);
					}

					return $q.reject(response);
				}
			};
		});

	}).factory("$exceptionHandler", /*@ngInject*/ function($injector) {
		return function(exception) {
			var $rootScope = $injector.get("$rootScope");
			var $timeout = $injector.get("$timeout");
			if (!$rootScope.forbidden) {
				$timeout(function() {
					$rootScope.showErrorModal = true;
				}, 1);
			}
			console.error(exception);
			//	throw exception;
		};
	})
	.run( /*@ngInject*/ function($rootScope, SecurityService) {
		$rootScope.security = SecurityService;

		$rootScope.$on("securityInfoChanged", function() {
			$rootScope.security = SecurityService;
		});
	});
require("ui-setup");
