/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;

import javax.annotation.Resource;

import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.book.view.data.AlertViewData;

/**
 * @author vinod kumar g
 *
 */
public class ThirdPartyFlightAlertViewDataPopulator implements Populator<Feedback, AlertViewData>
{

   @Resource
   private PropertyReader messagePropertyReader;

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
      if (newPrice.compareTo(oldPrice) > 0)
      {
         subValues[0] = newPrice.subtract(oldPrice).toString();
         target.setMessageHeader(messagePropertyReader
            .getValue("Increase_Price_Header_Third_Party_Flight"));
         target.setMessageText(messagePropertyReader.substitute(
            "Increase_Price_Text_Third_Party_Flight_FC", subValues));
      }
      else
      {
         subValues[0] = oldPrice.subtract(newPrice).toString();
         target.setMessageHeader(messagePropertyReader
            .getValue("Decrease_Price_Header_Third_Party_Flight"));
         target.setMessageText(messagePropertyReader.substitute(
            "Decrease_Price_Text_Third_Party_Flight", subValues));
      }
      target.setAlertLevel("default");
   }

}
