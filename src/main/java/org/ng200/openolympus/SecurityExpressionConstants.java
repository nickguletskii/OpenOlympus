package org.ng200.openolympus;

public class SecurityExpressionConstants {
	public static final String OR = " or ";
	public static final String AND = " and ";
	public static final String IS_ADMIN = " (hasAuthority('SYSTEM') or hasAuthority('SUPERUSER')) ";
	public static final String IS_USER = " hasAuthority('USER') ";
	public static final String NO_CONTEST_CURRENTLY = " @oolsec.noContest() ";
	public static final String USER_IS_OWNER = " (#user.username == authentication.name) ";
	public static final String IS_OWNER = ".username == authentication.name ";
	public static final String TASK_PUBLISHED = " #task.published ";
	public static final String CONTEST_OVER = " @oolsec.isContestOver(#contest) ";
	public static final String THIS_CONTEST_IN_PROGRESS_FOR_USER = " @oolsec.isContestInProgressForUser(#contest, authentication.name) ";
	public static final String SOLUTION_INSIDE_CURRENT_CONTEST_OR_NO_CONTEST = " (@oolsec.noContest() or @oolsec.isSolutionInCurrentContest(#solution)) ";
	public static final String TASK_IN_CONTEST = " (@oolsec.noContest() or @oolsec.isTaskInCurrentContest(#task)) ";
}
