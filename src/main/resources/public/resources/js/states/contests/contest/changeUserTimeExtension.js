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
import {
	toInteger as _toInteger
} from "lodash";

const controller =
	/* @ngInject*/
	($scope, $rootScope, $stateParams, FormDefaultHelperService, ValidationService, user) => {
		const validationRules = {
			time: {
				type: "positive number",
				custom: ValidationService.toTranslationPromise((value) => {
					const val = _toInteger(value);
					if (val < 1) {
						return "contest.changeUserTimeExtension.timeNotPositive";
					}
					return null;
				})
			}
		};

		class ContestChangeUserTimeExtensionForm extends FormDefaultHelperService.FormClass {
			constructor() {
				super($scope, "contest.changeUserTimeExtension");
				this.submitUrl = `/api/contest/${$stateParams.contestId}/addUserTime`;
			}

			transformDataForServer(data) {
				return {
					"user": $stateParams.userId,
					"time": data.time * (60 * 1000)
				};
			}

			setUpData() {
				this.data = {
					time: 0
				};
			}

			postSubmit(response) {
				super.postSubmit(response);
				$rootScope.$emit("userTimeExtendedInContest");
			}

			get validationRules() {
				return validationRules;
			}
		}
		$scope.user = user;
		$scope.form = new ContestChangeUserTimeExtensionForm();
	};

export default {
	"name": "contestParticipantsList.changeUserTimeExtension",
	"url": "/changeUserTimeExtension/{userId}",
	"templateUrl": "/partials/contests/contest/changeUserTimeExtension.html",
	controller,
	"modal": true,
	"resolve": {
		user: /* @ngInject*/ (UserService, $stateParams) => UserService.getUserById(
			$stateParams.userId)
	},
	"data": {
		canAccess: /* @ngInject*/
			($refStateParams, PromiseUtils, SecurityService) =>
			PromiseUtils.and(
				SecurityService.hasPermission("approved"),
				SecurityService.hasContestPermission($refStateParams.contestId,
					"extend_time"))
	},
	params: {
		userId: {
			squash: false
		}
	}
};
