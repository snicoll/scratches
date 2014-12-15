package net.nicoll.scratch.spring.cache;


import java.util.Collection;
import java.util.Collections;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;

/**
 * A sample {@link org.springframework.cache.interceptor.CacheResolver} that
 * demonstrates the runtime resolution of the cache(s) to use.
 * <p>This is a rather simple case that assumes the second parameter of
 * the method invocation is the name of the cache to use
 *
 * @author Stephane Nicoll
 */
public class RuntimeCacheResolver extends AbstractCacheResolver {

	public RuntimeCacheResolver(CacheManager cacheManager) {
		super(cacheManager);
	}

	@Override
	protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
		String cacheName = (String) context.getArgs()[1];
		return Collections.singleton(cacheName);
	}
}
