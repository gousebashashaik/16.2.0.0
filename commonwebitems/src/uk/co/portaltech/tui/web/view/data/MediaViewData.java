/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.io.Serializable;

/**
 * @author omonikhide
 *
 */
public class MediaViewData implements Serializable
{
   private String code;

   private String mainSrc;

   private String description;

   private String mime;

   private String altText;

   private String size;

   private String brand;

   private String roomPlanImages;

   private String roomPlanDescription;

   // Added caption for Hybris Content Services
   private String caption;

   /**
    * @return the brand
    */
   public String getBrand()
   {
      return brand;
   }

   /**
    * @return the roomPlanImages
    */
   public String getRoomPlanImages()
   {
      return roomPlanImages;
   }

   /**
    * @param roomPlanImages the roomPlanImages to set
    */
   public void setRoomPlanImages(final String roomPlanImages)
   {
      this.roomPlanImages = roomPlanImages;
   }

   /**
    * @return the roomPlanDescription
    */
   public String getRoomPlanDescription()
   {
      return roomPlanDescription;
   }

   /**
    * @param roomPlanDescription the roomPlanDescription to set
    */
   public void setRoomPlanDescription(final String roomPlanDescription)
   {
      this.roomPlanDescription = roomPlanDescription;
   }

   /**
    * @param brand the brand to set
    */
   public void setBrand(final String brand)
   {
      this.brand = brand;
   }

   /**
    * @return the code
    */
   public String getCode()
   {
      return code;
   }

   /**
    * @param code the code to set
    */
   public void setCode(final String code)
   {
      this.code = code;
   }

   /**
    * @return the mainSrc
    */
   public String getMainSrc()
   {
      return mainSrc;
   }

   /**
    * @param mainSrc the mainSrc to set
    */
   public void setMainSrc(final String mainSrc)
   {
      this.mainSrc = mainSrc;
   }

   /**
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(final String description)
   {
      this.description = description;
   }

   /**
    * @return the mime
    */
   public String getMime()
   {
      return mime;
   }

   /**
    * @param mime the mime to set
    */
   public void setMime(final String mime)
   {
      this.mime = mime;
   }

   /**
    * @return the altText
    */
   public String getAltText()
   {
      return altText;
   }

   /**
    * @param altText the altText to set
    */
   public void setAltText(final String altText)
   {
      this.altText = altText;
   }

   /**
    * @return the size
    */
   public String getSize()
   {
      return size;
   }

   /**
    * @param size the size to set
    */
   public void setSize(final String size)
   {
      this.size = size;
   }

   /**
    * @return the caption
    */
   public String getCaption()
   {
      return caption;
   }

   /**
    * @param caption the caption to set
    */
   public void setCaption(final String caption)
   {
      this.caption = caption;
   }

}
