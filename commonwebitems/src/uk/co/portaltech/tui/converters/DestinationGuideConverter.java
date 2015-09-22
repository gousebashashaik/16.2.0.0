package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.jalo.Location;
import uk.co.portaltech.tui.web.view.data.DestinationData;

/**
 * @author laxmibai.p
 *
 */
public class DestinationGuideConverter extends
   AbstractPopulatingConverter<Location, DestinationData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */
   @Override
   public DestinationData createTarget()
   {

      return new DestinationData();
   }

   @Override
   public void populate(final Location source, final DestinationData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      target.setId(source.getCode());
      target.setName(source.getName());

   }

}
