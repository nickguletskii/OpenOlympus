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

function getBlockNodes(nodes) {
	var node = nodes[0];
	var endNode = nodes[nodes.length - 1];
	var blockNodes = [node];

	do {
		node = node.nextSibling;
		if (!node) {
			break;
		}
		blockNodes.push(node);
	} while (node !== endNode);

	return angular.element(blockNodes);
}

function parseStateRef(ref, current) {
	var preparsed = ref.match(/^\s*({[^}]*})\s*$/),
		parsed;
	if (preparsed) {
		ref = current + "(" + preparsed[1] + ")";
	}
	parsed = ref.replace(/\n/g, " ").match(/^([^(]+?)\s*(\((.*)\))?$/);
	if (!parsed || parsed.length !== 4) {
		throw new Error("Invalid state ref '" + ref + "'");
	}
	return {
		state: parsed[1],
		paramExpr: parsed[3] || null
	};
}

angular.module("ool.directives").directive("secref", function($animate, $state) {
	return {
		multiElement: true,
		transclude: "element",
		priority: 601,
		terminal: true,
		restrict: "A",
		$$tlb: true,
		link: function($scope, $element, $attr, ctrl, $transclude) {
			var block, childScope, previousElements;
			$scope.$watch($attr.uiSref, function ngIfWatchAction() {

				let ref = parseStateRef($attr.uiSref, $state.current.name);

				let state = $state.get(ref.state, $state.current.name);

				let allowed = state.data.canAccess.apply(_.map(ref.paramExpr, $scope.$eval));

				function ngIf(condition) {
					if (condition) {
						if (!childScope) {
							$transclude(function(clone, newScope) {
								childScope = newScope;
								clone[clone.length++] = document.createComment(" end ngIf: " + $attr.ngIf + " ");
								// Note: We only need the first/last node of the cloned nodes.
								// However, we need to keep the reference to the jqlite wrapper as it might be changed later
								// by a directive with templateUrl when its template arrives.
								block = {
									clone: clone
								};
								$animate.enter(clone, $element.parent(), $element);
							});
						}
					} else {
						if (previousElements) {
							previousElements.remove();
							previousElements = null;
						}
						if (childScope) {
							childScope.$destroy();
							childScope = null;
						}
						if (block) {
							previousElements = getBlockNodes(block.clone);
							$animate.leave(previousElements).then(function() {
								previousElements = null;
							});
							block = null;
						}
					}
				}

				if (!allowed) {
					ngIf(false);
				} else if (allowed.then) {
					allowed.then((val) => ngIf(val));
				} else {
					ngIf(allowed);
				}
			});
		}
	};
});
