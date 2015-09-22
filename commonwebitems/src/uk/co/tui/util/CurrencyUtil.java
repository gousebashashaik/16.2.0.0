/**
 *
 */
package uk.co.tui.util;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.tui.utils.CurrencyUtils;

/**
 * The Class PriceUtil.
 *
 * @author samantha.gd
 */
public final class CurrencyUtil
{

    private CurrencyUtil()
    {

    }

    /**
     * Gets the currency appended price with precision.
     *
     * Gives -£90 or +£90 when -90/90 is passed respectively
     *
     * @param price
     *            the price
     * @return the currency appended price
     */
   public static String getCurrencyAppendedPrice(final BigDecimal price,String currencyCode)
   {
      String formattedPrice = StringUtils.EMPTY;
      if (price.compareTo(BigDecimal.ZERO) < 0)
      {
         formattedPrice =
            StringUtil.append("-", CurrencyUtils.getCurrencySymbol(currencyCode), String.valueOf(price.negate()));
      }
      else
      {
         formattedPrice = StringUtil.append("+", CurrencyUtils.getCurrencySymbol(currencyCode), String.valueOf(price));
      }
      return formattedPrice;
   }

}
