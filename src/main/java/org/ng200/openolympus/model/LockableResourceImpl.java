/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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
