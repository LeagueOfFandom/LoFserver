package com.lofserver.soma.controller.v1.response;

public class CommonItem {
    private String viewType;
    private Object viewObject;

    public CommonItem(String viewType, Object viewObject) {
        this.viewType = viewType;
        this.viewObject = viewObject;
    }
}
