/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class holds the Package related view data.
 *
 * @author madhumathi.m
 *
 */
public class PackageViewData
{

   /**
    * Holds the party Relevant View Data.
    */
   private PaxCompositionViewData paxViewData;

   /**
    * Holds the Accommodation Relevant View Data.
    */
   private List<AccommodationViewData> accomViewData;

   /**
    * Holds the Flight Relevant View Data.
    */
   private List<FlightViewData> flightViewData;

   /**
    * Holds the PriceBreakDown Relevant View Data.
    */
   private List<PriceBreakDownViewData> priceBreakdownViewData;

   /**
    * Holds the total Price of the holiday.
    */
   private BigDecimal roundUpTotalCost;

   /**
    * Holds the total Price of the holiday.
    */
   private BigDecimal pricePerPerson;

   /**
    * Holds the total Price of the holiday.
    */
   private BigDecimal onlineDiscount;

   /** The currency appended price per person. */
   private String currencyAppendedPricePerPerson = StringUtils.EMPTY;

   /** The currency appended round up total cost. */
   private String currencyAppendedRoundUpTotalCost = StringUtils.EMPTY;

   /** The currency appended online discount. */
   private String currencyAppendedOnlineDiscount = StringUtils.EMPTY;

   /** The summary panel static content view data. */
   private SummaryPanelStaticContentViewData summaryPanelStaticContentViewData;

   /** The selected. */
   private boolean selected;

   /** lapland day trip indicator. */
   private boolean isLaplandDayTrip;

   /**
    * Holds the selected extrafacility list.
    */
   private List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewData;

   /**
    * Represents number of seniors in the party.
    */
   private List<PassengerViewData> passenger = new ArrayList<PassengerViewData>();

   /** The package id. */
   private String id = StringUtils.EMPTY;

   /**
    * Holds the listOf Memos.
    */
   private MemoViewData memoViewData;

   private boolean saved;

   /**
    * Boolean to decide the display of Save Button.
    */
   private boolean displaySaveButton = true;

   /** The flag to identify multicom Third Party Flight. */
   private boolean multicomThirdPartyFlight;

   /** The CarrierCode of multicom Third Party Flight. */
   private String multicomThirdPartyFlightCarrierCode = StringUtils.EMPTY;

   /** The tracs third party flight. */
   private boolean tracsThirdPartyFlight;

   /**
    * Gets the pax view data.
    *
    * @return the paxViewData
    */
   public PaxCompositionViewData getPaxViewData()
   {
      if (this.paxViewData == null)
      {
         this.paxViewData = new PaxCompositionViewData();
      }
      return paxViewData;
   }

   /**
    * Sets the currency appended price per person.
    *
    * @param currencyAppendedPricePerPerson the currencyAppendedPricePerPerson to set
    */
   public void setCurrencyAppendedPricePerPerson(final String currencyAppendedPricePerPerson)
   {
      this.currencyAppendedPricePerPerson = currencyAppendedPricePerPerson;
   }

   /**
    * Sets the currency appended round up total cost.
    *
    * @param currencyAppendedRoundUpTotalCost the currencyAppendedRoundUpTotalCost to set
    */
   public void setCurrencyAppendedRoundUpTotalCost(final String currencyAppendedRoundUpTotalCost)
   {
      this.currencyAppendedRoundUpTotalCost = currencyAppendedRoundUpTotalCost;
   }

   /**
    * Sets the currency appended online discount.
    *
    * @param currencyAppendedOnlineDiscount the currencyAppendedOnlineDiscount to set
    */
   public void setCurrencyAppendedOnlineDiscount(final String currencyAppendedOnlineDiscount)
   {
      this.currencyAppendedOnlineDiscount = currencyAppendedOnlineDiscount;
   }

   /**
    * Sets the pax view data.
    *
    * @param paxViewData the paxViewData to set
    */
   public void setPaxViewData(final PaxCompositionViewData paxViewData)
   {
      this.paxViewData = paxViewData;
   }

   /**
    * Gets the price breakdown view data.
    *
    * @return the priceBreakdownViewData
    */
   public List<PriceBreakDownViewData> getPriceBreakdownViewData()
   {
      if (CollectionUtils.isEmpty(this.priceBreakdownViewData))
      {
         this.priceBreakdownViewData = new ArrayList<PriceBreakDownViewData>();
      }
      return this.priceBreakdownViewData;
   }

   /**
    * Gets the round up total cost.
    *
    * @return the roundUpTotalCost
    */
   public BigDecimal getRoundUpTotalCost()
   {
      return this.roundUpTotalCost;
   }

   /**
    * Gets the currency appended round up total cost.
    *
    * @return the roundUpTotalCost
    */
   public String getCurrencyAppendedRoundUpTotalCost()
   {
      return this.currencyAppendedRoundUpTotalCost;
   }

   /**
    * Sets the round up total cost.
    *
    * @param roundUpTotalCost the totalHolidayPrice to set
    */
   public void setRoundUpTotalCost(final BigDecimal roundUpTotalCost)
   {
      this.roundUpTotalCost = roundUpTotalCost;
   }

   /**
    * Gets the price per person.
    *
    * @return the pricePerPerson
    */
   public BigDecimal getPricePerPerson()
   {
      return pricePerPerson;
   }

   /**
    * Gets the currency appended price per person.
    *
    * @return the pricePerPerson
    */
   public String getCurrencyAppendedPricePerPerson()
   {
      return currencyAppendedPricePerPerson;
   }

   /**
    * Sets the price per person.
    *
    * @param pricePerPerson the pricePerPerson to set
    */
   public void setPricePerPerson(final BigDecimal pricePerPerson)
   {
      this.pricePerPerson = pricePerPerson;
   }

   /**
    * Gets the online discount.
    *
    * @return the onlineDiscount
    */
   public BigDecimal getOnlineDiscount()
   {
      return onlineDiscount;
   }

   /**
    * Gets the currency appended online discount which is negated.
    *
    * @return the onlineDiscount
    */
   public String getCurrencyAppendedOnlineDiscount()
   {
      return currencyAppendedOnlineDiscount;
   }

   /**
    * Sets the online discount.
    *
    * @param onlineDiscount the onlineDiscount to set
    */
   public void setOnlineDiscount(final BigDecimal onlineDiscount)
   {
      this.onlineDiscount = onlineDiscount;
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
    * Gets the extra facility category view data.
    *
    * @return the extraFacilityCategoryViewData
    */
   public List<ExtraFacilityCategoryViewData> getExtraFacilityCategoryViewData()
   {
      if (CollectionUtils.isEmpty(this.extraFacilityCategoryViewData))
      {
         this.extraFacilityCategoryViewData = new ArrayList<ExtraFacilityCategoryViewData>();
      }
      return extraFacilityCategoryViewData;
   }

   /**
    * Sets the extra facility category view data.
    *
    * @param extraFacilityCategoryViewData the extraFacilityCategoryViewData to set
    */
   public void setExtraFacilityCategoryViewData(
      final List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewData)
   {
      this.extraFacilityCategoryViewData = extraFacilityCategoryViewData;
   }

   /**
    * Gets the passenger.
    *
    * @return the passenger
    */
   public List<PassengerViewData> getPassenger()
   {
      return passenger;
   }

   /**
    * Sets the passenger.
    *
    * @param passenger the passenger to set
    */
   public void setPassenger(final List<PassengerViewData> passenger)
   {
      this.passenger = passenger;
   }

   /**
    * Checks if is selected.
    *
    * @return true, if is selected
    */
   public boolean isSelected()
   {
      return selected;
   }

   /**
    * Sets the selected.
    *
    * @param selected the new selected
    */
   public void setSelected(final boolean selected)
   {
      this.selected = selected;
   }

   /**
    * Gets the package id.
    *
    * @return the package id
    */
   public String getId()
   {
      return id;
   }

   /**
    * Sets the package id.
    *
    * @param id the new package id
    */
   public void setId(final String id)
   {
      this.id = id;
   }

   /**
    * Gets the memo view data.
    *
    * @return the memoViewData
    */
   public MemoViewData getMemoViewData()
   {
      return memoViewData;
   }

   /**
    * Sets the memo view data.
    *
    * @param memoViewData the memoViewData to set
    */
   public void setMemoViewData(final MemoViewData memoViewData)
   {
      this.memoViewData = memoViewData;
   }

   /**
    * Checks if the holiay is a lapland day trip.
    *
    * @return the isLaplandDayTrip
    */
   public boolean isLaplandDayTrip()
   {
      return isLaplandDayTrip;
   }

   /**
    * Sets the lapland day trip.
    *
    * @param isLaplandDayTrip the isLaplandDayTrip to set
    */
   public void setLaplandDayTrip(final boolean isLaplandDayTrip)
   {
      this.isLaplandDayTrip = isLaplandDayTrip;
   }

   /**
    * @return the summaryPanelStaticContentViewData
    */
   public SummaryPanelStaticContentViewData getSummaryPanelStaticContentViewData()
   {
      return summaryPanelStaticContentViewData;
   }

   /**
    * @param summaryPanelStaticContentViewData the summaryPanelStaticContentViewData to set
    */
   public void setSummaryPanelStaticContentViewData(
      final SummaryPanelStaticContentViewData summaryPanelStaticContentViewData)
   {
      this.summaryPanelStaticContentViewData = summaryPanelStaticContentViewData;
   }

   /**
    * @return the saved
    */
   public boolean isSaved()
   {
      return saved;
   }

   /**
    * @param saved the saved to set
    */
   public void setSaved(final boolean saved)
   {
      this.saved = saved;
   }

   /**
    * @return the displaySaveButton
    */
   public boolean isDisplaySaveButton()
   {
      return displaySaveButton;
   }

   /**
    * @param displaySaveButton the displaySaveButton to set
    */
   public void setDisplaySaveButton(final boolean displaySaveButton)
   {
      this.displaySaveButton = displaySaveButton;
   }

   /**
    * @return the multicomThirdPartyFlight
    */
   public boolean isMulticomThirdPartyFlight()
   {
      return multicomThirdPartyFlight;
   }

   /**
    * @param multicomThirdPartyFlight the multicomThirdPartyFlight to set
    */
   public void setMulticomThirdPartyFlight(final boolean multicomThirdPartyFlight)
   {
      this.multicomThirdPartyFlight = multicomThirdPartyFlight;
   }

   /**
    * @return the multicomThirdPartyFlightCarrierCode
    */
   public String getMulticomThirdPartyFlightCarrierCode()
   {
      return multicomThirdPartyFlightCarrierCode;
   }

   /**
    * @param multicomThirdPartyFlightCarrierCode the multicomThirdPartyFlightCarrierCode to set
    */
   public void setMulticomThirdPartyFlightCarrierCode(
      final String multicomThirdPartyFlightCarrierCode)
   {
      this.multicomThirdPartyFlightCarrierCode = multicomThirdPartyFlightCarrierCode;
   }

   /**
    * Checks if is tracs third party flight.
    *
    * @return true, if is tracs third party flight
    */
   public boolean isTracsThirdPartyFlight()
   {
      return tracsThirdPartyFlight;
   }

   /**
    * Sets the tracs third party flight.
    *
    * @param tracsThirdPartyFlight the new tracs third party flight
    */
   public void setTracsThirdPartyFlight(final boolean tracsThirdPartyFlight)
   {
      this.tracsThirdPartyFlight = tracsThirdPartyFlight;
   }

   /**
    * @return the accomViewData
    */
   public List<AccommodationViewData> getAccomViewData()
   {
      if (CollectionUtils.isEmpty(this.accomViewData))
      {
         this.accomViewData = new ArrayList<AccommodationViewData>();
      }
      return this.accomViewData;
   }

   /**
    * @param accomViewData the accomViewData to set
    */
   public void setAccomViewData(final List<AccommodationViewData> accomViewData)
   {
      this.accomViewData = accomViewData;
   }

   /**
    * @return the flightViewData
    */
   public List<FlightViewData> getFlightViewData()
   {
      return flightViewData;
   }

   /**
    * @param flightViewData the flightViewData to set
    */
   public void setFlightViewData(final List<FlightViewData> flightViewData)
   {
      this.flightViewData = flightViewData;
   }

}
