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
define(['lodash'], function(_) {
    String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };

    String.prototype.format = function() {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] != 'undefined' ? args[number] : match;
        });
    };

    var removeAngularElements = function(obj) {
        return _.mapValues(_.pick(obj, function(name) {
            if (typeof name == "string")
                return name.substring(0, 1) == "$";
            return true;
        }), removeAngularElements);
    };

    var util = {
        getURLParameters: function() {
            var result = {},
                tmp = [];
            location.search.substr(1).split("&").forEach(function(item) {
                tmp = item.split("=");
                result[tmp[0]] = decodeURIComponent(tmp[1]);
            });
            return result;
        },
        mergeObjects: function(obj1, obj2) {
            var obj3 = {};
            for (var attrname in obj1) {
                obj3[attrname] = obj1[attrname];
            }
            for (var attrname in obj2) {
                obj3[attrname] = obj2[attrname];
            }
            return obj3;
        },
        emptyToNull: function(obj) {
            return _.mapValues(obj, function(value) {
                if (value === "")
                    return null;
                return value;
            });
        },
        removeAngularElements: removeAngularElements,
        equalsWithoutAngular: function(obj1, obj2) {
            return _.isEqual(removeAngularElements(obj1), removeAngularElements(obj2));
        }
    };
    return util;
});
