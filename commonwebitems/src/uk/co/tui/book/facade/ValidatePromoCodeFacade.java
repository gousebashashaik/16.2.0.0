/**
 *
 */
package uk.co.tui.book.facade;

import uk.co.tui.book.view.data.PassengerDetailsViewData;

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
