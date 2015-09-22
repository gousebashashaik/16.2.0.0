/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class holds the view data for ExtraFacility..
 *
 * @author madhumathi.m
 *
 */
public class ExtraFacilityViewData
{
   /**
    * Holds the extra facility code.
    */
   private String code = StringUtils.EMPTY;

   /**
    * Holds the bag extra facility weight code.
    */
   private String weightCode = StringUtils.EMPTY;

   /**
    * Holds the extra facility groupCode.
    */
   private String groupCode = StringUtils.EMPTY;

   /**
    * Holds the description of the facility, if one sent.
    */
   private String description = StringUtils.EMPTY;

   /**
    * Holds the available number of this extras.
    */
   private int quantity;

   /** Holds the selected quantity. */
   private int selectedQuantity;

   /** The available quantity. */
   private int availableQuantity;

   /** The limited availability. */
   private boolean limitedAvailability;

   /** The limited availability. */
   private String limitedAvailabilityText;

   /** The seat warning message. */
   private String seatWarningMessage;

   /**
    * Holds whether the extra is default selected.
    */
   private boolean free;

   /**
    * Holds whether the extra is included.
    */
   private boolean included;

   /**
    * Holds the number of seconds for the fading message or (Not Applicable).
    */
   private String fadingTimer = "Na";

   /**
    * Holds the price of extra.
    */
   private BigDecimal price = BigDecimal.ZERO;

   /**
    * Holds the adultPrice of extra.
    */
   private BigDecimal adultPrice = BigDecimal.ZERO;

   /**
    * Holds the childPrice of extra.
    */
   private BigDecimal childPrice = BigDecimal.ZERO;

   /**
    * Holds the party Relevant View Data.
    */
   private int maxChildAge;

   /**
    * Holds the party Relevant View Data.
    */
   private int minAge;

   private int maxAge;

   /**
    * Holds the flag whether this extrafacility is selected or not.
    */
   private boolean selected;

   /** The category code. */
   private String categoryCode = StringUtils.EMPTY;

   /**
    * Holds the party Relevant View Data.
    */
   private String selection = StringUtils.EMPTY;

   /** The currency appended price. */
   private String currencyAppendedPrice = StringUtils.EMPTY;

   /** The currency appended per person price. */
   private String currencyAppendedPerPersonPrice = StringUtils.EMPTY;

   /** The currency appended price per night */
   private String currencyAppendedPricePerNight = StringUtils.EMPTY;

   /** The currency appended price per class */
   private String currencyAppendedPricePerClass = StringUtils.EMPTY;

   /** The mapping of passengers and this ExtraFacility. */
   private PassengerExtraFacilityMapping passengerExtraFacilityMapping;

   /**
    * Holds the max allowed passengers mainly used for Taxi transfer.
    */
   private int maxAllowedPassengers;

   /**
    * Holds the currency appended per taxi price.
    */
   private String currencyAppendedPerTaxiPrice = StringUtils.EMPTY;

   /** The currency appended perchild price. */
   private String currencyAppendedChildPrice = StringUtils.EMPTY;

   /** The currency appended peradult price. */
   private String currencyAppendedAdultPrice = StringUtils.EMPTY;

   private String paxType;

   /** The summary description. */
   private String summaryDescription = StringUtils.EMPTY;

   /** The currency appended Total price. */
   private String currencyAppendedTotalPrice = StringUtils.EMPTY;

   private int positionIndex;

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
    * @return the groupCode
    */
   public String getGroupCode()
   {
      return groupCode;
   }

   /**
    * @param groupCode the groupCode to set
    */
   public void setGroupCode(final String groupCode)
   {
      this.groupCode = groupCode;
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
   public int getQuantity()
   {
      return quantity;
   }

   /**
    * @param quantity the quantity to set
    */
   public void setQuantity(final int quantity)
   {
      this.quantity = quantity;
   }

   /**
    * @return the extraFree
    */
   public boolean isFree()
   {
      return free;
   }

   /**
    * @param free the extraFree to set
    */
   public void setFree(final boolean free)
   {
      this.free = free;
   }

   /**
    * @return the included
    */
   public boolean isIncluded()
   {
      return included;
   }

   /**
    * @param included the included to set
    */
   public void setIncluded(final boolean included)
   {
      this.included = included;
   }

   /**
    * @return the price
    */
   public BigDecimal getPrice()
   {
      return price.setScale(ViewDataConstants.ALLOWED_DECIMAL_TWO, BigDecimal.ROUND_UP);
   }

   /**
    * @param currencyAppendedPrice the currencyAppendedPrice to set
    */
   public void setCurrencyAppendedPrice(final String currencyAppendedPrice)
   {
      this.currencyAppendedPrice = currencyAppendedPrice;
   }

   /**
    * @return the price
    */
   public String getCurrencyAppendedPrice()
   {
      return currencyAppendedPrice;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final BigDecimal price)
   {
      this.price = price;
   }

   /**
    * @return the adultPrice
    */
   public BigDecimal getAdultPrice()
   {
      return adultPrice;
   }

   /**
    * @param adultPrice the adultPrice to set
    */
   public void setAdultPrice(final BigDecimal adultPrice)
   {
      this.adultPrice = adultPrice;
   }

   /**
    * @return the childPrice
    */
   public BigDecimal getChildPrice()
   {
      return childPrice;
   }

   /**
    * @param childPrice the childPrice to set
    */
   public void setChildPrice(final BigDecimal childPrice)
   {
      this.childPrice = childPrice;
   }

   /**
    * @return the indicator to state selected or not.
    */
   public boolean isSelected()
   {
      return selected;
   }

   /**
    * @param selected the indicator to state selected or not.
    */
   public void setSelected(final boolean selected)
   {
      this.selected = selected;
   }

   /**
    * Return the string representation of the object.
    *
    * @return the string representation of the object.
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return ToStringBuilder.reflectionToString(this);
   }

   /**
    * @return the passengerExtraFacilityMapping
    */
   public PassengerExtraFacilityMapping getPassengerExtraFacilityMapping()
   {
      if (passengerExtraFacilityMapping == null)
      {
         passengerExtraFacilityMapping = new PassengerExtraFacilityMapping();
      }
      return passengerExtraFacilityMapping;
   }

   /**
    * @param passengerExtraFacilityMapping the passengerExtraFacilityMapping to set
    */
   public void setPassengerExtraFacilityMapping(
      final PassengerExtraFacilityMapping passengerExtraFacilityMapping)
   {
      this.passengerExtraFacilityMapping = passengerExtraFacilityMapping;
   }

   /**
    * @return the maxChildAge
    */
   public int getMaxChildAge()
   {
      return maxChildAge;
   }

   /**
    * @param maxChildAge the maxChildAge to set
    */
   public void setMaxChildAge(final int maxChildAge)
   {
      this.maxChildAge = maxChildAge;
   }

   /**
    * @return the minAge
    */
   public int getMinAge()
   {
      return minAge;
   }

   /**
    * @param minAge the minAge to set
    */
   public void setMinAge(final int minAge)
   {
      this.minAge = minAge;
   }

   /**
    * @return the currencyAppendedPerPersonPrice
    */
   public String getCurrencyAppendedPerPersonPrice()
   {
      return currencyAppendedPerPersonPrice;
   }

   /**
    * @param currencyAppendedPerPersonPrice the currencyAppendedPerPersonPrice to set
    */
   public void setCurrencyAppendedPerPersonPrice(final String currencyAppendedPerPersonPrice)
   {
      this.currencyAppendedPerPersonPrice = currencyAppendedPerPersonPrice;
   }

   /**
    * @return the maxAllowedPassengers
    */
   public int getMaxAllowedPassengers()
   {
      return maxAllowedPassengers;
   }

   /**
    * @param maxAllowedPassengers the maxAllowedPassengers to set
    */
   public void setMaxAllowedPassengers(final int maxAllowedPassengers)
   {
      this.maxAllowedPassengers = maxAllowedPassengers;
   }

   /**
    * @return the currencyAppendedPerTaxiPrice
    */
   public String getCurrencyAppendedPerTaxiPrice()
   {
      return currencyAppendedPerTaxiPrice;
   }

   /**
    * @param currencyAppendedPerTaxiPrice the currencyAppendedPerTaxiPrice to set
    */
   public void setCurrencyAppendedPerTaxiPrice(final String currencyAppendedPerTaxiPrice)
   {
      this.currencyAppendedPerTaxiPrice = currencyAppendedPerTaxiPrice;
   }

   /**
    * @return the currencyAppendedChildPrice
    */
   public String getCurrencyAppendedChildPrice()
   {
      return currencyAppendedChildPrice;
   }

   /**
    * @param currencyAppendedChildPrice the currencyAppendedChildPrice to set
    */
   public void setCurrencyAppendedChildPrice(final String currencyAppendedChildPrice)
   {
      this.currencyAppendedChildPrice = currencyAppendedChildPrice;
   }

   /**
    * @return the currencyAppendedAdultPrice
    */
   public String getCurrencyAppendedAdultPrice()
   {
      return currencyAppendedAdultPrice;
   }

   /**
    * @param currencyAppendedAdultPrice the currencyAppendedAdultPrice to set
    */
   public void setCurrencyAppendedAdultPrice(final String currencyAppendedAdultPrice)
   {
      this.currencyAppendedAdultPrice = currencyAppendedAdultPrice;
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
    * @return the fadingTimer
    */
   public String getFadingTimer()
   {
      return fadingTimer;
   }

   /**
    * @param fadingTimer the fadingTimer to set
    */
   public void setFadingTimer(final String fadingTimer)
   {
      this.fadingTimer = fadingTimer;
   }

   /**
    * Gets the currencyAppendedPricePerNight
    */
   public String getCurrencyAppendedPricePerNight()
   {
      return currencyAppendedPricePerNight;
   }

   /**
    * Sets the currencyAppendedPricePerNight
    */
   public void setCurrencyAppendedPricePerNight(final String currencyAppendedPricePerNight)
   {
      this.currencyAppendedPricePerNight = currencyAppendedPricePerNight;
   }

   /**
    * Gets the currencyAppendedPricePerNight
    */
   public String getCurrencyAppendedPricePerClass()
   {
      return currencyAppendedPricePerClass;
   }

   /**
    * Sets the currencyAppendedPricePerNight
    */
   public void setCurrencyAppendedPricePerClass(final String currencyAppendedPricePerClass)
   {
      this.currencyAppendedPricePerClass = currencyAppendedPricePerClass;
   }

   /**
    * Gets the selectedQuantity
    */
   public int getSelectedQuantity()
   {
      return selectedQuantity;
   }

   /**
    * Sets the selectedQuantity
    */
   public void setSelectedQuantity(final int selectedQuantity)
   {
      this.selectedQuantity = selectedQuantity;
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
    * @return the weightCode
    */
   public String getWeightCode()
   {
      return weightCode;
   }

   /**
    * @param weightCode the weightCode to set
    */
   public void setWeightCode(final String weightCode)
   {
      this.weightCode = weightCode;
   }

   /**
    * @return the availableQuantity
    */
   public int getAvailableQuantity()
   {
      return availableQuantity;
   }

   /**
    * @param availableQuantity the availableQuantity to set
    */
   public void setAvailableQuantity(final int availableQuantity)
   {
      this.availableQuantity = availableQuantity;
   }

   /**
    * @return the limitedAvailability
    */
   public boolean isLimitedAvailability()
   {
      return limitedAvailability;
   }

   /**
    * @param limitedAvailability the limitedAvailability to set
    */
   public void setLimitedAvailability(final boolean limitedAvailability)
   {
      this.limitedAvailability = limitedAvailability;
   }

   /**
    * Gets the limited availability text.
    *
    * @return the limitedAvailabilityText
    */
   public String getLimitedAvailabilityText()
   {
      return limitedAvailabilityText;
   }

   /**
    * Sets the limited availability text.
    *
    * @param limitedAvailabilityText the limitedAvailabilityText to set
    */
   public void setLimitedAvailabilityText(final String limitedAvailabilityText)
   {
      this.limitedAvailabilityText = limitedAvailabilityText;
   }

   /**
    * @return the paxType
    */
   public String getPaxType()
   {
      return paxType;
   }

   /**
    * @param paxType the paxType to set
    */
   public void setPaxType(final String paxType)
   {
      this.paxType = paxType;
   }

   /**
    * @return the maxAge
    */
   public int getMaxAge()
   {
      return maxAge;
   }

   /**
    * @param maxAge the maxAge to set
    */
   public void setMaxAge(final int maxAge)
   {
      this.maxAge = maxAge;
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
    * @return the currencyAppendedTotalPrice
    */
   public String getCurrencyAppendedTotalPrice()
   {
      return currencyAppendedTotalPrice;
   }

   /**
    * @param currencyAppendedTotalPrice the currencyAppendedTotalPrice to set
    */
   public void setCurrencyAppendedTotalPrice(final String currencyAppendedTotalPrice)
   {
      this.currencyAppendedTotalPrice = currencyAppendedTotalPrice;
   }

   /**
    * @return the seatWarningMessage
    */
   public String getSeatWarningMessage()
   {
      return seatWarningMessage;
   }

   /**
    * @param seatWarningMessage the seatWarningMessage to set
    */
   public void setSeatWarningMessage(final String seatWarningMessage)
   {
      this.seatWarningMessage = seatWarningMessage;
   }

   /**
    * @return the positionIndex
    */
   public int getPositionIndex()
   {
      return positionIndex;
   }

   /**
    * @param positionIndex the positionIndex to set
    */
   public void setPositionIndex(final int positionIndex)
   {
      this.positionIndex = positionIndex;
   }

}
