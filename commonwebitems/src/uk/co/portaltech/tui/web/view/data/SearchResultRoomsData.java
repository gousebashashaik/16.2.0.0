/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

public class SearchResultRoomsData
{

   private String roomTypeGroup;

   private String roomType;

   private String roomCode;

   private int sellingout;

   private boolean availability;

   private String boardType;

   private String boardBasisCode;

   private boolean limitedAvailabilityThreshold;

   private SearchResultRoomsPriceData price = new SearchResultRoomsPriceData();
   private Occupancy occupancy = new Occupancy();

   /**
    * @return the roomType
    */
   public String getRoomType()
   {
      return roomType;
   }

   /**
    * @param roomType
    *           the roomType to set
    */
   public void setRoomType(final String roomType)
   {
      this.roomType = roomType;
   }

   /**
    * @return the sellingout
    */
   public int getSellingout()
   {
      return sellingout;
   }

   /**
    * @param sellingout
    *           the sellingout to set
    */
   public void setSellingout(final int sellingout)
   {
      this.sellingout = sellingout;
   }

   /**
    * @return the availability
    */
   public boolean isAvailability()
   {
      return availability;
   }

   /**
    * @param availability
    *           the availability to set
    */
   public void setAvailability(final boolean availability)
   {
      this.availability = availability;
   }

   /**
    * @return the price
    */
   public SearchResultRoomsPriceData getPrice()
   {
      return price;
   }

   /**
    * @param price
    *           the price to set
    */
   public void setPrice(final SearchResultRoomsPriceData price)
   {
      this.price = price;
   }

   /**
    * @return the roomCode
    */
   public String getRoomCode()
   {
      return roomCode;
   }

   /**
    * @param roomCode
    *           the roomCode to set
    */
   public void setRoomCode(final String roomCode)
   {
      this.roomCode = roomCode;
   }

   /**
    * @return the boardType
    */
   public String getBoardType()
   {
      return boardType;
   }

   /**
    * @param boardType
    *           the boardType to set
    */
   public void setBoardType(final String boardType)
   {
      this.boardType = boardType;
   }

   /**
    * @return the occupancy
    */
   public Occupancy getOccupancy()
   {
      return occupancy;
   }

   /**
    * @param occupancy
    *           the occupancy to set
    */
   public void setOccupancy(final Occupancy occupancy)
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
    * @param boardBasisCode
    *           the boardBasisCode to set
    */
   public void setBoardBasisCode(final String boardBasisCode)
   {
      this.boardBasisCode = boardBasisCode;
   }

   /**
    * @return the roomTypeGroup
    */
   public String getRoomTypeGroup()
   {
      return roomTypeGroup;
   }

   /**
    * @param roomTypeGroup
    *           the roomTypeGroup to set
    */
   public void setRoomTypeGroup(final String roomTypeGroup)
   {
      this.roomTypeGroup = roomTypeGroup;
   }

   /**
    * @return the limitedAvailabilityThreshold
    */
   public boolean isLimitedAvailabilityThreshold()
   {
      return limitedAvailabilityThreshold;
   }

   /**
    * @param limitedAvailabilityThreshold
    *           the limitedAvailabilityThreshold to set
    */
   public void setLimitedAvailabilityThreshold(final boolean limitedAvailabilityThreshold)
   {
      this.limitedAvailabilityThreshold = limitedAvailabilityThreshold;
   }



}
