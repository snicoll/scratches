package net.nicoll.scratch.spring.cache;

import static java.util.Arrays.*;
import static net.nicoll.scratch.spring.cache.BookRepositoryTestUtils.*;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Stephane Nicoll
 */
@ContextConfiguration
public class JCacheBookRepositoryTest extends AbstractBookRepositoryTest {

	@Configuration
	@EnableCaching
	static class Config extends JCacheConfigurerSupport {

		@Bean
		@Override
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
			return new JCacheBookRepository(createSampleRepository());
		}
	}
}
