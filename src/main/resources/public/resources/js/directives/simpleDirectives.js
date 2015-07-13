var angular = require("angular");
var _ = require("lodash");

var module = angular.module("ool.directives");

var directives = [{
	name: "spinner",
	template: "directives/spinner.html"
}];

_.forEach(directives, (directive) =>
	module.directive(directive.name, /*@ngInject*/ function() {
		return {
			restrict: "E",
			template: require("ng-cache!" + directive.template),
			scope: {},
			link: () => {}
		};
	})
);
