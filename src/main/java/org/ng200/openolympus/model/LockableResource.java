package org.ng200.openolympus.model;

import java.util.concurrent.locks.Lock;

public interface LockableResource<IdType> {
	public IdType getId();

	public Lock readLock();

	public Lock writeLock();
}
