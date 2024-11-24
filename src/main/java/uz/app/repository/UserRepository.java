package uz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.app.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "SELECT u FROM User u JOIN UserRelationship ur ON u.id = ur.followed.id WHERE ur.follower.id = :userId", nativeQuery = true)
    List<User> findFollowedUsers(@Param("userId") Long userId);

    @Query(value = "SELECT u FROM User u JOIN UserRelationship ur ON u.id = ur.follower.id WHERE ur.followed.id = :userId", nativeQuery = true)
    List<User> findFollowers(@Param("userId") Long userId);
}
