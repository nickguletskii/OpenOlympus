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
import module from "app";
import {
	merge as _merge
} from "lodash";

export function initialiseRouterLogic() {
	module.config(/* @ngInject*/ ($locationProvider) => {
		$locationProvider.html5Mode(true);
	});
	module
		.run(/* @ngInject*/ ($rootScope, $stateParams,
			$location, $state, $window, $injector, $log) => {
			// Make state parameters easily accessible
			$rootScope.$on("$stateNotFound",
				(event, unfoundState) => {
					$log.error("Couldn't find state: ", unfoundState);
					throw new Error(`Couldn't find state: ${unfoundState.to}`);
				});

			$rootScope.$on("$stateChangeStart",
				(event, toState, toParams, fromState, fromParams) => {
					if ($rootScope.showErrorModal) {
						event.preventDefault();
						$log.warn("Trying to recover from failure state by reloading!");
						// Refresh the page to prevent broken templates getting stuck in the page
						$window.location.reload(true);
						$state.go(toState, toParams, {
							reload: true,
							notify: false,
							inherit: false
						});
					} else {
						// Make state parameters easily accessible
						$rootScope.stateParams = toParams;
					}
				});

			$rootScope.$on("$stateChangeStart",
				(event, toState, toParams, fromState, fromParam) => {
					const allowed = !angular.isObject(toState.data) ||
						!(toState.data.canAccess) ||
						$injector.invoke(toState.data.canAccess, null, {
							"$refStateParams": toParams
						});
					if (!allowed) {
						event.preventDefault();
						$location.path("/forbidden");
					} else if (allowed.then) {
						allowed.then((val) => {
							if (!val) {
								$location.path("/forbidden");
							}
						});
					}
				});

			$rootScope.$on("$stateChangeSuccess",
				(event, toState, toParams, fromState, fromParams) => {
					$rootScope.stateConfig = toState;
					$rootScope.stateParams = toParams;
				});

			$rootScope.$on("$stateChangeError",
				(event, toState, toParams, fromState, fromParams, error) => {
					$log.error(error);
					if (error.status === 403) {
						event.preventDefault();
						$location.path("/forbidden");
					} else {
						$rootScope.showErrorModal = true;
					}
				});
			$rootScope.changePage = (page) => {
				$state.go(".", _merge($rootScope.stateParams, {
					page
				}));
			};
		});
}
