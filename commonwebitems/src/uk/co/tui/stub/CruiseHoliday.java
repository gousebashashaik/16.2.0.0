/**
 *
 */
package uk.co.tui.stub;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import uk.co.portaltech.travel.model.results.Accomodation;
import uk.co.portaltech.travel.model.results.Flight;
import uk.co.portaltech.travel.model.results.Itinerary;
import uk.co.portaltech.travel.model.results.Offers;
import uk.co.portaltech.travel.model.results.TracsDetails;
import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;

/**
 *
 */
public class CruiseHoliday
{

   private int index;

   private Accomodation accomodation = new Accomodation();

   private Itinerary cruiseitinerary;

   private Itinerary flightitinerary;

   private Offers offer = new Offers();

   private int duration;

   private String tripAdvisorRating;

   private String officialRating;

   private String packageId;

   private String price;

   private String perPersonPrice;

   private String totalPrice;

   private BigDecimal ppPrice = BigDecimal.ZERO;

   private BigDecimal tpp = BigDecimal.ZERO;

   private String discount;

   private BigDecimal ppDiscount = BigDecimal.ZERO;

   private BigDecimal totalDiscount = BigDecimal.ZERO;

   private String perPersonDiscount;

   private BigDecimal depositAmount;

   private String promotionalOffer;

   private String tracs;

   private Flight inbound;

   private List<BoardBasisDataResponse> boardBasisData;

   private Flight outbound;

   private boolean coachTransfer;

   private boolean worldCare;

   private BigDecimal childPrice = BigDecimal.ZERO;

   private Inventory inventory;

   private TracsDetails tracsDetails;

   private List<String> portOfCalls;

   private List<Integer> childages;

   private String cruiseType;

   private DateTime startDate;

   private DateTime endDate;

   /**
    * @return the cruiseitinerary
    */
   public Itinerary getCruiseitinerary()
   {
      return cruiseitinerary;
   }

   /**
    * @param cruiseitinerary the cruiseitinerary to set
    */
   public void setCruiseitinerary(final Itinerary cruiseitinerary)
   {
      this.cruiseitinerary = cruiseitinerary;
   }

   /**
    * @return the flightitinerary
    */
   public Itinerary getFlightitinerary()
   {
      return flightitinerary;
   }

   /**
    * @param flightitinerary the flightitinerary to set
    */
   public void setFlightitinerary(final Itinerary flightitinerary)
   {
      this.flightitinerary = flightitinerary;
   }

   /**
    * @return the childages
    */
   public List<Integer> getChildages()
   {
      return childages;
   }

   /**
    * @param childages the childages to set
    */
   public void setChildages(final List<Integer> childages)
   {
      this.childages = childages;
   }

   /**
    * @return the tracsDetails
    */
   public TracsDetails getTracsDetails()
   {
      return tracsDetails;
   }

   /**
    * @param tracsDetails the tracsDetails to set
    */
   public void setTracsDetails(final TracsDetails tracsDetails)
   {
      this.tracsDetails = tracsDetails;
   }

   /**
    * @return the boardBasisData
    */
   public List<BoardBasisDataResponse> getBoardBasisData()
   {
      return boardBasisData;
   }

   /**
    * @param boardBasisData the boardBasisData to set
    */
   public void setBoardBasisData(final List<BoardBasisDataResponse> boardBasisData)
   {
      this.boardBasisData = boardBasisData;
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
    * @return the depositAmount
    */
   public BigDecimal getDepositAmount()
   {
      return depositAmount;
   }

   /**
    * @return the offer
    */
   public Offers getOffer()
   {
      return offer;
   }

   /**
    * @param offer the offer to set
    */
   public void setOffer(final Offers offer)
   {
      this.offer = offer;
   }

   /**
    * @param depositAmount the depositAmount to set
    */
   public void setDepositAmount(final BigDecimal depositAmount)
   {
      this.depositAmount = depositAmount;
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
    * @return the accomodation
    */
   public Accomodation getAccomodation()
   {
      return accomodation;
   }

   /**
    * @param accomodation the accomodation to set
    */
   public void setAccomodation(final Accomodation accomodation)
   {
      this.accomodation = accomodation;
   }

   /**
    * @return the tripAdvisorRating
    */
   public String getTripAdvisorRating()
   {
      return tripAdvisorRating;
   }

   /**
    * @param tripAdvisorRating the tripAdvisorRating to set
    */
   public void setTripAdvisorRating(final String tripAdvisorRating)
   {
      this.tripAdvisorRating = tripAdvisorRating;
   }

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
    * @return the discount
    */
   public String getDiscount()
   {
      return discount;
   }

   /**
    * @param discount the discount to set
    */
   public void setDiscount(final String discount)
   {
      this.discount = discount;
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
    * @return the inbound
    */
   public Flight getInbound()
   {
      return inbound;
   }

   /**
    * @param inbound the inbound to set
    */
   public void setInbound(final Flight inbound)
   {
      this.inbound = inbound;
   }

   /**
    * @return the outbound
    */
   public Flight getOutbound()
   {
      return outbound;
   }

   /**
    * @param outbound the outbound to set
    */
   public void setOutbound(final Flight outbound)
   {
      this.outbound = outbound;
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
    *
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
    * @return the tpp
    */
   public BigDecimal getTpp()
   {
      return tpp;
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
    * @param tpp the tpp to set
    */
   public void setTpp(final BigDecimal tpp)
   {
      this.tpp = tpp;
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

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      final StringBuilder uniquePackageId = new StringBuilder();

      return uniquePackageId.toString();
   }

   /**
    * @return the inventory
    */
   public Inventory getInventory()
   {
      return inventory;
   }

   /**
    * @param inventory the inventory to set
    */
   public void setInventory(final Inventory inventory)
   {
      this.inventory = inventory;
   }

   /**
    * @return the portOfCalls
    */
   public List<String> getPortOfCalls()
   {
      return portOfCalls;
   }

   /**
    * @param portOfCalls the portOfCalls to set
    */
   public void setPortOfCalls(final List<String> portOfCalls)
   {
      this.portOfCalls = portOfCalls;
   }

   /**
    * @return the cruiseType
    */
   public String getCruiseType()
   {
      return cruiseType;
   }

   /**
    * @param cruiseType the cruiseType to set
    */
   public void setCruiseType(final String cruiseType)
   {
      this.cruiseType = cruiseType;
   }

   /**
    * @return the startDate
    */
   public DateTime getStartDate()
   {
      return startDate;
   }

   /**
    * @param startDate the startDate to set
    */
   public void setStartDate(final DateTime startDate)
   {
      this.startDate = startDate;
   }

   /**
    * @return the endDate
    */
   public DateTime getEndDate()
   {
      return endDate;
   }

   /**
    * @param endDate the endDate to set
    */
   public void setEndDate(final DateTime endDate)
   {
      this.endDate = endDate;
   }

}
