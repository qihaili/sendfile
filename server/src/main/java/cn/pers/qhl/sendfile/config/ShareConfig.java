package cn.pers.qhl.sendfile.config;

public class ShareConfig {

    private TtlOption[] ttlOptions;

    private Double scanInterval;

    private Double maxFileSize;

    public TtlOption[] getTtlOptions() {
        return ttlOptions;
    }

    public void setTtlOptions(TtlOption[] ttlOptions) {
        this.ttlOptions = ttlOptions;
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
