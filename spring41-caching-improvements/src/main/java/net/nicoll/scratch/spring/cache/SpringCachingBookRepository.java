package net.nicoll.scratch.spring.cache;


import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author Stephane Nicoll
 */
@CacheConfig(cacheNames = "default")
public class SpringCachingBookRepository implements BookRepository {

	private final BookRepository delegate;

	public SpringCachingBookRepository(BookRepository delegate) {
		this.delegate = delegate;
	}

	@Override
	@Cacheable
	public Book findBook(Long id) {
		return delegate.findBook(id);
	}

	@Override
	@Cacheable(cacheResolver = "runtimeCacheResolver", key="#p0")
	public Book findBook(Long id, String storeName) {
		return findBook(id);
	}

	@Override
	@CachePut(key = "#p0") // JSR-107 requires to specify the object to update
	public Book updateBook(Long id, Book book) {
		return delegate.updateBook(id, book);
	}

	@Override
	@CacheEvict
	public boolean removeBook(Long id) {
		return delegate.removeBook(id);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void removeAll() {
		delegate.removeAll();
	}
}
