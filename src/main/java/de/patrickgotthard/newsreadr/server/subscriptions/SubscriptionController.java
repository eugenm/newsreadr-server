package de.patrickgotthard.newsreadr.server.subscriptions;

import org.springframework.beans.factory.annotation.Autowired;

import de.patrickgotthard.newsreadr.server.common.web.ApiController;
import de.patrickgotthard.newsreadr.server.common.web.ApiRequestMapping;
import de.patrickgotthard.newsreadr.shared.request.AddSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.GetSubscriptionsRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.response.GetSubscriptionsResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;

@ApiController
class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @ApiRequestMapping(AddSubscriptionRequest.class)
    Response addSubscription(final AddSubscriptionRequest request) {
        return subscriptionService.addSubscription(request);
    }

    @ApiRequestMapping(GetSubscriptionsRequest.class)
    GetSubscriptionsResponse getSubscriptions() {
        return subscriptionService.getSubscriptions();
    }

    @ApiRequestMapping(UpdateSubscriptionRequest.class)
    Response updateSubscription(final UpdateSubscriptionRequest request) {
        return subscriptionService.updateSubscription(request);
    }

    @ApiRequestMapping(RemoveSubscriptionRequest.class)
    Response removeSubscription(final RemoveSubscriptionRequest request) {
        return subscriptionService.removeSubscription(request);
    }

}