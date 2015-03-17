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
package org.ng200.openolympus.services;

import java.nio.file.FileSystems;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.ng200.openolympus.SharedTemporaryStorageFactory;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.tasks.TaskContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskContainerCache {
	private final ConcurrentMap<Task, TaskContainer> taskContainers;
	private final StorageService storageService;
	private SharedTemporaryStorageFactory sharedTemporaryStorageFactory = null;

	@Autowired
	private SolutionService solutionService;

	private final ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor();

	@Autowired
	public TaskContainerCache(final StorageService storageService) {
		this.taskContainers = new ConcurrentHashMap<>();
		this.storageService = storageService;
		this.executorService.scheduleAtFixedRate(() -> {
			this.taskContainers.forEach((id, taskContainer) -> taskContainer
					.collectGarbage(this.solutionService));
		}, 10, 10, TimeUnit.SECONDS);
	}

	public TaskContainer getTaskContainerForTask(final Task task) {
		if (this.sharedTemporaryStorageFactory == null) {
			this.sharedTemporaryStorageFactory = new SharedTemporaryStorageFactory(
					FileSystems.getDefault().getPath(
							this.storageService.getStoragePath()));
		}
		return this.taskContainers.computeIfAbsent(
				task,
				(key) -> {
					try {
						return new TaskContainer(this.storageService
								.getTaskJudgeFile(task),
								this.sharedTemporaryStorageFactory,
								this.executorService);
					} catch (final Exception e) {
						throw new RuntimeException(
								"Couldn't load task container", e);
					}
				});
	}

	public void clear() {
		taskContainers.clear();
	}
}
