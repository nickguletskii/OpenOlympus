var angular = require("angular");
var moment = require("moment");
angular.module("ool.directives").directive("ngDateTime", /*@ngInject*/ function() {
	return {
		require: "ngModel",
		restrict: "A",
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$validators.dateTime = function(modelValue, viewValue) {
				return moment(viewValue, "YYYY-MM-DDTHH:mm:ss.SSSZ", true).isValid();
			};
		}
	};
});
