package com.br.quarkus.social.domain.repository;

import com.br.quarkus.social.domain.model.Follower;
import com.br.quarkus.social.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public Boolean follows(User user, User follower) {
        Map<String, Object> parameters = Parameters.with("follower", follower).and("user", user).map();
        PanacheQuery<Follower> query = find("follower =:follower and user =:user", parameters);
        Optional<Follower> resultOptional = query.firstResultOptional();
        return resultOptional.isPresent();
    }

    public List<Follower> findByUser(Long userId) {
        PanacheQuery<Follower> query = find("user.id", userId);
        return query.list();
    }

    public void deleteByUserAndFollower(Long userId, Long unfollowId) {
        Map<String, Object> parameters = Parameters.with("userId", userId).and("followerId", unfollowId).map();
        delete("user.id =:userId and follower.id =:followerId", parameters);
    }
}
