package io.fluent.qabox;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Configuration
@Controller
public class QaBoxWebConfig {

    @RequestMapping("/")
    public String index(HttpServletResponse response) {
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        return "forward:index.html?v=" + this.getClass().hashCode();
    }

}
