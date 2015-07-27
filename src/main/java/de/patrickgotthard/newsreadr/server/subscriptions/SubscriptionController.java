package de.patrickgotthard.newsreadr.server.subscriptions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.util.IOUtil;
import de.patrickgotthard.newsreadr.server.subscriptions.request.AddSubscriptionRequest;
import de.patrickgotthard.newsreadr.server.subscriptions.request.RemoveSubscriptionRequest;
import de.patrickgotthard.newsreadr.server.subscriptions.request.UpdateSubscriptionRequest;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node;

@Controller
@RequestMapping("/api/subscriptions")
class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void addSubscription(@Valid final AddSubscriptionRequest request, final User currentUser) {
        this.subscriptionService.addSubscription(request, currentUser);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Node> getSubscriptions(final User currentUser) {
        return this.subscriptionService.getSubscriptions(currentUser);
    }

    @RequestMapping(value = "/{subscriptionId}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateSubscription(@Valid final UpdateSubscriptionRequest request, final User currentUser) {
        this.subscriptionService.updateSubscription(request, currentUser);
    }

    @RequestMapping(value = "/{subscriptionId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void removeSubscription(@Valid final RemoveSubscriptionRequest request, final User currentUser) {
        this.subscriptionService.removeSubscription(request, currentUser);
    }

    // TODO return import summary
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public List<String> importSubscriptions(final MultipartFile opmlFile, final User currentUser) {
        return this.subscriptionService.importSubscriptions(opmlFile, currentUser);
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportSubscriptions(final HttpServletResponse response, final User currentUser) throws JAXBException, IOException {

        final String opml = this.subscriptionService.exportSubscriptions(currentUser);
        final int contentLength = opml.getBytes().length;

        response.setHeader("Content-Disposition", "attachment; filename=\"newsreadr.opml\"");
        response.setContentType("application/xml");
        response.setContentLength(contentLength);

        final ServletOutputStream outputStream = response.getOutputStream();
        IOUtil.write(opml, outputStream);
        outputStream.flush();

    }

}