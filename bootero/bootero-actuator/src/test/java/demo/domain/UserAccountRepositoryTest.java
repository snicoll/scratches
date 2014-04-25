package demo.domain;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicLong;

import demo.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserAccountRepositoryTest {

	private final AtomicLong counter = new AtomicLong();

	@Autowired
	private UserAccountRepository repository;

	UserAccount robert, dave, phil;

	@Before
	public void setUp() {
		repository.deleteAll();
		this.robert = new UserAccount(counter.getAndIncrement(), "Robert", "Webb");
		this.dave = new UserAccount(counter.getAndIncrement(), "Dave", "Syer");
		this.phil = new UserAccount(counter.getAndIncrement(), "Phil", "Webb");
		repository.save(asList(robert, dave, phil));
	}

	@Test
	public void findByUserName() {
		UserAccount account = repository.findByUsername("dave.syer");
		assertThat(account.getId(), is(1L));
	}

	@Test
	public void findByFamilyName() {
		assertThat(repository.findByFamilyName("Webb"), hasSize(2));
	}

}
