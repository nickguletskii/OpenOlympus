import module from "app";

export function startListeningToSecurityEvents() {
	module.run(/* @ngInject*/ ($rootScope, SecurityService) => {
		$rootScope.security = SecurityService;

		$rootScope.$on("securityInfoChanged", () => {
			$rootScope.security = SecurityService;
		});
	});
}
