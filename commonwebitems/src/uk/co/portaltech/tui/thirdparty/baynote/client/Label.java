package uk.co.portaltech.tui.thirdparty.baynote.client;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;


public class Label implements Serializable {
     @XmlAttribute(name = "n")
        private String name;
     @XmlAttribute(name = "v")
        private String value;
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }


}
