import { filters } from "app";
import { filter as _filter } from "lodash";
filters.filter("pairMap", () => (input, key, primary) => {
	const x = _filter(input, (pair) => pair.first[primary] === key[primary]);
	return x[0].second;
});
