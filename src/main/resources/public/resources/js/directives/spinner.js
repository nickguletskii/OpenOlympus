var $ = require("jquery");
var angular = require("angular");
var app = require("app");

module.exports = /*@ngInject*/ function() {
    return {
        restrict: 'E',
        template: require("ng-cache!directives/spinner.html"),
        scope: {
        },
        link: function($scope, $element, $attributes, formForController) {
        }
    };
}
