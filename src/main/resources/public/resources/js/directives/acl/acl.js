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

import angular from "angular";

const builtinGroups = ["superusers", "allUsers"]; // TODO: move to service
import {
	filter as _filter,
	map as _map,
	curryRight as _curryRight,
	assign as _assign,
	set as _set,
	cloneDeep as _cloneDeep,
	isEqual as _isEqual,
	find as _find,
	concat as _concat,
	findIndex as _findIndex,
	each as _each
} from "lodash";
import { directives } from "app";

directives
	.run(/* @ngInject*/ ($templateCache) => {
		$templateCache.put("directives/aclEditor/typeaheadUserItem.html", require(
			"ng-cache!directives/acl/aclTypeaheadItemUser.html"));
		$templateCache.put("directives/aclEditor/typeaheadGroupItem.html", require(
			"ng-cache!directives/acl/aclTypeaheadItemGroup.html"));
	})
	.directive("aclEditor", (ACLService, $q, $http, $translate) => {
		// Returns a promise that rejects with an object where attribute = translated key
		function translateAndReject(attribute, key) {
			const deferred = $q.defer();
			$translate([key])
				.then((map) =>
					deferred.reject(_set({}, attribute, map[key]))
				);
			return deferred.promise;
		}

		function link($scope, $element, $attributes) {
			function updateStatus() {
				$scope.changesCommitted =
					_isEqual(
						ACLService.cannonicalPermissionForm($scope.permissions),
						$scope.origPermissions
					);
			}

			$scope.collapsed = true;

			$scope.refresh = () =>
				ACLService.getACL($attributes.aclGetter)
				.then((response) => {
					const permissions = response.data;
					$scope.permissions = _map(permissions, perm => _assign(perm, {
						user: {}, // Form data object for adding users
						group: {} // Form data object for adding groups
					}));
					$scope.origPermissions = ACLService.cannonicalPermissionForm(_cloneDeep(
						$scope.permissions));
					updateStatus();
				});

			$scope.commit = () => {
				$scope.committing = true;
				ACLService.setACL($attributes.aclSetter, $scope.permissions)
					.then(() => {
						$scope.refresh();
						$scope.committing = false;
					});
			};


			$scope.refresh();

			$scope.localisationNamespace = $attributes.localisationNamespace;

			$scope.groupValidationRules = {};
			$scope.groupOptions = [];


			$scope.userValidationRules = {};
			$scope.userOptions = [];

			// Attempts to add principal using its string identification.
			function addPrincipalUsingIdentifier(permission, form, paramName,
				attributeName, providerURL, objectConsumer) {
				const deferred = $q.defer();
				$http.get(providerURL, {
					params: _set({}, paramName, form[attributeName])
				})
					.then((response) => {
						// The objectConsumer is a function that processes form data. Essentially,
						// this method exists solely to provide the objectConsumer with a workable
						// formData.
						objectConsumer(
								permission,
								_set({}, attributeName, response.data)
							)
							.then( // Escape double promise
								(success) => deferred.resolve(success),

								(failure) => deferred.reject(failure)
							);
					});
				return deferred.promise;
			}

			// Get principal suggestions from server
			function loadCompletion(filterText, url) {
				const deferred = $q.defer();
				$http.get(url, {
					params: {
						term: filterText
					}
				})
					.then((response) => {
						deferred.resolve(response.data);
					}, () => {
						deferred.reject();
						throw new Error("Principal completion failed.");
					});
				return deferred.promise;
			}

			// Add user from form data.
			$scope.addUser = (permission, userForm) => {
				if (!userForm.user) {
					return translateAndReject("user",
						`${$attributes.localisationNamespace}.form.validation.noUserSelected`);
				}

				// Can't deal with this form right now, need to load the user from the server.
				if (angular.isString(userForm.user)) {
					return addPrincipalUsingIdentifier(permission, userForm, "username",
						"user", "/api/user", $scope.addUser);
				}

				// User is already in the permission list.
				if (_find(permission.principals, {
					id: userForm.user.id
				})) {
					return translateAndReject("user",
						`${$attributes.localisationNamespace}.form.validation.userAlreadyInACL`
					);
				}

				if (!permission.principals) {
					permission.principals = [];
				}

				permission.principals.push(userForm.user);
				updateStatus();

				return $q.when(null);
			};

			$scope.addGroup = (permission, groupForm) => {
				if (!groupForm.group) {
					return translateAndReject("group",
						`${$attributes.localisationNamespace}.form.validation.noGroupSelected`
					);
				}

				// Can't deal with this form right now, need to load the group from the server.
				if (angular.isString(groupForm.group)) {
					return addPrincipalUsingIdentifier(permission, groupForm, "name",
						"group",
						"/api/group", $scope.addGroup);
				}

				// Group is already in the permission list.
				if (_find(permission.principals, {
					id: groupForm.group.id
				})) {
					return translateAndReject("group",
						`${$attributes.localisationNamespace}.form.validation.groupAlreadyInACL`
					);
				}

				if (!permission.principals) {
					permission.principals = [];
				}

				permission.principals.push(groupForm.group);
				updateStatus();

				return $q.when(null);
			};

			$scope.removePrincipal = (permission, toRemove) => {
				permission.principals.splice(
					_findIndex(permission.principals, {
						id: toRemove
					}), 1);
				updateStatus();
			};


			const groupCache = {};

			function getGroup(name) {
				if (groupCache[name]) {
					return $q.when(groupCache[name]);
				}
				return $http.get("/api/group", {
					params: {
						name
					}
				})
					.then((response) => {
						groupCache[name] = response.data;
						return response.data;
					});
			}

			$scope.userOptionProvider = _curryRight(loadCompletion)(
				"/api/userCompletion");
			$scope.groupOptionProvider = (text) => {
				const contains = (str) => str.indexOf(text) > -1;
				const deferred = $q.defer();
				const handleGroupCompletion = (suggestions) => {
					const additionalNames =
						_filter(builtinGroups,
							group =>
							// Check if the builtin group matches the filter string
							(contains(group) || $translate.instant(`builtinGroups.${group}`)) &&
							// Skip if this group was already suggested by the server-side
							// suggestion engine
							!_find(suggestions, (suggestion) => suggestion.name === `#{${group}}`)
						);

					$q.all(
							_concat([],
								// Load builtin groups from the server
								_map(additionalNames, name => getGroup(`#{${name}}`)),

								// Server-side suggestions were already loaded, but wrap them in a
								// promise
								_map(suggestions, group => $q.when(group))
							)
						)
						.then((val) => {
							deferred.resolve(val);
						});
				};
				loadCompletion(text, "/api/groupCompletion")
					.then(
						handleGroupCompletion
					);
				return deferred.promise;
			};
			_each(builtinGroups, group => getGroup(`#{${group}}"`));
		}

		return {
			restrict: "E",
			template: require("ng-cache!directives/acl/acl.html"),
			scope: {
				aclGetter: "@",
				aclSetter: "@"
			},
			link
		};
	})
	.directive("aclTypeahead", (FieldHelper, groupNameFilter) => (
		{
			require: "^formFor",
			restrict: "EA",
			template: require("ng-cache!directives/acl/aclTypeahead.html"),
			scope: {
				attribute: "@",
				debounce: "@?",
				disable: "=",
				filterDebounce: "@?",
				filterTextChanged: "&?",
				help: "@?",
				optionGetter: "=",
				typeaheadTemplate: "@",
				tooltip: "@?",
				buttonText: "@?",
				buttonSuccess: "=?"
			},
			link: ($scope, $element, $attributes, formForController) => {
				FieldHelper.manageFieldRegistration($scope, $attributes,
					formForController);
				formForController.registerSubmitButton($scope);
				$scope.selectItem = (item) => {
					$scope.model.bindable = item;
				};
				$scope.formatData = (model) => {
					if (angular.isString(model)) {
						return model;
					}
					if (!model) {
						return "";
					}
					return model.bindable.username || groupNameFilter(model.bindable.name);
				};
			}
		})
	);
