package com.lofserver.soma.controller.v1.response;

import com.lofserver.soma.data.ViewType;
import lombok.Getter;

@Getter
public class CommonItem {
    private String viewType;
    private Object viewObject;

    public CommonItem(Object viewObject) {
        this.viewType = new ViewType().getViewType(viewObject);
        this.viewObject = viewObject;
    }
}
