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
package org.ng200.openolympus.controller.admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BackupController {

	private static final Logger logger = LoggerFactory
			.getLogger(BackupController.class);
	@Value("${dbPassword}")
	private String postgresPassword;
	@Value("${dbAddress}")
	private String postgresAddress;

	@Value("${storagePath}")
	private String storagePath;

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	private Path dumpPostgres() throws ExecuteException, IOException {
		Path dir = FileAccess.createTempDirectory("pgdump");

		CommandLine commandLine = new CommandLine("pg_dump");
		commandLine.addArgument("--data-only");
		commandLine.addArgument(String.format("--host=%s", postgresAddress));
		commandLine.addArgument("--user=postgres");
		commandLine.addArgument("--password");
		commandLine.addArgument(String.format("--format=plain",
				postgresPassword));
		commandLine.addArgument("-f");
		commandLine.addArgument(dir.resolve("db.sql").toAbsolutePath()
				.toString());
		commandLine.addArgument("openolympus");
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(System.out, System.err,
				new ByteArrayInputStream(postgresPassword
						.getBytes(StandardCharsets.US_ASCII))));
		executor.execute(commandLine);
		return dir;
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	@RequestMapping(value = "/api/admin/backup", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody InputStreamResource doBackup()
			throws ExecuteException, IOException, ArchiveException,
			CompressorException {

		PipedOutputStream pipedOutputStream = new PipedOutputStream();
		PipedInputStream pipedInputStream = new PipedInputStream(
				pipedOutputStream);

		TarArchiveOutputStream archive = (TarArchiveOutputStream) new ArchiveStreamFactory()
				.createArchiveOutputStream(ArchiveStreamFactory.TAR,
						pipedOutputStream);
		archive.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

		InputStream inputStream = new InputStream() {

			private boolean closed = false;

			@Override
			public int read() throws IOException {
				if (closed)
					return -1;
				return pipedInputStream.read();
			}

			@Override
			public int read(byte[] b) throws IOException {
				if (closed)
					return -1;
				return pipedInputStream.read(b);
			}

			@Override
			public int read(byte[] b, int off, int len) throws IOException {
				if (closed)
					return -1;
				return pipedInputStream.read(b, off, len);
			}

			@Override
			public long skip(long n) throws IOException {
				return pipedInputStream.skip(n);
			}

			@Override
			public int available() throws IOException {
				return pipedInputStream.available();
			}

			@Override
			public void close() throws IOException {
				archive.flush();
				pipedOutputStream.flush();
				pipedInputStream.close();
				pipedOutputStream.close();
				archive.close();
				super.close();
				closed = true;
			}

			@Override
			public synchronized void mark(int readlimit) {
				pipedInputStream.mark(readlimit);
			}

			@Override
			public synchronized void reset() throws IOException {
				pipedInputStream.reset();
			}

			@Override
			public boolean markSupported() {
				return pipedInputStream.markSupported();
			}

		};
		executor.submit(() -> {
			try {
				Path db = dumpPostgres();
				try {
					putPathIntoArchive(archive, db, db.resolve("db.sql"));
					addStorageFilesToArchive(archive);
					archive.flush();
				} finally {
					FileAccess.deleteDirectoryByWalking(db);
				}

			} catch (Exception e) {
				throw new GeneralNestedRuntimeException(
						"Couldn't create archive: ", e);
			} finally {
				try {
					inputStream.close();
				} catch (Exception e) {
					throw new GeneralNestedRuntimeException(
							"Couldn't close archive stream: ", e);
				}
			}
		});

		return new InputStreamResource(inputStream);
	}

	private void addStorageFilesToArchive(ArchiveOutputStream archive)
			throws IOException {
		Path storage = FileSystems.getDefault().getPath(storagePath);
		FileAccess.walkFileTree(storage, new FileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path file,
					BasicFileAttributes attrs) throws IOException {
				logger.info("Adding: {}", file);
				try {
					putDirectoryIntoArchive(archive, storage, file);
				} catch (Exception e) {
					logger.error("Couldn't add directory: {}", e);
					throw new GeneralNestedRuntimeException(
							"Couldn't add files to archive: ", e);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				logger.info("Adding: {}", file);
				try {
					putPathIntoArchive(archive, storage, file);
				} catch (Exception e) {
					logger.error("Couldn't add file: {}", e);
					throw new GeneralNestedRuntimeException(
							"Couldn't add files to archive: ", e);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)
					throws IOException {
				logger.error("Couldn't visit file: {}, {}", file, exc);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}

		});
	}

	private void putPathIntoArchive(ArchiveOutputStream archive,
			Path relativeTo, Path path) throws IOException {
		TarArchiveEntry dbSql = new TarArchiveEntry(path.toFile(), relativeTo
				.relativize(path).toString());
		archive.putArchiveEntry(dbSql);

		try (InputStream is = Files.newInputStream(path)) {
			IOUtils.copy(is, archive);
		}

		archive.closeArchiveEntry();
	}

	private void putDirectoryIntoArchive(ArchiveOutputStream archive,
			Path relativeTo, Path path) throws IOException {
		TarArchiveEntry dbSql = new TarArchiveEntry(path.toFile(), relativeTo
				.relativize(path).toString());
		archive.putArchiveEntry(dbSql);
		archive.closeArchiveEntry();
	}
}
