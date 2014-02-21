package net.nicoll.scratch.ehcache.jcache.config;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

import org.ehcache.jcache.JCacheConfiguration;
import org.junit.After;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Stephane Nicoll
 */
public class JCacheEhCacheTest extends AbstractAnnotationTest {

	private CacheManager jCacheManager;

	@Override
	protected ApplicationContext getApplicationContext() {
		ApplicationContext context = new AnnotationConfigApplicationContext(EnableCachingConfig.class);
		jCacheManager = context.getBean("jCacheManager", CacheManager.class);
		return context;
	}

	@After
	public void shutdown() {
		jCacheManager.close();
	}

	@Configuration
	@EnableCaching
	static class EnableCachingConfig implements CachingConfigurer {

		@Override
		@Bean
		public org.springframework.cache.CacheManager cacheManager() {
			return new JCacheCacheManager(jCacheManager());
		}

		@Bean
		public CacheManager jCacheManager() {
			CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
			final MutableConfiguration<Object, Object> mutableConfiguration
					= new MutableConfiguration<Object, Object>();
			mutableConfiguration.setStoreByValue(false); // Otherwise value has to be Serializable
			cacheManager.createCache("testCache",
					new JCacheConfiguration<Object, Object>(mutableConfiguration));
			cacheManager.createCache("primary",
					new JCacheConfiguration<Object, Object>(mutableConfiguration));
			cacheManager.createCache("secondary",
					new JCacheConfiguration<Object, Object>(mutableConfiguration));

			return cacheManager;
		}

		@Bean
		public CacheableService<?> service() {
			return new DefaultCacheableService();
		}

		@Bean
		public CacheableService<?> classService() {
			return new AnnotatedClassCacheableService();
		}

		@Override
		@Bean
		public KeyGenerator keyGenerator() {
			return new SimpleKeyGenerator();
		}
	}
}
