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
"use strict";

const controller = /*@ngInject*/ function(ValidationService, $scope, FormDefaultHelperService) {

	const validationRules = require("controllers/groups/groupValidation")(ValidationService);

	class GroupCreationForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "group.createForm");
			this.submitUrl = "/api/groups/create";
		}

		preSubmit() {
			super.preSubmit();
			this.lastGroupId = null;
		}

		postSubmit(response) {
			super.postSubmit(response);
			this.lastGroupId = response.data.id;
		}

		get validationRules() {
			return validationRules;
		}
	}

	$scope.form = new GroupCreationForm();
};

module.exports = {
	"name": "createGroup",
	"url": "/groups/add",
	"templateUrl": "/partials/groups/add.html",
	"controller": controller,
	"data": {
		canAccess: /*@ngInject*/ function(PromiseUtils, SecurityService) {
			return SecurityService.hasPermission("create_group");
		}
	}
};
