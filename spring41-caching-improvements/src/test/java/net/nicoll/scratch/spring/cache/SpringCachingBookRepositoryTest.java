package net.nicoll.scratch.spring.cache;

import static java.util.Arrays.*;
import static net.nicoll.scratch.spring.cache.BookRepositoryTestUtils.*;

import org.junit.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Stephane Nicoll
 */
@ContextConfiguration
public class SpringCachingBookRepositoryTest extends AbstractBookRepositoryTest {


	@Test
	public void getWithCustomCacheResolver() {
		Cache anotherCache = cacheManager.getCache("another");
		Object key = generateKey(0L);

		assertCacheMiss(key, defaultCache, anotherCache);
		Book book = bookRepository.findBook(0L, "default");
		assertCacheHit(key, book, defaultCache);
		assertCacheMiss(key, anotherCache);

		Object key2 = generateKey(1L);
		assertCacheMiss(key2, defaultCache, anotherCache);
		Book book2 = bookRepository.findBook(1L, "another");
		assertCacheHit(key2, book2, anotherCache);
		assertCacheMiss(key2, defaultCache);
	}


	@Override
	protected Object generateKey(Long id) {
		return id;
	}

	@Configuration
	@EnableCaching
	static class Config {

		@Bean
		public CacheManager cacheManager() {
			SimpleCacheManager manager = new SimpleCacheManager();
			manager.setCaches(asList(
					new ConcurrentMapCache("default"),
					new ConcurrentMapCache("another"))
			);
			return manager;
		}

		@Bean
		public CacheResolver runtimeCacheResolver() {
			return new RuntimeCacheResolver(cacheManager());
		}

		@Bean
		public BookRepository bookRepository() {
			return new SpringCachingBookRepository(createSampleRepository());
		}
	}
}
