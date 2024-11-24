package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.app.entity.Subscription;
import uz.app.entity.User;
import uz.app.payload.SubscriptionDTO;
import uz.app.repository.SubscriptionRepository;
import uz.app.repository.UserRepository;
import uz.app.util.UserUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserUtil userUtil;

    public SubscriptionDTO followUser(Long followedId) {
        User follower = userUtil.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));

        Optional<Subscription> existingSubscription = subscriptionRepository.findByFollower_IdAndFollowed_Id(follower.getId(), followedId);
        if (existingSubscription.isPresent()) {
            throw new RuntimeException("You are already following this user.");
        }

        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setFollowed(followed);
        subscription.setSubscribedAt(LocalDateTime.now());
        subscription.setActive(true);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return new SubscriptionDTO(
                savedSubscription.getFollower().getId(),
                savedSubscription.getFollowed().getId(),
                savedSubscription.getSubscribedAt(),
                savedSubscription.isActive()
        );
    }

    public void unfollowUser(Long followedId) {
        User follower = userUtil.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        Subscription subscription = subscriptionRepository.findByFollower_IdAndFollowed_Id(follower.getId(), followedId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setActive(false);
        subscriptionRepository.save(subscription);
    }
}
