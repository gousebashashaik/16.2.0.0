/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.PromotionalDiscount;
import uk.co.tui.th.book.view.data.PromotionalCodeViewData;

/**
 * @author pradeep.as
 *
 */
public class PromotionalDiscountViewDataPopulator implements
   Populator<BasePackage, PromotionalCodeViewData>
{

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final BasePackage source, final PromotionalCodeViewData target)
      throws ConversionException
   {
      final PromotionalDiscount promotionalDiscount = source.getPromotionalDiscount();
      target.setApplicable(true);
      target.setPromotionalDiscount(promotionalDiscount.getPrice().getAmount().getAmount()
         .multiply(BigDecimal.valueOf(-1)).setScale(TWO, RoundingMode.HALF_UP));
      target.setTotalPrice(source.getPrice().getAmount().getAmount());
   }
}
