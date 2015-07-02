var angular = require("angular");
var app = require("app");

module.exports = /*@ngInject*/ function($compile) {
    return {
        restrict: 'A',
        replace: true,
        link: function(scope, element, attrs) {
            scope.$watch(attrs.dynamic, function(html) {
                element.html(html);
                $compile(element.contents())(scope);
            });
        }
    };
}
