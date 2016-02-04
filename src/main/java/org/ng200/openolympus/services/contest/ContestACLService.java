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

	public Map<ContestPermissionType, List<OlympusPrincipal>> getContestPermissionsAndPrincipalData(
			Contest contest) {
		return this.dslContext.select(Tables.CONTEST_PERMISSION.PERMISSION,
				Tables.CONTEST_PERMISSION.PRINCIPAL_ID)
				.from(Tables.CONTEST_PERMISSION)
				.where(Tables.CONTEST_PERMISSION.CONTEST_ID.eq(contest.getId()))
				.fetchGroups(Tables.CONTEST_PERMISSION.PERMISSION,
						(record) -> aclService
								.extractPrincipal(record.value2()));
	}

}