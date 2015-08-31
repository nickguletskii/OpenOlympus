"use strict";

module.exports = {
	"name": "administrativePersonalInfoModificationView",
	"url": "/admin/user/{userId:[0-9]+}/personalInfo",
	"templateUrl": "/partials/user/personalInfo.html",
	"controller": require("controllers/user/personalInfo"),
	"customWidth": "narrow",
	"resolve": {
		existingPersonalInfo: function($http, $stateParams) {
			return $http.get("/api/admin/user/" + $stateParams.userId + "/personalInfo", {});
		},
		personalInfoPatchUrl: function($stateParams) {
			return "/api/admin/user/" + $stateParams.userId + "/personalInfo";
		},
		passwordPatchUrl: function($stateParams) {
			return "/api/admin/user/" + $stateParams.userId + "/changePassword";
		},
		requireExistingPassword: function() {
			return false;
		}
	}
};
