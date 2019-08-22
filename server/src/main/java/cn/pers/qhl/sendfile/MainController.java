package cn.pers.qhl.sendfile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class MainController {

    @GetMapping({"/", "/ui"})
    public String home() {
        return "redirect:/ui/index.html";
    }

}
