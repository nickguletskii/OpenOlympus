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
var moment = require("moment");
require("services");

/* Filters */

module.exports = angular.module("ool.filters", ["ool.services"])
	.filter("localiseTimestamped", function() {
		return function(input) {
			if (!input || !angular.isString(input)) {
				return input;
			}
			return moment(input.replace(/T/, " ")).local().format("YYYY-MM-DD HH:mm:ss");
		};
	}).filter("minusOneNoValue", function() {
		return function(input) {
			if (input === -1) {
				return "-";
			}
			return input;
		};
	}).filter("pairMap", function() {
		return function(input, key, primary) {
			var x = _.filter(input, function(pair) {
				return pair.first[primary] === key[primary];
			});

			return x[0].second;
		};
	}).filter("captchaErrorToOwnKey", function() {
		return function(input) {
			return "recaptcha." + input;
		};
	}).filter("toKB", function() {
		return function(input) {
			if (input === -1) {
				return -1;
			}
			return Math.floor(input / 1024);
		};
	}).filter("renderFileList", function() {
		return function(input) {
			if (!input) {
				return null;
			}
			let perFile = (file) => file.name;
			if (input.length) {
				return _.chain(input)
					.map(perFile)
					.map((name) => "\"" + name + "\"")
					.join(", ")
					.value();
			}
			return perFile(input);
		};
	});

require("filters/group-name");
