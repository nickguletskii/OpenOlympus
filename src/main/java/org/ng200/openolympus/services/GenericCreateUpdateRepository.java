package org.ng200.openolympus.services;

import org.jooq.DSLContext;
import org.jooq.impl.TableImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Throwables;

public abstract class GenericCreateUpdateRepository {

	@Autowired
	private DSLContext dslContext;

	private static final Logger logger = LoggerFactory
			.getLogger(GenericCreateUpdateRepository.class);

	@SuppressWarnings("unchecked")
	protected <T, R extends UpdatableRecordImpl<?>> T insert(T value,
			TableImpl<R> table) {
		try {
			R record;
			record = table.getRecordType().newInstance();
			record.attach(dslContext.configuration());
			record.from(value);
			record.store();
			return (T) record.into(value.getClass());
		} catch (InstantiationException | IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T, R extends UpdatableRecordImpl<?>> T update(T value,
			TableImpl<R> table) {
		try {
			R record;
			record = table.getRecordType().newInstance();
			record.attach(dslContext.configuration());
			record.from(value);
			record.update();
			return (T) record.into(value.getClass());
		} catch (InstantiationException | IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

}
