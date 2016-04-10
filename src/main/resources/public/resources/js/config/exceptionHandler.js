import module from "app";
import errorHandler from "errorHandler";

export function registerExceptionHandler() {
	module
		.factory("$exceptionHandler", /* @ngInject*/ ($injector, $log) =>
			(exception) => {
				const $rootScope = $injector.get("$rootScope");
				if (!$rootScope.forbidden) {
					errorHandler.showUnknownError();
				}
				$log.error(exception);
				//	throw exception;
				$rootScope.$destroy();
			});
}
