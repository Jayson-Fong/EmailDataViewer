package org.jaysonfong.emaildataviewer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Header {

    private final SimpleStringProperty name;
    private final SimpleStringProperty value;

    public Header(final javax.mail.Header header) {
        this.name = new SimpleStringProperty(header.getName());
        this.value = new SimpleStringProperty(header.getValue());
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public String getName() {
        return this.name.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty valueProperty() {
        return this.value;
    }

    public String getValue() {
        return this.value.getValue();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

}
