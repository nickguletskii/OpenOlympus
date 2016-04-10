import { filters } from "app";
import moment from "moment";

filters.filter("localiseTimestamped", () => (input) => {
	if (!input || !angular.isString(input)) {
		return input;
	}
	return moment(input.replace(/T/, " "))
		.local()
		.format("YYYY-MM-DD HH:mm:ss");
});
