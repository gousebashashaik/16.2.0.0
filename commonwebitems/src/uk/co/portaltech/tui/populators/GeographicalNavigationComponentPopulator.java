/**
 *
 */
package uk.co.portaltech.tui.populators;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.endsWithIgnoreCase;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.components.model.GeographicalNavigationComponentModel;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.web.view.data.GeographicalNavigationComponentViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.TUICategoryData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author gagan
 * 
 */
public class GeographicalNavigationComponentPopulator implements
   Populator<GeographicalNavigationComponentModel, GeographicalNavigationComponentViewData>
{

   private static final TUILogUtils LOG = new TUILogUtils(
      "GeographicalNavigationComponentPopulator");

   @Resource
   private CategoryService categoryService;

   @Resource
   private LocationService tuiLocationService;

   @Resource
   private SessionService sessionService;

   @Resource
   private Converter<CategoryModel, TUICategoryData> tuiCategoryDataConverter;

   @Resource
   private LocationConverter locationConverter;

   @Resource
   private ConfigurablePopulator<LocationModel, LocationData, LocationOption> defaultLocationConfiguredPopulator;

   private Populator<CategoryModel, TUICategoryData> tuiCategoryDataPopulator;

   private RecursivePopulator<CategoryModel, TUICategoryData> tuiCategoryDataPopulatorWithDepth;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(
      final GeographicalNavigationComponentModel geographicalNavigationComponentModel,
      final GeographicalNavigationComponentViewData geographicalNavigationComponentViewData)
      throws ConversionException
   {
      final Collection<String> locationCategoryCodes =
         geographicalNavigationComponentModel.getCategoryCodes();
      final Integer navigationLevelDepth =
         geographicalNavigationComponentModel.getNavigationLevelDepth();
      if (navigationLevelDepth != null && navigationLevelDepth.intValue() > 0)
      {
         final List<TUICategoryData> locationTUICategoryDataList = new ArrayList<TUICategoryData>();
         final List<LocationModel> locationModels = new ArrayList<LocationModel>();
         final BrandDetails brandDetails = sessionService.getAttribute("brandDetails");
         final List<String> relevantBrands = brandDetails.getRelevantBrands();
         CategoryModel catModel = null;
         for (final String locationCategoryCode : locationCategoryCodes)
         {

            try
            {
               catModel = categoryService.getCategoryForCode(locationCategoryCode);
            }
            catch (final UnknownIdentifierException ex)
            {
               LOG.warn("Category with code '" + locationCategoryCode + "' not found", ex);
               continue;
            }
            if (catModel instanceof LocationModel)
            {
               locationModels.add((LocationModel) catModel);
            }
         }

         final List<LocationModel> filteredLocations =
            tuiLocationService.getLocationsFilteredByBrand(locationModels, relevantBrands);

         for (final CategoryModel location : filteredLocations)
         {
            final int ctr = 0;

            filterLocationAndGetValidSublocations(location, navigationLevelDepth, ctr);
            TUICategoryData detailedLocationCategoryData = null;
            if (location != null)
            {
               detailedLocationCategoryData = tuiCategoryDataConverter.convert(location);
               tuiCategoryDataPopulatorWithDepth.populate(location, detailedLocationCategoryData,
                  0, navigationLevelDepth);
            }
            locationTUICategoryDataList.add(detailedLocationCategoryData);
         }
         if (!locationTUICategoryDataList.isEmpty())
         {
            geographicalNavigationComponentViewData.setLocationName(locationTUICategoryDataList
               .get(0).getName());
            geographicalNavigationComponentViewData.setCategories(locationTUICategoryDataList);
         }

      }
      geographicalNavigationComponentViewData.setNavigationLevelDepth(navigationLevelDepth);
      geographicalNavigationComponentViewData.setLocationType(geographicalNavigationComponentModel
         .getLocationType());
   }

   /**
    * @return the tuiCategoryDataPopulator
    */
   public Populator<CategoryModel, TUICategoryData> getTuiCategoryDataPopulator()
   {
      return tuiCategoryDataPopulator;
   }

   /**
    * @param tuiCategoryDataPopulator the tuiCategoryDataPopulator to set
    */
   public void setTuiCategoryDataPopulator(
      final Populator<CategoryModel, TUICategoryData> tuiCategoryDataPopulator)
   {
      this.tuiCategoryDataPopulator = tuiCategoryDataPopulator;
   }

   /**
    * @param detailedLocationcategoryModel
    * @param navigationLevelDepth
    * @param ctrs
    */
   private void filterLocationAndGetValidSublocations(
      final CategoryModel detailedLocationcategoryModel, final Integer navigationLevelDepth,
      final int ctrs)
   {
      int ctr = ctrs;
      final BrandDetails brandDetails = sessionService.getAttribute("brandDetails");
      if (ctr < navigationLevelDepth.intValue())
      {
         final List<CategoryModel> subCategories =
            checkLocationOfCRBrand(brandDetails, detailedLocationcategoryModel.getCategories());

         if (isNotEmpty(subCategories))
         {
            final List<CategoryModel> validSubLocationListAfterFilter =
               new ArrayList<CategoryModel>();
            detailedLocationcategoryModel.setCategories(validSubLocationListAfterFilter);
            ++ctr;
            for (final CategoryModel subCategoryModel : subCategories)
            {
               validSubLocationListAfterFilter.add(subCategoryModel);
               filterLocationAndGetValidSublocations(subCategoryModel, navigationLevelDepth, ctr);
            }
         }
         else
         {
            detailedLocationcategoryModel.setCategories(null);
         }
      }
   }

   /**
    * @param brandDetails
    * @param subCategories
    * @return
    */
   private List<CategoryModel> checkLocationOfCRBrand(final BrandDetails brandDetails,
      final List<CategoryModel> subCategories)
   {
      final List<CategoryModel> cruiseCategories = subCategories;

      if (endsWithIgnoreCase(brandDetails.getSiteUid(), BrandType.CR.toString()))
      {
         final List<CategoryModel> cruiseCategoriesList = new ArrayList<CategoryModel>();
         for (final CategoryModel cruiseCategoryModel : cruiseCategories)
         {
            checkCRBrandForLocation(cruiseCategoriesList, cruiseCategoryModel);
         }
         return cruiseCategoriesList;
      }
      return cruiseCategories;
   }

   /**
    * @param cruiseCategoriesList
    * @param cruiseCategoryModel
    */
   private void checkCRBrandForLocation(final List<CategoryModel> cruiseCategoriesList,
      final CategoryModel cruiseCategoryModel)
   {

      if (!BrandUtils.isValidBrandCode(cruiseCategoryModel.getBrands(), BrandType.CR.toString()))
      {
         final LocationModel locationModel = (LocationModel) cruiseCategoryModel;
         final LocationData locationData = locationConverter.convert(locationModel);
         defaultLocationConfiguredPopulator.populate(locationModel, locationData,
            Arrays.asList(LocationOption.SUBCATEGORIES));
         for (final CategoryData categoryModel : locationData.getSubLocations())
         {
            final CategoryModel location =
               categoryService.getCategoryForCode(categoryModel.getCode());
            if (BrandUtils.isValidBrandCode(location.getBrands(), BrandType.CR.toString()))
            {
               cruiseCategoriesList.add(location);
            }
         }
      }
      else
      {
         cruiseCategoriesList.add(cruiseCategoryModel);
      }
   }

   /**
    * @param tuiCategoryDataPopulatorWithDepth the tuiCategoryDataPopulatorWithDepth to set
    */
   public void setTuiCategoryDataPopulatorWithDepth(
      final RecursivePopulator<CategoryModel, TUICategoryData> tuiCategoryDataPopulatorWithDepth)
   {
      this.tuiCategoryDataPopulatorWithDepth = tuiCategoryDataPopulatorWithDepth;
   }
}
