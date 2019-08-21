package cn.pers.qhl.sendfile;

public class ShareInfo {

    private String token;

    private Long ttlConfig;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTtlConfig() {
        return ttlConfig;
    }

    public void setTtlConfig(Long ttlConfig) {
        this.ttlConfig = ttlConfig;
    }
}
