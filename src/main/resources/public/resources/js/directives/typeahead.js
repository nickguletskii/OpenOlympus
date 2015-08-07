var angular = require("angular");
angular.module("ool.directives").directive("typeaheadBootstrap", /*@ngInject*/ function(FieldHelper) {
	return {
		require: "^formFor",
		restrict: "E",
		transclude: true,

		template: require("ng-cache!directives/typeahead.html"),
		scope: {
			attribute: "@",
			debounce: "@?",
			disable: "=",
			help: "@?",
			tooltip: "@?",
			buttonText: "@?"
		},
		compile: function(_scope, element) {
			return {
				pre: function($scope, $element, $attributes, formForController, $transclude) {
					FieldHelper.manageFieldRegistration($scope, $attributes, formForController);
					formForController.registerSubmitButton($scope);
					$scope.selectItem = function(item) {
						$scope.model.bindable = item;
					};
					$transclude(function(clone, scope) {
						scope.$$oolDirectiveIsolatedScope = $scope;
						$element.append(clone);
					});
				}
			};
		}
	};
}).directive("typeaheadBootstrapModel", /*@ngInject*/ function($compile) {
	return {
		restrict: "A",
		priority: 100000,
		terminal: true,
		compile: function() {
			return {
				post: function($scope, $element, $attributes) {
					$attributes.$set("ngModel", "$$oolDirectiveIsolatedScope.model.bindable");
					$attributes.$set("typeaheadOnSelect", "$$oolDirectiveIsolatedScope.selectItem($item, $model, $label)");
					$compile($element, null, 100000)($scope);
				}
			};
		}
	};
});
