This demo is based on `bootero-java` and demonstrates the creation of an object model
using MongoDB for the persistence. This is obviously using Spring Data.

# Updating our project

Based on the `bootero-java`, we first need to add the starter project to enable
Spring data and Mongo:

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

# Creating the model

Let's create a simple `UserAccount` model with `id` `username`, `givenName` and
`lastName`. On this model we create a Spring Data `UserAccountRepository` that
extends `MongoRepository` and provides a `findByUsername` method

# Testing the model

We then create a very simple test for our repository. Note that nothing has been
changed in our configuration, the repository has been picked up automatically and
a proxy implementation has been created for it.

# Updating the repository

If time permits, a simple `List<UserAccount> findByFamilyName(String familyName)`
method can be added on the repository interface and the test updated accordingly.

