/**
 *
 */
package uk.co.tui.cr.book.facade;

import uk.co.portaltech.tui.web.view.data.PromoComponentData;
import uk.co.tui.cr.book.view.data.PassengerDetailsViewData;

/**
 * @author uday.g
 *
 */
public interface PromoComponentFacade {
    /**
     * To get gePromoComonentViewData
     *
     * @param componentUID
     *            The componentUID
     * @return Returns PromoComponentData
     */
    PromoComponentData gePromoComonentViewData(String componentUID);

    /**
     * Method to remove existing promocode
     *
     * @return It returns PassengerDetailsViewData afet rremoving promocode
     */
    PassengerDetailsViewData removeExistingPromoCode();
}
