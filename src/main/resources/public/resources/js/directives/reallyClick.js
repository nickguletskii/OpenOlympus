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
	directives
} from "app";

directives.directive("ngReallyClick",
	/* @ngInject*/
	($modal) => {
		const ModalInstanceCtrl = ($scope, $modalInstance) => {
			$scope.ok = () => {
				$modalInstance.close();
			};

			$scope.cancel = () => {
				$modalInstance.dismiss("cancel");
			};
		};

		return {
			restrict: "A",
			scope: {
				ngReallyClick: "&",
				item: "="
			},
			link: (scope, element, attrs) => {
				element.bind("click", () => {
					const message = attrs.ngReallyMessage;
					const modalHtml =
						`<div class="modal-body">${message}</div>
						<div class="modal-footer">
							<button class="btn btn-danger" ng-click="ok()">
								${attrs.ngReallyYesButton}
							</button>
							<button class="btn btn-default" ng-click="cancel()">
								{{'confirmationDialog.cancel' | translate}}
							</button>
						</div>`;

					const modalInstance = $modal.open({
						template: modalHtml,
						controller: ModalInstanceCtrl
					});

					modalInstance.result.then(() => {
						scope.ngReallyClick({
							item: scope.item
						});
					}, () => {});
				});
			}
		};
	});
