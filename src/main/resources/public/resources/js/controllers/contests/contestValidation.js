module.exports =
	function($q, moment) {
		return {
			name: {
				required: true,
				minlength: 2,
				maxlength: 64
			},
			startTime: {
				custom: function(value) {
					var deferred = $q.defer();
					if (!moment(value, "YYYY-MM-DDTHH:mm:ss.SSSZ", true).isValid()) {
						deferred.reject("invalidDateTimeFormat");
					}
					deferred.resolve();
					return deferred.promise;
				}
			},
			duration: {
				requried: true,
				custom: function(value) {
					var deferred = $q.defer();
					var num = parseInt(value);
					if (num < 1) {
						deferred.reject("minimumDuration");
					}
					deferred.resolve();
					return deferred.promise;
				}
			}
		};
	};
