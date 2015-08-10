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

angular.module("ool.directives").directive("datepickerLocaldate", /*@ngInject*/ function() {
	function link(scope, element, attr, ctrls) {
		var ngModelController = ctrls[0];

		// called with a JavaScript Date object when picked from the datepicker
		ngModelController.$parsers.push(function(viewValue) {
			// undo the timezone adjustment we did during the formatting
			viewValue.setMinutes(viewValue.getMinutes() - viewValue.getTimezoneOffset());
			// we just want a local date in ISO format
			return viewValue.toISOString().substring(0, 10);
		});

		// called with a 'yyyy-mm-dd' string to format
		ngModelController.$formatters.push(function(modelValue) {
			if (!modelValue) {
				return undefined;
			}
			// date constructor will apply timezone deviations from UTC (i.e. if locale is behind UTC 'dt' will be one day behind)
			var dt = new Date(modelValue);
			// 'undo' the timezone offset again (so we end up on the original date again)
			dt.setMinutes(dt.getMinutes() + dt.getTimezoneOffset());
			return dt;
		});
	}
	var directive = {
		restrict: "A",
		require: ["ngModel"],
		link: link
	};
	return directive;
});
