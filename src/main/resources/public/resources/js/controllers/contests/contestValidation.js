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
module.exports =
	function(ValidationService, moment) {
		return {
			name: {
				required: true,
				minlength: 2,
				maxlength: 64
			},
			startTime: {
				custom: ValidationService.toTranslationPromise(function(value) {
					if (!moment(value, "YYYY-MM-DDTHH:mm:ss.SSSZ", true).isValid()) {
						return "validation.failed.invalidDateTimeFormat";
					}
				})
			},
			duration: {
				requried: true,
				custom: ValidationService.toTranslationPromise(function(value) {
					var num = parseInt(value);
					if (num < 1) {
						return "contest.form.validation.minimumDuration";
					}
				})
			}
		};
	};
