
package com.company.safekyc.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FaultType", propOrder = {
    "code",
    "message"
})
public class FaultType {

    protected int code;

    public int getCode() {
        return code;
    }

    protected String message;

    public void setCode(int value) {
        this.code = value;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String value) {
        this.message = value;
    }

}
