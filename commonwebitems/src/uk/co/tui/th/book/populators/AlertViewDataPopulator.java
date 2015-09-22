/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;

import javax.annotation.Resource;

import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.th.book.view.data.AlertViewData;

/**
 * @author srikanth.bs
 *
 */
public class AlertViewDataPopulator implements Populator<Feedback, AlertViewData>
{

   @Resource
   private PropertyReader messagePropertyReader;

   @Resource
   private CurrencyResolver currencyResolver;

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final Feedback source, final AlertViewData target)
      throws ConversionException
   {
      final BigDecimal newPrice = source.getNewPrice().setScale(TWO, BigDecimal.ROUND_HALF_UP);
      final BigDecimal oldPrice = source.getOldPrice().setScale(TWO, BigDecimal.ROUND_HALF_UP);
      final String[] subValues = new String[1];
      final String currencySymbol =
         CurrencyUtils.getCurrencySymbol(currencyResolver.getSiteCurrency());
      if (newPrice.compareTo(oldPrice) > 0)
      {
         subValues[0] = newPrice.subtract(oldPrice).toString();
         target.setMessageHeader(messagePropertyReader.getValue("Increase_Price_Header"));
         target.setMessageText(messagePropertyReader.substitute("Increase_Price_Text",
            currencySymbol + subValues[0]));
      }
      else if (newPrice.compareTo(oldPrice) < 0)
      {
         subValues[0] = oldPrice.subtract(newPrice).toString();
         target.setMessageHeader(messagePropertyReader.getValue("Decrease_Price_Header"));
         target.setMessageText(messagePropertyReader.substitute("Decrease_Price_Text",
            currencySymbol + subValues[0]));
      }
      target.setAlertLevel("default");
   }

}
