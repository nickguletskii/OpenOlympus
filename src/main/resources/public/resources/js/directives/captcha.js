var $ = require("jquery");
var angular = require("angular");
var app = require("app");

module.exports = /*@ngInject*/ function(FieldHelper, $http, $compile, vcRecaptchaService) {
    return {
        restrict: 'E',
        require: '^formFor',
        template: require("ng-cache!directives/captcha.html"),
        scope: {
            attribute: '@',
            label: '@'
        },
        link: function($scope, $element, $attributes, formForController) {
            FieldHelper.manageFieldRegistration($scope, $attributes, formForController);

            $scope.setWidgetId = function(widgetId) {
                $scope.widgetId = widgetId;
            };

            $http.get("/api/recaptchaPublicKey").success(function(recaptchaPublicKey) {
                if (_.isEmpty(recaptchaPublicKey)) {
                    $element.remove();
                    return;
                }
                $scope.recaptchaPublicKey = recaptchaPublicKey;
                $.getScript("//www.google.com/recaptcha/api.js?onload=vcRecaptchaApiLoaded&render=explicit");
                $($element[0]).find(".captchaLoading").replaceWith($compile('<div vc-recaptcha key="recaptchaPublicKey" ng-model="model.bindable" on-create="setWidgetId(widgetId)"></div>')($scope));
            });

            if ($attributes.resetOn)
                $scope.$parent.$on($attributes.resetOn, () => {
                    vcRecaptchaService.reload($scope.widgetId);
                });
        }
    };
}
