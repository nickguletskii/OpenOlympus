package org.ng200.openolympus.model;

import java.time.LocalDateTime;

import org.ng200.openolympus.jooq.tables.interfaces.ITask;

public interface ITaskSecurityDescription extends ITask {

	@Override
	public Integer getId();

	@Override
	public String getDescriptionFile();

	@Override
	public String getName();

	@Override
	public String getTaskLocation();

	@Override
	public LocalDateTime getCreatedDate();
}
