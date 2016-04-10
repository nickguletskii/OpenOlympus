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
	each as _each
} from "lodash";
import { services } from "app";

import flow from "lodash/fp/flow";
import sortBy from "lodash/fp/sortBy";
import map from "lodash/fp/map";
class ACLService {
	/* @ngInject*/
	constructor($http) {
		this.$http = $http;
	}

	cannonicalPermissionForm(permissions) {
		const data = {};
		_each(permissions, (p) => {
			data[p.permission] = flow(
				sortBy("id"),
				map("id")
			)(p.principals);
		});
		return data;
	}
	getACL(url) {
		return this.$http.get(url, {});
	}
	setACL(url, permissions) {
		return this.$http.put(url, this.cannonicalPermissionForm(permissions));
	}
	getGeneralPermissions(prinicpalId) {
		return this.$http.get(
			`/api/admin/principal/${prinicpalId}/generalPermissions`, {});
	}
	setGeneralPermissions(prinicpalId, permissions) {
		return this.$http.put(
			`/api/admin/principal/${prinicpalId}/generalPermissions`,
			permissions);
	}
}

services
	.service("ACLService", ACLService);
