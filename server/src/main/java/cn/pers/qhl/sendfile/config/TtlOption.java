package cn.pers.qhl.sendfile.config;

public class TtlOption {

    private String name;

    private String value;

    private Boolean defaultOption;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getDefaultOption() {
        return defaultOption;
    }

    public void setDefaultOption(Boolean defaultOption) {
        this.defaultOption = defaultOption;
    }

    @Override
    public String toString() {
        return "TtlOption{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", defaultOption=" + defaultOption +
                '}';
    }
}
