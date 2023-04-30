package org.jexpress.demo.cache;

import com.google.inject.Singleton;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.integration.cache.AuthTokenCache;
import org.summerboot.jexpress.integration.cache.SimpleLocalCacheImpl;

@Singleton
@Service(binding = AuthTokenCache.class)
public class CacheImpl extends SimpleLocalCacheImpl implements AuthTokenCache {

    @Override
    public void blacklist(String key, String value, long ttlMilliseconds) {
        put(key, value, ttlMilliseconds);
    }

    @Override
    public boolean isBlacklist(String key) {
        String v = get(key);
        return v != null;
    }

}
