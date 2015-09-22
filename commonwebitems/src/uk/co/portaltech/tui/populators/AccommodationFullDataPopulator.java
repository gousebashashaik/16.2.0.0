/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fest.util.Collections;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FeatureValueModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author Kandipr
 * 
 */
public class AccommodationFullDataPopulator implements
   Populator<AccommodationModel, AccommodationViewData>
{

   FeatureService featureService = (FeatureService) Registry.getApplicationContext().getBean(
      "featureService");

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

      final List<FeatureValueSetModel> featureValueSets =
         featureService.getFeatureValueSetsForItem(source);

      final Map<String, List<Object>> fvAndCodes = new HashMap<String, List<Object>>();

      if (!Collections.isEmpty(featureValueSets))
      {
         String featureCode = "";
         List<Object> featureValues = new ArrayList<Object>();
         target.setCode(source.getCode());
         for (final FeatureValueSetModel fvs : featureValueSets)
         {
            featureValues = new ArrayList<Object>();
            if (fvs.getFeatureDescriptor() != null)
            {
               featureCode = fvs.getFeatureDescriptor().getCode();
               for (final FeatureValueModel fv : fvs.getFeatureValues())
               {
                  featureValues.add(fv.getValue());
               }
            }

            fvAndCodes.put(featureCode, featureValues);
         }
         target.putFeatureCodesAndValues(fvAndCodes);
      }
   }
}
