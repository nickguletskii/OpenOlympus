module.exports = {
	username: {
		required: true,
		pattern: /[a-zA-Z0-9_-]+/,
		minlength: 4,
		maxlength: 16
	},
	firstNameMain: {
		required: true,
		minlength: 1
	},
	middleNameMain: {
		required: true,
		minlength: 1
	},
	lastNameMain: {
		required: true,
		minlength: 1
	}
};
