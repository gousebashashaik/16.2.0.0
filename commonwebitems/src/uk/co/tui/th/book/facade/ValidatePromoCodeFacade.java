/**
 *
 */
package uk.co.tui.th.book.facade;

import uk.co.tui.th.book.view.data.PassengerDetailsViewData;

/**
 * @author thyagaraju.e
 *
 */
public interface ValidatePromoCodeFacade {

    /**
     * @param promocode
     * @return PromotionalCodeViewData
     */
    PassengerDetailsViewData validate(String promocode);

}
