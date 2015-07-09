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
var _ = require("lodash");
var angular = require("angular");
var app = require("app");
angular.module('ool.services').factory('ContestService', /*@ngInject*/ function($http) {
    return {
        getContestsPage: function(page) {
            return $http.get('/api/contests', {
                params: {
                    page: page
                }
            }).then(_.property("data"));
        },
        countContests: function() {
            return $http.get('/api/contestsCount').then(_.property("data"));
        },
        getContestInfo: function(contestId) {
            return $http.get('/api/contest/' + contestId).then(_.property("data"));
        },
        countContestParticipants: function(contestId) {
            return $http.get('api/contest/' + contestId + "/participantsCount").then(_.property("data"));
        },
        getContestParticipantsPage: function(contestId, page) {
            return $http.get('api/contest/' + contestId + "/participants", {
                params: {
                    page: page
                }
            }).then(_.property("data"));
        },
        getContestResultsPage: function(contestId, page) {
            return $http.get('api/contest/' + contestId + "/results", {
                params: {
                    page: page
                }
            }).then(_.property("data"));
        },
        getContestEditData: function(contestId) {
            return $http.get("/api/contest/" + contestId + "/edit").then(_.property("data"));
        },
        removeParticipant: function(contestId, id) {
            return $http.delete("/api/contest/" + contestId + "/removeUser", {
                params: {
                    user: id
                }
            });
        },
        removeTask: function(contestId, id) {
            return $http.delete("/api/contest/" + contestId + "/removeTask", {
                params: {
                    task: id
                }
            });
        }
    };
});
