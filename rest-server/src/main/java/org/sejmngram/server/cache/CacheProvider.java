package org.sejmngram.server.cache;

import com.google.common.base.Optional;

public interface CacheProvider<T> {

	void store(String key, T value);
	Optional<T> tryGet(String key);
}
