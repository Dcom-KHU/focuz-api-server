package dcom.focuz.api.domain.friend.repository;

import dcom.focuz.api.domain.friend.Friend;
import dcom.focuz.api.domain.friend.FriendState;
import dcom.focuz.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    @Query("select f from Friend f left join fetch f.user where f.state = :state and f.targetUser = :targetUser")
    Optional<Friend> findAllByStateAndTargetUser(FriendState state, User targetUser);

    Optional<Friend> findByUserAndTargetUser(User user, User targetUser);

    @Query("select f from Friend f left join fetch f.targetUser where f.state = :state and f.user = :user")
    Optional<Friend> findAllByStateAndUser(FriendState state, User user);
}
