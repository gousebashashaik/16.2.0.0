/**
 *
 */
package uk.co.tui.shortlist.view.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.tui.book.view.data.ExtraFacilityCategoryViewData;

/**
 * This class holds SavedHoliday related view data.
 *
 * @author sravani.g
 */
public class SavedHolidayViewData
{

   /**
    * Holds the TripAdvisor relavant View Data.
    */

   private TripadvisorViewData tripadvisorData;

   /**
    * Holds the party Relevant View Data.
    */
   private PartyCompositionViewData paxViewData;

   /**
    * Holds the Accommodation Relevant View Data.
    */
   private SavedHolidayAccommViewData accomViewData;

   /**
    * Holds the Flight Relevant View Data.
    */
   private SavedHolidayFlightViewData flightViewData;

   /**
    * Holds the PriceBreakDown Relevant View Data.
    */
   private List<SavedHolidayPriceViewData> priceBreakdownViewData;

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

   /** The selected. */
   private boolean selected;

   /**
    * Holds Media View Data.
    */
   private String galleryImages;

   /**
    * Holds the total Extras count.
    */
   private int extrasCount;

   /**
    * Holds the Price Difference of savedPackage and UpdatedPackage .
    */
   private BigDecimal priceDiff;

   private String freeChildPlace;

   private String defaultExtrasDesc;

   private String resFlag;

   private String departureMonth;

   /** packageId. */
   private String packageId;

   private boolean isAvailable;

   private List<MediaViewData> galleryVideos;

   private String videoPresent;

   /**
    * Holds the Decreased Price of Holiday.
    */
   private boolean priceDecreased;

   /**
    * Holds the DatePasssedWishList.
    */
   private String datePassedWishlistEntryId;

   /**
    * Holds the wishlistEntryId of SavedHoliday.
    */
   private String wishlistEntryId;

   /**
    * Holds wishlistId of SavedHoliday.
    */
   private String wishlistId;

   /**
    * Holds the selected extrafacility list.
    */
   private List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewData;

   /**
    * Represents number of seniors in the party.
    */
   private List<SavedHolidayPassengerViewData> passenger =
      new ArrayList<SavedHolidayPassengerViewData>();

   /** The package id. */
   private String id = StringUtils.EMPTY;

   /**
    * Holds Room Availablity based on Rules.
    */
   private boolean isLowAvailabilityIndicator;

   /**
    * Holds Available Rooms.
    */
   private int availableRooms;

   /**
    * Holds Brand information.
    */
   private String brandType;

   /**
    * Holds cpsScore.
    */
   private int index;

   private String createdTime;

   private String descreption;

   private String increasedOrDecreased;

   private String changedPrice;

   private BigDecimal deposit;

   private String notes;

   private int truncate;

   private BigDecimal depositPP;

   private String coachTransfer;

   private String freeCarHire;

   private Map<String, String> locationMap;

   private String soldoutWishlistEntryId;

   private String ancillarySoldOut;

   /** isAncilarrySoldOut. */
   private boolean isAncilarrySoldOut;

   private int earlySalesCommercialPriority;

   private List ancillariesBreakup;

   private BigDecimal standardDepositPP;

   /**
    * @return the packageId
    */
   public String getPackageId()
   {
      return packageId;
   }

   /**
    * @param packageId the packageId to set
    */
   public void setPackageId(final String packageId)
   {
      this.packageId = packageId;
   }

   /**
    * @return the departureMonth
    */
   public String getDepartureMonth()
   {
      return departureMonth;
   }

   /**
    * @param departureMonth the departureMonth to set
    */
   public void setDepartureMonth(final String departureMonth)
   {
      this.departureMonth = departureMonth;
   }

   /**
    * @return the resFlag
    */
   public String getResFlag()
   {
      return resFlag;
   }

   /**
    * @param resFlag the resFlag to set
    */
   public void setResFlag(final String resFlag)
   {
      this.resFlag = resFlag;
   }

   /**
    * @return the locationMap
    */
   public Map<String, String> getLocationMap()
   {
      return locationMap;
   }

   /**
    * @param locationMap the locationMap to set
    */
   public void setLocationMap(final Map<String, String> locationMap)
   {
      this.locationMap = locationMap;
   }

   /**
    * @return the defaultExtrasDesc
    */
   public String getDefaultExtrasDesc()
   {
      return defaultExtrasDesc;
   }

   /**
    * @param defaultExtrasDesc the defaultExtrasDesc to set
    */
   public void setDefaultExtrasDesc(final String defaultExtrasDesc)
   {
      this.defaultExtrasDesc = defaultExtrasDesc;
   }

   /**
    * @return the freeChildPlace
    */
   public String getFreeChildPlace()
   {
      return freeChildPlace;
   }

   /**
    * @param freeChildPlace the freeChildPlace to set
    */
   public void setFreeChildPlace(final String freeChildPlace)
   {
      this.freeChildPlace = freeChildPlace;
   }

   /**
    * @return the createdTime
    */
   public String getCreatedTime()
   {
      return createdTime;
   }

   /**
    * @param createdTime the createdTime to set
    */
   public void setCreatedTime(final String createdTime)
   {
      this.createdTime = createdTime;
   }

   /**
    * Holds SalesCommercialPriority.
    */

   /**
    * @return the earlySalesCommercialPriority
    */
   public int getEarlySalesCommercialPriority()
   {
      return earlySalesCommercialPriority;
   }

   /**
    * @param earlySalesCommercialPriority the earlySalesCommercialPriority to set
    */
   public void setEarlySalesCommercialPriority(final int earlySalesCommercialPriority)
   {
      this.earlySalesCommercialPriority = earlySalesCommercialPriority;
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
    * @return the galleryImages
    */
   public String getGalleryImages()
   {
      return galleryImages;
   }

   /**
    * @param galleryImages the galleryImages to set
    */
   public void setGalleryImages(final String galleryImages)
   {
      this.galleryImages = galleryImages;
   }

   /**
    * @return the wishlistEntryId
    */
   public String getWishlistEntryId()
   {
      return wishlistEntryId;
   }

   /**
    * @param wishlistEntryId the wishlistEntryId to set
    */
   public void setWishlistEntryId(final String wishlistEntryId)
   {
      this.wishlistEntryId = wishlistEntryId;
   }

   /**
    * @return the wishlistId
    */
   public String getWishlistId()
   {
      return wishlistId;
   }

   /**
    * @param wishlistId the wishlistId to set
    */
   public void setWishlistId(final String wishlistId)
   {
      this.wishlistId = wishlistId;
   }

   /**
    * @return the extrasCount
    */
   public int getExtrasCount()
   {
      return extrasCount;
   }

   /**
    * @param extrasCount the extrasCount to set
    */
   public void setExtrasCount(final int extrasCount)
   {
      this.extrasCount = extrasCount;
   }

   /**
    * @return the priceDiff
    */
   public BigDecimal getPriceDiff()
   {
      return priceDiff;
   }

   /**
    * @param priceDiff the priceDiff to set
    */
   public void setPriceDiff(final BigDecimal priceDiff)
   {
      this.priceDiff = priceDiff;
   }

   /**
    * Gets the pax view data.
    *
    * @return the paxViewData
    */
   public PartyCompositionViewData getPaxViewData()
   {
      if (this.paxViewData == null)
      {
         this.paxViewData = new PartyCompositionViewData();
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
   public void setPaxViewData(final PartyCompositionViewData paxViewData)
   {
      this.paxViewData = paxViewData;
   }

   /**
    * Gets the accom view data.
    *
    * @return the accomViewData
    */
   public SavedHolidayAccommViewData getAccomViewData()
   {
      if (this.accomViewData == null)
      {
         this.accomViewData = new SavedHolidayAccommViewData();
      }
      return this.accomViewData;
   }

   /**
    * Sets the accom view data.
    *
    * @param accomViewData the accomViewData to set
    */
   public void setAccomViewData(final SavedHolidayAccommViewData accomViewData)
   {
      this.accomViewData = accomViewData;
   }

   /**
    * Gets the flight view data.
    *
    * @return the flightViewData
    */
   public SavedHolidayFlightViewData getFlightViewData()
   {
      if (this.flightViewData == null)
      {
         this.flightViewData = new SavedHolidayFlightViewData();
      }
      return this.flightViewData;
   }

   /**
    * Sets the flight view data.
    *
    * @param flightViewData the flightViewData to set
    */
   public void setFlightViewData(final SavedHolidayFlightViewData flightViewData)
   {
      this.flightViewData = flightViewData;
   }

   /**
    * Gets the price breakdown view data.
    *
    * @return the priceBreakdownViewData
    */
   public List<SavedHolidayPriceViewData> getPriceBreakdownViewData()
   {
      if (CollectionUtils.isEmpty(this.priceBreakdownViewData))
      {
         this.priceBreakdownViewData = new ArrayList<SavedHolidayPriceViewData>();
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
   public List<SavedHolidayPassengerViewData> getPassenger()
   {
      return passenger;
   }

   /**
    * Sets the passenger.
    *
    * @param list the passenger to set
    */
   public void setPassenger(final List<SavedHolidayPassengerViewData> list)
   {
      this.passenger = list;
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
    * @return the priceDecreased
    */
   public boolean isPriceDecreased()
   {
      return priceDecreased;
   }

   /**
    * @param priceDecreased the priceDecreased to set
    */
   public void setPriceDecreased(final boolean priceDecreased)
   {
      this.priceDecreased = priceDecreased;
   }

   /**
    * @return the similarAncillariesBreakup
    */
   public List getAncillariesBreakup()
   {
      return ancillariesBreakup;
   }

   /**
    * @param similarAncillariesBreakup the similarAncillariesBreakup to set
    */
   public void setAncillariesBreakup(final List similarAncillariesBreakup)
   {
      this.ancillariesBreakup = similarAncillariesBreakup;
   }

   /**
    * @return the isLowAvailabilityIndicator
    */
   public boolean isLowAvailabilityIndicator()
   {
      return isLowAvailabilityIndicator;
   }

   /**
    * @param isLowAvailabilityIndicator the isLowAvailabilityIndicator to set
    */
   public void setLowAvailabilityIndicator(final boolean isLowAvailabilityIndicator)
   {
      this.isLowAvailabilityIndicator = isLowAvailabilityIndicator;
   }

   /**
    * @return the availableRooms
    */
   public int getAvailableRooms()
   {
      return availableRooms;
   }

   /**
    * @param availableRooms the availableRooms to set
    */
   public void setAvailableRooms(final int availableRooms)
   {
      this.availableRooms = availableRooms;
   }

   /**
    * @return the soldoutWishlistEntryId
    */
   public String getSoldoutWishlistEntryId()
   {
      return soldoutWishlistEntryId;
   }

   /**
    * @param soldoutWishlistEntryId the soldoutWishlistEntryId to set
    */
   public void setSoldoutWishlistEntryId(final String soldoutWishlistEntryId)
   {
      this.soldoutWishlistEntryId = soldoutWishlistEntryId;
   }

   /**
    * @return the datePassedWishlistEntryId
    */
   public String getDatePassedWishlistEntryId()
   {
      return datePassedWishlistEntryId;
   }

   /**
    * @param datePassedWishlistEntryId the datePassedWishlistEntryId to set
    */
   public void setDatePassedWishlistEntryId(final String datePassedWishlistEntryId)
   {
      this.datePassedWishlistEntryId = datePassedWishlistEntryId;
   }

   /**
    * @return the tripadvisorData
    */
   public TripadvisorViewData getTripadvisorData()
   {
      return tripadvisorData;
   }

   /**
    * @param tripadvisorData the tripadvisorData to set
    */
   public void setTripadvisorData(final TripadvisorViewData tripadvisorData)
   {
      this.tripadvisorData = tripadvisorData;
   }

   /**
    * @param priceBreakdownViewData the priceBreakdownViewData to set
    */
   public void setPriceBreakdownViewData(
      final List<SavedHolidayPriceViewData> priceBreakdownViewData)
   {
      this.priceBreakdownViewData = priceBreakdownViewData;
   }

   /**
    * @return the deposit
    */
   public BigDecimal getDeposit()
   {
      return deposit;
   }

   /**
    * @param deposit the deposit to set
    */
   public void setDeposit(final BigDecimal deposit)
   {
      this.deposit = deposit;
   }

   /**
    * @return the index
    */
   public int getIndex()
   {
      return index;
   }

   /**
    * @param index the index to set
    */
   public void setIndex(final int index)
   {
      this.index = index;
   }

   /**
    * @return the truncate
    */
   public int getTruncate()
   {
      return truncate;
   }

   /**
    * @param truncate the truncate to set
    */
   public void setTruncate(final int truncate)
   {
      this.truncate = truncate;
   }

   /**
    * @return the notes
    */
   public String getNotes()
   {
      return notes;
   }

   /**
    * @param notes the notes to set
    */
   public void setNotes(final String notes)
   {
      this.notes = notes;
   }

   /**
    * @return the descreption
    */
   public String getDescreption()
   {
      return descreption;
   }

   /**
    * @param descreption the descreption to set
    */
   public void setDescreption(final String descreption)
   {
      this.descreption = descreption;
   }

   /**
    * @return the increasedOrDecreased
    */
   public String getIncreasedOrDecreased()
   {
      return increasedOrDecreased;
   }

   /**
    * @param increasedOrDecreased the increasedOrDecreased to set
    */
   public void setIncreasedOrDecreased(final String increasedOrDecreased)
   {
      this.increasedOrDecreased = increasedOrDecreased;
   }

   /**
    * @return the changedPrice
    */
   public String getChangedPrice()
   {
      return changedPrice;
   }

   /**
    * @param changedPrice the changedPrice to set
    */
   public void setChangedPrice(final String changedPrice)
   {
      this.changedPrice = changedPrice;
   }

   /**
    * @return the freeCarHire
    */
   public String getFreeCarHire()
   {
      return freeCarHire;
   }

   /**
    * @param freeCarHire the freeCarHire to set
    */
   public void setFreeCarHire(final String freeCarHire)
   {
      this.freeCarHire = freeCarHire;
   }

   /**
    * @return the coachTransfer
    */
   public String getCoachTransfer()
   {
      return coachTransfer;
   }

   /**
    * @param coachTransfer the coachTransfer to set
    */
   public void setCoachTransfer(final String coachTransfer)
   {
      this.coachTransfer = coachTransfer;
   }

   /**
    * @return the ancillarySoldOut
    */
   public String getAncillarySoldOut()
   {
      return ancillarySoldOut;
   }

   /**
    * @param ancillarySoldOut the ancillarySoldOut to set
    */
   public void setAncillarySoldOut(final String ancillarySoldOut)
   {
      this.ancillarySoldOut = ancillarySoldOut;
   }

   /**
    * isAncilarrySoldOut.
    *
    * @return the isAncilarrySoldOut
    */
   public boolean isAncilarrySoldOut()
   {
      return isAncilarrySoldOut;
   }

   /**
    * setAncilarrySoldOut.
    *
    * @param isAncilarrySoldOut the isAncilarrySoldOut to set
    */
   public void setAncilarrySoldOut(final boolean isAncilarrySoldOut)
   {
      this.isAncilarrySoldOut = isAncilarrySoldOut;
   }

   /**
    * @return the depositPP
    */
   public BigDecimal getDepositPP()
   {
      return depositPP;
   }

   /**
    * @param depositPP the depositPP to set
    */
   public void setDepositPP(final BigDecimal depositPP)
   {
      this.depositPP = depositPP;
   }

   /**
    * @return the isAvailable
    */
   public boolean isAvailable()
   {
      return isAvailable;
   }

   /**
    * @param isAvailable the isAvailable to set
    */
   public void setAvailable(final boolean isAvailable)
   {
      this.isAvailable = isAvailable;
   }

   /**
    * @return the galleryVideos
    */
   public List<MediaViewData> getGalleryVideos()
   {
      return galleryVideos;
   }

   /**
    * @param galleryVideos the galleryVideos to set
    */
   public void setGalleryVideos(final List<MediaViewData> galleryVideos)
   {
      this.galleryVideos = galleryVideos;
   }

   /**
    * @return the videoPresent
    */
   public String getVideoPresent()
   {
      return videoPresent;
   }

   /**
    * @param videoPresent the videoPresent to set
    */
   public void setVideoPresent(final String videoPresent)
   {
      this.videoPresent = videoPresent;
   }

   /**
    * @return the standardDepositPP
    */
   public BigDecimal getStandardDepositPP()
   {
      return standardDepositPP;
   }

   /**
    * @param standardDepositPP the standardDepositPP to set
    */
   public void setStandardDepositPP(final BigDecimal standardDepositPP)
   {
      this.standardDepositPP = standardDepositPP;
   }

}
