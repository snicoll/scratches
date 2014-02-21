package net.nicoll.scratch.ehcache.jcache.config;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * @author Stephane Nicoll
 */
@Cacheable("testCache")
public class AnnotatedClassCacheableService implements CacheableService<Object> {

	private final AtomicLong counter = new AtomicLong();

	public static final AtomicLong nullInvocations = new AtomicLong();

	@Override
	public Object cache(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	public Object conditional(int field) {
		return null;
	}

	@Override
	@Cacheable(value = "testCache", unless = "#result > 10")
	public Object unless(int arg) {
		return arg;
	}

	@Override
	@CacheEvict("testCache")
	public void invalidate(Object arg1) {
	}

	@Override
	@CacheEvict("testCache")
	public void evictWithException(Object arg1) {
		throw new RuntimeException("exception thrown - evict should NOT occur");
	}

	@Override
	@CacheEvict(value = "testCache", allEntries = true)
	public void evictAll(Object arg1) {
	}

	@Override
	@CacheEvict(value = "testCache", beforeInvocation = true)
	public void evictEarly(Object arg1) {
		throw new RuntimeException("exception thrown - evict should still occur");
	}

	@Override
	@CacheEvict(value = "testCache", key = "#p0")
	public void evict(Object arg1, Object arg2) {
	}

	@Override
	@CacheEvict(value = "testCache", key = "#p0", beforeInvocation = true)
	public void invalidateEarly(Object arg1, Object arg2) {
		throw new RuntimeException("exception thrown - evict should still occur");
	}

	@Override
	@Cacheable(value = "testCache", key = "#p0")
	public Object key(Object arg1, Object arg2) {
		return counter.getAndIncrement();
	}

	@Override
	@Cacheable(value = "testCache")
	public Object varArgsKey(Object... args) {
		return counter.getAndIncrement();
	}

	@Override
	@Cacheable(value = "testCache", key = "#root.methodName + #root.caches[0].name")
	public Object name(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Cacheable(value = "testCache", key = "#root.methodName + #root.method.name + #root.targetClass + #root.target")
	public Object rootVars(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@CachePut("testCache")
	public Object update(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@CachePut(value = "testCache", condition = "#arg.equals(3)")
	public Object conditionalUpdate(Object arg) {
		return arg;
	}

	@Override
	public Object nullValue(Object arg1) {
		nullInvocations.incrementAndGet();
		return null;
	}

	@Override
	public Number nullInvocations() {
		return nullInvocations.get();
	}

	@Override
	public Long throwChecked(Object arg1) throws Exception {
		throw new UnsupportedOperationException(arg1.toString());
	}

	@Override
	public Long throwUnchecked(Object arg1) {
		throw new UnsupportedOperationException();
	}

// multi annotations

	@Override
	@Caching(cacheable = {@Cacheable("primary"), @Cacheable("secondary")})
	public Object multiCache(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Caching(evict = {@CacheEvict("primary"), @CacheEvict(value = "secondary", key = "#a0"), @CacheEvict(value = "primary", key = "#p0 + 'A'")})
	public Object multiEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Caching(cacheable = {@Cacheable(value = "primary", key = "#root.methodName")}, evict = {@CacheEvict("secondary")})
	public Object multiCacheAndEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Caching(cacheable = {@Cacheable(value = "primary", condition = "#a0 == 3")}, evict = {@CacheEvict("secondary")})
	public Object multiConditionalCacheAndEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Caching(put = {@CachePut("primary"), @CachePut("secondary")})
	public Object multiUpdate(Object arg1) {
		return arg1;
	}

	@Override
	@CachePut(value = "primary", key = "#result.id")
	public TestEntity putRefersToResult(TestEntity arg1) {
		arg1.setId(Long.MIN_VALUE);
		return arg1;
	}
}
