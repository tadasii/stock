package com.zz.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class Cache {

    private static final Logger logger = LoggerFactory.getLogger(Cache.class);

    private static final int MAXIMUN_SIZE = 100000;

    private static LoadingCache<String, Object> instance;

    private Cache() {
    }

    static {
        instance = CacheBuilder.newBuilder().maximumSize(MAXIMUN_SIZE)
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public String load(String key) throws Exception {
                        return null;
                    }
                });
    }

    public static void put(String key, Object value) {
        instance.put(key, value);
    }

    public static Object get(String key) {
        return instance.getIfPresent(key);
    }

    public static void remove(String key) {
        instance.invalidate(key);
    }


}
