/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author veena.pn
 *
 */
public class PackageViewData
{
   /**
    * Holds the party Relevant View Data.
    */
   private PaxCompositionViewData paxViewData;

   private List<ErrataViewData> errataViewData;

   private List<ErrataViewData> flightErrataViewData;

   private String bookingRefNum;

   private String tAndCDomainURL;

   private String cpsTAndCDomain;

   private String relativeTAndCUrl;

   private String inventoryType;

   private List<SummaryRoomViewData> summaryRoomViewData;

   /**
    * Holds the PriceBreakDown Relevant View Data.
    */
   private BigDecimal totalBasicCost;

   private BigDecimal totalExtraCost;

   private boolean makePaymentButtonStatus;

   private BigDecimal roundUpTotalCost;

   private List<PriceBreakDownViewData> priceBreakdownViewData;

   private String termsAndConditionURl;

   private BookingDetailsViewData bookingDetails;

   private List<DepositViewData> deposits;

   private Map<String, String> termsAndConditionsLinks = new HashMap<String, String>();

   private BigDecimal pricePerPerson;

   private BigDecimal onlineDiscount;

   /** The currency appended price per person. */
   private String currencyAppendedPricePerPerson = StringUtils.EMPTY;

   /** The currency appended round up total cost. */
   private String currencyAppendedRoundUpTotalCost = StringUtils.EMPTY;

   /** The currency appended online discount. */
   private String currencyAppendedOnlineDiscount = StringUtils.EMPTY;

   /** The selected. */
   private boolean selected;

   /** The summary panel static content view data. */
   private SummaryPanelStaticContentViewData summaryPanelStaticContentViewData;

   /**
    * Holds the selected extrafacility list.
    */
   private List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewData;

   /**
    * Represents number of seniors in the party.
    */
   private List<PassengerViewData> passenger = new ArrayList<PassengerViewData>();

   /**
    * Holds the high level booking flag.
    */
   private HighLevelBookingViewData highLevelbookingViewData;

   private AvailableAccommodationViewData accomViewData;

   /** The package id. */
   private String id = StringUtils.EMPTY;

   /**
    * Holds the listOf Memos.
    */
   private MemoViewData memoViewData;

   /**
    * Holds the Flight Relevant View Data.
    */
   private FlightDetailsViewData flightViewData;

   private boolean saved;

   /** The flag to identify multicom Third Party Flight. */
   private boolean multicomThirdPartyFlight;

   /** The CarrierCode of multicom Third Party Flight. */
   private String multicomThirdPartyFlightCarrierCode = StringUtils.EMPTY;

   /** The tracs third party flight. */
   private boolean tracsThirdPartyFlight;

   /**
    * Boolean to decide the display of Save Button.
    */
   private boolean displaySaveButton = true;

   private int totalBagAllowance;

   private String bkgType;

   /** The show lead pax address. */
   private boolean showLeadPaxAddress;

   private boolean isPremiumSeat;

   /**
    * @return the isPremiumSeat
    */
   public boolean isPremiumSeat()
   {
      return isPremiumSeat;
   }

   /**
    * @param isPremiumSeat the isPremiumSeat to set
    */
   public void setPremiumSeat(final boolean isPremiumSeat)
   {
      this.isPremiumSeat = isPremiumSeat;
   }

   public BigDecimal getTotalBasicCost()
   {
      return totalBasicCost;
   }

   /**
    * @param totalBasicCost the totalBasicCost to set
    */
   public void setTotalBasicCost(final BigDecimal totalBasicCost)
   {
      this.totalBasicCost = totalBasicCost;
   }

   /**
    * @return the roundUpTotalCost
    */
   public BigDecimal getRoundUpTotalCost()
   {
      return roundUpTotalCost;
   }

   /**
    * @param roundUpTotalCost the roundUpTotalCost to set
    */
   public void setRoundUpTotalCost(final BigDecimal roundUpTotalCost)
   {
      this.roundUpTotalCost = roundUpTotalCost;
   }

   /**
    * @return the makePaymentButtonStatus
    */
   public boolean isMakePaymentButtonStatus()
   {
      return makePaymentButtonStatus;
   }

   /**
    * @param makePaymentButtonStatus the makePaymentButtonStatus to set
    */
   public void setMakePaymentButtonStatus(final boolean makePaymentButtonStatus)
   {
      this.makePaymentButtonStatus = makePaymentButtonStatus;
   }

   /**
    * @return the highLevelbookingViewData
    */
   public HighLevelBookingViewData getHighLevelbookingViewData()
   {
      return highLevelbookingViewData;
   }

   /**
    * @param highLevelbookingViewData the highLevelbookingViewData to set
    */
   public void setHighLevelbookingViewData(final HighLevelBookingViewData highLevelbookingViewData)
   {
      this.highLevelbookingViewData = highLevelbookingViewData;
   }

   /**
    * @return the flightViewData
    */
   public FlightDetailsViewData getFlightViewData()
   {
      return flightViewData;
   }

   /**
    * @param flightViewData the flightViewData to set
    */
   public void setFlightViewData(final FlightDetailsViewData flightViewData)
   {
      this.flightViewData = flightViewData;
   }

   /**
    * @return the bkgType
    */
   public String getBkgType()
   {
      return bkgType;
   }

   /**
    * @param bkgType the bkgType to set
    */
   public void setBkgType(final String bkgType)
   {
      this.bkgType = bkgType;
   }

   /**
    * @return the totalBagAllowance
    */
   public int getTotalBagAllowance()
   {
      return totalBagAllowance;
   }

   /**
    * @param totalBagAllowance the totalBagAllowance to set
    */
   public void setTotalBagAllowance(final int totalBagAllowance)
   {
      this.totalBagAllowance = totalBagAllowance;
   }

   /**
    * @return the availableAccommodationViewData
    */
   public AvailableAccommodationViewData getAvailableAccommodationViewData()
   {
      return accomViewData;
   }

   /**
    * @param availableAccommodationViewData the availableAccommodationViewData to set
    */
   public void setAvailableAccommodationViewData(final AvailableAccommodationViewData accomViewData)
   {
      this.accomViewData = accomViewData;
   }

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
    * Gets the currency appended round up total cost.
    *
    * @return the roundUpTotalCost
    */
   public String getCurrencyAppendedRoundUpTotalCost()
   {
      return this.currencyAppendedRoundUpTotalCost;
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
    * @return the memoViewData
    */
   public MemoViewData getMemoViewData()
   {
      return memoViewData;
   }

   /**
    * @param memoViewData the memoViewData to set
    */
   public void setMemoViewData(final MemoViewData memoViewData)
   {
      this.memoViewData = memoViewData;
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
    * @return the bookingRefNum
    */
   public String getBookingRefNum()
   {
      return bookingRefNum;
   }

   /**
    * @param bookingRefNum the bookingRefNum to set
    */
   public void setBookingRefNum(final String bookingRefNum)
   {
      this.bookingRefNum = bookingRefNum;
   }

   /**
    * @param priceBreakdownViewData the priceBreakdownViewData to set
    */
   public void setPriceBreakdownViewData(final List<PriceBreakDownViewData> priceBreakdownViewData)
   {
      this.priceBreakdownViewData = priceBreakdownViewData;
   }

   /**
    * @return the tAndCDomainURL
    */
   public String gettAndCDomainURL()
   {
      return tAndCDomainURL;
   }

   /**
    * @param tAndCDomainURL the tAndCDomainURL to set
    */
   public void settAndCDomainURL(final String tAndCDomainURL)
   {
      this.tAndCDomainURL = tAndCDomainURL;
   }

   /**
    * @return the cpsTAndCDomain
    */
   public String getCpsTAndCDomain()
   {
      return cpsTAndCDomain;
   }

   /**
    * @param cpsTAndCDomain the cpsTAndCDomain to set
    */
   public void setCpsTAndCDomain(final String cpsTAndCDomain)
   {
      this.cpsTAndCDomain = cpsTAndCDomain;
   }

   /**
    * @return the termsAndConditionURl
    */
   public String getTermsAndConditionURl()
   {
      return termsAndConditionURl;
   }

   /**
    * @param termsAndConditionURl the termsAndConditionURl to set
    */
   public void setTermsAndConditionURl(final String termsAndConditionURl)
   {
      this.termsAndConditionURl = termsAndConditionURl;
   }

   /**
    * @return the relativeTAndCUrl
    */
   public String getRelativeTAndCUrl()
   {
      return relativeTAndCUrl;
   }

   /**
    * @param relativeTAndCUrl the relativeTAndCUrl to set
    */
   public void setRelativeTAndCUrl(final String relativeTAndCUrl)
   {
      this.relativeTAndCUrl = relativeTAndCUrl;
   }

   /**
    * @return the termsAndConditionsLinks
    */
   public Map<String, String> getTermsAndConditionsLinks()
   {
      return termsAndConditionsLinks;
   }

   /**
    * @param termsAndConditionsLinks the termsAndConditionsLinks to set
    */
   public void setTermsAndConditionsLinks(final Map<String, String> termsAndConditionsLinks)
   {
      this.termsAndConditionsLinks = termsAndConditionsLinks;
   }

   /**
    * @return the bookingDetails
    */
   public BookingDetailsViewData getBookingDetails()
   {
      return bookingDetails;
   }

   /**
    * @return the errataViewData
    */
   public List<ErrataViewData> getErrataViewData()
   {
      return errataViewData;
   }

   /**
    * @param bookingDetails the bookingDetails to set
    */
   public void setBookingDetails(final BookingDetailsViewData bookingDetails)
   {
      this.bookingDetails = bookingDetails;
   }

   /**
    * @param errataViewData the errataViewData to set
    */
   public void setErrataViewData(final List<ErrataViewData> errataViewData)
   {
      this.errataViewData = errataViewData;
   }

   /**
    * @return the flightErrataViewData
    */
   public List<ErrataViewData> getFlightErrataViewData()
   {
      return flightErrataViewData;
   }

   /**
    * @param flightErrataViewData the flightErrataViewData to set
    */
   public void setFlightErrataViewData(final List<ErrataViewData> flightErrataViewData)
   {
      this.flightErrataViewData = flightErrataViewData;
   }

   /**
    * @return the deposits
    */
   public List<DepositViewData> getDeposits()
   {
      return deposits;
   }

   /**
    * @param deposits the deposits to set
    */
   public void setDeposits(final List<DepositViewData> deposits)
   {
      this.deposits = deposits;
   }

   /**
    * @return the summaryRoomViewData
    */
   public List<SummaryRoomViewData> getSummaryRoomViewData()
   {
      return summaryRoomViewData;
   }

   /**
    * @param summaryRoomViewData the summaryRoomViewData to set
    */
   public void setSummaryRoomViewData(final List<SummaryRoomViewData> summaryRoomViewData)
   {
      this.summaryRoomViewData = summaryRoomViewData;
   }

   /**
    * @return the totalExtraCost
    */
   public BigDecimal getTotalExtraCost()
   {
      return totalExtraCost;
   }

   /**
    * @param totalExtraCost the totalExtraCost to set
    */
   public void setTotalExtraCost(final BigDecimal totalExtraCost)
   {
      this.totalExtraCost = totalExtraCost;
   }

   /**
    * @return the showLeadPaxAddress
    */
   public boolean isShowLeadPaxAddress()
   {
      return showLeadPaxAddress;
   }

   /**
    * @param showLeadPaxAddress the showLeadPaxAddress to set
    */
   public void setShowLeadPaxAddress(final boolean showLeadPaxAddress)
   {
      this.showLeadPaxAddress = showLeadPaxAddress;
   }

   /**
    * @return the inventoryType
    */
   public String getInventoryType()
   {
      return inventoryType;
   }

   /**
    * @param inventoryType the inventoryType to set
    */
   public void setInventoryType(final String inventoryType)
   {
      this.inventoryType = inventoryType;
   }

}
