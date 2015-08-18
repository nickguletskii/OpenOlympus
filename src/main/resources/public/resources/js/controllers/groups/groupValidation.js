"use strict";
module.exports =
	function() {
		return {
			name: {
				required: true,
				minlength: 2,
				maxlength: 64
			}
		};
	};
