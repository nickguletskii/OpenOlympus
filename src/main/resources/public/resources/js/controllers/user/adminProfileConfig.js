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
		principalId: function($stateParams) {
			return $stateParams.userId;
		},
		enablePermissionsEditor: () => true
	},
	"data": {
		canAccess: /*@ngInject*/ function(PromiseUtils, SecurityService) {
			return PromiseUtils.and(
				SecurityService.hasPermission("view_other_users_personal_info"),
				PromiseUtils.or(
					SecurityService.hasPermission("change_other_users_personal_info"),
					SecurityService.hasPermission("change_other_users_password")
				));
		}
	}
};
