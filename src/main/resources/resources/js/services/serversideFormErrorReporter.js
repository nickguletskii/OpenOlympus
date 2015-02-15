define(requirements, function(angular, app, _) {
    return function(app) {
        app.factory('ServersideFormErrorReporter', function() {
            return function(fieldMap) {
                return {
                    existingErrorKeys: {},
                    report: function(ctrl, field, errors) {
                        if (!ctrl) {
                            return;
                        }
                        ctrl.forceValidation = true;
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
