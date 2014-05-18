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
package org.ng200.openolympus.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.HtmlUtils;
import org.ng200.openolympus.Pair;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.services.TestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DiagnosticsController {
	@Autowired
	private SolutionService solutionService;

	@Autowired
	private TestingService testingService;

	@Autowired
	private VerdictRepository verdictRepository;

	public boolean isChrootPresent() {
		final File chroot = new File("/usr/chroot");
		return chroot.exists() && chroot.isDirectory();
	}

	public boolean isExecutable(final File file) {
		return this.isFile(file) && FileAccess.isExecutable(file);
	}

	private boolean isFile(final File file) {
		return file != null && file.exists() && !file.isDirectory();
	}

	public boolean isFpcPresent() {
		return this.isExecutable(this.which("ppcx64"));
	}

	public boolean isGppPresent() {
		return this.isExecutable(this.which("g++"));
	}

	public boolean isJavacPresent() {
		return this.isExecutable(this.which("javac"));
	}

	public boolean isLibsandboxPresent() {
		return this.isFile(new File("/usr/lib/libsandbox.so"));
	}

	public boolean isOlrunnerPresent() {
		return this.isExecutable(this.which("olrunner"));
	}

	public boolean isRsyncPresent() {
		return this.isExecutable(this.which("rsync"));
	}

	@RequestMapping(value = "/admin/diagnostics", method = RequestMethod.GET)
	public String showDiagnosticsPage(final Model model) {
		final Map<String, Boolean> statusMap = new TreeMap<String, Boolean>();
		statusMap.put("olrunner", this.isOlrunnerPresent());
		statusMap.put("GCC", this.isGppPresent());
		statusMap.put("FPC", this.isFpcPresent());
		statusMap.put("JDK", this.isJavacPresent());
		statusMap.put("rsync", this.isRsyncPresent());
		statusMap.put("libsandbox", this.isLibsandboxPresent());
		statusMap.put("chroot template", this.isChrootPresent());
		model.addAttribute("statusMap", statusMap);
		model.addAttribute("numberOfPendingVerdicts", this.verdictRepository
				.findByTestedOrderByIdAsc(false).size());
		model.addAttribute(
				"testingServiceInternalErrors",
				this.testingService
				.getInternalErrors()
				.stream()
				.map(p -> new Pair<>(p.getFirst(), HtmlUtils
						.stringToHtml(p.getSecond())))
						.collect(Collectors.toList()));
		return "admin/diagnostics";
	}

	private File which(final String applicationName) {
		try {
			final DefaultExecutor executor = new DefaultExecutor();
			executor.setWatchdog(new ExecuteWatchdog(20000));
			final CommandLine commandLine = new CommandLine("which");
			commandLine.addArgument(applicationName);
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			executor.setStreamHandler(new PumpStreamHandler(
					byteArrayOutputStream));
			executor.execute(commandLine);
			final String fileName = byteArrayOutputStream.toString().trim();
			if (fileName.isEmpty()) {
				return null;
			}
			return new File(fileName);
		} catch (final IOException e) {
			return null;
		}
	}
}
