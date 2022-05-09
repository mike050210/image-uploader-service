package com.epam.aws.training.service;


public interface SubscriptionService {
    void subscribeEmail(String email);

    void unsubscribeEmail(String email);

    void sendMessageToQueue(String message);

}
