/**
 *
 */
package uk.co.tui.fo.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.fo.book.view.data.PackageViewData;

/**
 * The Class PriceViewDataPopulator.
 *
 * @author samantha.gd
 */
public class PriceViewDataPopulator implements Populator<BasePackage, PackageViewData>
{

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   @Resource
   private CurrencyResolver currencyResolver;

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final BasePackage source, final PackageViewData target)
      throws ConversionException
   {
      final String defaultCurrencyCode = currencyResolver.getSiteCurrency();
      calculateOnlineDiscountPrice(source, target, defaultCurrencyCode);
      if (null != source.getPrice())
      {
         target.setPricePerPerson(source.getPrice().getRate().getAmount());
         target.setCurrencyAppendedPricePerPerson(getDescription("PRICEPERPERSON",
            CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
               + source.getPrice().getRate().getAmount().setScale(TWO, RoundingMode.HALF_UP)));

         // US 7277 - showing total price with decimals.
         target.setCurrencyAppendedRoundUpTotalCost(CurrencyUtils
            .getCurrencySymbol(defaultCurrencyCode)
            + source.getPrice().getAmount().getAmount().setScale(TWO, RoundingMode.HALF_UP));
      }

   }

   /**
    * @param source
    * @param target
    * @param defaultCurrencyCode
    */
   private void calculateOnlineDiscountPrice(final BasePackage source,
      final PackageViewData target, final String defaultCurrencyCode)
   {
      if (SyntacticSugar.isNotNull(source.getDiscount()))
      {
         BigDecimal viewDataOnlineDiscountPrice =
            source.getDiscount().getPrice().getAmount().getAmount();
         if (viewDataOnlineDiscountPrice.signum() == -1)
         {
            viewDataOnlineDiscountPrice = viewDataOnlineDiscountPrice.negate();
         }
         updateOnlineDiscount(target, defaultCurrencyCode, viewDataOnlineDiscountPrice);
      }
   }

   /**
    * Method to update online discount details.
    *
    * @param target
    * @param defaultCurrencyCode
    * @param viewDataOnlineDiscountPrice
    */
   private void updateOnlineDiscount(final PackageViewData target,
      final String defaultCurrencyCode, final BigDecimal viewDataOnlineDiscountPrice)
   {
      if (viewDataOnlineDiscountPrice.compareTo(BigDecimal.ZERO) > 0)
      {
         target.setCurrencyAppendedOnlineDiscount(getDescription("DISCOUNT",
            " " + CurrencyUtils.getCurrencySymbol(defaultCurrencyCode)
               + viewDataOnlineDiscountPrice.setScale(TWO, RoundingMode.HALF_UP)));
         target.setOnlineDiscount(viewDataOnlineDiscountPrice);
      }
   }

   /**
    * Gets the description from content csv.
    *
    * @param key the key
    * @param value the value
    * @return the description
    */
   private String getDescription(final String key, final String... value)
   {
      final Map<String, String> summaryContentMap = staticContentServ.getSummaryContents();

      if (StringUtils.isNotEmpty(key))
      {
         final String summaryContent = summaryContentMap.get(key);
         if (SyntacticSugar.isNotNull(summaryContent))
         {
            if (SyntacticSugar.isEmpty(value))
            {
               return summaryContent;
            }
            return StringUtil.substitute(summaryContent, value);
         }
      }
      return StringUtils.EMPTY;
   }

}
