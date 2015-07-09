var $ = require("jquery");
var angular = require("angular");
var app = require("app");

module.exports = /*@ngInject*/ function($http, $compile) {
    return {
        restrict: 'E',
        template: require("ng-cache!directives/acl.html"),
        scope: {

        },
        link: function($scope, $element, $attributes) {
            $scope.localisationNamespace = $attributes.localisationNamespace;
            $scope.permissions = {
                "edit": {
                    "principals": [{
                        "type": "user",
                        "id": 1,
                        "data": {
                            "username": "Admin"
                        }
                    }],
                    "user": {},
                    "group": {}
                },
                "view": {
                    "principals": [{
                        "type": "group",
                        "id": 2,
                        "data": {
                            "name": "All approved users"
                        }
                    }],
                    "user": {},
                    "group": {}
                }
            };
            $scope.groupValidationRules = {};
            $scope.groupOptions = [{
                value: "lol",
                label: "lol2"
            }];
            $scope.addGroup = function(group) {
                console.log(group);
            }
            $scope.userValidationRules = {};
            $scope.userOptions = [{
                value: "lol",
                label: "lol2"
            }];
            $scope.addUser = function(user) {
                console.log(user);
            }
        }
    };
}
