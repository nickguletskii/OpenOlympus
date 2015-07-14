var angular = require("angular");

var module = angular.module("ool.directives");

function defineSimpleDirective(name, template) {
	module.directive(name, /*@ngInject*/ function() {
		return {
			restrict: "E",
			template: template,
			scope: {},
			link: () => {}
		};
	});
}

defineSimpleDirective("spinner", require("ng-cache!directives/spinner.html"));
