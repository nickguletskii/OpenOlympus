var angular = require("angular");

angular.module("ool.directives").directive("fileNgModel", /*@ngInject*/ function() {
	return {
		require: "ngModel",
		restrict: "A",
		link: function($scope, element, attrs, ngModel) {
			element.bind("change", function(event) {
				var files = event.target.files;

				ngModel.$setViewValue(files);
				element.triggerHandler("fileSelectionChanged");

				$scope.$apply();
			});
			$scope.$watch(() => ngModel.$viewValue, function(value) {
				if (!value) {
					element.val(null);
				}
			});
		}
	};
});
