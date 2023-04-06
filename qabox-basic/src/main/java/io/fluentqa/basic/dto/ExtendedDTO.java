package io.fluentqa.basic.dto;

import java.util.HashMap;
import java.util.Map;

public abstract class ExtendedDTO extends GenericDTO {

    private static final long serialVersionUID = 1L;

    /**
     * This is for extended values
     */
    protected Map<String, Object> extValues = new HashMap<String, Object>();

    public Object getExtField(String key){
        if(extValues != null){
            return extValues.get(key);
        }
        return null;
    }

    public ExtendedDTO addExtField(String fieldName, Object value){
        this.extValues.put(fieldName, value);
        return this;
    }

}
