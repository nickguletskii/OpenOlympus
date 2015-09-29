/*
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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
"use strict";

var _ = require("lodash");

const controller = /*@ngInject*/ function($scope, $stateParams, FormDefaultHelperService, ValidationService, group) {
	$scope.groupId = $stateParams.groupId;
	const validationRules = require("controllers/groups/groupValidation")(ValidationService);

	class GroupModificationForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "group.editForm");
			this.submitUrl = "/api/group/" + $stateParams.groupId + "/edit";
		}

		setUpData() {
			if (this.data) {
				return;
			}
			this.data = _.clone(group);
		}

		get validationRules() {
			return validationRules;
		}
	}

	$scope.form = new GroupModificationForm();
};

module.exports = {
	"name": "editGroup",
	"url": "/group/{groupId:[0-9]+}/edit",
	"templateUrl": "/partials/groups/group/edit.html",
	"controller": controller,
	"resolve": {
		"group": function(GroupService, $stateParams) {
			return GroupService.getGroupEditData($stateParams.groupId);
		}
	},
	"data": {
		canAccess: /*@ngInject*/ function(PromiseUtils, SecurityService, $refStateParams) {
			return PromiseUtils.and(
				SecurityService.hasPermission("list_groups"),
				SecurityService.hasGroupPermission($refStateParams.groupId, "edit")
			);
		}
	}
};
