require("font-awesome-webpack");

var codemirror = require("codemirror");
var moment = require("moment");
var _ = require("lodash");
require("angular-ui-router");
require("angular-ui-bootstrap");
require("angular-ui-bootstrap-tpls");
require("angular-translate");
require("angular-translate-loader-url");
require("angular-ui-codemirror");
require("angular-recaptcha");
require("angular-animate");
require("angular-sanitize");
require("angular-form-for");
require("angular-form-for-bootstrap");
require("ng-file-upload");

var app = require("app");
var filters = require("filters");
var services = require("services");
var directives = require("directives");
var controllers = require("controllers");

window.CodeMirror = codemirror;

app.config(['$translateProvider', function($translateProvider) {
    $translateProvider.useSanitizeValueStrategy('escape');
    $translateProvider.useUrlLoader('/translation');
    $translateProvider.preferredLanguage('ru');
}]);

app.config( /*@ngInject*/ function($httpProvider, $locationProvider, $stateProvider) {

    $locationProvider.html5Mode(true);

    $httpProvider.interceptors.push( /*@ngInject*/ function($q, $rootScope, $location) {
        return {
            'responseError': function(rejection) {
                var status = rejection.status;
                var config = rejection.config;
                if (!config)
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

    $httpProvider.interceptors.push( /*@ngInject*/ function($q, $rootScope, $location) {
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
app.factory('$exceptionHandler', /*@ngInject*/ function($injector) {
    return function(exception, cause) {
        var $rootScope = $injector.get("$rootScope");
        var $timeout = $injector.get("$timeout");
        $timeout(function() {
            $rootScope.showErrorModal = true;
        }, 1);
        console.error(exception);
        throw exception;
    };
});

app.run( /*@ngInject*/ function($rootScope, AuthenticationProvider) {
    $rootScope.security = AuthenticationProvider;

    $rootScope.$on('securityInfoChanged', function() {
        $rootScope.security = AuthenticationProvider;
    });

});
require("ui-setup");
