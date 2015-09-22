/**
 *
 */
package uk.co.tui.fj.book.facade;

import uk.co.tui.book.criteria.RoomSelectionCriteria;
import uk.co.tui.fj.book.view.data.RoomAndBoardOptionsPageViewData;
import uk.co.tui.fj.exception.TUIBusinessException;

/**
 * The Interface RoomOptionsPageFacade.
 *
 * @author samantha.gd
 */
public interface RoomOptionsPageFacade
{
   
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
    * Gets the RoomAndBoardOptionsPageViewData.
    *
    * @return the roomAndBoardOptionsPageViewData
    */
   RoomAndBoardOptionsPageViewData createRoomAndBoardOptionsPageViewData();
   
   /**
    * @param roomAndBoardOptionsPageViewData
    */
   void populateRoomsForWebAnalytics(RoomAndBoardOptionsPageViewData roomAndBoardOptionsPageViewData);
   
}
