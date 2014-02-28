## Spring 4.1 caching improvements

This simple project demonstrates the improvements made to the caching abstraction
infrastructure.

These are:

* Custom `KeyGenerator` per cache operation ([SPR-10629](https://jira.springsource.org/browse/SPR-10629))
* Custom `CacheManager` per cache operation ([SPR-8696](https://jira.springsource.org/browse/SPR-8696))
* New class-level `@CacheConfig` annotation ([SPR-11316](https://jira.springsource.org/browse/SPR-11316))
* JSR-107 (`javax.cache`) annotation support ([SPR-9616](https://jira.springsource.org/browse/SPR-9616))
* Runtime resolution of caches (([SPR-11490](https://jira.springsource.org/browse/SPR-11490))

Branches:

* [snicoll/SPR-8696](https://github.com/snicoll/spring-framework/commits/SPR-8696)
* [snicoll/SPR-11316](https://github.com/snicoll/spring-framework/commits/SPR-11316)
* [snicoll/jsr-107](https://github.com/snicoll/spring-framework/commits/jsr-107)
* [snicoll/SPR-11490](https://github.com/snicoll/spring-framework/commits/SPR-11490)


### Building

To build this project, you first need the latest developments that are available on
my fork, checkout the `caching-improvements` branch of [my fork](https://github.com/snicoll/spring-framework/)

Simple run `./gradlew install` to update your local maven repository with Spring
version `4.0.3-CACHING-SNAPSHOT`

### JSR-107 support

`JCacheBookRepository` is an example of JSR-107 annotated repository. It uses the
standard JSR-107 annotations, that is `CacheResult`, `CachePut`, `CacheRemove`,
`CacheRemoveAll` and `CacheDefaults`

### @CachingConfig

`@CachingConfig` is a new class-level annotation that gathers some default options to
be applied to all methods without enabling any caching operation at all. The most obvious
use case is to share the name of the cache(s) to use for the class.

See `SpringCachingBookRepository` for an example.