package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Value("${sendfile.share.max-file-size}")
    private Long maxFileSize;

    @Value("${sendfile.share.ttl}")
//    @JsonSerialize(using = SendFileDurationSerializer.class)
    private String shareTTL;

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getShareTTL() {
        return shareTTL;
    }

    public void setShareTTL(String shareTTL) {
        this.shareTTL = shareTTL;
    }
}
