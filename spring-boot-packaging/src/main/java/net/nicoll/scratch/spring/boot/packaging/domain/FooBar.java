package net.nicoll.scratch.spring.boot.packaging.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Stephane Nicoll
 */
@Entity
public class FooBar {

	@Id
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
