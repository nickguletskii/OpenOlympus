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
	mapValues as _mapValues,
	isEqual as _isEqual,
	clone as _clone
} from "lodash";
import {
	directives
} from "app";

directives
	.directive("principalPermissionsEditor",
		/* @ngInject*/
		(ACLService) => ({
			restrict: "E",
			template: require(
				"ng-cache!directives/principalPermissions/principalPermissions.html"),
			scope: {
				aclGetter: "@",
				aclSetter: "@"
			},
			link: ($scope, $element, $attributes) => {
				$scope.permissions = {};

				const trueElseFalse = (obj) => _mapValues(obj, (val) => val === true);

				function updateStatus() {
					$scope.changesCommitted =
						_isEqual(
							trueElseFalse($scope.permissions),
							$scope.origPermissions
						);
				}

				$scope.collapsed = true;

				$scope.$watchCollection(() => $scope.permissions, updateStatus);
				$scope.$watchCollection(() => $scope.origPermissions, updateStatus);

				$scope.refresh = () =>
					ACLService.getGeneralPermissions($attributes.principalId)
					.success((permissions) => {
						$scope.permissions = _clone(permissions);
						$scope.origPermissions = _clone(permissions);
					});

				$scope.commit = () => {
					$scope.committing = true;
					ACLService.setGeneralPermissions($attributes.principalId, trueElseFalse(
							$scope.permissions))
						.then(() => {
							$scope.refresh();
							$scope.committing = false;
						});
				};

				$scope.refresh();
			}
		})
	);
