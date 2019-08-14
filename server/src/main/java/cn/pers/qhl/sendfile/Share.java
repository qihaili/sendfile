package cn.pers.qhl.sendfile;

import java.util.List;

public class Share {

    private String id;

    private Long ttl;

    private String token;

    private Long lastModified;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public List<ShareFile> getFiles() {
        return files;
    }

    public void setFiles(List<ShareFile> files) {
        this.files = files;
    }
}
