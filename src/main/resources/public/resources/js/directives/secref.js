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

/*

Slightly modified code from ui-router

BEGIN CODE FROM ANGULARUI ROUTER

The MIT License

Copyright (c) 2013-2015 The AngularUI Team, Karsten Sperling

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

*/

function parseStateRef(ref, current) {
	var preparsed = ref.match(/^\s*({[^}]*})\s*$/),
		parsed;
	if (preparsed) {
		ref = current + "(" + preparsed[1] + ")";
	}
	parsed = ref.replace(/\n/g, " ")
		.match(/^([^(]+?)\s*(\((.*)\))?$/);
	if (!parsed || parsed.length !== 4) {
		throw new Error("Invalid state ref '" + ref + "'");
	}
	return {
		state: parsed[1],
		paramExpr: parsed[3] || null
	};
}

/*

END CODE FROM ANGULARUI ROUTER

*/

import {
	directives
} from "app";

import ngIfBuilder from "directives/angularInternal/ngIf";

directives.directive("secref",
	/* @ngInject*/
	($animate, $state, $injector, $rootScope) => ({
		multiElement: true,
		transclude: "element",
		priority: 601,
		terminal: true,
		restrict: "A",
		$$tlb: true,
		link: ($scope, $element, $attr, ctrl, $transclude) => {
			const ngIf = ngIfBuilder($element, $attr,
				$transclude, $animate);

			function ngIfWatchAction() {
				const ref = parseStateRef($attr.uiSref, $state.current.name);

				const state = $state.get(ref.state, $state.current.name);

				const allowed = !angular.isObject(state.data) || !(state.data.canAccess) ||
					$injector.invoke(state.data.canAccess, null, {
						"$refStateParams": $scope.$eval(ref.paramExpr)
					});

				if (!allowed) {
					ngIf(false);
				} else if (allowed.then) {
					allowed.then((val) => {
						ngIf(val);
					});
				} else {
					ngIf(allowed);
				}
			}

			$rootScope.$on("securityInfoChanged", ngIfWatchAction);
			$scope.$watch($attr.uiSref, ngIfWatchAction);
		}
	}));
