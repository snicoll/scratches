package demo.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<UserAccount, Long> {

	UserAccount findByUsername(String username);

	List<UserAccount> findByFamilyName(String familyName);

}
