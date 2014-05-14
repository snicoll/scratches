package demo.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "users")
public interface UserAccountRepository extends MongoRepository<UserAccount, Long> {

	@RestResource(path = "username")
	UserAccount findByUsername(@Param("name") String username);

	List<UserAccount> findByFamilyName(@Param("name") String familyName);

}
