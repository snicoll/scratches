package demo.domain;

public class UserAccount {

	private Long id;

	private String username;

	private String givenName;

	private String familyName;

	public UserAccount(Long id, String givenName, String familyName) {
		this.id = id;
		this.username = givenName.toLowerCase() + "." + familyName.toLowerCase();
		this.givenName = givenName;
		this.familyName = familyName;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	@Override
	public String toString() {
		return String.format(
				"UserAccount[id=%s, userName='%s' givenName='%s', familyName='%s']",
				id, username, givenName, familyName);
	}

}
