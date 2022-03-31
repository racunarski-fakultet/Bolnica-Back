package raf.si.bolnica.user.query;

public class SearchCriteria {
    private String key;
    private Object value;

    public SearchCriteria(String key,Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
