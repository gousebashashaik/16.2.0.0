/**
 *
 */
package uk.co.tui.manage.facade;

import java.util.Map;

import uk.co.tui.manage.services.exception.TUIBusinessException;

/**
 * @author premkumar.nd
 *
 */
public interface PaymentFacade
{
   String getPaymentPageRelativeURL(Map<String, String> urlMap, String hostName)
      throws TUIBusinessException;

   /**
    * @return
    */
   boolean isPreAuthSuccess();

}
