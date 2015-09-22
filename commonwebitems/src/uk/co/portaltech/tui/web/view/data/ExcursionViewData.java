/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.commercefacades.product.data.ImageData;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Abi
 * @author l.furrer
 *
 */
public class ExcursionViewData extends AttractionViewData
{
   private static final int DESCRIPTION_LENGTH = 120;

   private String description;

   private String fromPrice;

   private String childPrice;

   private String imageUrl;

   private String bookingUrl;

   private String childAgeMin;

   private String childAgeMax;

   private List<ImageData> imageDataList;

   /**
    *
    */
   public ExcursionViewData()
   {
      super();
   }

   /**
    * @return the imageUrl
    */
   public String getImageUrl()
   {
      return imageUrl;
   }

   /**
    * @param imageUrl the imageUrl to set
    */
   public void setImageUrl(final String imageUrl)
   {
      this.imageUrl = imageUrl;
   }

   /**
    * @return the fromPrice
    */
   public String getFromPrice()
   {
      return fromPrice;
   }

   /**
    * @param fromPrice the fromPrice to set
    */
   public void setFromPrice(final String fromPrice)
   {
      this.fromPrice = fromPrice;
   }

   /**
    * @return the childPrice
    */
   public String getChildPrice()
   {
      return childPrice;
   }

   /**
    * @param childPrice the childPrice to set
    */
   public void setChildPrice(final String childPrice)
   {
      this.childPrice = childPrice;
   }

   /**
     *
     */
   public String getSubStringedDescriptionContent()
   {
      if (StringUtils.isNotEmpty(this.getDescription())
         && this.getDescription().length() > DESCRIPTION_LENGTH)
      {
         final String str = this.getDescription().substring(0, DESCRIPTION_LENGTH);
         return str.substring(0, str.lastIndexOf(' '));
      }
      return this.getDescription();
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
    * @return the imageDataList
    */
   public List<ImageData> getImageDataList()
   {
      return imageDataList;
   }

   /**
    * @param imageDataList the imageDataList to set
    */
   public void setImageDataList(final List<ImageData> imageDataList)
   {
      this.imageDataList = imageDataList;
   }

   /**
    * @return the bookingUrl
    */
   public String getBookingUrl()
   {
      return bookingUrl;
   }

   /**
    * @param bookingUrl the bookingUrl to set
    */
   public void setBookingUrl(final String bookingUrl)
   {
      this.bookingUrl = bookingUrl;
   }

   /**
    * @return the childAgeMin
    */
   public String getChildAgeMin()
   {
      return childAgeMin;
   }

   /**
    * @param childAgeMin the childAgeMin to set
    */
   public void setChildAgeMin(final String childAgeMin)
   {
      this.childAgeMin = childAgeMin;
   }

   /**
    * @return the childAgeMax
    */
   public String getChildAgeMax()
   {
      return childAgeMax;
   }

   /**
    * @param childAgeMax the childAgeMax to set
    */
   public void setChildAgeMax(final String childAgeMax)
   {
      this.childAgeMax = childAgeMax;
   }

}
