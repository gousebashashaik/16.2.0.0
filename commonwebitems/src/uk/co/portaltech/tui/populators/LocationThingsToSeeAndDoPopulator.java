/**
 * 
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author niranjani.r
 * 
 */
public class LocationThingsToSeeAndDoPopulator implements Populator<LocationModel, LocationData>
{
   @Resource
   private FeatureService featureService;

   @Resource
   private BrandUtils brandUtils;

   private static final String BEACHESTITLE1 = "beaches1Title";

   private static final String BEACHESBODY1 = "beaches1Body";

   private static final String BEACHESTITLE2 = "beaches2Title";

   private static final String BEACHESBODY2 = "beaches2Body";

   private static final String BEACHESTITLE3 = "beaches3Title";

   private static final String BEACHESBODY3 = "beaches3Body";

   private static final String FOODANDDRINKTITLE1 = "foodDrinkIntro1Title";

   private static final String FOODANDDRINKBODY1 = "foodDrinkIntro1Body";

   private static final String FOODANDDRINKTITLE2 = "foodDrinkIntro2Title";

   private static final String FOODANDDRINKBODY2 = "foodDrinkIntro2Body";

   private static final String FOODANDDRINKTITLE3 = "foodDrinkIntro3Title";

   private static final String FOODANDDRINKBODY3 = "foodDrinkIntro3Body";

   private static final String FOODANDDRINKTITLE4 = "foodDrinkIntro4Title";

   private static final String FOODANDDRINKBODY4 = "foodDrinkIntro4Body";

   private static final String FOODANDDRINKTITLE5 = "foodDrinkIntro5Title";

   private static final String FOODANDDRINKBODY5 = "foodDrinkIntro5Body";

   private static final String LAIDBACK = "nightLifeLaidBack";

   private static final String LIVELY = "nightLifeLively";

   private static final String BARGAIN = "bargain";

   private static final String DESIGNER = "designer";

   private static final String MIDRANGE = "midRange";

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final LocationModel source, final LocationData target)
      throws ConversionException
   {
      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");

      target.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
         Arrays.asList(new String[] { BEACHESTITLE1, BEACHESTITLE2, BEACHESTITLE3, BEACHESBODY1,
            BEACHESBODY2, BEACHESBODY3, BARGAIN, DESIGNER, MIDRANGE, LAIDBACK, LIVELY,
            FOODANDDRINKTITLE1, FOODANDDRINKBODY1, FOODANDDRINKTITLE2, FOODANDDRINKBODY2,
            FOODANDDRINKTITLE3, FOODANDDRINKBODY3, FOODANDDRINKTITLE4, FOODANDDRINKBODY4,
            FOODANDDRINKTITLE5, FOODANDDRINKBODY5 }), source, new Date(),
         brandUtils.getFeatureServiceBrand(source.getBrands())));
   }

}
