var angular = require("angular");
var app = require("app");

module.exports = /*@ngInject*/ function($compile, $translate, $rootScope) {
    return {
        restrict: "AE",
        link: function(scope, element, attrs, ctrl, transclude) {
            var name = attrs.lang;

            function changeTranslation() {
                if (name && name !== $rootScope.currentLanguage) {
                    element[0].hidden = true;
                } else {
                    element[0].hidden = false;
                }
            }
            changeTranslation();
            $rootScope.$watch("currentLanguage", changeTranslation);
        }
    };
}
