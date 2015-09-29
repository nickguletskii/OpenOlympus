module.exports = {
	"name": "personalInfoModificationView",
	"url": "/user/profile",
	"templateUrl": "/partials/user/profile.html",
	"controller": require("controllers/user/profile"),
	"customWidth": "narrow",
	"resolve": {
		existingPersonalInfo: function(UserService) {
			return UserService.getCurrentUser();
		},
		personalInfoPatchUrl: function() {
			return "/api/user/personalInfo";
		},
		passwordPatchUrl: function() {
			return "/api/user/changePassword";
		},
		requireExistingPassword: function() {
			return true;
		},
		principalId: function(SecurityService) {
			return SecurityService.user.id;
		},
		enablePermissionsEditor: () => false
	},
	"data": {
		canAccess: /*@ngInject*/ function(PromiseUtils, SecurityService) {
			return SecurityService.isLoggedIn;
		}
	}
};
