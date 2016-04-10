import { filters } from "app";
filters.filter("toKB", () => (input) => {
	if (input === -1) {
		return -1;
	}
	return Math.floor(input / 1024);
});
