package de.patrickgotthard.newsreadr.server.subscriptions;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.patrickgotthard.newsreadr.shared.request.AddSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.GetSubscriptionsRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.response.GetSubscriptionsResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;

@Controller
@RequestMapping("/api")
class SubscriptionController {

    private static final String CONTENT_DISPOSITION_HEADER_NAME = "Content-Disposition";
    private static final String ATTACHMENT_HEADER_VALUE = "attachment; filename=\"newsreadr.opml\"";
    private static final String APPLICATION_XML_MIME_TYPE = "application/xml";

    private final SubscriptionService subscriptionService;

    @Autowired
    SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping(params = AddSubscriptionRequest.METHOD)
    @ResponseBody
    Response addSubscription(final AddSubscriptionRequest request) {
        return subscriptionService.addSubscription(request);
    }

    @RequestMapping(params = GetSubscriptionsRequest.METHOD)
    @ResponseBody
    GetSubscriptionsResponse getSubscriptions() {
        return subscriptionService.getSubscriptions();
    }

    @RequestMapping(params = UpdateSubscriptionRequest.METHOD)
    @ResponseBody
    Response updateSubscription(final UpdateSubscriptionRequest request) {
        return subscriptionService.updateSubscription(request);
    }

    @RequestMapping(params = RemoveSubscriptionRequest.METHOD)
    @ResponseBody
    Response removeSubscription(final RemoveSubscriptionRequest request) {
        return subscriptionService.removeSubscription(request);
    }

    @RequestMapping("/import")
    @ResponseBody
    Response importSubscriptions(final MultipartFile opmlFile) {
        return subscriptionService.importSubscriptions(opmlFile);
    }

    @RequestMapping("/export")
    void exportSubscriptions(final HttpServletResponse response) {
        try {

            final String export = subscriptionService.exportSubscriptions();
            final int contentLength = export.getBytes().length;

            response.setHeader(CONTENT_DISPOSITION_HEADER_NAME, ATTACHMENT_HEADER_VALUE);
            response.setContentType(APPLICATION_XML_MIME_TYPE);
            response.setContentLength(contentLength);

            final ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.write(export, outputStream);
            outputStream.flush();

        } catch (final IOException | JAXBException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}