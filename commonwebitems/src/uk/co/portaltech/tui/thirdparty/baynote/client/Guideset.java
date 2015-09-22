/**
 *
 */
package uk.co.portaltech.tui.thirdparty.baynote.client;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author geethanjali.k
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "guideset")
public class Guideset implements Serializable
{

    @XmlElement(name = "guides")
    private List<Guides> listOfGuides;

    /**
     * @return the listOfGuides
     */
    public List<Guides> getListOfGuides()
    {
        return listOfGuides;
    }




}
