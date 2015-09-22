/**
 *
 */
package uk.co.tui.book.facade;

import java.util.List;
import java.util.Map;

import uk.co.tui.book.view.data.BoardBasisViewData;


/**
 * The interface AllInclusiveBoardFacade.
 *
 * @author sunilkumar.sahu
 *
 */
public interface AllInclusiveBoardFacade
{

    /**
     * To give the view data about Board Basis.
     *
     * @return BoardBasisViewData
     */
    BoardBasisViewData getBoardBasisViewData();


    /**
     * This method populates sportsActivities, foodAndDrinks like facilities
     *
     */
    Map<String, List<Object>> getOtherBoardBasisFacilities();
}
