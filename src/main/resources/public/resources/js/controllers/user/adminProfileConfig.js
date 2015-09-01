"use strict";

module.exports = {
	"name": "administrativePersonalInfoModificationView",
	"url": "/admin/user/{userId:[0-9]+}/profile",
	"templateUrl": "/partials/user/profile.html",
	"controller": require("controllers/user/profile"),
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
		},
		enablePermissionsEditor: () => true
	}
};
