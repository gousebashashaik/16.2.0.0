/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.components.model.GeographicalNavigationComponentModel;
import uk.co.portaltech.tui.converters.AccommodationOption;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.GeographicalNavigationComponentFacade;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.GeographicalNavigationComponentViewData;
import uk.co.portaltech.tui.web.view.data.TUICategoryData;
import uk.co.tui.async.logging.TUILogUtils;

import com.googlecode.ehcache.annotations.Cacheable;

/**
 * @author gagan
 *
 */
public class DefaultGeographicalNavigationComponentFacade implements
   GeographicalNavigationComponentFacade
{

   private static final TUILogUtils LOG = new TUILogUtils(
      "DefaultGeographicalNavigationComponentFacade");

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private Converter<GeographicalNavigationComponentModel, GeographicalNavigationComponentViewData> geographicalNavigationComponentConverter;

   @Resource
   private Populator<GeographicalNavigationComponentModel, GeographicalNavigationComponentViewData> geographicalNavigationComponentPopulator;

   @Resource
   private AccommodationFacade accomodationFacade;

   @Resource
   private CategoryService categoryService;

   @Resource
   private TUIUrlResolver<LocationModel> tuiCategoryModelUrlResolver;

   /*
    * (non-Javadoc)
    *
    * @see
    * uk.co.portaltech.tui.facades.GeographicalNavigationComponentFacade#getGeographicalNavigationData
    * (java.lang.String, java.lang.String)
    */
   @Override
   @Cacheable(cacheName = "geoNavCache")
   public GeographicalNavigationComponentViewData getGeographicalNavigationData(
      final String componentUID, final String categoryCode, final String seoPage, final String brand)
   {
      LocationModel location = null;
      final GeographicalNavigationComponentModel geographicalNavigationComponentModel =
         getGeographicalNavigationComponent(componentUID);
      final GeographicalNavigationComponentViewData geographicalNavigationComponentViewData =
         geographicalNavigationComponentConverter.convert(geographicalNavigationComponentModel);

      setLocationCategoryIfNotExists(geographicalNavigationComponentModel, categoryCode);
      if (StringUtils.isNotEmpty(categoryCode) && !"destinationlanding".equals(categoryCode))
      {
         location = (LocationModel) categoryService.getCategoryForCode(categoryCode);
      }

      geographicalNavigationComponentPopulator.populate(geographicalNavigationComponentModel,
         geographicalNavigationComponentViewData);

      if (categoriesCheck(geographicalNavigationComponentViewData)
         || (seoPageCheck(seoPage) && location != null && LocationType.RESORT.equals(location
            .getType())))
      {
         geographicalNavigationComponentViewData.setLocationType(location.getType());
         final Collection<String> categoryCodes =
            geographicalNavigationComponentModel.getCategoryCodes();
         if (!categoryCodes.isEmpty() && categoryCodes.iterator() != null)
         {
            geographicalNavigationComponentViewData.setAccommodations(accomodationFacade
               .getTHAccommodations(categoryCodes.iterator().next(), -1,
                  Arrays.asList(AccommodationOption.BASIC, AccommodationOption.PRODUCTRANGE)));
         }
         else
         {
            geographicalNavigationComponentViewData.setAccommodations(Collections.<String,List<AccommodationViewData>>emptyMap());
         }
      }

      tuiCategoryModelUrlResolver.setOverrideSubPageType("places-to-go");
      final String url = tuiCategoryModelUrlResolver.resolve(location);
      geographicalNavigationComponentViewData.setPlacesToGoUrl(url);

      final Map<String, List<TUICategoryData>> catMapByAccom =
         new HashMap<String, List<TUICategoryData>>();
      catMapByAccom.put("VILLA", new ArrayList<TUICategoryData>());
      catMapByAccom.put("HOTEL", new ArrayList<TUICategoryData>());
      if (geographicalNavigationComponentViewData.getCategories() != null)
      {
         createCategoryMapByAccomType(geographicalNavigationComponentViewData.getCategories()
            .get(0).getCategories(), catMapByAccom);
      }
      geographicalNavigationComponentViewData.setCategoriesByAccomType(catMapByAccom);

      return geographicalNavigationComponentViewData;
   }

   /**
    * @param seoPage
    * @return
    */
   private boolean seoPageCheck(final String seoPage)
   {
      return seoPage != null && !"none".equals(seoPage);
   }

   /**
    * @param geographicalNavigationComponentViewData
    * @return
    */
   private boolean categoriesCheck(
      final GeographicalNavigationComponentViewData geographicalNavigationComponentViewData)
   {
      return geographicalNavigationComponentViewData.getCategories() != null
         && geographicalNavigationComponentViewData.getCategories().get(0).getCategories() == null;
   }

   public void createCategoryMapByAccomType(final List<TUICategoryData> tuiCategories,
      final Map<String, List<TUICategoryData>> catMapByAccom)
   {
      final List<TUICategoryData> villaCategoryData = catMapByAccom.get("VILLA");
      final List<TUICategoryData> hotelCategoryData = catMapByAccom.get("HOTEL");

      if (tuiCategories != null && !tuiCategories.isEmpty())
      {
         for (final TUICategoryData category : tuiCategories)
         {
            if (category.getLocationAccomTypes() != null)
            {
               if (category.getLocationAccomTypes().contains(AccommodationType.VILLA.getCode()))
               {
                  villaCategoryData.add(category);
               }
               // hotel is compared as location accom type population happens in a way : if villa
               // then villa else all considered as hotel.
               if (category.getLocationAccomTypes().contains(AccommodationType.HOTEL.getCode()))
               {
                  hotelCategoryData.add(category);
               }
            }

         }
      }
   }

   /**
    * @param geographicalNavigationComponentModel
    * @param categoryCode
    */
   private void setLocationCategoryIfNotExists(
      final GeographicalNavigationComponentModel geographicalNavigationComponentModel,
      final String categoryCode)
   {
      final Collection<String> locationCategoryCodes =
         geographicalNavigationComponentModel.getCategoryCodes();
      if (locationCategoryCodes == null || locationCategoryCodes.isEmpty())
      {
         final List<String> locationCategoryCodeList = new ArrayList<String>();
         locationCategoryCodeList.add(categoryCode);
         geographicalNavigationComponentModel.setCategoryCodes(locationCategoryCodeList);
      }
   }

   private GeographicalNavigationComponentModel getGeographicalNavigationComponent(
      final String componentUID)
   {
      GeographicalNavigationComponentModel geographicalNavigationComponentModel = null;
      try
      {
         geographicalNavigationComponentModel =
            (GeographicalNavigationComponentModel) cmsComponentService
               .getSimpleCMSComponent(componentUID);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOG.info("No CMS Item found for component id : " + componentUID, e);
      }
      return geographicalNavigationComponentModel;
   }

}
