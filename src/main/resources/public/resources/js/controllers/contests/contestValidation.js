"use strict";
module.exports =
	function(ValidationService, moment) {
		return {
			name: {
				required: true,
				minlength: 2,
				maxlength: 64
			},
			startTime: {
				custom: ValidationService.toTranslationPromise(function(value) {
					if (!moment(value, "YYYY-MM-DDTHH:mm:ss.SSSZ", true).isValid()) {
						return "validation.failed.invalidDateTimeFormat";
					}
				})
			},
			duration: {
				requried: true,
				custom: ValidationService.toTranslationPromise(function(value) {
					var num = parseInt(value);
					if (num < 1) {
						return "contest.form.validation.minimumDuration";
					}
				})
			}
		};
	};
