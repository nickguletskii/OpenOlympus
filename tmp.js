const WIDE = "wide";
const NARROW = "narrow";

var x = [{
	"name": "userSolutionList",
	"url": "/user/solutions?page",
	"templateUrl": "/partials/user/solutions.html",
	"controller": "controllers/user/solutions",
	"params": {
		"page": "1"
	},
	"resolve": {
		"solutions": function(SolutionService, $stateParams) {
			return SolutionService.getUserSolutionsPage($stateParams.page);
		},
		"solutionCount": function(SolutionService, $stateParams) {
			return SolutionService.countUserSolutions();
		}
	}
}, {
	"name": "adminSolutionList",
	"url": "/admin/solutions?page",
	"templateUrl": "/partials/admin/solutions.html",
	"controller": "controllers/admin/solutions",
	"params": {
		"page": "1"
	},
	"resolve": {
		"solutions": function(SolutionService, $stateParams) {
			return SolutionService.getSolutionsPage($stateParams.page);
		},
		"solutionCount": function(SolutionService, $stateParams) {
			return SolutionService.countSolutions();
		}
	}
}, {
	"name": "solutionView",
	"url": "/solution/{solutionId:[0-9]+}",
	"templateUrl": "/partials/solution.html",
	"controller": "controllers/solution",
	"resolve": {
		"data": function(SolutionService, $stateParams) {
			return SolutionService.getVerdicts($stateParams.solutionId);
		}
	}
}, {
	"name": "createTask",
	"url": "/archive/tasks/add",
	"templateUrl": "/partials/archive/tasks/add.html",
	"controller": "controllers/archive/tasks/add"
}, {
	"name": "createContest",
	"url": "/contests/add",
	"templateUrl": "/partials/contests/add.html",
	"controller": "controllers/contests/add"
}, {
	"name": "editContest",
	"url": "/contest/{contestId:[0-9]+}/edit",
	"templateUrl": "/partials/contests/contest/edit.html",
	"controller": "controllers/contests/contest/edit",
	"resolve": {
		"contest": function(ContestService, $stateParams) {
			return ContestService.getContestEditData($stateParams.contestId);
		}
	}
}, {
	"name": "login",
	"url": "/login?failure",
	"templateUrl": "/partials/login.html",
	"controller": "controllers/login",
	"customWidth": NARROW,
	"params": {
		"failure": "false",
		"showAdministratorApprovalRequiredMessage": "false"
	}
}, {
	"name": "register",
	"url": "/register",
	"templateUrl": "/partials/register.html",
	"controller": "controllers/register",
	"customWidth": NARROW
}, {
	"name": "personalInfoModificationView",
	"url": "/user/personalInfo",
	"templateUrl": "/partials/user/personalInfo.html",
	"controller": "controllers/user/personalInfo",
	"customWidth": NARROW,
	"resolve": {
		personalInfoPatchUrl: function() {
			return '/api/user/personalInfo';
		},
		passwordPatchUrl: function() {
			return '/api/user/changePassword';
		},
		requireExistingPassword: function() {
			return true;
		}
	}
}, {
	"name": "administrativePersonalInfoModificationView",
	"url": "/admin/user/{userId:[0-9]+}/personalInfo",
	"templateUrl": "/partials/user/personalInfo.html",
	"controller": "controllers/user/personalInfo",
	"customWidth": NARROW,
	"resolve": {
		personalInfoPatchUrl: function($stateParams) {
			return '/api/admin/user/' + $stateParams.userId + '/personalInfo';
		},
		passwordPatchUrl: function($stateParams) {
			return '/api/admin/user/' + $stateParams.userId + '/changePassword';
		},
		requireExistingPassword: function() {
			return false;
		}
	}
}, {
	"name": "home",
	"url": "/",
	"templateUrl": "/partials/home.html",
	"controller": "controllers/home",
	"type": "requireController",
	"customWidth": NARROW
}, {
	"name": "pendingUsersList",
	"url": "/admin/pendingUsers?page",
	"templateUrl": "/partials/admin/pendingUsers.html",
	"customWidth": WIDE,
	"controller": "controllers/admin/pendingUsers",
	"params": {
		"page": "1"
	},
	"resolve": {
		"users": function(UserService, $stateParams) {
			return UserService.getPendingUsersPage($stateParams.page);
		},
		"userCount": function(UserService, $stateParams) {
			return UserService.countPendingUsers();
		}
	}
}, {
	"name": "adminUsersList",
	"url": "/admin/users?page",
	"templateUrl": "/partials/admin/users.html",
	"customWidth": WIDE,
	"controller": "controllers/admin/users",
	"params": {
		"page": "1"
	},
	"resolve": {
		"users": function(UserService, $stateParams) {
			return UserService.getUsersPage($stateParams.page);
		},
		"userCount": function(UserService, $stateParams) {
			return UserService.countUsers();
		}
	}
}, {
	"name": "administrationHub",
	"url": "/admin",
	"templateUrl": "/partials/admin.html",
	"controller": "controllers/admin"
}];
