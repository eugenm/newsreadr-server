package de.patrickgotthard.newsreadr.server.frontend;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.patrickgotthard.newsreadr.server.subscriptions.SubscriptionService;
import de.patrickgotthard.newsreadr.shared.response.Response;

@Controller
class FrontendController {

    private static final String CONTENT_DISPOSITION_HEADER_NAME = "Content-Disposition";
    private static final String ATTACHMENT_HEADER_VALUE = "attachment; filename=\"newsreadr.opml\"";
    private static final String APPLICATION_XML_MIME_TYPE = "application/xml";

    private final SubscriptionService subscriptionService;

    @Autowired
    FrontendController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping("/")
    String index() {
        return "index";
    }

    @RequestMapping("/login")
    String login() {
        return "login";
    }

    @RequestMapping("/partial/{path}")
    String account(@PathVariable final String path) {
        return "partial/" + path;
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
