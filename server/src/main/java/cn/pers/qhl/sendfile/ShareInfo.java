package cn.pers.qhl.sendfile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class ShareInfo {

    @JsonIgnore
    private String id;

    @JsonIgnore
    private Long ttl;

    @JsonIgnore
    private Long lastModified;

    private String token;

    private Long ttlConfig;

    private String password;

    @JsonIgnore
    private List<ShareFile> files;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ShareFile> getFiles() {
        return files;
    }

    public void setFiles(List<ShareFile> files) {
        this.files = files;
    }
}
