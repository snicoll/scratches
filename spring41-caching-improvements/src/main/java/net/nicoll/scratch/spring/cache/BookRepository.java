package net.nicoll.scratch.spring.cache;

/**
 * @author Stephane Nicoll
 */
public interface BookRepository {

	Book findBook(Long id);

	// This is essentially a showcase to demonstrate how a cache
	// can be resolved at runtime. storeName is the name of the
	// cache to use
	Book findBook(Long id, String storeName);

	Book updateBook(Long id, Book book);

	boolean removeBook(Long id);

	void removeAll();

}
