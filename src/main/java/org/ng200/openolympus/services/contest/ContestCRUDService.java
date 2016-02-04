package org.ng200.openolympus.services.contest;

import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.daos.ContestDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.services.GenericCreateUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestCRUDService extends GenericCreateUpdateRepository {

	@Autowired
	private ContestDao contestDao;

	public long countContests() {
		return this.contestDao.count();
	}

	@Transactional
	public void deleteContest(Contest contest) {
		this.contestDao.delete(contest);
	}

	public Contest getContestByName(final String name) {
		return this.contestDao.fetchOneByName(name);
	}

	@Transactional
	public Contest insertContest(Contest contest) {
		return this.insert(contest, Tables.CONTEST);
	}

	@Transactional
	public Contest updateContest(Contest contest) {
		return this.update(contest, Tables.CONTEST);
	}

}