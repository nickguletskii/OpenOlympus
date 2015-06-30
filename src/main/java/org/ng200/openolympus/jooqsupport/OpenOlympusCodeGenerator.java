package org.ng200.openolympus.jooqsupport;

import org.jooq.util.ColumnDefinition;
import org.jooq.util.GeneratorStrategy.Mode;
import org.jooq.util.JavaGenerator;
import org.jooq.util.JavaWriter;
import org.jooq.util.TableDefinition;
import org.joor.Reflect;

public class OpenOlympusCodeGenerator extends JavaGenerator {
	@Override
	protected void generateDaoClassFooter(TableDefinition table, JavaWriter out) {
		ColumnDefinition idColumn = table.getColumn("id", true);

		if (idColumn == null)
			return;

		String identityName = getJavaType(idColumn.getType());

		if (!Number.class.isAssignableFrom(Reflect.on(identityName).type()))
			return;

		out.tab(1).println("public %s fetchOneById(String id) {",
				getStrategy().getFullJavaClassName(table, Mode.POJO));
		out.tab(2)
				.println("return fetchOneById(%s.valueOf(id));", identityName);
		out.tab(1).println("}");
	}
}