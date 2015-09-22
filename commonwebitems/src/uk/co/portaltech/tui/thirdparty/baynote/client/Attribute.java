package uk.co.portaltech.tui.thirdparty.baynote.client;

/**
 * @author veena.pn
 *
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Attribute
{

   @XmlElement
   private String name;

   @XmlElement
   private String values;

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * @return the values
    */
   public String getValues()
   {
      return values;
   }

   /**
    * @param values the values to set
    */
   public void setValues(final String values)
   {
      this.values = values;
   }

}
