package com.epam.aws.training.controller;

import com.epam.aws.training.service.impl.SubscriptionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionServiceImpl subscriptionService;

    @PutMapping("/{email}")
    public void subscribeEmail(@PathVariable String email) {
        subscriptionService.subscribeEmail(email);
    }

    @DeleteMapping("/{email}")
    public void unsubscribeEmail(@PathVariable String email) {
        subscriptionService.unsubscribeEmail(email);
    }
}
