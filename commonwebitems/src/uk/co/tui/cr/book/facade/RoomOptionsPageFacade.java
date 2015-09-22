/**
 *
 */
package uk.co.tui.cr.book.facade;

import uk.co.tui.book.criteria.RoomSelectionCriteria;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.cr.book.view.data.RoomAndBoardOptionsPageViewData;
import uk.co.tui.cr.exception.TUIBusinessException;

/**
 * The Interface RoomOptionsPageFacade.
 *
 * @author samantha.gd
 */
public interface RoomOptionsPageFacade
{
   /**
    * Gets the alternate rooms.
    *
    * @return the alternate rooms
    */
   RoomAndBoardOptionsPageViewData getAlternateRooms();
   
   /**
    * Update selected room.
    *
    * @param roomCriteria the room criteria
    * @return the room and board options page view data
    */
   RoomAndBoardOptionsPageViewData updateSelectedRoom(RoomSelectionCriteria roomCriteria);
   
   /**
    * Update selected room allocation.
    *
    * @param roomCriteria the room criteria
    * @return the room and board options page view data
    * @throws TUIBusinessException
    */
   RoomAndBoardOptionsPageViewData updateSelectedRoomAllocation(RoomSelectionCriteria roomCriteria)
      throws TUIBusinessException;
   
   /**
    * Update selected board basis.
    *
    * @param boardBasisCode the board basis code
    * @return the room and board options page view data
    */
   RoomAndBoardOptionsPageViewData updateSelectedBoardBasis(String boardBasisCode);
   
   /**
    * Populate room options static content view data.
    *
    * @param roomAndBoardOptionsPageViewData the room and board options page view data
    */
   void populateRoomOptionsStaticContentViewData(
      RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData);
   
   /**
    * @param roomAndBoardOptionsPageViewData
    */
   void populateRoomsForWebAnalytics(RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData);
   
   /**
    * @param packageModel
    * @param roomOptionsViewData
    */
   void populateRoomOptionsAllView(BasePackage packageModel,
      RoomAndBoardOptionsPageViewData roomOptionsViewData);
   
}
