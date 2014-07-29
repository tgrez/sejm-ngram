package org.sejmngram.server.cache;

import java.util.Set;

public interface Counter {

	void increment(String ngramName);
	Set<String> getTop(int limit);
}