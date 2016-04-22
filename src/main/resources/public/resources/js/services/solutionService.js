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
import { services } from "app";
import property from "lodash/fp/property";
import pickBy from "lodash/fp/pickBy";
import {
	isEmpty as _isEmpty
} from "lodash";

class SolutionService {
	/* @ngInject*/
	constructor($http) {
		this.$http = $http;
	}
	countUserSolutions() {
		return this.$http.get("/api/user/solutionsCount")
			.then(property("data"));
	}
	getUserSolutionsPage(page) {
		return this.$http.get("/api/user/solutions", {
			params: {
				page
			}
		}).then(property("data"))
		.then(pickBy((x) => !_isEmpty(x)));
	}
	countSolutions() {
		return this.$http.get("/api/admin/solutionsCount")
			.then(property("data"));
	}
	getSolutionsPage(page) {
		return this.$http.get("/api/admin/solutions", {
			params: {
				page
			}
		}).then(property("data"))
		.then(pickBy((x) => !_isEmpty(x)));
	}
	getVerdicts(solutionId) {
		return this.$http.get(`/api/solution/${solutionId}`)
			.then(property("data"))
			.then(pickBy((x) => !_isEmpty(x)));
	}
}

services
	.service("SolutionService", SolutionService);
