var angular = require("angular");
angular.module("ool.services")
	.service("ACLService", /*@ngInject*/ function($http) {
		this.getACL = (url) => $http.get(url, {});
		this.setACL = (url) => $http.put(url, {});
	});
