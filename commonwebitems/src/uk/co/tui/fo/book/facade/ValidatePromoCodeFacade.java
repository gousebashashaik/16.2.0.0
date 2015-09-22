/**
 *
 */
package uk.co.tui.fo.book.facade;

import uk.co.tui.fo.book.view.data.PassengerDetailsViewData;

/**
 * @author sreevidhya.r
 *
 */
public interface ValidatePromoCodeFacade
{

   /**
    * @param promocode
    * @return PromotionalCodeViewData
    */
   PassengerDetailsViewData validate(String promocode);

}
