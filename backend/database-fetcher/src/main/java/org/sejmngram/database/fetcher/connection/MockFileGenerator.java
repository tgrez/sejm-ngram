package org.sejmngram.database.fetcher.connection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MockFileGenerator {

	private static final String filename = "ngrams_mocking.txt";
	private static final Random rand = new Random();
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void generateMockFile(int numberOfRows) throws IOException {
		File file = new File(filename);
		file.delete();
		BufferedWriter out = new BufferedWriter(new FileWriter(file), 32768);
		String blobDelimeter = ";";
		String lineDelimeter = "?";
		for (int i = 0; i < numberOfRows; ++i) {
			int n = 1000;
			StringBuilder blob = new StringBuilder();
			for (int k = 0; k < n; ++k) {
				blob.append(df.format(generateRandomDate()));
				blob.append(blobDelimeter);
				blob.append(rand.nextInt(1000));
				blob.append(blobDelimeter);
				blob.append(rand.nextInt(30));
				blob.append(blobDelimeter);
			}
			StringBuilder line = new StringBuilder();
			line.append(i);
			line.append(lineDelimeter);
			line.append(df.format(getDate(1990)));
			line.append(lineDelimeter);
			line.append(df.format(getDate(2014)));
			line.append(lineDelimeter);
			line.append(n);
			line.append(lineDelimeter);
			line.append(blob.toString());
			out.write(line.toString());
			out.newLine();
		}
		out.close();
	}
	
	private static Date getDate(int year) {
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
	}

	public static Date generateRandomDate() {
        Calendar cal = Calendar.getInstance();
        int year = randBetween(1990, 2013);
        cal.set(Calendar.YEAR, year);
        int dayOfYear = randBetween(1, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return cal.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

}
