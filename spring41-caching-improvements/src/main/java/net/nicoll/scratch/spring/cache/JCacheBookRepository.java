package net.nicoll.scratch.spring.cache;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;


/**
 * @author Stephane Nicoll
 */
@CacheDefaults(cacheName = "default")
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

	@Override
	public Book findBook(Long id, String storeName) {
		throw new UnsupportedOperationException();
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
