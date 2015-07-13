var angular = require("angular");

angular.module("ool.directives").directive("dynamic", /*@ngInject*/ function($compile) {
	return {
		restrict: "A",
		replace: true,
		link: function(scope, element, attrs) {
			scope.$watch(attrs.dynamic, function(html) {
				element.html(html);
				$compile(element.contents())(scope);
			});
		}
	};
});
