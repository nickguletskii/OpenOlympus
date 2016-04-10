/*
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import userProfileController from "states/user/profile";

export default {
	"name": "administrativePersonalInfoModificationView",
	"url": "/admin/user/{userId:[0-9]+}/profile",
	"templateUrl": "/partials/user/profile.html",
	"controller": userProfileController,
	"customWidth": "narrow",
	"resolve": {
		existingPersonalInfo: /* @ngInject*/ ($http, $stateParams) =>
			$http.get(`/api/admin/user/${$stateParams.userId}/personalInfo`, {}),
		personalInfoPatchUrl: /* @ngInject*/ ($stateParams) =>
			`/api/admin/user/${$stateParams.userId}/personalInfo`,
		passwordPatchUrl: /* @ngInject*/ ($stateParams) =>
			`/api/admin/user/${$stateParams.userId}/changePassword`,
		requireExistingPassword: () => false,
		principalId: /* @ngInject*/ ($stateParams) =>
			$stateParams.userId,
		enablePermissionsEditor: () => true
	},
	"data": {
		canAccess: /* @ngInject*/ (PromiseUtils, SecurityService) => PromiseUtils.and(
			SecurityService.hasPermission("view_other_users_personal_info"),
			PromiseUtils.or(
				SecurityService.hasPermission("change_other_users_personal_info"),
				SecurityService.hasPermission("change_other_users_password")
			))
	}
};
