package net.nicoll.scratch.spring.cache;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Stephane Nicoll
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractBookRepositoryTest {

	@Autowired
	protected BookRepository bookRepository;

	@Autowired
	protected CacheManager cacheManager;

	protected Cache defaultCache;

	@Before
	public void setUp() {
		this.defaultCache = cacheManager.getCache("default");
	}

	protected abstract Object generateKey(Long id);

	@Test
	public void get() {
		Object key = generateKey(0L);

		assertDefaultCacheMiss(key);
		Book book = bookRepository.findBook(0L);
		assertDefaultCacheHit(key, book);
	}

	@Test
	public void put() {
		Object key = generateKey(1L);

		Book book = bookRepository.findBook(1L); // initialize cache
		assertDefaultCacheHit(key, book);

		Book updatedBook = new Book(1L, "Another title");
		bookRepository.updateBook(1L, updatedBook);
		assertDefaultCacheHit(key, updatedBook);
	}

	@Test
	public void evict() {
		Object key = generateKey(2L);

		Book book = bookRepository.findBook(2L); // initialize cache
		assertDefaultCacheHit(key, book);

		assertTrue(bookRepository.removeBook(2L));
		assertDefaultCacheMiss(key);
	}

	@Test
	public void evictAll() {
		bookRepository.findBook(3L);
		bookRepository.findBook(4L);

		assertFalse("Cache is not empty", isEmpty(defaultCache));
		bookRepository.removeAll();
		assertTrue("Cache should be empty", isEmpty(defaultCache));
	}

	protected boolean isEmpty(Cache cache) { // assuming simple implementation
		return ((ConcurrentMapCache) cache).getNativeCache().isEmpty();
	}

	protected void assertCacheMiss(Object key, Cache... caches) {
		for (Cache cache : caches) {
			assertNull("No entry should have been found in " + cache + " with key " + key, cache.get(key));
		}
	}

	protected void assertCacheHit(Object key, Book book, Cache... caches) {
		for (Cache cache : caches) {
			Cache.ValueWrapper wrapper = cache.get(key);
			assertNotNull("An entry should have been found in " + cache + " with key " + key, wrapper);
			assertEquals("Wrong value for entry in " + cache + " with key " + key, book, wrapper.get());
		}
	}

	protected void assertDefaultCacheMiss(Object key) {
		assertCacheMiss(key, defaultCache);
	}

	protected void assertDefaultCacheHit(Object key, Book book) {
		assertCacheHit(key, book, defaultCache);
	}
}
