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
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author s.consolino
 * 
 */
public class AccommodationHighlightsPopulator implements
   Populator<AccommodationModel, AccommodationViewData>
{

   @Resource
   private FeatureService featureService;

   @Resource
   private BrandUtils brandUtils;

   private static final String FALCON_ACCOM_USPS = "falcon_usp_override";

   private static final String USPS = "usps";

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final AccommodationModel source, final AccommodationViewData target)
      throws ConversionException
   {
      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");
      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { USPS, FALCON_ACCOM_USPS }));
      final Map<String, List<Object>> uspFeatureValues =
         featureService.getOptimizedValuesForFeatures(featureDescriptorList, source, new Date(),
            brandUtils.getFeatureServiceBrand(source.getBrands()));

      final List<Object> featureValues = uspFeatureValues.get(FALCON_ACCOM_USPS);
      if (CollectionUtils.isNotEmpty(featureValues))
      {
         uspFeatureValues.put(USPS, featureValues);
      }
      target.putFeatureCodesAndValues(uspFeatureValues);
   }
}
