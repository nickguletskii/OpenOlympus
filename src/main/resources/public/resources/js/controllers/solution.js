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
var Util = require("oolutil");
var angular = require("angular");
var _ = require("lodash");

module.exports = /*@ngInject*/ function($timeout, $q, $scope, $rootScope, $http, $location,
    $stateParams, SolutionService, data) {

    var DELAY_MIN = 100;
    var DELAY_STEP = 100;
    var DELAY_MAX = 1000;
    var delay = DELAY_MIN;

    function setData(solution) {
        if (!Util.equalsWithoutAngular($scope.solution, solution)) {
            $scope.solution = solution;
            delay = DELAY_MIN;
        }
        return solution;
    }

    function update(callback) {
        SolutionService.getVerdicts($stateParams.solutionId).then(setData).then(callback);
    }
    setData(data);

    var promise;

    function poller() {
        delay = Math.min(delay + DELAY_STEP, DELAY_MAX);
        update(function() {
            promise = $timeout(poller, delay);
        });
    }
    promise = $timeout(poller, delay);
    $scope.$on('$destroy', function() {
        $timeout.cancel(promise);
    });

    // TODO: Websocket integration: server should push verdict updates.
};
