import { filters } from "app";
import map from "lodash/fp/map";
import flow from "lodash/fp/flow";
import join from "lodash/fp/flow";

filters.filter("renderFileList", () => (input) => {
	if (!input) {
		return null;
	}
	const perFile = (file) => file.name;
	if (input.length) {
		return flow(
				map(perFile),
				map((name) => `"${name}"`),
				join(", ")
			)(input);
	}
	return perFile(input);
});
