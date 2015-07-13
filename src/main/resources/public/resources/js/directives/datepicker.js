var angular = require("angular");
angular.module("ool.directives").directive("datepickerField",
	/*@ngInject*/
	function(FieldHelper) {
		return {
			restrict: "E",
			require: "^formFor",
			template: require("ng-cache!directives/datepicker.html"),
			scope: {
				attribute: "@",
				label: "@"
			},
			link: function($scope, $element, $attributes, formForController) {
				FieldHelper.manageFieldRegistration($scope, $attributes, formForController);
				$scope.label = $attributes.label;
			}
		};
	});
