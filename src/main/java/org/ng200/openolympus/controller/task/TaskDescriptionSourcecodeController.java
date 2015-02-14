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
package org.ng200.openolympus.controller.task;

import static org.ng200.openolympus.SecurityExpressionConstants.IS_ADMIN;

import java.io.IOException;
import java.nio.charset.Charset;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaskDescriptionSourcecodeController extends
TaskFilesystemManipulatingController {

	@Autowired
	private StorageService storageService;

	@PreAuthorize(IS_ADMIN)
	@RequestMapping(value = "/api/taskSourcecode", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getTaskSourcecode(
			@RequestParam(value = "id") final Task task) throws IOException {
		Assertions.resourceExists(task);
		final HttpHeaders responseHeaders = new HttpHeaders();
		final Charset charset = Charset.forName("UTF-8");
		responseHeaders.setContentType(new MediaType("text", "plain", charset));
		return new ResponseEntity<String>(
				this.storageService.getTaskDescriptionSourcecode(task),
				responseHeaders, HttpStatus.OK);
	}
}
