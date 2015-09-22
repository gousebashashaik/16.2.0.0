package uk.co.portaltech.tui.thirdparty.baynote.client;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;


public class Display implements Serializable
{

    @XmlElement(name = "labels")
    private Labels labels;

    public Labels getLabels()
    {
        return labels;
    }


}
