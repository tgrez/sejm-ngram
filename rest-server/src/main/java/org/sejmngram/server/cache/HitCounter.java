package org.sejmngram.server.cache;

import java.util.Set;

public interface HitCounter {

    void increment(String ngramName);
    Set<String> getTop(int limit);
}