/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ExchangeRateModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.services.ExchangeRateService;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;

/**
 * @author anirudha.rb
 *
 */
public class ExcursionPricePopulator implements Populator<ExcursionModel, ExcursionViewData>
{

   @Resource
   private ExchangeRateService exchangeRateService;

   @Resource
   private CMSSiteService cmsSiteService;

   private static final double POINT_FIFTY = 0.50;

   private static final double POINT_NINTY_NINE = 0.99;

   private static final int TWO = 2;

   @Override
   public void populate(final ExcursionModel sourceModel, final ExcursionViewData targetData)
      throws ConversionException
   {

      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");

      final ExchangeRateModel exchangeRateModel =
         exchangeRateService.getExchangeRate(((ExcursionPriceModel) CollectionUtils.get(
            sourceModel.getExcursionPrices(), 0)).getCurrency(), cmsSiteService
            .getCurrentCatalogVersion());

      final BigDecimal exchangeRate = exchangeRateModel.getCurrencyExchangeRate();
      BigDecimal lowestPrice = null;
      BigDecimal lowestChildPrice = null;

      final BigDecimal pointFifty =
         (BigDecimal.valueOf(POINT_FIFTY)).setScale(TWO, RoundingMode.FLOOR);
      final BigDecimal pointNintyNine =
         (BigDecimal.valueOf(POINT_NINTY_NINE)).setScale(TWO, RoundingMode.FLOOR);

      for (final ExcursionPriceModel price : sourceModel.getExcursionPrices())
      {
         if (lowestPrice == null)
         {
            lowestPrice = price.getAdultPrice();
         }
         else if (price.getAdultPrice().compareTo(lowestPrice) < 0)
         {
            lowestPrice = price.getAdultPrice();
         }

         if (lowestChildPrice == null)
         {
            lowestChildPrice = price.getChildPrice();
         }
         else if (price.getChildPrice().compareTo(lowestChildPrice) < 0)
         {
            lowestChildPrice = price.getChildPrice();
         }
      }
      final BigDecimal fromPrice =
         getPriceForLowestPrice(lowestPrice, exchangeRate, pointFifty, pointNintyNine);
      targetData.setFromPrice(fromPrice.setScale(TWO, RoundingMode.FLOOR).toString());

      final BigDecimal childPrice =
         getPriceForLowestChildPrice(lowestChildPrice, exchangeRate, pointFifty, pointNintyNine);
      targetData.setChildPrice(childPrice.setScale(TWO, RoundingMode.FLOOR).toString());

   }

   /**
    * @param lowest
    * @param rate
    * @param fifty
    * @param nintyNine
    * @return fromPrice
    */
   private BigDecimal getPriceForLowestPrice(final BigDecimal lowest, final BigDecimal rate,
      final BigDecimal fifty, final BigDecimal nintyNine)
   {

      final BigDecimal lowestPrice = lowest;
      final BigDecimal exchangeRate = rate;
      final BigDecimal pointFifty = fifty;
      final BigDecimal pointNintyNine = nintyNine;
      BigDecimal fromPrice = null;

      if (lowestPrice != null && exchangeRate != null)
      {
         fromPrice = lowestPrice.divide(exchangeRate, 0, RoundingMode.FLOOR);
         final double decimalValue =
            lowestPrice.doubleValue() / exchangeRate.doubleValue() - fromPrice.doubleValue();
         if (decimalValue != 0.00)
         {
            if (decimalValue <= POINT_FIFTY)
            {
               fromPrice = fromPrice.add(pointFifty);
            }
            else
            {
               fromPrice = fromPrice.add(pointNintyNine);
            }
         }
      }
      return fromPrice;
   }

   /**
    * @param lowest
    * @param rate
    * @param fifty
    * @param nintyNine
    * @return childPrice
    */
   private BigDecimal getPriceForLowestChildPrice(final BigDecimal lowest, final BigDecimal rate,
      final BigDecimal fifty, final BigDecimal nintyNine)
   {
      final BigDecimal lowestChildPrice = lowest;
      final BigDecimal exchangeRate = rate;
      final BigDecimal pointFifty = fifty;
      final BigDecimal pointNintyNine = nintyNine;
      BigDecimal childPrice = null;

      if (lowestChildPrice != null && exchangeRate != null)
      {
         childPrice = lowestChildPrice.divide(exchangeRate, 0, RoundingMode.FLOOR);
         final double decimalValue =
            lowestChildPrice.doubleValue() / exchangeRate.doubleValue() - childPrice.doubleValue();
         if (decimalValue != 0.00)
         {
            if (decimalValue <= POINT_FIFTY)
            {
               childPrice = childPrice.add(pointFifty);
            }
            else
            {
               childPrice = childPrice.add(pointNintyNine);
            }
         }
      }
      return childPrice;
   }
}
