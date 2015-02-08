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
'use strict';

define(['require', 'angular', 'bootstrap', 'filters', 'services', 'directives', 'controllers',
    'ui-route', 'restangular', 'bootstrap-tpls', 'angular-form-validation', 'angular-file-upload',
    'angular-translate', "angular-translate-loader-url", "angular-ui-codemirror", "angular-no-captcha"
], function(require, angular,
    bootstrap, filters, services, directives, controllers) {

    window.CodeMirror = require('codemirror');

    var app = angular.module('ool', ['pascalprecht.translate', 'restangular', 'ui.router',
        'ui.bootstrap', 'ool.filters', 'ool.controllers',
        'ool.services', 'ool.directives', 'ngFormValidation', 'angularFileUpload', 'ui.codemirror',
        'noCAPTCHA'
    ]);
    app.config([
        'formValidationDecorationsProvider',
        'formValidationErrorsProvider',
        function(
            formValidationDecorationsProvider,
            formValidationErrorsProvider
        ) {
            formValidationDecorationsProvider
                .useBuiltInDecorator('bootstrap');
            formValidationErrorsProvider
                .useBuiltInErrorListRenderer('bootstrap');
        }
    ]);

    var dictionary = {
        instantTranslationFunc: {},
        getString: function(name, parameters, language) {
            return dictionary.instantTranslationFunc("validation.errors." + name);
        }
    };

    app.run(function($rootScope, $translate) {
        $rootScope.$on('$translateChangeSuccess',
            function(event, language) {
                dictionary.instantTranslationFunc = $translate.instant;
            });
        $rootScope.availableLanguages = ["en", "ru"]; // TODO: Make this dynamic
        $rootScope.changeLanguage = $translate.use;
    });

    app.config(['$translateProvider', 'formValidationErrorsProvider', '$injector', function($translateProvider, $formValidationErrorsProvider) {
        $translateProvider.useUrlLoader('/translation');
        $translateProvider.preferredLanguage('ru');
        $formValidationErrorsProvider.setDictionary(dictionary);
    }]);

    app.config(function($httpProvider, $locationProvider, $stateProvider) {

        $locationProvider.html5Mode(true);

        $httpProvider.interceptors.push(function($q, $rootScope, $location) {
            return {
                'responseError': function(rejection) {
                    var status = rejection.status;
                    var config = rejection.config;
                    if(!config)
                        return $q.reject(rejection);
                    var method = config.method;
                    var url = config.url;

                    if (status == 401) {
                        $location.path("/login");
                    } else {
                        $rootScope.error = method + " on " + url + " failed with status " + status;
                    }

                    return $q.reject(rejection);
                }
            };
        });

        $httpProvider.interceptors.push(function($q, $rootScope, $location) {
            return {
                'request': function(config) {
                    var isRestCall = config.url.indexOf('rest') === 0;
                    if (isRestCall && angular.isDefined($rootScope.authToken)) {
                        var authToken = $rootScope.authToken;
                        config.headers['X-Auth-Token'] = authToken;
                    }
                    return config || $q.when(config);
                },
                'response': function(response) {
                    if (response.status == 401) {
                        $location.path("/login");
                        return $q.reject(response);
                    }

                    return response;
                },
                'responseError': function(response) {
                    if (response.status == 500) {
                        $rootScope.showErrorModal = true;
                        return $q.reject(response);
                    }

                    if (response.status == 403) {
                        $location.path("/forbidden");
                        return $q.reject(response);
                    }

                    return $q.reject(response);
                }
            };
        });

    });
    app.factory('$exceptionHandler', function($injector) {
        return function(exception, cause) {
            var $rootScope = $injector.get("$rootScope");
            var $timeout = $injector.get("$timeout");
            $timeout(function() {
                $rootScope.showErrorModal = true;
            }, 1);
            throw exception;
        };
    });
    return app;
});
