var angular = require("angular");
var app = require("app");

module.exports = ['$modal',
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
]