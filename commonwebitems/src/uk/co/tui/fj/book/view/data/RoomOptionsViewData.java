/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.List;

/**
 * The Class RoomOptionsViewData.
 *
 * @author samantha.gd
 */
public class RoomOptionsViewData
{

   /** The room index. */
   private int roomIndex;

   /** The list of room view data. */
   private List<RoomViewData> listOfRoomViewData;

   /** The occupancy. */
   private OccupancyViewData occupancy;

   /**
    * Gets the room index.
    *
    * @return the room index
    */
   public int getRoomIndex()
   {
      return roomIndex;
   }

   /**
    * Sets the room index.
    *
    * @param roomIndex the new room index
    */
   public void setRoomIndex(int roomIndex)
   {
      this.roomIndex = roomIndex;
   }

   /**
    * Gets the list of room view data.
    *
    * @return the list of room view data
    */
   public List<RoomViewData> getListOfRoomViewData()
   {
      return listOfRoomViewData;
   }

   /**
    * Sets the list of room view data.
    *
    * @param listOfRoomViewData the new list of room view data
    */
   public void setListOfRoomViewData(List<RoomViewData> listOfRoomViewData)
   {
      this.listOfRoomViewData = listOfRoomViewData;
   }

   /**
    * Gets the occupancy.
    *
    * @return the occupancy
    */
   public OccupancyViewData getOccupancy()
   {
      return occupancy;
   }

   /**
    * Sets the occupancy.
    *
    * @param occupancy the new occupancy
    */
   public void setOccupancy(OccupancyViewData occupancy)
   {
      this.occupancy = occupancy;
   }
}
