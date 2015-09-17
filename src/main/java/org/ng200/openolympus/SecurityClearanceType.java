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

public enum SecurityClearanceType {

	/**
	 * This security clearance type is granted to everyone, including users who
	 * are not logged in.
	 */
	ANONYMOUS, /**
	            * This security clearance type is granted to users who were
	            * approved by the administrators.
	            */
	APPROVED_USER, /**
	                * This security clearance type is granted to users who can
	                * view and modify user details.
	                */
	ADMINISTRATIVE_USER, /**
	                      * This security clearance type is granted to users who
	                      * can create contests.
	                      */
	CONTEST_ORGANISER, /**
	                    * This security clearance type is granted to users who
	                    * can create contests.
	                    */
	TASK_SUPERVISOR, /**
	                  * This security clearance type is granted to users who can
	                  * create tasks.
	                  */
	SUPERUSER, /**
	            * This security clearance type is only granted to accounts used
	            * by the internal OpenOlympus services.
	            */
	INTERNAL, /**
	           * This security clearance type requires the user to be logged in,
	           * i.e. not anonymous.
	           */
	LOGGED_IN, /**
	            * Clearance should rarely, if ever, be denied to superusers.
	            * However, in some cases this may prove to be more readable,
	            * e.g. when there is no authorisation and the predicate requests
	            * superuser access.
	            */
	DENIED,

	CHANGE_OTHER_USERS_PASSWORD,

	VIEW_OTHER_USERS_PERSONAL_INFO,

	CHANGE_OTHER_USERS_PERSONAL_INFO,

	ENUMERATE_ALL_USERS,

	APPROVE_USER_REGISTRATIONS,

	DELETE_USER,

	CREATE_GROUP,

	LIST_GROUPS,

	VIEW_ALL_SOLUTIONS,
	
	MANAGE_PRINCIPAL_PERMISSIONS,
	
	VIEW_ARCHIVE_DURING_CONTEST;

}
