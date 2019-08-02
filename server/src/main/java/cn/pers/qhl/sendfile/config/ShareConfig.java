package cn.pers.qhl.sendfile.config;

public class ShareConfig {

    private Double ttl;

    private Double scanInterval;

    private Double maxFileSize;

    public Double getTtl() {
        return ttl;
    }

    public void setTtl(Double ttl) {
        this.ttl = ttl;
    }

    public Double getScanInterval() {
        return scanInterval;
    }

    public void setScanInterval(Double scanInterval) {
        this.scanInterval = scanInterval;
    }

    public Double getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Double maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
