package net.nicoll.scratch.spring.cache;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Stephane Nicoll
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractBookRepositoryTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CacheManager cacheManager;

	private Cache defaultCache;

	@Before
	public void setUp() {
		this.defaultCache = cacheManager.getCache("default");
	}

	protected abstract Object generateKey(Long id);

	@Test
	public void get() {
		Object key = generateKey(0L);

		assertCacheMiss(key);
		Book book = bookRepository.findBook(0L);
		assertCacheHit(key, book);
	}

	@Test
	public void put() {
		Object key = generateKey(1L);

		Book book = bookRepository.findBook(1L); // initialize cache
		assertCacheHit(key, book);

		Book updatedBook = new Book(1L, "Another title");
		bookRepository.updateBook(1L, updatedBook);
		assertCacheHit(key, updatedBook);
	}

	@Test
	public void evict() {
		Object key = generateKey(2L);

		Book book = bookRepository.findBook(2L); // initialize cache
		assertCacheHit(key, book);

		assertTrue(bookRepository.removeBook(2L));
		assertCacheMiss(key);
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

	protected void assertCacheMiss(Object key) {
		assertNull("No entry should have been found with key " + key, defaultCache.get(key));
	}

	protected void assertCacheHit(Object key, Book book) {
		Cache.ValueWrapper wrapper = defaultCache.get(key);
		assertNotNull("An entry should have been found with key " + key, wrapper);
		assertEquals("Wrong value for entry with key " + key, book, wrapper.get());
	}
}
