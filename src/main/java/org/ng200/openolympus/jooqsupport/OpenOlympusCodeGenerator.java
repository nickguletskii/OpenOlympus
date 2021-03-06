/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ng200.openolympus.jooqsupport;

import java.time.OffsetDateTime;

import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.Definition;
import org.jooq.util.GeneratorStrategy.Mode;
import org.jooq.util.JavaGenerator;
import org.jooq.util.JavaWriter;
import org.jooq.util.TableDefinition;
import org.joor.Reflect;

public class OpenOlympusCodeGenerator extends JavaGenerator {

	@Override
	protected void generateDaoClassFooter(TableDefinition table,
			JavaWriter out) {
		final ColumnDefinition idColumn = table.getColumn("id", true);

		if (idColumn == null) {
			return;
		}

		final String identityName = this.getJavaType(idColumn.getType());

		if (!Number.class.isAssignableFrom(Reflect.on(identityName).type())) {
			return;
		}

		out.tab(1).println("public %s fetchOneById(String id) {",
				this.getStrategy().getFullJavaClassName(table, Mode.POJO));
		out.tab(2)
				.println("return fetchOneById(%s.valueOf(id));", identityName);
		out.tab(1).println("}");
	}

	@Override
	protected String getJavaType(DataTypeDefinition type, Mode udtMode) {
		if (type.getType().equals("timestamp with time zone")) {
			return OffsetDateTime.class.getName();
		}
		return super.getJavaType(type, udtMode);
	}

	@Override
	protected String getJavaTypeReference(Database database,
			DataTypeDefinition type) {
		if (type.getType().equals("timestamp with time zone")) {
			return CustomTypes.class.getName() + ".OFFSETDATETIME";
		}
		if (type.getType().equals("date")) {
			return "org.jooq.impl.SQLDataType.LOCALDATE";
		}
		return super.getJavaTypeReference(database, type);
	}

	@Override
	protected void printPackage(JavaWriter out, Definition definition,
			Mode mode) {
		out.println(
				"/**\n * The MIT License\n * Copyright (c) 2014-2016 Nick Guletskii\n *\n * Permission is hereby granted, free of charge, to any person obtaining a copy\n * of this software and associated documentation files (the \"Software\"), to deal\n * in the Software without restriction, including without limitation the rights\n * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n * copies of the Software, and to permit persons to whom the Software is\n * furnished to do so, subject to the following conditions:\n *\n * The above copyright notice and this permission notice shall be included in\n * all copies or substantial portions of the Software.\n *\n * THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n * THE SOFTWARE.\n */");
		super.printPackage(out, definition, mode);
	}

}