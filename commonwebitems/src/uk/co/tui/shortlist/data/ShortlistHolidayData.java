/**
 *
 */
package uk.co.tui.shortlist.data;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;

/**
 * @author akhileshvarma.d
 *
 */
public class ShortlistHolidayData
{

   private ShortlistItineryData itinerary = new ShortlistItineryData();

   private ShortlistAccomData accomodation = new ShortlistAccomData();

   private int duration;

   private String packageId;

   private String wishListId = StringUtils.EMPTY;

   private String price;

   private String perPersonPrice;

   private String totalPrice;

   private String tracs;

   private BigDecimal totalDiscount = BigDecimal.ZERO;

   private String perPersonDiscount;

   private InventoryInfo inventoryInfo = new InventoryInfo();

   private BigDecimal depositAmount;

   // this is only for supporting iscape shortlist and can be removed later once iscape is not
   // supported
   private List<BoardBasisDataResponse> alternateBoard;

   private boolean worldCare;

   private String bookUrl;

   private BigDecimal ppDiscount = BigDecimal.ZERO;

   private String promotionalOffer;

   private BigDecimal ppPrice = BigDecimal.ZERO;

   private BigDecimal tpp = BigDecimal.ZERO;

   private BigDecimal childPrice = BigDecimal.ZERO;

   /**
    * @return the ppDiscount
    */
   public BigDecimal getPpDiscount()
   {
      return ppDiscount;
   }

   /**
    * @param ppDiscount the ppDiscount to set
    */
   public void setPpDiscount(final BigDecimal ppDiscount)
   {
      this.ppDiscount = ppDiscount;
   }

   /**
    * @return the promotionalOffer
    */
   public String getPromotionalOffer()
   {
      return promotionalOffer;
   }

   /**
    * @param promotionalOffer the promotionalOffer to set
    */
   public void setPromotionalOffer(final String promotionalOffer)
   {
      this.promotionalOffer = promotionalOffer;
   }

   /**
    * @return the ppPrice
    */
   public BigDecimal getPpPrice()
   {
      return ppPrice;
   }

   /**
    * @param ppPrice the ppPrice to set
    */
   public void setPpPrice(final BigDecimal ppPrice)
   {
      this.ppPrice = ppPrice;
   }

   /**
    * @return the tpp
    */
   public BigDecimal getTpp()
   {
      return tpp;
   }

   /**
    * @param tpp the tpp to set
    */
   public void setTpp(final BigDecimal tpp)
   {
      this.tpp = tpp;
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
    * @return the bookUrl
    */
   public String getBookUrl()
   {
      return bookUrl;
   }

   /**
    * @param bookUrl the bookUrl to set
    */
   public void setBookUrl(final String bookUrl)
   {
      this.bookUrl = bookUrl;
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
    * @return the inventoryInfo
    */
   public InventoryInfo getInventoryInfo()
   {
      return inventoryInfo;
   }

   /**
    * @param inventoryInfo the inventoryInfo to set
    */
   public void setInventoryInfo(final InventoryInfo inventoryInfo)
   {
      this.inventoryInfo = inventoryInfo;
   }

   /**
    * @return the depositAmount
    */
   public BigDecimal getDepositAmount()
   {
      return depositAmount;
   }

   /**
    * @param depositAmount the depositAmount to set
    */
   public void setDepositAmount(final BigDecimal depositAmount)
   {
      this.depositAmount = depositAmount;
   }

   /**
    * @return the alternateBoard
    */
   public List<BoardBasisDataResponse> getAlternateBoard()
   {
      return alternateBoard;
   }

   /**
    * @param alternateBoard the alternateBoard to set
    */
   public void setAlternateBoard(final List<BoardBasisDataResponse> alternateBoard)
   {
      this.alternateBoard = alternateBoard;
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
   public void setPrice(final String price)
   {
      this.price = price;
   }

   /**
    * @return the perPersonPrice
    */
   public String getPerPersonPrice()
   {
      return perPersonPrice;
   }

   /**
    * @param perPersonPrice the perPersonPrice to set
    */
   public void setPerPersonPrice(final String perPersonPrice)
   {
      this.perPersonPrice = perPersonPrice;
   }

   /**
    * @return the totalPrice
    */
   public String getTotalPrice()
   {
      return totalPrice;
   }

   /**
    * @param totalPrice the totalPrice to set
    */
   public void setTotalPrice(final String totalPrice)
   {
      this.totalPrice = totalPrice;
   }

   /**
    * @return the tracs
    */
   public String getTracs()
   {
      return tracs;
   }

   /**
    * @param tracs the tracs to set
    */
   public void setTracs(final String tracs)
   {
      this.tracs = tracs;
   }

   /**
    * @return the totalDiscount
    */
   public BigDecimal getTotalDiscount()
   {
      return totalDiscount;
   }

   /**
    * @param totalDiscount the totalDiscount to set
    */
   public void setTotalDiscount(final BigDecimal totalDiscount)
   {
      this.totalDiscount = totalDiscount;
   }

   /**
    * @return the perPersonDiscount
    */
   public String getPerPersonDiscount()
   {
      return perPersonDiscount;
   }

   /**
    * @param perPersonDiscount the perPersonDiscount to set
    */
   public void setPerPersonDiscount(final String perPersonDiscount)
   {
      this.perPersonDiscount = perPersonDiscount;
   }

   /**
    * @return the itinerary
    */
   public ShortlistItineryData getItinerary()
   {
      return itinerary;
   }

   /**
    * @param itinerary the itinerary to set
    */
   public void setItinerary(final ShortlistItineryData itinerary)
   {
      this.itinerary = itinerary;
   }

   /**
    * @return the accomodation
    */
   public ShortlistAccomData getAccomodation()
   {
      return accomodation;
   }

   /**
    * @param accomodation the accomodation to set
    */
   public void setAccomodation(final ShortlistAccomData accomodation)
   {
      this.accomodation = accomodation;
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

}
