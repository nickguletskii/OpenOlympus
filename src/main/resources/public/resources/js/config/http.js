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
import module from "app";
import errorHandler from "errorHandler";
import {
	has as _has,
	includes as _includes
} from "lodash";

class OpenOlympusRequestInterceptor {

	/* @ngInject*/
	constructor($q, $rootScope,
		$location, $cookies, $log) {
		this.$q = $q;
		this.$rootScope = $rootScope;
		this.$location = $location;
		this.$cookies = $cookies;
		this.$log = $log;
	}
	get request() {
		return (config) => {
			config.headers["X-CSRF-TOKEN"] =
				this.$cookies.get("X-CSRF-TOKEN");

			if (angular.isDefined(this.$rootScope.authToken)) {
				config.headers["X-Auth-Token"] = this.$rootScope.authToken;
			}
			return config || this.$q.when(config);
		};
	}
	get response() {
		return (response) => {
			if (response.status === 401) {
				this.$location.path("/api/login");
				return this.$q.reject(response);
			}

			return response;
		};
	}
	get responseError() {
		return (response) => {
			if (!_has(response, "config.method")) {
				this.$log.error("No response method for response", response);
			}
			if (response.status === 500 || !response.config.method) {
				errorHandler.showUnknownError();
				this.$rootScope.$destroy();
			}
			const status = response.status;
			const config = response.config;
			const method = config.method;
			const url = config.url;
			if (_includes(config.acceptableFailureCodes, status)) {
				return this.$q.reject(response);
			}
			switch (status) {
			case -1:
				if (response.config.ignoreFailure) {
					return this.$q.reject(response);
				}
				errorHandler.showConnectionLostError();
				this.$rootScope.$destroy();
				return null;
			case 403:
				this.$location.path("/forbidden");
				this.$rootScope.forbidden = true;
				break;
			case 401:
				this.$location.path("/login");
				break;
			default:
				throw new Error(
						`${method} on ${url} failed with status ${status}`);
			}

			return this.$q.reject(response);
		};
	}
}

module.service("OpenOlympusRequestInterceptor", OpenOlympusRequestInterceptor);

export function addHttpInterceptors() {
	module.config(/* @ngInject*/ ($httpProvider) => {
		$httpProvider.interceptors.push("OpenOlympusRequestInterceptor");
	});
}
