package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface  PersonRepository extends JpaRepository<User, UUID> {


    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID uuid);

    User findByRole(String role);

    Optional<User> findUserByUsernameAndPassword(String username, String password);


    @Query(value = "SELECT p " +
            "FROM users p " +
            "WHERE p.name = :name " +
            "AND p.role= :device ")
    Optional<User> findSeniorsByName(@Param("name") String name);

}
