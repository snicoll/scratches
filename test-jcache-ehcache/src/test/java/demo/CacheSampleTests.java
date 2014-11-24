package demo;

import org.ehcache.jcache.JCacheConfiguration;
import org.junit.Test;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

/**
 * @author Stephane Nicoll
 */
public class CacheSampleTests {

    @Test
    public void cacheWithNoDefault() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EnableCachingConfig.class);
        try {
            context.getBean(FooService.class).defaultGet(123L);
        } finally {
            context.close();
        }
    }

    @Test
    public void cacheWithDefault() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(EnableCachingConfigWithDefaults.class);
        try {
            context.getBean(FooService.class).defaultGet(123L);
        } finally {
            context.close();
        }
    }


    @Configuration
    @EnableCaching
    static class EnableCachingConfig extends CachingConfigurerSupport {

        @Override
        @Bean
        public org.springframework.cache.CacheManager cacheManager() {
            return new JCacheCacheManager(jCacheManager());
        }

        @Bean
        public CacheManager jCacheManager() {
            CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
            MutableConfiguration<Object, Object> mutableConfiguration = new MutableConfiguration<Object, Object>();
            mutableConfiguration.setStoreByValue(false);  // otherwise value has to be Serializable
            cacheManager.createCache("primary", new JCacheConfiguration<Object, Object>(mutableConfiguration));
            return cacheManager;
        }

        @Bean
        public FooService fooService() {
            return new FooServiceImpl();
        }
    }

    @Configuration
    @EnableCaching
    static class EnableCachingConfigWithDefaults extends CachingConfigurerSupport {

        @Override
        @Bean
        public org.springframework.cache.CacheManager cacheManager() {
            return new JCacheCacheManager(jCacheManager());
        }

        @Bean
        public CacheManager jCacheManager() {
            CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
            MutableConfiguration<Object, Object> mutableConfiguration = new MutableConfiguration<Object, Object>();
            mutableConfiguration.setStoreByValue(false);  // otherwise value has to be Serializable
            cacheManager.createCache("primary", new JCacheConfiguration<Object, Object>(mutableConfiguration));
            cacheManager.createCache("default", new JCacheConfiguration<Object, Object>(mutableConfiguration));
            return cacheManager;
        }

        @Bean
        public FooService fooService() {
            return new FooServiceImpl();
        }
    }
}
