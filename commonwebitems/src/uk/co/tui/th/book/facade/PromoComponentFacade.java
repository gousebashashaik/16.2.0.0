/**
 *
 */
package uk.co.tui.th.book.facade;

import uk.co.portaltech.tui.web.view.data.PromoComponentData;
import uk.co.tui.th.book.view.data.PassengerDetailsViewData;

/**
 * PromoComponentFacade
 *
 * @author akshay.an
 *
 */
public interface PromoComponentFacade {
    /**
     * To get gePromoComonentViewData
     * @param componentUID The componentUID
     * @return Returns PromoComponentData
     */
    PromoComponentData gePromoComonentViewData(String componentUID);

    /**
     * Method to remove existing promocode
     * @return It returns PassengerDetailsViewData afet rremoving promocode
     */
    PassengerDetailsViewData removeExistingPromoCode();
}
