/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author omonikhide
 *
 */
public class LocationThumbnailMapPopulator implements Populator<LocationModel, LocationData>
{

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private FeatureService featureService;

   @Resource
   private TUIUrlResolver<LocationModel> tuiCategoryModelUrlResolver;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Override
   public void populate(final LocationModel sourceModel, final LocationData targetData)
      throws ConversionException
   {
      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");

      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { "longitude", "latitude", "capitalCity" }));
      targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
         featureDescriptorList, sourceModel, new Date(),
         brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));
      targetData.setLocationType(sourceModel.getType());

      String url = tuiCategoryModelUrlResolver.resolve(sourceModel);

      if (StringUtils.equalsIgnoreCase(tuiUtilityService.getSiteBrand(), "FJ"))
      {
         url = tuiCategoryModelUrlResolver.getTabUrl(url, "places-to-go");
      }
      else
      {
         url = addLocationThumbnailMapURL(url);
      }

      targetData.setThingstodoMapUrl(url);

   }

   /**
    * @param url
    * @return string
    */
   private String addLocationThumbnailMapURL(final String url)
   {
      String thumpnailMapURL = url;

      if (thumpnailMapURL.contains(".html"))
      {
         thumpnailMapURL = tuiCategoryModelUrlResolver.getTabUrl(thumpnailMapURL, "places-to-go");
      }
      else
      {
         thumpnailMapURL = thumpnailMapURL.replace("overview", "places-to-go");
      }
      return thumpnailMapURL;
   }

}
