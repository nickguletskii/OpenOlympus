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
/*

Modified Angular.js sourcecode.

BEGIN CODE FROM ANGULAR.JS

The MIT License

Copyright (c) 2010-2015 Google, Inc. http://angularjs.org

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

import getBlockNodes from "directives/angularInternal/getBlockNodes";

module.exports = function($element, $attr, $transclude, $animate) {
	var block, childScope, previousElements;

	return function(condition) {
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
	};
};

/*

END CODE FROM ANGULAR.JS

*/
