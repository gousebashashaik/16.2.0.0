package uk.co.portaltech.tui.thirdparty.baynote.client;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Result implements Serializable
{

   private static final long serialVersionUID = 1L;

   @XmlAttribute(name = "u")
   private String productUrl;

   @XmlAttribute(name = "rk")
   private String rank;

   @XmlAttribute(name = "g")
   private String guide;

   @XmlElement(name = "display")
   private Display display;

   @XmlElement(name = "a")
   private List<Attributes> listOfAttributes;

   public String getRank()
   {
      return rank;
   }

   public String getGuide()
   {
      return guide;
   }

   public Display getDisplay()
   {
      return display;
   }

   public List<Attributes> getListOfAttributes()
   {
      return listOfAttributes;
   }

   public String getProductUrl()
   {
      return productUrl;
   }

}