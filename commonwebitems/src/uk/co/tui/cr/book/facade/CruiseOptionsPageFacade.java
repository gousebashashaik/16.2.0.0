/**
 *
 */
package uk.co.tui.cr.book.facade;

import uk.co.tui.book.criteria.RoomSelectionCriteria;
import uk.co.tui.cr.book.criteria.DeckPlanCriteria;
import uk.co.tui.cr.book.view.data.CruiseOptionsPageViewData;
import uk.co.tui.cr.book.view.data.DeckPlanViewData;
import uk.co.tui.cr.exception.TUIBusinessException;

/**
 * @author ramkishore.p
 *
 */
public interface CruiseOptionsPageFacade {

    /**
     * Gets the alternate cabins.
     *
     * @return the alternate cabins
     */
    CruiseOptionsPageViewData getAlternateCabins();

    /**
     * Update the selected cabin.
     *
     * @param cabinCriteria
     * @return updated cabin
     */
    CruiseOptionsPageViewData updateSelectedCabin(
            RoomSelectionCriteria cabinCriteria);

    /**
     * Change cabin allocation.
     *
     * @param cabinCriteria
     * @return CruiseOptionsPageViewData
     */
    CruiseOptionsPageViewData updateSelectedCabinAllocation(
            RoomSelectionCriteria cabinCriteria) throws TUIBusinessException;

    /**
     * @param boardBasisCode
     * @return CruiseOptionsPageViewData
     */
    CruiseOptionsPageViewData updateSelectedBoardBasis(String boardBasisCode);

    /**
     * Gets the Deck plan.
     *
     * @param deckPlanCriteria
     * @return DeckPlanViewData
     */
    DeckPlanViewData getDeckPlanViewData(DeckPlanCriteria deckPlanCriteria);

}
