/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;

/**
 * @author l.furrer
 *
 */
public class AttractionConverter extends
   AbstractPopulatingConverter<AttractionModel, AttractionViewData>
{

   @Override
   public AttractionViewData createTarget()
   {
      return new AttractionViewData();
   }

   @Override
   public void populate(final AttractionModel source, final AttractionViewData target)
   {
      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");
      target.setType(source.getAttractionType().getCode());
      target.setName(source.getName());
      super.populate(source, target);
   }
}
