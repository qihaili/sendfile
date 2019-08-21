package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SendFileConfig shareFileConfig;

    @GetMapping("config")
    SendFileConfig getConfig() {
        SendFileConfig result = new SendFileConfig();
        BeanUtils.copyProperties(shareFileConfig, result);
        return result;
    }
}
