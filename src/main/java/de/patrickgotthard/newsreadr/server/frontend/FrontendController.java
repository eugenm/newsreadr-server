package de.patrickgotthard.newsreadr.server.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class FrontendController {

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

}
