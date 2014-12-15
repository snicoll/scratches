package net.nicoll.scratch.spring.cache;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;


/**
 * @author Stephane Nicoll
 */
@CacheDefaults(cacheName = "default")
@CacheConfig(cacheNames = "default")
public class JCacheBookRepository implements BookRepository {

	private final BookRepository delegate;

	public JCacheBookRepository(BookRepository delegate) {
		this.delegate = delegate;
	}

	@Override
	@CacheResult
	public Book findBook(Long id) {
		return delegate.findBook(id);
	}

	@Override // Example of mixed operations, can be tricky with keys
	@Cacheable(cacheResolver = "runtimeCacheResolver", // Don't want to play with JSR-107 cache
			key="#p0")
	public Book findBook(Long id, String storeName) {
		return delegate.findBook(id);
	}

	@Override
	@CachePut
	public Book updateBook(Long id, @CacheValue Book book) {
		return delegate.updateBook(id, book);
	}

	@Override
	@CacheRemove
	public boolean removeBook(Long id) {
		return delegate.removeBook(id);
	}

	@Override
	@CacheRemoveAll
	public void removeAll() {
		delegate.removeAll();
	}
}
