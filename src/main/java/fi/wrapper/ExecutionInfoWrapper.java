package fi.wrapper;

import java.util.List;

public class ExecutionInfoWrapper {

    private List<Integer> items;
    private String guid;

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
