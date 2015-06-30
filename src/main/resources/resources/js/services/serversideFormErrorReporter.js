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
define(['oolutil', 'angular', 'app', 'lodash'], function(Util, angular, app, _) {
    return function(app) {
        app.factory('ServersideFormErrorReporter', function() {
            return function(fieldMap) {
                return {
                    existingErrorKeys: {},
                    report: function(ctrl, field, errors) {
                        if (!ctrl) {
                            return;
                        }
                        var self = this;
                        if (!_.has(self.existingErrorKeys, field))
                            self.existingErrorKeys[field] = [];
                        var errorsToAdd = [];
                        _.forEach(errors, function(error) {
                            if (error.field === field || (!!fieldMap ? _.contains(fieldMap[error.field], field) : false)) {
                                errorsToAdd.push(error);
                            }
                        });
                        _.forEach(self.existingErrorKeys[field], function(errorKey) {
                            if (_(errorsToAdd).map(function(error) {
                                    return error.defaultMessage;
                                }).contains(errorKey))
                                return;
                            ctrl.$setValidity(errorKey, true);
                        });

                        self.existingErrorKeys[field].length = 0;

                        _.forEach(errorsToAdd, function(error) {
                            ctrl.$setValidity(error.defaultMessage, false);
                            self.existingErrorKeys[field].push(error.defaultMessage);
                        });
                        ctrl.$setDirty();
                    }
                };
            };
        });
    };
});
