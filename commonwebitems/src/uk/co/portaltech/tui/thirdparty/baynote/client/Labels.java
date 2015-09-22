package uk.co.portaltech.tui.thirdparty.baynote.client;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class Labels implements Serializable
{
    @XmlElement(name = "l")
    private List<Label> listOfLabels;

    public List<Label> getListOfLabels()
    {
        return listOfLabels;
    }


}
