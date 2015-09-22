/**
 *
 */
package uk.co.tui.fj.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.fj.book.view.data.teasers.DurationTeaserViewData;

/**
 * @author samantha.gd
 *
 */
public class DurationTeaserViewDataPopulator implements Populator<BasePackage, DurationTeaserViewData>
{

   /* (non-Javadoc)
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
    */
   @Override
   public void populate(BasePackage source, DurationTeaserViewData target)
   {
      target.setDuration(source.getDuration().intValue());
      target.setPackageId(source.getId());
      target.setPerPersonPrice(source.getPrice().getRate().getAmount());
      target.setTotalPrice(source.getPrice().getAmount().getAmount());
   }

}
