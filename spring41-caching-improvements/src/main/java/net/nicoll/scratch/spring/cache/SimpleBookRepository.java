package net.nicoll.scratch.spring.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stephane Nicoll
 */
public class SimpleBookRepository implements BookRepository {

	private final Logger logger = LoggerFactory.getLogger(SimpleBookRepository.class);

	private final ConcurrentHashMap<Long, Book> content;

	public SimpleBookRepository(Map<Long, Book> content) {
		this.content = new ConcurrentHashMap<Long, Book>();
		if (content != null) {
			this.content.putAll(content);
		}
	}

	@Override
	public Book findBook(Long id) {
		logger.debug("looking up book with id {}", id);
		return content.get(id);
	}

	@Override
	public Book findBook(Long id, String storeName) {
		logger.debug("looking up book with id {} and cache it in {}", id, storeName);
		return content.get(id);
	}

	@Override
	public Book updateBook(Long id, Book book) {
		logger.debug("Updating book with id {} to {}", id, book);
		content.put(id, book);
		return book; // no transformation
	}

	@Override
	public boolean removeBook(Long id) {
		logger.debug("Removing book with id {}", id);
		return content.remove(id) != null;
	}

	@Override
	public void removeAll() {
		logger.debug("Removing all books");
		content.clear();
	}
}
