var $ = require("jquery");
var angular = require("angular");

module.exports = /*@ngInject*/ function(FieldHelper) {
	return {
		restrict: "E",
		require: "^formFor",
		template: require("ng-cache!directives/file-input.html"),
		scope: {
			attribute: "@",
			label: "@",
			multiple: "@"
		},
		link: function($scope, $element, $attributes, formForController) {
			FieldHelper.manageFieldRegistration($scope, $attributes, formForController);
			let bindableFix = $scope.$watch(() => $scope.model.bindable, function() {
				Object.defineProperty($scope.model, "bindable", {
					set: (value) => {
						//Hide bindable from Angular's shitty built-in validation that somehow casts the files to string.
						if (angular.isString(value)) {
							return;
						}
						this.hideYoBindable = value;
					},
					get: () => this.hideYoBindable
				});
			});
			let watcher = $scope.$watch($attributes.multiple, function(val) {
				$($element[0]).find(".directive-main-element").attr("multiple", val);
			});
			$scope.$on("$destroy", () => {
				watcher();
				bindableFix();
			});
		}
	};
};
