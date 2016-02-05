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
package org.ng200.openolympus.services.contest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.records.ContestPermissionRecord;
import org.ng200.openolympus.model.OlympusPrincipal;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestACLService extends ContestResultsService {
	@Autowired
	private DSLContext dslContext;

	@Autowired
	private AclService aclService;

	public Map<ContestPermissionType, List<OlympusPrincipal>> getContestPermissionsAndPrincipalData(
			Contest contest) {
		return this.dslContext.select(Tables.CONTEST_PERMISSION.PERMISSION,
				Tables.CONTEST_PERMISSION.PRINCIPAL_ID)
				.from(Tables.CONTEST_PERMISSION)
				.where(Tables.CONTEST_PERMISSION.CONTEST_ID.eq(contest.getId()))
				.fetchGroups(Tables.CONTEST_PERMISSION.PERMISSION,
						(record) -> this.aclService
								.extractPrincipal(record.value2()));
	}

	@Transactional
	public void setContestPermissionsAndPrincipals(Contest contest,
			Map<ContestPermissionType, List<Long>> map) {
		this.dslContext.delete(Tables.CONTEST_PERMISSION)
				.where(Tables.CONTEST_PERMISSION.CONTEST_ID.eq(contest.getId()))
				.execute();
		this.dslContext.batchInsert(
				map.entrySet().stream().flatMap(e -> e.getValue().stream()
						.map(id -> new Pair<>(e.getKey(), id)))
						.map(p -> {
							final ContestPermissionRecord record = new ContestPermissionRecord();
							record.setContestId(contest.getId());
							record.setPrincipalId(p.getSecond());
							record.setPermission(p.getFirst());
							record.attach(this.dslContext.configuration());
							return record;
						}).collect(Collectors.toList()))
				.execute();
	}

}