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
define(
    ["jquery", "bootstrap", "angular", "app", "oolutil"],
    function($, bootstrap, angular, app, Util) {
        var ie = (function() {
            var undef, v = 3,
                div = document.createElement('div');

            while (div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->', div
                .getElementsByTagName('i')[0])
            ;

            return v > 4 ? v : undef;
        }());

        if (ie) {
            window.navigate("/badbrowser");
        }
        angular
            .module("template/pagination/pagination.html", [])
            .run(
                [
                    "$templateCache",
                    function($templateCache) {
                        $templateCache
                            .put(
                                "template/pagination/pagination.html",
                                "<div class=\"pagination-centered\"><ul class=\"pagination\">\n" + "  <li ng-repeat=\"page in pages\" ng-class=\"{arrow: $first || $last, current: page.active, unavailable: page.disabled}\"><a ui-sref-opts=\"reload\" ui-sref=\"{page: page.number}\">{{page.text}}</a></li>\n" + "</ul>\n" + "</div>");
                    }
                ]);

        angular.module("template/pagination/pagination.html", []).run(["$templateCache", function($templateCache) {
            $templateCache.put("template/pagination/pagination.html",
                "<div class=\"pagination-centered hidden-print\">\n" +
                "  <ul class=\"pagination pagination-md\">\n" +
                "    <li ng-if=\"boundaryLinks\" ng-class=\"{disabled: noPrevious()}\"><a eat-click-if=\"noPrevious()\" ui-sref=\"{page: 1}\">{{getText('first')}}</a></li>\n" +
                "    <li ng-if=\"directionLinks\" ng-class=\"{disabled: noPrevious()}\"><a eat-click-if=\"noPrevious()\" ui-sref=\"{page: page-1}\">{{getText('previous')}}</a></li>\n" +
                "    <li ng-repeat=\"page in pages track by $index\" ng-class=\"{active: page.active}\"><a ui-sref=\"{page: page.number}\">{{page.text}}</a></li>\n" +
                "    <li ng-if=\"directionLinks\" ng-class=\"{disabled: noNext()}\"><a eat-click-if=\"noNext()\" ui-sref=\"{page: page+1}\">{{getText('next')}}</a></li>\n" +
                "    <li ng-if=\"boundaryLinks\" ng-class=\"{disabled: noNext()}\"><a eat-click-if=\"noNext()\" ui-sref=\"{page: totalPages}\">{{getText('last')}}</a></li>\n" +
                "  </ul>\n" +
                "</div>");
        }]);

        app.run(function($rootScope, AuthenticationProvider) {
            $rootScope.security = AuthenticationProvider;
            $rootScope.$on('$stateNotFound',
                function(event, unfoundState, fromState, fromParams) {
                    console.log("Couldn't find state: " + unfoundState);
                });
            $rootScope.$on('securityInfoChanged', function() {
                $rootScope.security = AuthenticationProvider;
            });
            $rootScope.$on('$stateChangeSuccess',
                function(event, toState, toParams, fromState, fromParams) {
                    $rootScope.stateConfig = toState;
                });
        });

        angular.bootstrap(document, ['ool']);

        return {};
    });
