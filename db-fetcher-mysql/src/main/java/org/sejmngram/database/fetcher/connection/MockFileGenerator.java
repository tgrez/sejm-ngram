package org.sejmngram.database.fetcher.connection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

public class MockFileGenerator {

	private static final String filename = "ngrams_mocking.txt";
	private static final Random rand = new Random();
	private static DateFormat df = new SimpleDateFormat("yyyyMMdd");
	private static final Logger LOG = Logger.getLogger(MockFileGenerator.class);
	
	private static final String[] ngramArray =  {
		"aaa",
		"bbb",
		"ccc",
		"aa",
		"bla",
		"blabla",
		"co"
		};
	
	public static void generateMockFile(int numberOfRows) throws IOException {
		File file = new File(filename);
		file.delete();
		BufferedWriter out = new BufferedWriter(new FileWriter(file), 32768);
		String lineDelimeter = "?";
		for (int i = 0; i < numberOfRows; ++i) {
			int n = 1000;
			byte[] blob = new byte[32768];
			for (int k = 0; k < n; ++k) {
				System.arraycopy(generateRandomDate(), 0, blob, 32 * k, 8);
				byte [] poselId = new byte[2];
				rand.nextBytes(poselId);
				System.arraycopy(poselId, 0, blob, 32 * k + 8, 2);
				byte[] partiaId = new byte[1];
				rand.nextBytes(partiaId);
				System.arraycopy(partiaId, 0, blob, 32 * k + 10, 1);
			}
			StringBuilder line = new StringBuilder();
			line.append(i);
			line.append(lineDelimeter);
			line.append(getRandomNgram());
			line.append(lineDelimeter);
			line.append(df.format(getDate(1990)));
			line.append(lineDelimeter);
			line.append(df.format(getDate(2014)));
			line.append(lineDelimeter);
			line.append(n);
			line.append(lineDelimeter);
			line.append(new String(blob, "UTF-8"));
			out.write(line.toString());
		}
		out.close();
	}
	
	private static String getRandomNgram() {
		return ngramArray[rand.nextInt(ngramArray.length)];
	}

	private static Date getDate(int year) {
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
	}

	public static byte[] generateRandomDate() throws UnsupportedEncodingException {
        Calendar cal = Calendar.getInstance();
        int year = randBetween(1990, 2013);
        cal.set(Calendar.YEAR, year);
        int dayOfYear = randBetween(1, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.DAY_OF_YEAR, dayOfYear);
        String date = df.format(cal.getTime());
        
        return date.getBytes("UTF-8");
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

}
