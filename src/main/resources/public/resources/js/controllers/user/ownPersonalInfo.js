module.exports = {
	"name": "personalInfoModificationView",
	"url": "/user/personalInfo",
	"templateUrl": "/partials/user/personalInfo.html",
	"controller": require("controllers/user/personalInfo"),
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
		}
	}
};
