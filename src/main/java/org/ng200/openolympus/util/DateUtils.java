package org.ng200.openolympus.util;

import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

public class DateUtils {
	public static final DateTimeFormatter ISO_OFFSET_DATE_TIME = new DateTimeFormatterBuilder()
	        .parseCaseInsensitive()
	        .append(DateTimeFormatter.ISO_LOCAL_DATE)
	        .appendLiteral(' ')
	        .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .appendOffset("+HH", "Z")
	        .toFormatter()
	        .withChronology(IsoChronology.INSTANCE)
	        .withResolverStyle(ResolverStyle.STRICT);
}
 