var angular = require("angular");
angular.module("ool.filters").filter("groupName", /*@ngInject*/ function($translate) {

	var filter = function(name) {
		if (!name) {
			return name;
		}
		var arr = name.match(/^\#\{(.*)\}$/);
		if (!arr || !arr[1]) {
			return name;
		}
		if (arr[1]) {
			return $translate.instant("builtinGroups." + arr[1]);
		}
		return name;
	};

	if ($translate.statefulFilter()) {
		filter.$stateful = true;
	}

	return filter;
});
