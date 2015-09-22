/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;

/**
 * @author abi
 *
 */
public class ExcursionConverter extends
   AbstractPopulatingConverter<ExcursionModel, ExcursionViewData>
{

   @Override
   public ExcursionViewData createTarget()
   {
      return new ExcursionViewData();
   }

   @Override
   public void populate(final ExcursionModel source, final ExcursionViewData target)
   {
      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      super.populate(source, target);
   }
}
