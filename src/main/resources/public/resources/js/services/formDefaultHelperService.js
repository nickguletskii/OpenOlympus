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
var angular = require("angular");
var _ = require("lodash");

angular.module("ool.services").factory("FormDefaultHelperService", /*@ngInject*/ function(ValidationService, $q) {
	const validationRules = {};
	return {
		FormClass: class FormClass {

			constructor($scope, localisationNamespace) {
				if (!$scope || !localisationNamespace || !angular.isString(localisationNamespace)) {
					throw new Error("Incorrect constructor parameters");
				}

				this.submitting = false;
				this.uploadProgress = null;
				this.localisationNamespace = localisationNamespace;
				this.setUpWatches($scope);
				this.setUpData();
			}

			setUpData() {
				this.data = {};
			}

			setUpWatches($scope) {
				this.setUpDirtyWatch($scope);
			}

			setUpDirtyWatch($scope) {
				$scope.$watch(() => (this.form || {}).$dirty, () => {
					if ((this.form || {}).$dirty) {
						this.justSubmitted = false;
					}
				});
			}

			get submitButtonTextNormal() {
				return this.localisationNamespace + ".submitButtonNormal";
			}

			get submitButtonTextWhileSubmitting() {
				return this.localisationNamespace + ".submitButtonSubmitting";
			}

			get submitButtonTextAfterJustSubmitted() {
				return this.localisationNamespace + ".submitButtonJustSubmitted";
			}

			get submitButtonText() {
				if (this.submitting) {
					return this.submitButtonTextWhileSubmitting;
				}
				if (this.justSubmitted) {
					return this.submitButtonTextAfterJustSubmitted;
				}
				return this.submitButtonTextNormal;
			}

			get submitButtonIconBase() {
				return " fa fa-fw fa-lg ";
			}

			get submitButtonIconNormal() {
				return this.submitButtonIconBase + " fa-upload ";
			}

			get submitButtonIconWhileSubmitting() {
				return this.submitButtonIconBase + " fa-cog fa-spinning ";
			}

			get submitButtonIconAfterJustSubmitted() {
				return this.submitButtonIconBase + " fa-check ";
			}

			get submitButtonIcon() {
				if (this.submitting) {
					return this.submitButtonIconWhileSubmitting;
				}
				if (this.justSubmitted) {
					return this.submitButtonIconAfterJustSubmitted;
				}
				return this.submitButtonIconNormal;
			}

			get submitButtonClassBase() {
				return " btn ";
			}

			get submitButtonClassNormal() {
				return this.submitButtonClassBase + " btn-primary ";
			}

			get submitButtonClassWhileSubmitting() {
				return this.submitButtonClassBase + " btn-primary ";
			}

			get submitButtonClassAfterJustSubmitted() {
				return this.submitButtonClassBase + " btn-success ";
			}

			get submitButtonClass() {
				if (this.submitting) {
					return this.submitButtonClassWhileSubmitting;
				}
				if (this.justSubmitted) {
					return this.submitButtonClassAfterJustSubmitted;
				}
				return this.submitButtonClassNormal;
			}

			set progress(progress) {
				if (!progress) {
					this.progressVal = null;
					return;
				}
				this.progressVal = {
					loaded: progress.loaded,
					total: progress.total
				};
			}

			get progress() {
				return this.progressVal;
			}

			preSubmit() {
				this.submitting = true;
				if (!this.progress) {
					this.progress = {};
				}
				this.progress.loaded = 0;
				this.progress.total = 0;
			}

			resetProgress() {
				this.submitting = false;
				this.progress = null;
			}

			postSubmit(serverResponse) {
				this.resetProgress();
				this.justSubmitted = true;
				this.form.$setPristine();
				this.formController.resetFields();
				this.setUpData();
			}

			transformDataForServer(data) {
				return data;
			}

			transformSuccessResponse(response) {
				return response;
			}

			transformFailureResponse(response) {
				return response;
			}

			submit() {
				this.preSubmit();
				console.log(this.data);
				let transformedData = this.transformDataForServer(_.cloneDeep(this.data));
				var deferred = $q.defer();
				ValidationService.postToServer(this.submitUrl, transformedData, (progress) => this.progress = progress)
					.then((response) => {
						this.postSubmit(response);
						deferred.resolve(this.transformSuccessResponse(response));
					}, (response) => deferred.reject(this.transformFailureResponse(response)));
				return deferred.promise;
			}

			get validationRules() {
				return validationRules;
			}
		}
	};
});
