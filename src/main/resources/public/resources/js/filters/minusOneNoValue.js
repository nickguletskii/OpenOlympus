import { filters } from "app";
filters.filter("minusOneNoValue", () => (input) => {
	if (input === -1) {
		return "-";
	}
	return input;
});
