package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ShareService service;

    @Autowired
    private SendFileConfig shareFileConfig;

    @Autowired
    private HttpSession session;

    @GetMapping("config")
    SendFileConfig getConfig() {
        SendFileConfig result = new SendFileConfig();
        BeanUtils.copyProperties(shareFileConfig, result);
        return result;
    }

    @GetMapping("check_authentication")
    void checkAuthentication() {
        if (session.getAttribute("authenticated") == null) {
            throw new UnauthorizedException();
        }
    }

    @PostMapping("login")
    void login(@RequestBody LoginInput input) {
        if (shareFileConfig.getAdminPassword().equals(input.getPassword())) {
            session.setAttribute("authenticated", new Object());
        } else {
            throw new UnauthorizedException();
        }
    }

    @PostMapping("logout")
    void logout() {
        session.removeAttribute("authenticated");
    }

}
