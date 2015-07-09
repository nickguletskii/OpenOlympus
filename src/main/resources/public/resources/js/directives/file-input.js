var $ = require("jquery");
var angular = require("angular");
var app = require("app");

module.exports = /*@ngInject*/ function(FieldHelper, $http, $compile, $timeout) {
	return {
		restrict: 'E',
		require: '^formFor',
		template: require("ng-cache!directives/file-input.html"),
		scope: {
			attribute: '@',
			label: '@',
			multiple: '@'
		},
		link: function($scope, $element, $attributes, formForController) {
			FieldHelper.manageFieldRegistration($scope, $attributes, formForController);

			$scope.$watch($attributes.multiple, function(val) {
				$($element[0]).find(".directive-main-element").attr("multiple", val);
			});
		}
	};
}
