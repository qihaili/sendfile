package cn.pers.qhl.sendfile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sendfile")
public class SendFileConfig {

    private ShareConfig share;

    private String adminPassword;

    public ShareConfig getShare() {
        return share;
    }

    public void setShare(ShareConfig share) {
        this.share = share;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
