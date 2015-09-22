package uk.co.portaltech.tui.thirdparty.baynote.client;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "guides")
public class Guides implements Serializable
{

   private static final long serialVersionUID = 1L;

   @XmlElement(name = "r")
   private List<Result> listOfResults;

   @XmlAttribute(name = "gr")
   private String baynoteReq;

   @XmlAttribute(name = "tot")
   private int tot;

   @XmlAttribute(name = "g")
   private String g;

   @XmlAttribute(name = "q")
   private String q;

   @XmlAttribute(name = "w")
   private String subTitle;

   public List<Result> getListOfResults()
   {

      return listOfResults;

   }

   /**
    * @return the subTitile
    */

   public String getSubTitle()
   {
      return subTitle;
   }

   public int getTot()
   {
      return tot;
   }

   public void setTot(final int tot)
   {
      this.tot = tot;
   }

   public String getG()
   {
      return g;
   }

   public void setG(final String g)
   {
      this.g = g;
   }

   public String getQ()
   {
      return q;
   }

   public void setQ(final String q)
   {
      this.q = q;
   }

   public String getBaynoteReq()
   {
      return baynoteReq;
   }

}