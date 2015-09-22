/**
 *
 */
package uk.co.portaltech.tui.facades;

import uk.co.portaltech.tui.web.view.data.PromoComponentData;
import uk.co.tui.book.view.data.PassengerDetailsViewData;

/**
 * The Interface PromoComponentFacade.
 *
 * @author pts
 */
public interface PromoComponentFacade {

    /**
     * Ge promo comonent view data.
     *
     * @param componentUID
     *            the component uid
     * @return the promo component data
     */
    PromoComponentData gePromoComonentViewData(String componentUID);

    /**
     * Removes the existing promo code.
     *
     * @return the passenger details view data
     */
    PassengerDetailsViewData removeExistingPromoCode();
}
