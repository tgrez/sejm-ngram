package org.sejmngram.database.fetcher;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.sejmngram.database.fetcher.DbManager.A;
import org.sejmngram.database.fetcher.connection.MockFileGenerator;

public class App {
	
	private static final Logger LOG = Logger.getLogger(App.class);

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Use one of following command line parameters:");
			System.out.println("\tgenerate");
			System.out.println("\tread");
			return;
		}
		System.out.println("Using option: " + args[0]);
		if ("generate".equals(args[0])) {
			if (args.length > 1 && args[1] != null) {
				MockFileGenerator.generateMockFile(Integer.valueOf(args[1]));
			} else {
				MockFileGenerator.generateMockFile(30);
			}
		} else if ("read".equals(args[0])) {
			testBlobReading();
		}
	}

	private static void testBlobReading() {
		int partyId = 10;
		DbManager db = new DbManager();
		long startTime = System.nanoTime();
		LOG.debug("Start time:\t" + startTime);
		db.filterByParty("whatever", partyId);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		LOG.debug("End time:\t" + endTime);
		LOG.debug("Total time:\t" + duration);
		LOG.debug("Total time (s):\t" + TimeUnit.NANOSECONDS.toSeconds(duration));
	}

}
