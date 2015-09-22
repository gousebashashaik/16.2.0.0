/**
 *
 */
package uk.co.tui.shortlist.data;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

/**
 * @author akhileshvarma.d
 *
 */
public class ShortlistAccomData
{
   private String accomodationName;

   private String accomCode;

   private DateTime accomStartDate;

   private List<ShortlistRoomData> roomDetails = new ArrayList<ShortlistRoomData>();

   private String brandType;

   private MoneyData price = new MoneyData();

   private String officialRating;

   // Added for supporting iscape shortlist
   private String commercialPriority;

   /**
    * @return the officialRating
    */
   public String getOfficialRating()
   {
      return officialRating;
   }

   /**
    * @param officialRating the officialRating to set
    */
   public void setOfficialRating(final String officialRating)
   {
      this.officialRating = officialRating;
   }

   /**
    * @return the commercialPriority
    */
   public String getCommercialPriority()
   {
      return commercialPriority;
   }

   /**
    * @param commercialPriority the commercialPriority to set
    */
   public void setCommercialPriority(final String commercialPriority)
   {
      this.commercialPriority = commercialPriority;
   }

   /**
    * @return the brandType
    */
   public String getBrandType()
   {
      return brandType;
   }

   /**
    * @param brandType the brandType to set
    */
   public void setBrandType(final String brandType)
   {
      this.brandType = brandType;
   }

   /**
    * @return the price
    */
   public MoneyData getPrice()
   {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final MoneyData price)
   {
      this.price = price;
   }

   /**
    * @return the accomodationName
    */
   public String getAccomodationName()
   {
      return accomodationName;
   }

   /**
    * @param accomodationName the accomodationName to set
    */
   public void setAccomodationName(final String accomodationName)
   {
      this.accomodationName = accomodationName;
   }

   /**
    * @return the accomCode
    */
   public String getAccomCode()
   {
      return accomCode;
   }

   /**
    * @param accomCode the accomCode to set
    */
   public void setAccomCode(final String accomCode)
   {
      this.accomCode = accomCode;
   }

   /**
    * @return the accomStartDate
    */
   public DateTime getAccomStartDate()
   {
      return accomStartDate;
   }

   /**
    * @param accomStartDate the accomStartDate to set
    */
   public void setAccomStartDate(final DateTime accomStartDate)
   {
      this.accomStartDate = accomStartDate;
   }

   /**
    * @return the roomDetails
    */
   public List<ShortlistRoomData> getRoomDetails()
   {
      return roomDetails;
   }

   /**
    * @param roomDetails the roomDetails to set
    */
   public void setRoomDetails(final List<ShortlistRoomData> roomDetails)
   {
      this.roomDetails = roomDetails;
   }

}
