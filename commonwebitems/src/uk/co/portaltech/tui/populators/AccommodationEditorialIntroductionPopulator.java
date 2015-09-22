/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author omonikhide
 * @edited by :Abir Pal
 */
public class AccommodationEditorialIntroductionPopulator implements
   Populator<AccommodationModel, AccommodationViewData>
{

   private final List<String> villaEditorialIntroductionFeatureList = Arrays.asList(new String[] {
      "introduction", "swimmingPool", "arrivalDay", "onlyFrom", "sleepsUpTo", "noOfBathrooms",
      "noOfBedrooms", "airCon", "airConInfo", "freeChildPlaces", "noOfFloors" });

   // FCU067,ACCGLD feature stands for Gold Medal FC, TH resp.
   // ACCTRA is for showing travelers' choice.
   // awards will have mapping for FCU067,ACCGLD and ACCTRA.
   private final List<String> accommEditorialIntroductionFeatureList = Arrays.asList(new String[] {
      "introduction", "overview_1_new", "overview_2_new", "overview_3_new", "officialRating",
      "noOfBuildings", "noOfFloors", "noOfrooms", "onlyFrom", "noOfLifts", "awards", "DISMAG",
      "tripadvisor_id", "no_tripadvisor_rating_message", "ACCALT" });

   private static final String VILLA = "villa";

   @Resource
   private FeatureService featureService;

   @Resource
   private BrandUtils brandUtils;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final AccommodationModel sourceModel, final AccommodationViewData targetData)
      throws ConversionException
   {
      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");
      if (sourceModel.getType() != null)
      {
         if (sourceModel.getType().getCode().equalsIgnoreCase(VILLA))
         {
            targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
               villaEditorialIntroductionFeatureList, sourceModel, new Date(),
               brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));
         }
         else
         {
            targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
               accommEditorialIntroductionFeatureList, sourceModel, new Date(),
               brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));
         }

         if (sourceModel.getReviewRating() != null)
         {
            targetData.setReviewRating(sourceModel.getReviewRating().toString());
         }
         if (sourceModel.getReviewsCount() != null)
         {
            targetData.setReviewCount(sourceModel.getReviewsCount().toString());
         }
         targetData.setRatingBar(sourceModel.getRatingsBar());

      }

      final List<FacilityModel> facilities = (List<FacilityModel>) sourceModel.getFacilities();
      for (final FacilityModel facility : facilities)
      {
         if ("STO7".equalsIgnoreCase(facility.getType().getCode()))

         {
            targetData.setAccomwifi(true);
         }

      }

   }
}
