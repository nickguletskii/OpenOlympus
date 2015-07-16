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
var _ = require("lodash");
var angular = require("angular");

var removeAngularElements = function(obj) {
	if (!angular.isObject(obj)) {
		return obj;
	}
	return _.mapValues(_.pick(obj, function(name) {
		if (angular.isString(name)) {
			return name.substring(0, 1) === "$";
		}
		return true;
	}), removeAngularElements);
};

module.exports = {
	emptyToNull: function(obj) {
		return _.mapValues(obj, function(value) {
			if (value === "") {
				return null;
			}
			return value;
		});
	},
	removeAngularElements: removeAngularElements,
	equalsWithoutAngular: function(obj1, obj2) {
		return _.isEqual(removeAngularElements(obj1), removeAngularElements(obj2));
	}
};
