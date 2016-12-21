package com.khai.model.proposed;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Field {

    @Attribute(name = "type")
    private String type;
    @Element(name = "multipart-separator-before", required = false)
    private String multipartSeparatorBefore;
    @Element(name = "info", required = false)
    private String info;
    @Element(name = "multipart-separator-after", required = false)
    private String multipartSeparatorAfter;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMultipartSeparatorBefore() {
        return multipartSeparatorBefore;
    }

    public void setMultipartSeparatorBefore(String multipartSeparatorBefore) {
        this.multipartSeparatorBefore = multipartSeparatorBefore;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMultipartSeparatorAfter() {
        return multipartSeparatorAfter;
    }

    public void setMultipartSeparatorAfter(String multipartSeparatorAfter) {
        this.multipartSeparatorAfter = multipartSeparatorAfter;
    }
}