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

module.exports = /*@ngInject*/ function($scope, FormDefaultHelperService, ValidationService, personalInfoPatchUrl, requireExistingPassword, existingPersonalInfo, passwordPatchUrl, enablePermissionsEditor, principalId) {
	$scope.principalId = principalId;
	$scope.requireExistingPassword = requireExistingPassword;
	$scope.enablePermissionsEditor = enablePermissionsEditor;

	const personalInfoValidationRules = require("controllers/user/userInfoValidation");

	const passwordFormValidationRules = {
		password: {
			required: true
		},
		passwordConfirmation: {
			required: true,
			custom: ValidationService.toTranslationPromise(function(value, model) {
				if (value !== model.password) {
					return "user.passwordForm.validation.passwordsDontMatch";
				}
			})
		}
	};

	class PersonalInfoForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "user.personalInfoForm");
			this.submitUrl = personalInfoPatchUrl;
		}

		setUpData() {
			if (this.data) {
				return;
			}

			this.data = existingPersonalInfo;
		}

		get validationRules() {
			return personalInfoValidationRules;
		}
	}

	$scope.personalInfoForm = new PersonalInfoForm();

	class PasswordForm extends FormDefaultHelperService.FormClass {
		constructor() {
			super($scope, "user.changePasswordForm");
			this.submitUrl = passwordPatchUrl;
		}

		transformDataForServer(data) {
			return {
				password: data.password,
				existingPassword: data.existingPassword
			};
		}

		get validationRules() {
			return passwordFormValidationRules;
		}
	}

	$scope.passwordForm = new PasswordForm();
};
