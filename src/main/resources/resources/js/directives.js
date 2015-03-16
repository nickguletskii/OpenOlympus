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

define(['angular', 'services', 'lodash', 'moment', 'textile'], function(angular, services, _, moment, textile) {

    /* Directives */

    angular.module('ool.directives', ['ool.services']).directive('eatClickIf', ['$parse', '$rootScope',
        function($parse, $rootScope) {
            return {
                // this ensure eatClickIf be compiled before ngClick
                priority: 100,
                restrict: 'A',
                compile: function($element, attr) {
                    var fn = $parse(attr.eatClickIf);
                    return {
                        pre: function link(scope, element) {
                            var eventName = 'click';
                            element.on(eventName, function(event) {
                                var callback = function() {
                                    if (fn(scope, {
                                            $event: event
                                        })) {
                                        // prevents ng-click to be executed
                                        event.stopImmediatePropagation();
                                        // prevents href 
                                        event.preventDefault();
                                        return false;
                                    }
                                };
                                if ($rootScope.$$phase) {
                                    scope.$evalAsync(callback);
                                } else {
                                    scope.$apply(callback);
                                }
                            });
                        },
                        post: function() {}
                    }
                }
            }
        }
    ]).directive("oolinput", ['$compile', function($compile) {
        return {
            restrict: "A",
            link: function(scope, element, attrs, ctrl, transclude) {
                var wrapper = angular.element('<div class="form-group" />');
                var label = angular.element('<label class="control-label"></label>');
                var name = attrs.ngModel.match(/(^.*\.|^)([a-zA-Z0-9_-]+)$/)[2];
                label.attr({
                    "for": name
                }).html("{{'" + attrs.label + "' | translate}}");
                element.after(wrapper);
                element.removeAttr("oolinput");
                attrs.$set("name", name);
                wrapper.append(label);
                var children = element.parent().children();
                wrapper.append(element);
                $compile(label)(scope);
                scope.$on("$destroy", function() {
                    wrapper.after(element);
                    wrapper.remove();
                });
            }
        };
    }]).directive('ngUsername', function($q, $timeout) {
        var USERNAME_REGEXP = /^[a-zA-Z0-9_-]+$/;

        return {
            require: 'ngModel',
            restrict: 'A',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$validators.username = function(modelValue, viewValue) {
                    return USERNAME_REGEXP.test(viewValue);
                };
            }
        };
    }).directive('ngDateTime', function($q, $timeout) {
        return {
            require: 'ngModel',
            restrict: 'A',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$validators.dateTime = function(modelValue, viewValue) {
                    return moment(viewValue, "YYYY-MM-DDTHH:mm:ss.SSSZ", true).isValid();
                };
            }
        };
    }).directive('ngLength', function($q, $timeout) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$validators.length = function(modelValue, viewValue) {
                    if (!modelValue)
                        return true;

                    var tokens = attrs.ngLength.split(/,/);
                    var minLength = parseInt(tokens[0]);
                    var maxLength = parseInt(tokens[1]);
                    if (isNaN(maxLength))
                        maxLength = Number.MAX_VALUE;
                    return modelValue.length >= minLength && modelValue.length <= maxLength;
                };
            }
        };
    }).directive('ngPasswordsMatchSlave', function($q, $timeout, ServersideFormErrorReporter, ValidationService) {
        return {
            restrict: 'A',
            scope: {

            },
            require: ['ngModel', '^form'],
            link: function(scope, elem, attr, controllers) {
                var ctrl = controllers[0];
                var formCtrl = controllers[1];
                $timeout(function() {
                    ctrl.$validators.passwordsMatch = function(modelValue) {
                        return formCtrl[attr.ngPasswordsMatchSlave].$modelValue == modelValue;
                    };
                });
                ctrl.$validators.passwordsMatch = function(modelValue) {
                    return true;
                };
            }
        };
    }).directive('ngPasswordsMatchMaster', function($q, $timeout, ServersideFormErrorReporter, ValidationService) {
        return {
            restrict: 'A',
            scope: {

            },
            require: ['ngModel', '^form'],
            link: function(scope, elem, attr, controllers) {
                var ctrl = controllers[0];
                var formCtrl = controllers[1];
                scope.$watch(function() {
                    return ctrl.$viewValue;
                }, function(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    formCtrl[attr.ngPasswordsMatchMaster].$setViewValue("", '', true);
                    formCtrl[attr.ngPasswordsMatchMaster].$render();
                });
            }
        };
    }).directive('datepickerLocaldate', ['$parse', function($parse) {
        var directive = {
            restrict: 'A',
            require: ['ngModel'],
            link: link
        };
        return directive;

        function link(scope, element, attr, ctrls) {
            var ngModelController = ctrls[0];

            // called with a JavaScript Date object when picked from the datepicker
            ngModelController.$parsers.push(function(viewValue) {
                // undo the timezone adjustment we did during the formatting
                viewValue.setMinutes(viewValue.getMinutes() - viewValue.getTimezoneOffset());
                // we just want a local date in ISO format
                return viewValue.toISOString().substring(0, 10);
            });

            // called with a 'yyyy-mm-dd' string to format
            ngModelController.$formatters.push(function(modelValue) {
                if (!modelValue) {
                    return undefined;
                }
                // date constructor will apply timezone deviations from UTC (i.e. if locale is behind UTC 'dt' will be one day behind)
                var dt = new Date(modelValue);
                // 'undo' the timezone offset again (so we end up on the original date again)
                dt.setMinutes(dt.getMinutes() + dt.getTimezoneOffset());
                return dt;
            });
        }
    }]).directive('ngReallyClick', ['$modal',
        function($modal) {

            var ModalInstanceCtrl = function($scope, $modalInstance) {
                $scope.ok = function() {
                    $modalInstance.close();
                };

                $scope.cancel = function() {
                    $modalInstance.dismiss('cancel');
                };
            };

            return {
                restrict: 'A',
                scope: {
                    ngReallyClick: "&",
                    item: "="
                },
                link: function(scope, element, attrs) {
                    element.bind('click', function() {
                        var message = attrs.ngReallyMessage;

                        var modalHtml = '<div class="modal-body">' + message + '</div>';
                        modalHtml += '<div class="modal-footer"><button class="btn btn-danger" ng-click="ok()">' + attrs.ngReallyYesButton + '</button><button class="btn btn-default" ng-click="cancel()">{{\'confirmationDialog.cancel\' | translate}}</button></div>';

                        var modalInstance = $modal.open({
                            template: modalHtml,
                            controller: ModalInstanceCtrl
                        });

                        modalInstance.result.then(function() {
                            scope.ngReallyClick({
                                item: scope.item
                            });
                        }, function() {});

                    });

                }
            };
        }
    ]).directive("lang", ['$compile', '$translate', '$rootScope', function($compile, $translate, $rootScope) {
        return {
            restrict: "AE",
            link: function(scope, element, attrs, ctrl, transclude) {
                var name = attrs.lang;

                function changeTranslation() {
                    if (name && name !== $rootScope.currentLanguage) {
                        element[0].hidden = true;
                    } else {
                        element[0].hidden = false;
                    }
                }
                changeTranslation();
                $rootScope.$watch("currentLanguage", changeTranslation);
            }
        };
    }]).directive('dynamic', function($compile) {
        return {
            restrict: 'A',
            replace: true,
            link: function(scope, element, attrs) {
                scope.$watch(attrs.dynamic, function(html) {
                    element.html(html);
                    $compile(element.contents())(scope);
                });
            }
        };
    }).directive('textile', function($compile) {
        return {
            restrict: 'EA',
            replace: true,
            link: function(scope, element, attrs) {
                scope.$watch(attrs.textile, function(txtl) {
                    element.html(textile(txtl || ""));
                });
            }
        };
    });
});
