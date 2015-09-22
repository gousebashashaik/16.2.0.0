/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.domain.lite.ExtraFacilityGroup;
import uk.co.tui.book.domain.lite.Passenger;

/**
 * @author sunil.bd
 *
 */
public class ExtraFacilityViewData implements Cloneable
{
   private String inventoryId;

   private String inventoryCode;

   private String code;

   private String description;

   private String summaryDescription;

   private String infantDescription;

   private String details;

   private Integer quantity;

   private ExtraFacilityGroup extraFacilityGroup;

   private DynamicContentViewData flightOptionsContentViewData;

   private BigDecimal price;

   /** The currency appended price. */
   private String currencyAppendedPrice = StringUtils.EMPTY;

   private boolean selected;

   /**
    * Holds whether the extra is included.
    */

   private boolean bookable;

   private List<Passenger> passengers;

   private Integer paxCount;

   private List<PassengerViewData> passengerViewData = new ArrayList<PassengerViewData>();

   private PaxCompositionViewData paxCompositionViewData;

   private String startDate;

   private String endDate;

   private Map<String, Integer> infantOptionsMap = new HashMap<String, Integer>();

   private InfantOptionsCompViewData infantOptionsCompViewData;

   private String selection;

   /** The category code. */
   private String categoryCode = StringUtils.EMPTY;

   private static final int PRIME = 31;

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
    * @param passengerViewData the passengerViewData to set
    */
   public void setPassengerViewData(final List<PassengerViewData> passengerViewData)
   {
      this.passengerViewData = passengerViewData;
   }

   /**
    * @return the passengerViewData
    */
   public List<PassengerViewData> getPassengerViewData()
   {
      return passengerViewData;
   }

   /**
    * @return the details
    */
   public String getDetails()
   {
      return details;
   }

   /**
    * @param details the details to set
    */
   public void setDetails(final String details)
   {
      this.details = details;
   }

   /**
    * @return the inventoryId
    */
   public String getInventoryId()
   {
      return inventoryId;
   }

   /**
    * @param inventoryId the inventoryId to set
    */
   public void setInventoryId(final String inventoryId)
   {
      this.inventoryId = inventoryId;
   }

   /**
    * @return the inventoryCode
    */
   public String getInventoryCode()
   {
      return inventoryCode;
   }

   /**
    * @param inventoryCode the inventoryCode to set
    */
   public void setInventoryCode(final String inventoryCode)
   {
      this.inventoryCode = inventoryCode;
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
    * @return the quantity
    */
   public Integer getQuantity()
   {
      return quantity;
   }

   /**
    * @param quantity the quantity to set
    */
   public void setQuantity(final Integer quantity)
   {
      this.quantity = quantity;
   }

   /**
    * @return the extraFacilityGroup
    */
   public ExtraFacilityGroup getExtraFacilityGroup()
   {
      return extraFacilityGroup;
   }

   /**
    * @param extraFacilityGroup the extraFacilityGroup to set
    */
   public void setExtraFacilityGroup(final ExtraFacilityGroup extraFacilityGroup)
   {
      this.extraFacilityGroup = extraFacilityGroup;
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
   public void setSelected(final boolean selected)
   {
      this.selected = selected;
   }

   /**
    * @return the bookable
    */
   public boolean isBookable()
   {
      return bookable;
   }

   /**
    * @param bookable the bookable to set
    */
   public void setBookable(final boolean bookable)
   {
      this.bookable = bookable;
   }

   /**
    * @return the passengers
    */
   public List<Passenger> getPassengers()
   {
      return passengers;
   }

   /**
    * @param passengers the passengers to set
    */
   public void setPassengers(final List<Passenger> passengers)
   {
      this.passengers = passengers;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {

      int result = 1;
      result = PRIME * result + ((inventoryCode == null) ? 0 : inventoryCode.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      return super.equals(obj);
   }

   /**
    * @return the paxCompositionViewData
    */
   public PaxCompositionViewData getPaxCompositionViewData()
   {
      return paxCompositionViewData;
   }

   /**
    * @param paxCompositionViewData the paxCompositionViewData to set
    */
   public void setPaxCompositionViewData(final PaxCompositionViewData paxCompositionViewData)
   {
      this.paxCompositionViewData = paxCompositionViewData;
   }

   /**
    * @return the startDate
    */
   public String getStartDate()
   {
      return startDate;
   }

   /**
    * @param startDate the startDate to set
    */
   public void setStartDate(final String startDate)
   {
      this.startDate = startDate;
   }

   /**
    * @return the endDate
    */
   public String getEndDate()
   {
      return endDate;
   }

   /**
    * @param endDate the endDate to set
    */
   public void setEndDate(final String endDate)
   {
      this.endDate = endDate;
   }

   /**
    * @return the infantOptionsMap
    */
   public Map<String, Integer> getInfantOptionsMap()
   {
      return infantOptionsMap;
   }

   /**
    * @param infantOptionsMap the infantOptionsMap to set
    */
   public void setInfantOptionsMap(final Map<String, Integer> infantOptionsMap)
   {
      this.infantOptionsMap = infantOptionsMap;
   }

   /**
    * @return the infantOptionsCompViewData
    */
   public InfantOptionsCompViewData getInfantOptionsCompViewData()
   {
      return infantOptionsCompViewData;
   }

   /**
    * @param infantOptionsCompViewData the infantOptionsCompViewData to set
    */
   public void setInfantOptionsCompViewData(
      final InfantOptionsCompViewData infantOptionsCompViewData)
   {
      this.infantOptionsCompViewData = infantOptionsCompViewData;
   }

   /**
    * @return the paxCount
    */
   public Integer getPaxCount()
   {
      return paxCount;
   }

   /**
    * @param paxCount the paxCount to set
    */
   public void setPaxCount(final Integer paxCount)
   {
      this.paxCount = paxCount;
   }

   /**
    * @return the currencyAppendedPrice
    */
   public String getCurrencyAppendedPrice()
   {
      return currencyAppendedPrice;
   }

   /**
    * @param currencyAppendedPrice the currencyAppendedPrice to set
    */
   public void setCurrencyAppendedPrice(final String currencyAppendedPrice)
   {
      this.currencyAppendedPrice = currencyAppendedPrice;
   }

   /**
    * @return the flightOptionsContentViewData
    */
   public DynamicContentViewData getFlightOptionsContentViewData()
   {
      return flightOptionsContentViewData;
   }

   /**
    * @param flightOptionsContentViewData the flightOptionsContentViewData to set
    */
   public void setFlightOptionsContentViewData(
      final DynamicContentViewData flightOptionsContentViewData)
   {
      this.flightOptionsContentViewData = flightOptionsContentViewData;
   }

   /**
    * @return the infantDescription
    */
   public String getInfantDescription()
   {
      return infantDescription;
   }

   /**
    * @param infantDescription the infantDescription to set
    */
   public void setInfantDescription(final String infantDescription)
   {
      this.infantDescription = infantDescription;
   }

   /**
    * @return the summaryDescription
    */
   public String getSummaryDescription()
   {
      return summaryDescription;
   }

   /**
    * @param summaryDescription the summaryDescription to set
    */
   public void setSummaryDescription(final String summaryDescription)
   {
      this.summaryDescription = summaryDescription;
   }

   /**
    * @return the selection
    */
   public String getSelection()
   {
      return selection;
   }

   /**
    * @param selection the selection to set
    */
   public void setSelection(final String selection)
   {
      this.selection = selection;
   }

   /**
    * @return the categoryCode
    */
   public String getCategoryCode()
   {
      return categoryCode;
   }

   /**
    * @param categoryCode the categoryCode to set
    */
   public void setCategoryCode(final String categoryCode)
   {
      this.categoryCode = categoryCode;
   }

   /**
    * @return the price
    */
   public BigDecimal getPrice()
   {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final BigDecimal price)
   {
      this.price = price;
   }
}
