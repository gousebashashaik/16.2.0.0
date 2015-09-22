/**
 *
 */
package uk.co.tui.cr.book.facade;

import uk.co.tui.cr.book.view.data.PassengerDetailsViewData;

/**
 * @author uday.g
 *
 */
public interface ValidatePromoCodeFacade {

    /**
     * @param promocode
     * @return PromotionalCodeViewData
     */
    PassengerDetailsViewData validate(String promocode);

}
