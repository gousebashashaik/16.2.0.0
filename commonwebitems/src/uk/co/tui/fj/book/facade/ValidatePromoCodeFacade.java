/**
 *
 */
package uk.co.tui.fj.book.facade;

import uk.co.tui.fj.book.view.data.PassengerDetailsViewData;

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
