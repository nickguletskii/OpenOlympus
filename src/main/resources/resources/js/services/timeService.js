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
define(['oolutil', 'angular', 'app', 'lodash', 'moment'], function(Util, angular, app, _, moment) {
    return function(app) {
        app.factory('datetime', function($timeout, $http) {

            function timeTo(referenceTime) {
                var currentTime = moment().add(currentServerTimeOffset());
                var from = moment(referenceTime);
                var dur = moment.duration(from.diff(currentTime), "milliseconds");
                return {
                    days: dur.days(),
                    hours: dur.hours(),
                    minutes: dur.minutes(),
                    seconds: dur.seconds()
                };
            }

            function currentServerTime(referenceTime) {
                return moment().add(currentServerTimeOffset());
            }

            var offset = 0;

            $http.get("/api/time").success(
                function(time) {
                    offset = moment(time).diff(moment());
                }
            );

            function currentServerTimeOffset() {
                return offset;
            }

            return {
                timeTo: timeTo,
                currentServerTime: currentServerTime,
                currentServerTimeOffset: currentServerTimeOffset
            };
        });
    };
});