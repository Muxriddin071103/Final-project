package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.payload.SubscriptionDTO;
import uz.app.service.SubscriptionService;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService followService;

    @PostMapping("/{followedId}")
    public ResponseEntity<SubscriptionDTO> followUser(@PathVariable Long followedId) {
        SubscriptionDTO subscriptionDTO = followService.followUser(followedId);
        return ResponseEntity.ok(subscriptionDTO);
    }

    @DeleteMapping("/{followedId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followedId) {
        followService.unfollowUser(followedId);
        return ResponseEntity.noContent().build();
    }
}
