/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.Date;
import java.util.List;

/**
 * The Class DepartureDateAvailabilityViewData.
 *
 * @author samantha.gd
 */
public class DepartureDateAvailabilityViewData
{

   /** The date. */
   private Date date;

   /** The display date. */
   private String displayDate;

   /** The selected. */
   private boolean selected;

   /** The price. */
   private String price;

   /** The flight view data. */
   private List<PackageViewData> flightViewData;


   /**
    * @return the date
    */
   public Date getDate()
   {
      return (Date) date.clone();
   }


   /**
    * @param date the date to set
    */
   public void setDate(Date date)
   {
      this.date = (Date) date.clone();
   }


   /**
    * @return the displayDate
    */
   public String getDisplayDate()
   {
      return displayDate;
   }


   /**
    * @param displayDate the displayDate to set
    */
   public void setDisplayDate(String displayDate)
   {
      this.displayDate = displayDate;
   }


   /**
    * @return the selected
    */
   public boolean isSelected()
   {
      return selected;
   }


   /**
    * @param selected the selected to set
    */
   public void setSelected(boolean selected)
   {
      this.selected = selected;
   }


   /**
    * @return the price
    */
   public String getPrice()
   {
      return price;
   }


   /**
    * @param price the price to set
    */
   public void setPrice(String price)
   {
      this.price = price;
   }


   /**
    * @return the flightViewData
    */
   public List<PackageViewData> getFlightViewData()
   {
      return flightViewData;
   }


   /**
    * @param flightViewData the flightViewData to set
    */
   public void setFlightViewData(List<PackageViewData> flightViewData)
   {
      this.flightViewData = flightViewData;
   }


}
