import angular from "angular";
import module from "app";

export function initialiseRouterLogic() {
	module.config(/* @ngInject*/ ($locationProvider) => {
		$locationProvider.html5Mode(true);
	});
	module
		.run(/* @ngInject*/ ($rootScope, $stateParams,
			$location, $state, $window, $injector, $log) => {
			// Make state parameters easily accessible
			$rootScope.stateParams = $stateParams;
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
		});
}
