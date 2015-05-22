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
package org.ng200.openolympus;

public class SecurityExpressionConstants {
	public static final String OR = " or ";
	public static final String AND = " and ";
	public static final String IS_ADMIN = " (hasAuthority('SYSTEM') or hasAuthority('SUPERUSER')) ";
	public static final String IS_USER = " hasAuthority('USER') ";
	public static final String NO_CONTEST_CURRENTLY = " @oolsec.noContest() ";
	public static final String USER_IS_OWNER = " (#user.username == authentication.name) ";
	public static final String IS_OWNER = ".username == authentication.name ";
	public static final String TASK_PUBLISHED = " (#task.published or @oolsec.isTaskInCurrentContest(#task)) ";
	public static final String CONTEST_OVER = " @oolsec.isContestOver(#contest) ";
	public static final String THIS_CONTEST_IN_PROGRESS_FOR_USER = " @oolsec.isContestInProgressForUser(#contest, authentication.name) ";
	public static final String TASK_IN_CONTEST = " (@oolsec.noContest() or (@oolsec.isTaskInCurrentContest(#task) and @oolsec.isContestInProgressForUser(@oolsec.getCurrentContest(), authentication.name))) ";
}
