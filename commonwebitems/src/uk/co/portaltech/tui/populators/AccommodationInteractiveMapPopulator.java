/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.AttractionConverter;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.services.LinkedItemService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author omonikhide
 *
 */
public class AccommodationInteractiveMapPopulator implements
   Populator<AccommodationModel, MapDataWrapper>
{

   @Resource
   private FeatureService featureService;

   @Resource
   private AttractionConverter attractionConverter;

   @Resource
   private MediaDataPopulator mediaDataPopulator;

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   @Resource
   private TUIUrlResolver<AttractionModel> attractionUrlResolver;

   @Resource
   private AccommodationProductRangePopulator accommodationProductRangePopulator;

   @Resource
   private SessionService sessionService;

   @Resource
   private LinkedItemService linkedItemService;

   @Resource
   private BrandUtils brandUtils;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final AccommodationModel source, final MapDataWrapper targetParam)
      throws ConversionException
   {
      MapDataWrapper target = targetParam;
      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");

      final BrandDetails brandDetails =
         (BrandDetails) sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final Collection<CategoryModel> supercategories = source.getSupercategories();
      if (brandDetails != null && StringUtils.isNotBlank(brandDetails.getSiteUid()))
      {
         target.setSiteBrand(brandDetails.getSiteUid());
      }
      if (supercategories != null && !supercategories.isEmpty())
      {
         for (final CategoryModel categoryModel : supercategories)
         {
            if (categoryModel instanceof LocationModel)
            {
               final LocationModel locationModel = (LocationModel) categoryModel;
               final Collection<AttractionModel> attractionsModels = locationModel.getAttractions();

               target.setTopLocationName(featureService.getFirstFeatureValueAsString("name",
                  locationModel, new Date(),
                  brandUtils.getFeatureServiceBrand(locationModel.getBrands())));
               target = getAttractionDataForInteractiveMap(attractionsModels, target);
               final List<ProductModel> products = ((LocationModel) categoryModel).getProducts();
               if (products != null && !products.isEmpty())
               {
                  AccommodationViewData accommodationData = null;
                  for (final ProductModel product : products)
                  {
                     if (product instanceof AccommodationModel)
                     {
                        final AccommodationModel accommodationModel = (AccommodationModel) product;
                        accommodationData = new AccommodationViewData();
                        final List<String> featureDescriptorList =
                           new ArrayList(Arrays.asList(new String[] { "latitude", "longitude",
                              "name" }));
                        if (!linkedItemService.isMultiCentre(accommodationModel))
                        {
                           featureDescriptorList.add("tRating");
                        }

                        final String brand = brandUtils.getFeatureServiceBrand(source.getBrands());
                        accommodationData.putFeatureCodesAndValues(featureService
                           .getValuesForFeatures(featureDescriptorList, accommodationModel,
                              new Date(), brand));
                        accommodationData.setUrl(tuiProductUrlResolver.resolve(accommodationModel));
                        accommodationData.setAccommodationType(accommodationModel.getType()
                           .toString());
                        accommodationData.setBrand(brand);
                        accommodationProductRangePopulator.populate(accommodationModel,
                           accommodationData);
                        accommodationData.setCode(accommodationModel.getCode());
                        checkThumbNail(accommodationData, accommodationModel);
                     }

                     if ("thomson".equals(brandDetails.getSiteName()))
                     {
                        if (accommodationData.getAccommodationType().equalsIgnoreCase(
                           AccommodationType.VILLA.toString()))
                        {
                           target.getVillas().add(accommodationData);
                        }
                        else
                        {
                           target.getHotels().add(accommodationData);
                        }
                     }
                     else if ("firstchoice".equals(brandDetails.getSiteName()))
                     {
                        target.getAccommodations().add(accommodationData);
                     }
                     else if ("falcon".equals(brandDetails.getSiteName()))
                     {
                        target.getHotels().add(accommodationData);
                     }
                     else if ("cruise".equals(brandDetails.getSiteName()))
                     {
                        target.getHotels().add(accommodationData);
                     }

                  }
               }

            }
         }

      }

   }

   /**
    * @param accommodationData
    * @param accommodationModel
    */
   private void checkThumbNail(final AccommodationViewData accommodationData,
      final AccommodationModel accommodationModel)
   {
      final MediaModel thumbnail = accommodationModel.getThumbnail();
      if (thumbnail != null)
      {
         final MediaViewData mediaData = new MediaViewData();
         mediaDataPopulator.populate(thumbnail, mediaData);
         accommodationData.setThumbnail(mediaData);
      }
   }

   /**
    * @param attractionsModel
    * @param targetData
    * @return MapDataWrapper
    *
    *         This method returns the Attraction data with sights , events .
    *
    */
   private MapDataWrapper getAttractionDataForInteractiveMap(
      final Collection<AttractionModel> attractionsModel, final MapDataWrapper targetData)
   {
      final Collection<AttractionModel> attractionsModels = attractionsModel;
      final MapDataWrapper target = targetData;

      if (attractionsModels != null && !attractionsModels.isEmpty())
      {
         for (final AttractionModel attractionModel : attractionsModels)
         {
            final AttractionViewData attractionData = attractionConverter.convert(attractionModel);
            final List<String> featureDescriptorList =
               new ArrayList(Arrays.asList(new String[] { "latitude", "longitude", "name" }));
            attractionData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
               featureDescriptorList, attractionModel, new Date(), null));
            attractionData.setUrl(attractionUrlResolver.resolve(attractionModel));
            attractionData.setCode(attractionModel.getCode());
            final MediaModel thumbnail = attractionModel.getThumbnail();
            if (thumbnail != null)
            {
               final MediaViewData mediaData = new MediaViewData();
               mediaDataPopulator.populate(thumbnail, mediaData);
               attractionData.setThumbnail(mediaData);
            }
            attractionData.setType(attractionModel.getAttractionType().toString());
            if ("SIGHT".equals(attractionData.getType()))
            {
               target.getSights().add(attractionData);
            }
            else if ("EVENT".equals(attractionData.getType()))
            {
               target.getEvents().add(attractionData);
            }
            else
            {
               // If it is any other type such as 'OTHER' treat it as a sight.
               target.getSights().add(attractionData);
            }
         }
      }
      return target;
   }

}
