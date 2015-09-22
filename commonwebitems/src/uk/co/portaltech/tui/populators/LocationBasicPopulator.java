package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.media.services.LocationMediaService;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.resolvers.AttractionUrlResolver;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author abi
 *
 */
public class LocationBasicPopulator implements Populator<LocationModel, LocationData>
{

   @Resource
   private Populator<Collection<MediaContainerModel>, List<ImageData>> locationImageDataPopulator;

   @Resource
   private UrlResolver<LocationModel> tuiCategoryModelUrlResolver;

   @Resource
   private AttractionUrlResolver attractionUrlResolver;

   @Resource
   private FeatureService featureService;

   @Resource
   private LocationService tuiLocationService;

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private LocationMediaService locationMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   private static final TUILogUtils LOG = new TUILogUtils("LocationBasicPopulator");

   @Override
   public void populate(final LocationModel sourceModel, final LocationData targetData)
      throws ConversionException
   {

      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");

      targetData.setName(sourceModel.getName());
      targetData.setCode(sourceModel.getCode());
      targetData.setUrl(tuiCategoryModelUrlResolver.resolve(sourceModel));
      targetData.setLocationType(sourceModel.getType());
      targetData.setLocationAccomTypes(tuiLocationService.getLocationAccomTypes(sourceModel
         .getCode()));
      LocationModel locationPage = new LocationModel();
      final String brand = brandUtils.getFeatureServiceBrand(sourceModel.getBrands());
      targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
         Arrays.asList(new String[] { "name", "strapline", "intro1Title", "intro2Title",
            "intro3Title", "intro4Title", "intro5Title", "intro1Body", "intro2Body", "intro3Body",
            "intro4Body", "intro5Body" }), sourceModel, new Date(), brand));
      final List<CategoryModel> superCategoryModels =
         new ArrayList<CategoryModel>(sourceModel.getSupercategories());
      for (final CategoryModel categoryModel : superCategoryModels)
      {
         if ("Location".equalsIgnoreCase(categoryModel.getItemtype()))
         {
            locationPage = (LocationModel) categoryModel;
         }
      }

      final Map<String, List<Object>> introTextLinkMap =
         introTextLinkPopulator(sourceModel, targetData, locationPage);
      if (introTextLinkMap != null)
      {
         targetData.putFeatureCodesAndValues(introTextLinkMap);
      }

      final Collection<MediaContainerModel> mediaContainers = sourceModel.getGalleryImages();
      if (mediaContainers != null && !mediaContainers.isEmpty())
      {
         final List<ImageData> imageDataList = new ArrayList<ImageData>();
         locationImageDataPopulator.populate(mediaContainers, imageDataList);
         targetData.setImages(imageDataList);
      }

      final List<MediaViewData> mediaList = new ArrayList<MediaViewData>();
      mediaPopulatorLite.populate(locationMediaService.getImageMedias(sourceModel), mediaList);

      targetData.getGalleryImages().addAll(mediaList);
   }

   /**
    *
    * This method converts the intro text of the Editorial Component to into linkable intro text ,
    * by fetching source model and adding to the targetmodel.
    *
    * @param sourceModel the model where intro text is fetched.
    * @param targetDataParam the model where link-able intro text is added.
    * @return Map of link-able into text.
    */
   public Map<String, List<Object>> introTextLinkPopulator(final LocationModel sourceModel,
      final LocationData targetDataParam, final LocationModel locationPage)
      throws ConversionException
   {
      LocationData targetData = targetDataParam;
      Map<String, List<Object>> changedIntroMap = null;
      try
      {

         final List<Object> introTextList = targetData.getFeatureCodesAndValues().get("intro1Body");
         String introText = "";
         if (introTextList != null && !introTextList.isEmpty())
         {

            introText = introTextList.get(0).toString();
         }

         List<String> introList = null;
         changedIntroMap = new HashMap<String, List<Object>>();
         // fetches all the super-categories of the source model and generates location link Map.
         final List<CategoryModel> superCategoryModels =
            new ArrayList<CategoryModel>(sourceModel.getAllSupercategories());
         // code to check for continents

         targetData =
            getLinkForLocation(superCategoryModels, introText, locationPage, sourceModel,
               targetData);

         final List<AttractionModel> attractionModels =
            new ArrayList<AttractionModel>(sourceModel.getAttractions());
         for (final AttractionModel attractionModel : attractionModels)
         {
            if (StringUtils.containsIgnoreCase(introText, attractionModel.getName()))
            {
               targetData.getLocationLink().put(attractionModel.getName(),
                  attractionUrlResolver.resolve(attractionModel));
            }

         }
         final Map<String, String> locationMap = targetData.getLocationLink();
         introList = getIntroTextList(locationMap, introText);
         final Object[] introArray = introList.toArray(new String[0]);
         final List<Object> changedIntroTextList = Arrays.asList(introArray);
         changedIntroMap.put("intro1BodyLink", changedIntroTextList);
      }
      catch (final Exception e)
      {

         LOG.error("does not populate introtext", e);
      }

      return changedIntroMap;
   }

   /**
    * @param map
    * @param text
    * @return Intro-link list
    *
    *         This method returns the list of linkable text.
    */

   private List<String> getIntroTextList(final Map<String, String> map, final String text)
   {
      final Map<String, String> locationMap = map;
      final String introText = text;
      final List<String> introList = new ArrayList<String>();
      if (locationMap.isEmpty())
      {
         introList.add(introText);
      }
      else
      {
         String replacedIntroText = introText;
         for (final Entry<String, String> entry : locationMap.entrySet())
         {
            final String value = entry.getValue();
            final String nameLink = entry.getKey();
            if (StringUtils.containsIgnoreCase(replacedIntroText, nameLink + " "))
            {
               final String name = "<a href=" + value + ">" + nameLink + "</a>";
               replacedIntroText = replacedIntroText.replaceFirst(nameLink + " ", name + " ");
               continue;
            }
            if (StringUtils.containsIgnoreCase(replacedIntroText, nameLink + "."))
            {
               final String name = "<a href=" + value + ">" + nameLink + "</a>";
               replacedIntroText = replacedIntroText.replaceFirst(nameLink + ".", name + ".");
               continue;
            }
            if (StringUtils.containsIgnoreCase(replacedIntroText, nameLink + ","))
            {
               final String name = "<a href=" + value + ">" + nameLink + "</a>";
               replacedIntroText = replacedIntroText.replaceFirst(nameLink + ",", name + ",");
               continue;
            }
         }

         introList.add(replacedIntroText);
      }
      return introList;
   }

   /**
    * @param superCategoryModel
    * @param text
    * @param locationModels
    * @param source
    * @param target
    * @return LocationData
    *
    *         This method returns the target with intro-text links.
    */
   private LocationData getLinkForLocation(final List<CategoryModel> superCategoryModel,
      final String text, final LocationModel locationModels, final LocationModel source,
      final LocationData target)
   {

      final List<CategoryModel> superCategoryModels = superCategoryModel;
      final String introText = text;
      final LocationModel locationPage = locationModels;
      final LocationModel sourceModel = source;
      final LocationData targetData = target;
      for (final CategoryModel category : superCategoryModels)
      {
         if ((!("Category".toString().equalsIgnoreCase(category.getItemtype())))
            && (!("InventoryGroup".equalsIgnoreCase(category.getItemtype().toString()))))
         {
            final LocationModel locationModel = (LocationModel) category;
            if ("Country".equalsIgnoreCase(locationModel.getType().toString()))
            {
               final List<CategoryModel> categoryModels =
                  new ArrayList<CategoryModel>(locationModel.getAllSubcategories());
               categoryModels.add(category);
               for (final CategoryModel categorys : categoryModels)
               {
                  if ((!("Category".equalsIgnoreCase(categorys.getItemtype().toString())))
                     && (!("InventoryGroup".equalsIgnoreCase(categorys.getItemtype().toString())))
                     && StringUtils.containsIgnoreCase(introText, categorys.getName()))
                  {
                     final LocationModel location = (LocationModel) categorys;
                     if ("Continent".equalsIgnoreCase(location.getType().toString())
                        || location.getName().equalsIgnoreCase(locationPage.getName())
                        || location.getName().equalsIgnoreCase(sourceModel.getName()))
                     {
                        continue;
                     }
                     targetData.getLocationLink().put(location.getName(),
                        tuiCategoryModelUrlResolver.resolve(location));

                  }
               }
            }
         }
      }
      return targetData;
   }
}
