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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "widgetResults")
public class WidgetResults implements Serializable
{
   @XmlElement(name = "slotResults")
   private List<SlotResults> listOfslotResults;

   @XmlElement
   private String id;

   /**
    * @return the listOfslotResults
    */
   public List<SlotResults> getListOfslotResults()
   {
      return listOfslotResults;
   }

   /**
    * @param listOfslotResults the listOfslotResults to set
    */
   public void setListOfslotResults(final List<SlotResults> listOfslotResults)
   {
      this.listOfslotResults = listOfslotResults;
   }

   /**
    * @return the id
    */
   public String getId()
   {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(final String id)
   {
      this.id = id;
   }

}
