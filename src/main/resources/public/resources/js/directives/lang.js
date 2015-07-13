var angular = require("angular");

angular.module("ool.directives").directive("lang", /*@ngInject*/ function($compile, $translate, $rootScope) {
	return {
		restrict: "AE",
		link: function(scope, element, attrs) {
			var name = attrs.lang;

			function changeTranslation() {
				if (name && name !== $rootScope.currentLanguage) {
					element[0].hidden = true;
				} else {
					element[0].hidden = false;
				}
			}
			changeTranslation();
			$rootScope.$watch("currentLanguage", changeTranslation);
		}
	};
});
