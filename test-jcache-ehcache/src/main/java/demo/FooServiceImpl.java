package demo;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheResult;

/**
 * @author Stephane Nicoll
 */
@CacheDefaults(cacheName = "primary")
class FooServiceImpl implements FooService {

    @Override
    @CacheResult
    public String get(long key) {
        return String.valueOf(key);
    }

    @Override
    @CacheResult(cacheName = "default")
    public String defaultGet(long key) {
        return String.valueOf(key);
    }


}

