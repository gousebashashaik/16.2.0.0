package uk.co.portaltech.tui.thirdparty.baynote.client;

/**
 * @author veena.pn
 *
 */
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "recResult")
public class RecResult implements Serializable
{
   @XmlElement(name = "widgetResults")
   private List<WidgetResults> widgetResults;

   /**
    * @return the widgetResults
    */
   public List<WidgetResults> getWidgetResults()
   {
      return widgetResults;
   }

   /**
    * @param widgetResults the widgetResults to set
    */
   public void setWidgetResults(final List<WidgetResults> widgetResults)
   {
      this.widgetResults = widgetResults;
   }
}
