package org.sejmngram.database.fetcher.connection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;
import org.sejmngram.database.fetcher.model.Ngram;

public class DbConnectorMock implements DbConnector {

	private static final Logger LOG = Logger.getLogger(DbConnectorMock.class);

	private static final String filename = "ngrams_mocking.txt";
	private static DateFormat df = new SimpleDateFormat("yyyyMMdd");

	private static final Pattern ROW_SPLIT_PATTERN = Pattern.compile("\\?");

	@Override
	public void connect() {

	}

	@Override
	public NgramResponse retrieve(String ngramName, Date from, Date to,
			int partyId) {
		List<Ngram> ngramFromDb = new ArrayList<Ngram>();
		List<ListDate> result = new ArrayList<ListDate>();
		LOG.debug("Reading data from file...");
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename),
					32768);
			String line;
			char[] cbuf = new char[65536];
			while (br.read(cbuf) > 0) {
				line = new String(cbuf);
				String[] columns = ROW_SPLIT_PATTERN.split(line);
				Ngram ngram = new Ngram();
				// ngram.setId(Integer.valueOf(columns[0]));
				ngram.setNgram(columns[1]);
				ngram.setFrom(df.parse(columns[2]));
				ngram.setTo(df.parse(columns[3]));
				ngram.setCount(Integer.valueOf(columns[4]));
				ngram.setBlob(columns[5].getBytes("UTF-8"));
				ngramFromDb.add(ngram);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LOG.debug("Finished reading data from file.");
		LOG.debug("Filtering data...");

		byte[] date = new byte[8];
		byte[] poselId = new byte[2];
		byte[] partiaId = new byte[1];

		long startTime = System.nanoTime();
		LOG.debug("Filtering start time:\t" + startTime);
		try {
			for (Ngram ngram : ngramFromDb) {
				if (!ngramName.equals(ngram.getNgram()))
					continue;
				final byte[] byteArray = ngram.getBlob();
				if (byteArray != null) {
					for (int k = 0; k < 1024; ++k) {
						System.arraycopy(byteArray, 32 * k, date, 0, 8);
						System.arraycopy(byteArray, 32 * k + 8, poselId, 0, 2);
						System.arraycopy(byteArray, 32 * k + 10, partiaId, 0, 1);
						String dateString;
						dateString = new String(date, "UTF-8");
						int poselIdInt = ByteBuffer.wrap(poselId).getInt();
						int partiaIdInt = ByteBuffer.wrap(partiaId).getInt();
						if (partyId == partiaIdInt) {
							addListDate(result, dateString, poselIdInt,
									partiaIdInt);
						}
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		LOG.debug("Filtering end time:\t" + startTime);
		LOG.debug("Filtering total time:\t" + duration);
		LOG.debug("Filtering total time (s):\t"
				+ TimeUnit.NANOSECONDS.toSeconds(duration));
		LOG.debug("Retrieved " + result.size() + " entries");
		List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();
		partiesNgrams.add(new PartiesNgrams("PartiaB", result));
		return new NgramResponse(ngramName, partiesNgrams);
	}

	private void addListDate(List<ListDate> result, String date, int poselId,
			int partyId) {
		result.add(new ListDate(date, 1));
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void readIdFiles(String partyFilename, String poselFilename) {
		// TODO Auto-generated method stub

	}
}
