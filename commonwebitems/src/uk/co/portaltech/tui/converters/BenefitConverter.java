/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.media.MediaContainerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.BenefitModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.BenefitViewData;

/**
 * @author omonikhide
 *
 */
public class BenefitConverter extends AbstractPopulatingConverter<BenefitModel, BenefitViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   // Here, the population of the data object is done in the converter

   @Resource
   private FeatureService featureService;

   private Populator<Collection<MediaContainerModel>, List<ImageData>> accommodationBenefitImageDataPopulator;

   private static final String NAME = "name";

   private static final String DESCRIPTION = "description";

   private static final String INTRO = "intro";

   @Override
   public BenefitViewData createTarget()
   {

      return new BenefitViewData();
   }

   @Override
   public void populate(final BenefitModel source, final BenefitViewData target)
   {
      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { NAME, DESCRIPTION, INTRO }));
      final Map<String, List<Object>> features =
         featureService.getOptimizedValuesForFeatures(featureDescriptorList, source, new Date(),
            null);

      target.setName(getFirstFeatureValue(features.get(NAME)).toString());
      setDescription(features, target);

      final Collection<MediaContainerModel> galleryImages = source.getGalleryImages();

      if (galleryImages != null && !galleryImages.isEmpty())
      {
         final List<ImageData> imageDataList = new ArrayList<ImageData>();
         accommodationBenefitImageDataPopulator.populate(galleryImages, imageDataList);
         target.setList(imageDataList);
         target.setThunbnailUrl(imageDataList.get(0).getUrl());
      }
      super.populate(source, target);
   }

   public Populator<Collection<MediaContainerModel>, List<ImageData>> getAccommodationBenefitImageDataPopulator()
   {
      return accommodationBenefitImageDataPopulator;
   }

   public void setAccommodationBenefitImageDataPopulator(
      final Populator<Collection<MediaContainerModel>, List<ImageData>> accommodationBenefitImageDataPopulator)
   {
      this.accommodationBenefitImageDataPopulator = accommodationBenefitImageDataPopulator;
   }

   /**
    * Returns the first value from the list
    *
    * @param featureValues
    * @return object
    */
   private Object getFirstFeatureValue(final List<Object> featureValues)
   {
      if (featureValues != null && !featureValues.isEmpty())
      {
         return featureValues.get(0);
      }
      return "";
   }

   /**
    * Returns the first value from the list
    *
    * @param featureValues
    * @return object
    */
   private void setDescription(final Map<String, List<Object>> features,
      final BenefitViewData target)
   {
      final Object description = getFirstFeatureValue(features.get(DESCRIPTION));
      final Object intro = getFirstFeatureValue(features.get(INTRO));

      if (!("").equals(description))
      {
         target.setDescription(description.toString());
      }
      else
      {
         target.setDescription(intro.toString());
      }
   }

}
