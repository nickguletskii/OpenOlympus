/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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
package org.ng200.openolympus.repositories;

import java.util.Date;
import java.util.List;

import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
	Long countByUser(User user);

	Long countByUserAndTask(User user, Task task);

	List<Solution> findByTask(Task task);

	List<Solution> findByTaskAndUserAndTimeAddedBetweenOrderByTimeAddedDesc(
			Task task, User user, Date start, Date end);

	List<Solution> findByUserAndTask(User user, Task task);

	List<Solution> findByUserAndTaskOrderByTimeAddedDesc(
			@Param("user") User user, @Param("task") Task task,
			Pageable pageable);

	List<Solution> findByUserOrderByTimeAddedDesc(@Param("user") User user,
			Pageable pageable);
}