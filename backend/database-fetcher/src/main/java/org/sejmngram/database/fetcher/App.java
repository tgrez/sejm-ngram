package org.sejmngram.database.fetcher;

import java.io.IOException;

import org.sejmngram.database.fetcher.connection.MockFileGenerator;

public class App {

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
		filterByParty(10);
	}

}
