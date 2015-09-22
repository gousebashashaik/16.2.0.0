/**
 *
 */
package uk.co.tui.shortlist.data;

/**
 * @author akhileshvarma.d
 *
 */
public class ShortlistRoomData
{

   /** The room code. */
   private String roomCode;

   /** The room type. */
   private String roomType;

   /** The no of rooms. */
   private int noOfRooms;

   /** The occupancy. */
   private ShortlistOccupancy occupancy = new ShortlistOccupancy();

   /** The board basis code. */
   private String boardBasisCode;

   /** The price. */
   private String price;

   /** The price per person. */
   private String pricePerPerson;

   /** The discount. */
   private String discount;

   /** The discount per person. */
   private String discountPerPerson;

   /** The chargeable pax count. */
   private String chargeablePaxCount;

   /** The offer. */
   private ShortlistOffersData offer = new ShortlistOffersData();

   /**
    * @return the roomCode
    */
   public String getRoomCode()
   {
      return roomCode;
   }

   /**
    * @param roomCode the roomCode to set
    */
   public void setRoomCode(final String roomCode)
   {
      this.roomCode = roomCode;
   }

   /**
    * @return the roomType
    */
   public String getRoomType()
   {
      return roomType;
   }

   /**
    * @param roomType the roomType to set
    */
   public void setRoomType(final String roomType)
   {
      this.roomType = roomType;
   }

   /**
    * @return the noOfRooms
    */
   public int getNoOfRooms()
   {
      return noOfRooms;
   }

   /**
    * @param noOfRooms the noOfRooms to set
    */
   public void setNoOfRooms(final int noOfRooms)
   {
      this.noOfRooms = noOfRooms;
   }

   /**
    * @return the occupancy
    */
   public ShortlistOccupancy getOccupancy()
   {
      return occupancy;
   }

   /**
    * @param occupancy the occupancy to set
    */
   public void setOccupancy(final ShortlistOccupancy occupancy)
   {
      this.occupancy = occupancy;
   }

   /**
    * @return the boardBasisCode
    */
   public String getBoardBasisCode()
   {
      return boardBasisCode;
   }

   /**
    * @param boardBasisCode the boardBasisCode to set
    */
   public void setBoardBasisCode(final String boardBasisCode)
   {
      this.boardBasisCode = boardBasisCode;
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
    * @return the pricePerPerson
    */
   public String getPricePerPerson()
   {
      return pricePerPerson;
   }

   /**
    * @param pricePerPerson the pricePerPerson to set
    */
   public void setPricePerPerson(final String pricePerPerson)
   {
      this.pricePerPerson = pricePerPerson;
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
    * @return the discountPerPerson
    */
   public String getDiscountPerPerson()
   {
      return discountPerPerson;
   }

   /**
    * @param discountPerPerson the discountPerPerson to set
    */
   public void setDiscountPerPerson(final String discountPerPerson)
   {
      this.discountPerPerson = discountPerPerson;
   }

   /**
    * @return the chargeablePaxCount
    */
   public String getChargeablePaxCount()
   {
      return chargeablePaxCount;
   }

   /**
    * @param chargeablePaxCount the chargeablePaxCount to set
    */
   public void setChargeablePaxCount(final String chargeablePaxCount)
   {
      this.chargeablePaxCount = chargeablePaxCount;
   }

   /**
    * @return the offer
    */
   public ShortlistOffersData getOffer()
   {
      return offer;
   }

   /**
    * @param offer the offer to set
    */
   public void setOffer(final ShortlistOffersData offer)
   {
      this.offer = offer;
   }

}
