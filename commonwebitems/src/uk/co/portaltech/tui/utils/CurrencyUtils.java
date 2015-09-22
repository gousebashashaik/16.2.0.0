/*
 */
package uk.co.portaltech.tui.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;

/**
 * This Class handles the utlities regarding currency.
 *
 * @author amaresh.d
 */
public final class CurrencyUtils
{
   private static final String GBP = "GBP";

   private static final int TWO = 2;

   /**
    * Private constructor.
    */
   private CurrencyUtils()
   {
      // Private constructor.
   }

   /**
    * Gets the currency symbol for UK locale.
    *
    * @param currencyCode the currency code
    * @return the currency symbol for UK locale.
    */
   public static String getCurrencySymbol(final String currencyCode)
   {
      final List<String> supportedCodes = Arrays.asList(GBP, "EUR");
      String localCurrency = StringUtils.upperCase(currencyCode);
      if (StringUtils.isNotEmpty(localCurrency) && !supportedCodes.contains(localCurrency))
      {
         throw new IllegalArgumentException(String.format("%s is an unsupported currency for UK",
            localCurrency));
      }
      if (StringUtils.isEmpty(localCurrency))
      {
         localCurrency = GBP;
      }
      Locale.setDefault(Locale.UK);
      final Currency curr = Currency.getInstance(StringUtils.upperCase(localCurrency));
      return curr.getSymbol(Locale.UK);
   }

   /**
    * Gets the currency appended price with precision.
    *
    * Gives -£90 or +£90 when -90/90 is passed respectively
    *
    * @param price the price
    * @return the currency appended price
    */
   public static String getCurrencyAppendedPrice(final BigDecimal price)
   {
      String formattedPrice = StringUtils.EMPTY;
      if (price.compareTo(BigDecimal.ZERO) < 0)
      {
         formattedPrice =
            StringUtil.append("-", getCurrencySymbol(GBP), String.valueOf(price.negate()));
      }
      else
      {
         formattedPrice = StringUtil.append("+", getCurrencySymbol(GBP), String.valueOf(price));
      }
      return formattedPrice;
   }

   public static String getCurrencyAppendedPriceWithDecimalPart(final BigDecimal price)
   {
      return getCurrencySymbol(GBP) + price.setScale(TWO, BigDecimal.ROUND_HALF_UP).toString();

   }
}