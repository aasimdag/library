package com.masparaga.library.repository;

import com.masparaga.library.model.User;
import com.masparaga.library.model.exceptions.NotFoundException;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import java.util.List;
import java.util.Optional;


@Repository @CacheConfig(cacheNames = "users")
public interface UserRepository extends MongoRepository<User, ObjectId> {

    @CacheEvict(allEntries = true)
    <S extends User> List<S> saveAll(Iterable<S> entities);

    @Caching(evict = {
            @CacheEvict(key = "#p0.id", condition="#p0.id != null"),
            @CacheEvict(key = "#p0.username", condition="#p0.username != null")
    })
    <S extends User> S save(S entity);

    @Cacheable
    Optional<User> findById(ObjectId objectId);

    @Cacheable
    default User getById(ObjectId id) {
        Optional<User> optionalUser = findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(User.class, id);
        }
        if (!optionalUser.get().isEnabled()) {
            throw new NotFoundException(User.class, id);
        }
        return optionalUser.get();
    }

    @Cacheable
    Optional<User> findByUsername(String username);

}