var angular = require("angular");
angular.module("ool.directives").directive("aclEditor", /*@ngInject*/ function(ACLService) {
	return {
		restrict: "E",
		template: require("ng-cache!directives/acl.html"),
		scope: {
			aclGetter: "@",
			aclSetter: "@"
		},
		link: function($scope, $element, $attributes) {
			$scope.refresh = () =>
				ACLService.getACL($attributes.aclGetter).success((permissions) =>
					$scope.permissions = permissions);

			$scope.refresh();

			$scope.localisationNamespace = $attributes.localisationNamespace;
			$scope.groupValidationRules = {};
			$scope.groupOptions = [];
			$scope.addGroup = function(group) {
				console.log(group);
			};
			$scope.userValidationRules = {};
			$scope.userOptions = [];
			$scope.addUser = function(user) {
				console.log(user);
			};
		}
	};
});
