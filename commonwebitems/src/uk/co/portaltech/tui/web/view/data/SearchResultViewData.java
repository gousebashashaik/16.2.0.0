/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SearchResultViewData
{

   private SearchResultAccomodationViewData accommodation = new SearchResultAccomodationViewData();

   private SearchResultPriceViewData price = new SearchResultPriceViewData();

   private SearchResultFlightViewData itinerary = new SearchResultFlightViewData();

   private List<BoardBasisType> alternateBoard;

   private String pageLabel;

   private String packageId;

   private String tracsUnitCode;

   private String sellingCode;

   private Offer offer;

   private int duration;

   private boolean coachTransfer;

   private boolean worldCare;

   private String tracsPackageId;

   private String wishListId = StringUtils.EMPTY;

   private Date availbleFrom;

   private boolean isInboundThirdParty;

   private boolean isOutboundThirdParty;

   private NonCoreProductIndicator nonCoreProduct;

   private String brandType;

   private String holidayType;

   // Should have the duration code to be used for mobile.
   private int modifiedDuration;

   private boolean greatDeal;

   private int greatDealPercentage;

   // Flag added for shortlisting
   private boolean shortlisted;

   private String productCode;

   private String subProductCode;

   private int index;

   public SearchResultViewData()
   {

   }

   /**
   *
   */
   public SearchResultViewData(final int index)
   {
      this.index = index;
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
    * @return the shortlisted
    */
   public boolean isShortlisted()
   {
      return shortlisted;
   }

   /**
    * @param shortlisted the shortlisted to set
    */
   public void setShortlisted(final boolean shortlisted)
   {
      this.shortlisted = shortlisted;
   }

   /**
    * @return the nonCoreProduct
    */
   public NonCoreProductIndicator getNonCoreProduct()
   {
      return nonCoreProduct;
   }

   /**
    * @param nonCoreProduct the nonCoreProduct to set
    */
   public void setNonCoreProduct(final NonCoreProductIndicator nonCoreProduct)
   {
      this.nonCoreProduct = nonCoreProduct;
   }

   /**
    * @return the pageLabel
    */
   public String getPageLabel()
   {
      return pageLabel;
   }

   /**
    * @param pageLabel the pageLabel to set
    */
   public void setPageLabel(final String pageLabel)
   {
      this.pageLabel = pageLabel;
   }

   /**
    * @return the alternateBoard
    */
   public List<BoardBasisType> getAlternateBoard()
   {
      return alternateBoard;
   }

   /**
    * @param alternateBoard the alternateBoard to set
    */
   public void setAlternateBoard(final List<BoardBasisType> alternateBoard)
   {
      this.alternateBoard = alternateBoard;
   }

   /**
    * @return the coachTransfer
    */
   public boolean isCoachTransfer()
   {
      return coachTransfer;
   }

   /**
    * @param coachTransfer the coachTransfer to set
    */
   public void setCoachTransfer(final boolean coachTransfer)
   {
      this.coachTransfer = coachTransfer;
   }

   /**
    * @return the accommodation
    */
   public SearchResultAccomodationViewData getAccommodation()
   {
      return accommodation;
   }

   /**
    * @return the offer
    */
   public Offer getOffer()
   {
      return offer;
   }

   /**
    * @param offer the offer to set
    */
   public void setOffer(final Offer offer)
   {
      this.offer = offer;
   }

   /**
    * @param accommodation the accommodation to set
    */
   public void setAccommodation(final SearchResultAccomodationViewData accommodation)
   {
      this.accommodation = accommodation;
   }

   /**
    * @return the price
    */
   public SearchResultPriceViewData getPrice()
   {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final SearchResultPriceViewData price)
   {
      this.price = price;
   }

   /**
    * @return the itinerary
    */
   public SearchResultFlightViewData getItinerary()
   {
      return itinerary;
   }

   /**
    * @param itinerary the itinerary to set
    */
   public void setItinerary(final SearchResultFlightViewData itinerary)
   {
      this.itinerary = itinerary;
   }

   /**
    * @return the tracsUnitCode
    */
   public String getTracsUnitCode()
   {
      return tracsUnitCode;
   }

   /**
    * @param tracsUnitCode the tracsUnitCode to set
    */
   public void setTracsUnitCode(final String tracsUnitCode)
   {
      this.tracsUnitCode = tracsUnitCode;
   }

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
    * @return the duration
    */
   public int getDuration()
   {
      return duration;
   }

   /**
    * @param duration the duration to set
    */
   public void setDuration(final int duration)
   {
      this.duration = duration;
   }

   /**
    * @return the productCode
    */
   public String getProductCode()
   {
      return productCode;
   }

   /**
    * @param productCode the productCode to set
    */
   public void setProductCode(final String productCode)
   {
      this.productCode = productCode;
   }

   /**
    * @return the subProductCode
    */
   public String getSubProductCode()
   {
      return subProductCode;
   }

   /**
    * @param subProductCode the subProductCode to set
    */
   public void setSubProductCode(final String subProductCode)
   {
      this.subProductCode = subProductCode;
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
    * @return the worldCare
    */
   public boolean isWorldCare()
   {
      return worldCare;
   }

   /**
    * @param worldCare the worldCare to set
    */
   public void setWorldCare(final boolean worldCare)
   {
      this.worldCare = worldCare;
   }

   /**
    * @return the sellingCode
    */
   public String getSellingCode()
   {
      return sellingCode;
   }

   /**
    * @param sellingCode the sellingCode to set
    */
   public void setSellingCode(final String sellingCode)
   {
      this.sellingCode = sellingCode;
   }

   /**
    * @return the wishListId
    */
   public String getWishListId()
   {
      return wishListId;
   }

   /**
    * @param wishListId the wishListId to set
    */
   public void setWishListId(final String wishListId)
   {
      this.wishListId = wishListId;
   }

   /**
    * @return the tracsPackageId
    */
   public String getTracsPackageId()
   {
      return tracsPackageId;
   }

   /**
    * @param tracsPackageId the tracsPackageId to set
    */
   public void setTracsPackageId(final String tracsPackageId)
   {
      this.tracsPackageId = tracsPackageId;
   }

   /**
    * @return the availbleFrom
    */
   public Date getAvailbleFrom()
   {
      if (this.availbleFrom != null)
      {
         return new Date(availbleFrom.getTime());
      }
      return null;
   }

   /**
    * @param availbleFrom the availbleFrom to set
    */
   public void setAvailbleFrom(final Date availbleFrom)
   {
      if (availbleFrom != null)
      {
         this.availbleFrom = new Date(availbleFrom.getTime());
      }
   }

   /**
    * @return the holidayType
    */
   public String getHolidayType()
   {
      return holidayType;
   }

   /**
    * @param holidayType the holidayType to set
    */
   public void setHolidayType(final String holidayType)
   {
      this.holidayType = holidayType;
   }

   /**
    * @return the modifiedDuration
    */
   public int getModifiedDuration()
   {
      return modifiedDuration;
   }

   /**
    * @param modifiedDuration the modifiedDuration to set
    */
   public void setModifiedDuration(final int modifiedDuration)
   {
      this.modifiedDuration = modifiedDuration;
   }

   /**
    * @return the greatDeal
    */
   public boolean isGreatDeal()
   {
      return greatDeal;
   }

   /**
    * @param greatDeal the greatDeal to set
    */
   public void setGreatDeal(final boolean greatDeal)
   {
      this.greatDeal = greatDeal;
   }

   /**
    * @return the greatDealPercentage
    */
   public int getGreatDealPercentage()
   {
      return greatDealPercentage;
   }

   /**
    * @param greatDealPercentage the greatDealPercentage to set
    */
   public void setGreatDealPercentage(final int greatDealPercentage)
   {
      this.greatDealPercentage = greatDealPercentage;
   }

   /**
    * @return the isInboundThirdParty
    */
   public boolean isInboundThirdParty()
   {
      return isInboundThirdParty;
   }

   /**
    * @param isInboundThirdParty the isInboundThirdParty to set
    */
   public void setInboundThirdParty(final boolean isInboundThirdParty)
   {
      this.isInboundThirdParty = isInboundThirdParty;
   }

   /**
    * @return the isOutboundThirdParty
    */
   public boolean isOutboundThirdParty()
   {
      return isOutboundThirdParty;
   }

   /**
    * @param isOutboundThirdParty the isOutboundThirdParty to set
    */
   public void setOutboundThirdParty(final boolean isOutboundThirdParty)
   {
      this.isOutboundThirdParty = isOutboundThirdParty;
   }

}
