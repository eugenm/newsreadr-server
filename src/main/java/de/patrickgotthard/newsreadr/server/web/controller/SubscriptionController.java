package de.patrickgotthard.newsreadr.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.service.SubscriptionService;
import de.patrickgotthard.newsreadr.shared.request.AddSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.GetSubscriptionsRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.response.GetSubscriptionsResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RestController
@RequestMapping("/api")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping(params = AddSubscriptionRequest.METHOD)
    public Response addSubscription(final AddSubscriptionRequest request) {
        return subscriptionService.addSubscription(request);
    }

    @RequestMapping(params = GetSubscriptionsRequest.METHOD)
    public GetSubscriptionsResponse getSubscriptions() {
        return subscriptionService.getSubscriptions();
    }

    @RequestMapping(params = UpdateSubscriptionRequest.METHOD)
    public Response updateSubscription(final UpdateSubscriptionRequest request) {
        return subscriptionService.updateSubscription(request);
    }

    @RequestMapping(params = RemoveSubscriptionRequest.METHOD)
    public Response removeSubscription(final RemoveSubscriptionRequest request) {
        return subscriptionService.removeSubscription(request);
    }

}