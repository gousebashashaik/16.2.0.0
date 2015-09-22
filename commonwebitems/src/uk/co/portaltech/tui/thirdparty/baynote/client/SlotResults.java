/**
 *
 */
package uk.co.portaltech.tui.thirdparty.baynote.client;

/**
 * @author veena.pn
 *
 */
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "slotResults")
public class SlotResults implements Serializable
{
   @XmlElement(name = "attrs")
   private List<Attribute> listOfattrs;

   @XmlAttribute
   private String slot;

   /**
    * @return the listOfattrs
    */
   public List<Attribute> getListOfattrs()
   {
      return listOfattrs;
   }

   /**
    * @param listOfattrs the listOfattrs to set
    */
   public void setListOfattrs(final List<Attribute> listOfattrs)
   {
      this.listOfattrs = listOfattrs;
   }

   /**
    * @return the slot
    */
   public String getSlot()
   {
      return slot;
   }

   /**
    * @param slot the slot to set
    */
   public void setSlot(final String slot)
   {
      this.slot = slot;
   }

}
