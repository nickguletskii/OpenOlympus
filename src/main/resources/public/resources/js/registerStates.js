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
	states
} from "app";
import {
	each as _each,
	clone as _clone,
	pickBy as _pickBy,
	concat as _concat,
	values as _values
} from "lodash";
import "services";

import * as normalStates from "states/normalStates";
import * as plainStates from "states/plainStates";
import * as modalStates from "states/modalStates";

const WIDTH_CLASS_MAP = {
	"narrow": "col-md-6 col-md-offset-3",
	"wide": "col-md-12 col-md-offset-0"
};

const takeStates = states => _values(_pickBy(states, state =>
	state.name));

const stateList = _concat(takeStates(normalStates), takeStates(plainStates),
	takeStates(modalStates));

states.config(/* @ngInject*/ ($stateProvider) => {
	_each(stateList, (state) => {
		const stateConfig = _clone(state);
		if (state.customWidth && WIDTH_CLASS_MAP[state.customWidth]) {
			stateConfig.customWidth = WIDTH_CLASS_MAP[state.customWidth];
		}
		$stateProvider.state(stateConfig.name, stateConfig);
	});
});

module.exports = states;
