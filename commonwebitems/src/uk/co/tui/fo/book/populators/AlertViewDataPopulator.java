/**
 *
 */
package uk.co.tui.fo.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.fo.book.view.data.AlertViewData;

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

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

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
         populatePriceIncreaseAlert(subValues[0], target);
      }
      else if (newPrice.compareTo(oldPrice) < 0)
      {
         subValues[0] = oldPrice.subtract(newPrice).toString();
         populatePriceDecreaseAlert(subValues[0], target);
      }
      target.setAlertLevel("default");
   }

   /**
    * @param string
    * @param target
    */
   private void populatePriceDecreaseAlert(final String string, final AlertViewData target)
   {
      final PackageHoliday holiday = (PackageHoliday) packageCartService.getBasePackage();
      final String currencySymbol =
         CurrencyUtils.getCurrencySymbol(currencyResolver.getSiteCurrency());
      if (CollectionUtils.isNotEmpty(holiday.getItinerary().getInBound()))
      {
         target.setMessageHeader(messagePropertyReader.getValue("FO_Return_Decrease_Price_Header"));
         target.setMessageText(messagePropertyReader.substitute("FO_Return_Decrease_Price_Text",
            currencySymbol + string));
      }
      else
      {
         target.setMessageHeader(messagePropertyReader.getValue("FO_OneWay_Decrease_Price_Header"));
         target.setMessageText(messagePropertyReader.substitute("FO_OneWay_Decrease_Price_Text",
            currencySymbol + string));
      }
   }

   /**
    * @param string
    * @param target
    */
   private void populatePriceIncreaseAlert(final String string, final AlertViewData target)
   {
      final PackageHoliday holiday = (PackageHoliday) packageCartService.getBasePackage();
      final String currencySymbol =
         CurrencyUtils.getCurrencySymbol(currencyResolver.getSiteCurrency());
      if (CollectionUtils.isNotEmpty(holiday.getItinerary().getInBound()))
      {
         target.setMessageHeader(messagePropertyReader.getValue("FO_Return_Increase_Price_Header"));
         target.setMessageText(messagePropertyReader.substitute("FO_Return_Increase_Price_Text",
            currencySymbol + string));
      }
      else
      {
         target.setMessageHeader(messagePropertyReader.getValue("FO_OneWay_Increase_Price_Header"));
         target.setMessageText(messagePropertyReader.substitute("FO_OneWay_Increase_Price_Text",
            currencySymbol + string));
      }

   }

}
