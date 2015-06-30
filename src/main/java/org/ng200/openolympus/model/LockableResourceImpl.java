package org.ng200.openolympus.model;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public abstract class LockableResourceImpl<IdType> implements
		LockableResource<IdType> {

	private static final LoadingCache<Object, ReadWriteLock> locks = CacheBuilder
			.newBuilder().weakValues()
			.build(new CacheLoader<Object, ReadWriteLock>() {
				@Override
				public ReadWriteLock load(Object key) throws Exception {
					return new ReentrantReadWriteLock();
				}
			});

	@Override
	public Lock writeLock() {
		try {
			return locks.get(getId()).writeLock();
		} catch (ExecutionException e) {
			throw new GeneralNestedRuntimeException(
					"Couldn't get write lock: ", e);
		}
	}

	@Override
	public Lock readLock() {
		try {
			return locks.get(getId()).readLock();
		} catch (ExecutionException e) {
			throw new GeneralNestedRuntimeException("Couldn't get read lock: ",
					e);
		}
	}

}
