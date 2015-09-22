/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;

/**
 * @author l.furrer
 *
 */
public class FacilityConverter extends AbstractPopulatingConverter<FacilityModel, FacilityViewData>
{

   @Override
   public FacilityViewData createTarget()
   {
      return new FacilityViewData();
   }

}
