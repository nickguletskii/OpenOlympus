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
var angular = require("angular");
var _ = require("lodash");

let builtinGroups = ["superusers", "allUsers"]; // TODO: move to service

angular.module("ool.directives")
	.run( /*@ngInject*/ function($templateCache) {
		$templateCache.put("directives/aclEditor/typeaheadUserItem.html", require("ng-cache!directives/acl-type-ahead-item-user.html"));
		$templateCache.put("directives/aclEditor/typeaheadGroupItem.html", require("ng-cache!directives/acl-type-ahead-item-group.html"));
	})
	.directive("aclEditor", /*@ngInject*/ function(ACLService, $q, $http, $translate) {

		// Returns a promise that rejects with an object where attribute = translated key
		function translateAndReject(attribute, key) {
			var deferred = $q.defer();
			$translate([key])
				.then((map) =>
					deferred.reject(_.set({}, attribute, map[key]))
				);
			return deferred.promise;
		}

		function link($scope, $element, $attributes) {
			function updateStatus() {
				$scope.changesCommitted =
					_.isEqual(
						ACLService.cannonicalPermissionForm($scope.permissions),
						$scope.origPermissions
					);
			}

			$scope.refresh = () =>
				ACLService.getACL($attributes.aclGetter).success((permissions) => {
					$scope.permissions = _.map(permissions, perm => _.assign(perm, {
						user: {}, // Form data object for adding users
						group: {} // Form data object for adding groups
					}));
					$scope.origPermissions = ACLService.cannonicalPermissionForm(_.cloneDeep($scope.permissions));
					updateStatus();
				});

			$scope.commit = () => {
				$scope.committing = true;
				ACLService.setACL($attributes.aclSetter, $scope.permissions).success(() => {
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
			function addPrincipalUsingIdentifier(permission, form, paramName, attributeName, providerURL, objectConsumer) {
				var deferred = $q.defer();
				$http.get(providerURL, {
					params: _.set({}, paramName, form[attributeName])
				}).then((response) => {
					// The objectConsumer is a function that processes form data. Essentially, this method exists solely to provide the objectConsumer with a workable formData.
					objectConsumer(
							permission,
							_.set({}, attributeName, response.data)
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
				var deferred = $q.defer();
				$http.get(url, {
					params: {
						term: filterText
					}
				}).then((response) => {
					deferred.resolve(response.data);
				}, () => {
					deferred.reject();
					throw new Error("Principal completion failed.");
				});
				return deferred.promise;
			}

			// Add user from form data.
			$scope.addUser = function(permission, userForm) {

				if (!userForm.user) {
					return translateAndReject("user", $attributes.localisationNamespace + ".form.validation.noUserSelected");
				}

				// Can't deal with this form right now, need to load the user from the server.
				if (angular.isString(userForm.user)) {
					return addPrincipalUsingIdentifier(permission, userForm, "username", "user", "/api/user", $scope.addUser);
				}

				// User is already in the permission list.
				if (_.find(permission.principals, (val) => val.id === userForm.user.id)) {
					return translateAndReject("user", $attributes.localisationNamespace + ".form.validation.userAlreadyInACL");
				}

				permission.principals.push(userForm.user);
				updateStatus();

				return $q.when(null);
			};

			$scope.addGroup = function(permission, groupForm) {

				if (!groupForm.group) {
					return translateAndReject("group", $attributes.localisationNamespace + ".form.validation.noGroupSelected");
				}

				// Can't deal with this form right now, need to load the group from the server.
				if (angular.isString(groupForm.group)) {
					return addPrincipalUsingIdentifier(permission, groupForm, "name", "group",
						"/api/group", $scope.addGroup);
				}

				// Group is already in the permission list.
				if (_.find(permission.principals, (val) => val.id === groupForm.group.id)) {
					return translateAndReject("group", $attributes.localisationNamespace + ".form.validation.groupAlreadyInACL");
				}

				permission.principals.push(groupForm.group);
				updateStatus();

				return $q.when(null);
			};

			$scope.removePrincipal = function(permission, toRemove) {
				permission.principals.splice(_.findIndex(permission.principals, (principal) => principal.id === toRemove), 1);
				updateStatus();
			};


			var groupCache = {};

			function getGroup(name) {
				if (groupCache[name]) {
					return $q.when(groupCache[name]);
				}
				return $http.get("/api/group", {
					params: {
						name: name
					}
				}).then((response) => {
					groupCache[name] = response.data;
					return response.data;
				});
			}

			$scope.userOptionProvider = _.curryRight(loadCompletion)("/api/userCompletion");
			$scope.groupOptionProvider = function(text) {
				var contains = (str) => str.indexOf(text) > -1;
				var deferred = $q.defer();
				loadCompletion(text, "/api/groupCompletion").then(
					(suggestions) => {
						var additionalNames =
							_(builtinGroups)
							.filter(group =>
								// Check if the builtin group matches the filter string
								(contains(group) || $translate.instant("builtinGroups." + group)) &&
								// Skip if this group was already suggested by the server-side suggestion engine
								!_.find(suggestions, (suggestion) => suggestion.name === "#{" + group + "}"))

						.value();

						$q.all(
							_([])
							.concat(
								// Load builtin groups from the server
								_.map(additionalNames, name => getGroup("#{" + name + "}")),

								// Server-side suggestions were already loaded, but wrap them in a promise
								_(suggestions).map(group => $q.when(group)).value()
							)
							.value()
						).then((val) => {
							deferred.resolve(val);
						});
					}
				);
				return deferred.promise;
			};
			_(builtinGroups).forEach(group => getGroup("#{" + group + "}"));
		}

		return {
			restrict: "E",
			template: require("ng-cache!directives/acl.html"),
			scope: {
				aclGetter: "@",
				aclSetter: "@"
			},
			link: link
		};
	})
	.directive("aclTypeAhead", /*@ngInject*/ function(ACLService, FieldHelper, groupNameFilter) {
		return {
			require: "^formFor",
			restrict: "EA",
			template: require("ng-cache!directives/acl-type-ahead.html"),
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
				buttonText: "@?"
			},
			link: function($scope, $element, $attributes, formForController) {
				FieldHelper.manageFieldRegistration($scope, $attributes, formForController);
				formForController.registerSubmitButton($scope);
				$scope.selectItem = function(item) {
					$scope.model.bindable = item;
				};
				$scope.formatData = function(model) {
					if (angular.isString(model)) {
						return model;
					}
					if (!model) {
						return "";
					}
					return model.bindable.username || groupNameFilter(model.bindable.name);
				};
			}
		};
	});
