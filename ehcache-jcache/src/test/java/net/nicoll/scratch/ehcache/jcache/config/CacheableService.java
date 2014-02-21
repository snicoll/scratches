package net.nicoll.scratch.ehcache.jcache.config;

/**
 * @author Stephane Nicoll
 */
public interface CacheableService<T> {

	T cache(Object arg1);

	void invalidate(Object arg1);

	void evictEarly(Object arg1);

	void evictAll(Object arg1);

	void evictWithException(Object arg1);

	void evict(Object arg1, Object arg2);

	void invalidateEarly(Object arg1, Object arg2);

	T conditional(int field);

	T unless(int arg);

	T key(Object arg1, Object arg2);

	T varArgsKey(Object... args);

	T name(Object arg1);

	T nullValue(Object arg1);

	T update(Object arg1);

	T conditionalUpdate(Object arg2);

	Number nullInvocations();

	T rootVars(Object arg1);

	T throwChecked(Object arg1) throws Exception;

	T throwUnchecked(Object arg1);

	// multi annotations
	T multiCache(Object arg1);

	T multiEvict(Object arg1);

	T multiCacheAndEvict(Object arg1);

	T multiConditionalCacheAndEvict(Object arg1);

	T multiUpdate(Object arg1);

	TestEntity putRefersToResult(TestEntity arg1);
}
