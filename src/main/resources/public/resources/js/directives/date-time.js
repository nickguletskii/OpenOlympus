var angular = require("angular");
var app = require("app");

module.exports = /*@ngInject*/ function($q, $timeout) {
    return {
        require: 'ngModel',
        restrict: 'A',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$validators.dateTime = function(modelValue, viewValue) {
                return moment(viewValue, "YYYY-MM-DDTHH:mm:ss.SSSZ", true).isValid();
            };
        }
    };
}
