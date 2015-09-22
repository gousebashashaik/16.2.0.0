/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.LocationData;

/**
 * @author omonikhide
 *
 */
public class LocationConverter extends AbstractPopulatingConverter<LocationModel, LocationData>
{

   @Resource
   private FeatureService featureService;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public LocationData createTarget()
   {

      return new LocationData();
   }

   @Override
   public void populate(final LocationModel source, final LocationData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");
      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { "name", "strapline" }));
      target.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
         featureDescriptorList, source, new Date(), source.getBrands().get(0).getCode()));
   }

}
