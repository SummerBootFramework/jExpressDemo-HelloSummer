package org.jexpress.demo.cache;

import com.google.inject.Singleton;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.integration.cache.AuthTokenCache;
import org.summerboot.jexpress.integration.cache.SimpleLocalCacheImpl;

@Singleton
@Service(binding = AuthTokenCache.class)
public class CacheImpl extends SimpleLocalCacheImpl implements AuthTokenCache {

    @Override
    public void putOnBlacklist(String key, String value, long expireInSeconds) {
        put(key, value, (int) expireInSeconds);
    }

    @Override
    public boolean isOnBlacklist(String key) {
        String v = get(key);
        return v != null;
    }

}
