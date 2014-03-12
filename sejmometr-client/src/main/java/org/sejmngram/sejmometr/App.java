package org.sejmngram.sejmometr;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.common.Configuration;
import org.sejmngram.sejmometr.client.RestClient;
import org.sejmngram.sejmometr.transform.TransformExecutor;

public class App {
	
	private static final RestClient sejmometrRestClient = new RestClient(
			Configuration.getInstance().getSejmometrWystapienia());
	private static final String downloadedDir = Configuration.getInstance().getSejmometrDownloadedDir();
	
	public static void main(String[] args) {
		try {
			Map<Integer, String> responses = new HashMap<Integer, String>();
			for (Integer id : Configuration.getInstance().getSejmometrIds()) {
				String responseString = sejmometrRestClient.get(id);
				writeToFile(id, responseString);
				responses.put(id, responseString);
			}
			TransformExecutor.process(responses);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeToFile(Integer id, String responseString) throws IOException {
		File dir = new File(downloadedDir);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
		String tempDownloadDir = downloadedDir;
		if (!tempDownloadDir.endsWith(File.separator)) {
			tempDownloadDir += File.separator;
		}
		String filename = tempDownloadDir + "wystapienie" + Integer.toString(id) + ".json";
		FileUtils.writeStringToFile(new File(filename), responseString, "UTF-8", false);
	}
}
