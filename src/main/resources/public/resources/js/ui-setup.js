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

import "angular-animate";
import "angular-ui-bootstrap";
import module from "app";


export function initialiseTemplates() {
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
};

export function scheduleRemovalOfLoadingOverlay() {
	module.run(function($rootScope, $timeout) {
		$timeout(function() {
			$rootScope.hideResourcesLoading = true;
		}, 0);
	});
}


export function initialiseFullScreenManagement() {
	module.run( /*@ngInject*/ function($rootScope) {
		$rootScope.toggleFullscreen = function(fullScreen) {
			$rootScope.fullScreen = fullScreen;
		};
	});
}

export function bootstrap() {
	angular.bootstrap(document, ["ool"]);
}
