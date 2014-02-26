package net.nicoll.scratch.spring.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephane Nicoll
 */
public class BookRepositoryTestUtils {

	public static BookRepository createSampleRepository() {
		Map<Long, Book> content = new HashMap<Long, Book>();
		for (long i = 0; i < 10; i++) {
			content.put(i, new Book(i, "Sample book " + i));
		}
		return new SimpleBookRepository(content);
	}
}
