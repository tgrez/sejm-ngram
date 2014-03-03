package org.sejmngram.database.fetcher.connection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
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
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	private static final Pattern ROW_SPLIT_PATTERN = Pattern.compile("\\?");
	private static final Pattern BLOB_SPLIT_PATTERN = Pattern.compile(";");

	@Override
	public void connect() {

	}

	@Override
	public NgramResponse retrieve(String ngramName, Date from, Date to,
			int partyId) {
		List<Ngram> ngramFromDb = new ArrayList<Ngram>();
		List<ListDate> result = new ArrayList<ListDate>();
		String partyIdString = Integer.toString(partyId);
		LOG.debug("Reading data from file...");
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename),
					32768);
			String line;
			line = br.readLine();
//			while ((line = br.readLine()) != null) {
				String[] columns = ROW_SPLIT_PATTERN.split(line);
				Ngram ngram = new Ngram();
				ngram.setId(Integer.valueOf(columns[0]));
				ngram.setFrom(df.parse(columns[1]));
				ngram.setTo(df.parse(columns[2]));
				ngram.setCount(Integer.valueOf(columns[3]));
				ngram.setBlob(columns[4].getBytes("UTF-8"));
				ngramFromDb.add(ngram);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LOG.debug("Finished reading data from file.");
		LOG.debug("Filtering data...");
		long startTime = System.nanoTime();
		LOG.debug("Filtering start time:\t" + startTime);
		for (Ngram ngram : ngramFromDb) {
				final byte[] byteArray = ngram.getBlob();
				if (byteArray != null) {
					// final ByteBuffer byteBuffer =
					// ByteBuffer.allocate(byteArray.length);
					// byteBuffer.put(byteArray);
					// final CharBuffer charBuffer =
					// byteBuffer.asCharBuffer().asReadOnlyBuffer();
					ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray)
							.asReadOnlyBuffer();
					CharBuffer charBuffer = StandardCharsets.UTF_8.decode(
							byteBuffer).asReadOnlyBuffer();
					String[] tokens = BLOB_SPLIT_PATTERN.split(charBuffer);
					if (tokens.length % 3 != 0) {
						throw new AssertionError("invalid length of blob after splitting");
					}
					for (int i = 0; i < tokens.length; i += 3) {
//						LOG.debug("PartyId=" + tokens[i+2]);
						if (partyIdString.equals(tokens[i + 2])) {
							result.add(createListDate(tokens[i], tokens[i + 1],
									tokens[i + 2]));
						}
					}
			}
		}
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		LOG.debug("Filtering end time:\t" + startTime);
		LOG.debug("Filtering total time:\t" + duration);
		LOG.debug("Filtering total time (s):\t"
				+ TimeUnit.NANOSECONDS.toSeconds(duration));

		List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();
		partiesNgrams.add(new PartiesNgrams(ngramName, result));
		return new NgramResponse(ngramName, partiesNgrams);
	}

	private ListDate createListDate(String date, String poselId, String partyId) {
		return new ListDate(date, 1);
	}
}
