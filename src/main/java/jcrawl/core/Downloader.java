package jcrawl.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A very very simple downloader.
 */
public class Downloader {
	
	public static void main(final String[] args) throws IOException {
		final long start = System.currentTimeMillis();
		final AtomicLong total = new AtomicLong();
		final Path source = Paths.get(args[0]);
		final Path dir = Paths.get(args[1]);
		
		Files.lines(source).forEach(line -> {
			if (line.startsWith("#") || line.trim().length() == 0) {
				return;
			}

			for (;;) {
				final long elapsed = System.currentTimeMillis() - start;
				// 20 bytes / millisecond
				if (total.get() / elapsed <= 20) {
					break;
				}
				else {
					// Too much data downloaded; sleep for one second.
					sleep(1000);
				}
			}

			final String withoutProtocol = line.replaceFirst("https?\\://", "");
			final Path dest = dir.resolve(withoutProtocol);
			
			if (Files.exists(dest) == false) {
				try (final InputStream is = new URL("http://" + withoutProtocol).openStream()) {
					final Path tempFile = Files.createTempFile(null, null);
					Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
					Files.createDirectories(dest.getParent());
					Files.move(tempFile, dest);
					total.addAndGet(Files.size(dest));
				}
				catch (final FileNotFoundException ex) {
					System.out.format("# FileNotFoundException while processing [%s]\n", line);
					sleep(1000);
				}
				catch (final IOException ex) {
					System.out.format("# IOException while processing [%s]\n", line);
					sleep(1000);
				}

			}

		});
		
	}
	
	private static void sleep(final int ms) {
		try {
			Thread.sleep(1000);
		}
		catch (final InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}
