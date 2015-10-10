package org.ng200.openolympus;

import java.util.Random;
import java.util.stream.Collectors;

public class TestUtils {
	private static Random random = new Random();

	public static String generateId() {
		return random.ints(0, 16).limit(16)
				.mapToObj(x -> Integer.toHexString(x))
				.collect(Collectors.joining());
	}
}
