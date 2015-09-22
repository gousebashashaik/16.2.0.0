/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.data.impl.DefaultCMSDataFactory;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.DestinationLandingModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.components.model.ABTestComponentModel;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.SearchResultsSystemException;

/**
 * This class handles special Travel pages based on certain extra conditions and returns the correct
 * page for a category or product.
 *
 * @author James
 */
public class TuiPageService
{
   private static final String ALL_IN_ONE = "allinonepackage";

   private static final String PAGE_TYPE = "pageType";

   private static final String PRODUCT_RANGE = "productRange";

   // Autowire in our services.
   @Resource
   private FlexibleSearchService flexibleSearchService;

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private CatalogVersionService catalogVersionService;

   @Resource
   private CMSRestrictionService cmsRestrictionService;

   @Resource
   private FeatureService featureService;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private BrandUtils brandUtils;

   // Load the logger.

   private static final TUILogUtils LOG = new TUILogUtils("TuiPageService");

   private static final String ACTIVE_CATALOG_VERSION = "activeCatalogVersions";

   private static final String ERRORCODE = "1014";

   private static final String CRUISE_AND_STAY = "nilecruiseandstay";

   private static final String CRUISE_ONLY = "nilecruise";

   private static final String TOUR_AND_STAY = "tourandstay";

   private static final String GULET_CRUISE = "guletcruise";

   private static final String TWIN_CENTRE = "twinmulticentre";

   private static final String COUNTRY = "country";

   private static final String LAPLAND = "lapland";

   private static final String UNDERSCORE = "_";

   private static final String HYPEN = "-";

   private static final String HOLIDAYS = "HOLIDAYS";

   private static final String RETURNING = "Returning ";

   private static final String RETURNING_FIRST_PAGE = ". returning first page";

   private static final String OVERVIEW = "OVERVIEW";

   private static final String SAFARI_AND_STAY = "safariandstay";

   // formatter:off
   private static final String TRAVEL_CATEGORY_PAGE_FOR_PAGETYPE_AND_CATEGORYTYPE =
      "SELECT {pk} FROM {LocationPage AS lp "
         + "JOIN LocationPageType AS cpt ON {lp.pagetype} = {cpt.pk} "
         + "JOIN CategoryType as ct ON {lp.categorytype} = {ct.pk}} "
         + "WHERE {ct.code} = ?categoryType " + "AND {cpt.code} = ?pageType AND ";

   private static final String PRODUCT_RANGE_PAGE_FOR_PAGETYPE_AND_CATEGORYTYPE =
      "SELECT {pk} FROM {ProductRangePage AS prp "
         + "LEFT JOIN ProductRangePageType as prpt ON {prp.pageType} = {prpt.pk}} WHERE {prpt.code} = ?pageType AND {prp.uid} = ?productRange AND";

   private static final String PRODUCT_RANGE_CATEGORY_PAGE_FOR_PAGETYPE_AND_CATEGORYTYPE =
      "SELECT {pk} FROM {ProductRangeCategoryPage AS prcp "
         + "LEFT JOIN ProductRangeCategoryPageType AS prcpt ON {prcp.pageType} = {prcpt.pk}} WHERE {prcpt.code} = ?pageType AND ";

   private static final String ACCOMMODATION_PAGE_FOR_PAGE_TYPE =
      "SELECT {pk} FROM {AccommodationPage AS accmpage "
         + "LEFT JOIN AccommodationPageType as accmPageType ON {accmpage.pageType} = {accmPageType.pk}} WHERE {accmPageType.code} = ?pageType AND {catalogVersion} IN (?activeCatalogVersions)";

   private static final String BOOKFLOW_ACCOMMODATION_PAGE_FOR_PAGE_TYPE =
      "SELECT {pk} FROM {BookFlowAccommodationPage AS accmpage "
         + "LEFT JOIN AccommodationPageType as accmPageType ON {accmpage.pageType} = {accmPageType.pk}} WHERE {accmPageType.code} = ?pageType AND {catalogVersion} IN (?activeCatalogVersions)";

   private static final String PAGE_FOR_ATTRACTION_OR_EXCURSION =
      "SELECT {pk} FROM {AttractionPage} " + "WHERE {catalogVersion} IN (?activeCatalogVersions)";

   private static final String BOOKFLOW_PAGE_FOR_ATTRACTION_OR_EXCURSION =
      "SELECT {pk} FROM {BookFlowAttractionPage} "
         + "WHERE {catalogVersion} IN (?activeCatalogVersions)";

   private static final String DESTINATION_LANDING_CATEGORY_PAGE_FOR_PAGETYPE_AND_CATEGORYTYPE =
      "SELECT {pk} FROM {DestinationLandingPage AS dlp "
         + "LEFT JOIN DestinationLandingPageType as dlpt ON {dlp.pageType} = {dlpt.pk}} WHERE {dlpt.code} = ?pageType AND ";

   // formatter:on

   /**
    * Performs a Flexible Search based on location and pageType and calls evaluation of restrictions
    * on any pages found in order to return the correct page for the location and page type. The
    * page will normally either be a default page for that location and type of page, or a custom
    * one that has been defined using a restriction just for that location.
    *
    * @param location The Location object that we are finding the category page for.
    * @param pageTypeParam The page type that we are finding the category page for.
    * @return An AbstractPageModel representing the category page matching the parameters.
    * @throws CMSItemNotFoundException When no pages are found.
    */
   public AbstractPageModel getLocationPageForPageType(final LocationModel location,
      final String subPageTypeParam) throws CMSItemNotFoundException
   {
      final Map<String, Object> params = new HashMap<String, Object>();

      params.put("categoryType", location.getType().toString());

      final String subPageType = getSubPageType(location, subPageTypeParam);

      params.put(PAGE_TYPE, cleansePageSubType(subPageType));
      LOG.debug("Getting category page for " + location.getType() + " "
         + cleansePageSubType(subPageType));
      final String queryString =
         TRAVEL_CATEGORY_PAGE_FOR_PAGETYPE_AND_CATEGORYTYPE
            + FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
               "{catalogVersion} IN (?activeCatalogVersions)", ACTIVE_CATALOG_VERSION, "OR",
               catalogVersionService.getSessionCatalogVersions(), params);

      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
      final SearchResult<AbstractPageModel> result =
         flexibleSearchService.<AbstractPageModel> search(query);
      final List<AbstractPageModel> pages = result.getResult();
      final DefaultCMSDataFactory cmsDataFactory = new DefaultCMSDataFactory();
      final RestrictionData data = cmsDataFactory.createRestrictionData(location);
      final Collection<AbstractPageModel> results =
         cmsRestrictionService.evaluatePages(pages, data);

      if (results.size() > 1)
      {
         LOG.warn("More than one page found matching Category Type " + location.getType()
            + " and PageType " + cleansePageSubType(subPageType)
            + " with restrictions. Returning first available page.");
      }
      // If our restriction service returns no results:
      if (results.isEmpty())
      {
         // Check that we have a page to display at all:
         if (pages.isEmpty())
         {
            throw new CMSItemNotFoundException("");
         }
         else
         {
            // If we have more than one before restrictions were applied,
            // warn:
            if (pages.size() > 1)
            {
               LOG.debug("More than one page found matching Category Type " + location.getType()
                  + " and PageType " + cleansePageSubType(subPageType)
                  + ". Searching for first page with no restrictions...");
               for (final AbstractPageModel page : pages)
               {
                  if (page.getRestrictions().isEmpty())
                  {
                     LOG.debug(RETURNING + page.getName());
                     return page;
                  }
               }
               LOG.warn("All found pages for Category Type " + location.getType()
                  + " and PageType " + cleansePageSubType(subPageType)
                  + " seem to have restrictions. Returning first page.");
               LOG.debug(RETURNING + pages.get(0).getName());
               return pages.get(0);
            }
            // Return the first page that was retrieved before restrictions
            // were applied.
            else
            {
               LOG.debug(RETURNING + pages.get(0).getName());
               return pages.get(0);
            }
         }
      }
      else
      {
         // Return the first result from the restrictions
         LOG.debug(RETURNING + results.iterator().next().getName());
         return results.iterator().next();
      }
   }

   private String getSubPageType(final LocationModel location, final String subPageTypeParam)
   {
      String subPageType = subPageTypeParam;
      /* separate page for lapland location */
      if (LAPLAND.equalsIgnoreCase(location.getName()))
      {
         final StringBuilder sb = new StringBuilder();

         if (!subPageType.toUpperCase().contains(HOLIDAYS))
         {
            if (subPageType.contains(HYPEN))
            {
               subPageType = subPageType.replaceAll(HYPEN, "");
            }

            sb.append(COUNTRY).append(UNDERSCORE).append(subPageType).append(UNDERSCORE)
               .append(LAPLAND);
         }
         else
         {
            sb.append(subPageType);
         }

         return sb.toString().toUpperCase();

      }
      else if ("resort".equalsIgnoreCase(String.valueOf(location.getType()))
         && OVERVIEW.equalsIgnoreCase(subPageType))
      {
         for (final CategoryModel locat : location.getSupercategories())
         {
            if (LAPLAND.equalsIgnoreCase(locat.getName()))
            {
               return "RESORT_OVERVIEW_LAPLAND";
            }
         }
      }

      return subPageType;
   }

   /**
    * Gets the matching page for the categoryModel provided.<br/>
    * It automatically checks the of which class is the actual Category passes. (ProductRange,
    * ProductRangeCategory, DestinationLanding)
    *
    * @param categoryModel
    * @param subPageType
    */
   public AbstractPageModel getCategoryPageForPageType(final CategoryModel categoryModel,
      final String subPageType) throws CMSItemNotFoundException
   {
      final Map<String, Object> params = new HashMap<String, Object>();

      params.put(PAGE_TYPE, cleansePageSubType(subPageType));
      String queryString = StringUtils.EMPTY;
      if (categoryModel instanceof ProductRangeModel)
      {
         queryString = PRODUCT_RANGE_PAGE_FOR_PAGETYPE_AND_CATEGORYTYPE;
         params.put(PRODUCT_RANGE, categoryModel.getCode());
      }
      else if (categoryModel instanceof ProductRangeCategoryModel)
      {
         queryString = PRODUCT_RANGE_CATEGORY_PAGE_FOR_PAGETYPE_AND_CATEGORYTYPE;
      }
      else if (categoryModel instanceof DestinationLandingModel)
      {
         queryString = DESTINATION_LANDING_CATEGORY_PAGE_FOR_PAGETYPE_AND_CATEGORYTYPE;
      }

      queryString +=
         FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
            "{catalogVersion} IN (?activeCatalogVersions)", ACTIVE_CATALOG_VERSION, "OR",
            catalogVersionService.getSessionCatalogVersions(), params);

      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
      final SearchResult<AbstractPageModel> result =
         flexibleSearchService.<AbstractPageModel> search(query);
      final List<AbstractPageModel> pages = result.getResult();
      // Check that we have a page to display at all:
      if (pages.isEmpty())
      {
         throw new CMSItemNotFoundException("");
      }
      else
      {
         // If we have more than one before restrictions were applied, warn:
         if (pages.size() > 1)
         {
            LOG.debug("More than one page found matching Category '" + categoryModel.getName()
               + "' and PageType " + cleansePageSubType(subPageType) + RETURNING_FIRST_PAGE);
            return pages.get(0);
         }
         // Return the first page that was retrieved before restrictions were
         // applied.
         else
         {
            LOG.debug(RETURNING + pages.get(0).getName());
            return pages.get(0);
         }
      }

   }

   public ProductPageModel getProductPageForPageType(final String pageTypeCode)
      throws CMSItemNotFoundException
   {
      final Map<String, Object> params = new HashMap<String, Object>();
      params.put("uid", pageTypeCode);

      final String queryString =
         "SELECT{PK} FROM {ProductPage} WHERE {uid}=?uid AND "
            + FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
               "{catalogVersion} in (?activeCatalogVersions)", ACTIVE_CATALOG_VERSION, "OR",
               catalogVersionService.getSessionCatalogVersions(), params);
      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);

      final SearchResult<ProductPageModel> result =
         flexibleSearchService.<ProductPageModel> search(query);

      final List<ProductPageModel> results = result.getResult();
      if (results.size() > 1)
      {
         LOG.warn("More than one page found. Returning default.");
      }
      if (results.isEmpty())
      {
         throw new CMSItemNotFoundException("");
      }
      else
      {
         return results.get(0);
      }
   }

   /**
    *
    * @param subPageTypeParam
    * @param accommodationModel
    * @return subPageType
    * @description This method resolves sub page type for an accommodation using the type of
    *              accommodation or holidaytype for that accommodation.
    */
   private String getSubPageTypeForDiffrentAccommTypes(final String subPageTypeParam,
      final AccommodationModel accommodationModel)
   {
      String subPageType = subPageTypeParam;

      if ("VILLA".equals(accommodationModel.getType().toString()))
      {
         return subPageType.concat("_VILLA");
      }

      // Temporary fix done for FC and both mobile websites (TH & FC).
      if (StringUtils.equalsIgnoreCase(tuiUtilityService.getSiteBrand(), "FC")
         || viewSelector.checkIsMobile())
      {
         return subPageType;
      }

      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { "holiday_type" }));
      final Map<String, List<Object>> featureMap =
         featureService.getValuesForFeatures(featureDescriptorList, accommodationModel, new Date(),
            brandUtils.getFeatureServiceBrand(accommodationModel.getBrands()));
      final String holidayType =
         featureMap.values().iterator().next().toString().toLowerCase().replaceAll("[^a-z]", "");

      if (StringUtils.isNotEmpty(holidayType))
      {
         if ("DayTrip".equalsIgnoreCase(holidayType))
         {
            subPageType = subPageType.concat("_LAPLAND");
         }
         else if (CRUISE_ONLY.equalsIgnoreCase(holidayType)
            && OVERVIEW.equalsIgnoreCase(subPageType))
         {
            subPageType = "CRUISE_ONLY";
         }
         else if (GULET_CRUISE.equalsIgnoreCase(holidayType)
            && OVERVIEW.equalsIgnoreCase(subPageType))
         {
            subPageType = "GULET_CRUISE";
         }
         else if (CRUISE_AND_STAY.equalsIgnoreCase(holidayType))
         {
            subPageType = "CRUISE_AND_STAY_".concat(subPageType);
         }
         else if (SAFARI_AND_STAY.equalsIgnoreCase(holidayType))
         {
            subPageType = "SAFARI_AND_STAY_".concat(subPageType);
         }
         else if (TOUR_AND_STAY.equalsIgnoreCase(holidayType))
         {
            subPageType = "TOUR_AND_STAY_".concat(subPageType);
         }
         else if (TWIN_CENTRE.equalsIgnoreCase(holidayType))
         {
            if (StringUtils.contains(subPageType.toUpperCase(), "HOTEL_"))
            {
               subPageType = "TWIN_CENTRE_".concat(subPageType);
            }
            else
            {
               subPageType = "TWIN_CENTRE_HOTEL_".concat(subPageType);
            }
         }
         else if (ALL_IN_ONE.equalsIgnoreCase(holidayType)
            && OVERVIEW.equalsIgnoreCase(subPageType))
         {
            subPageType = "ALL_IN_ONE_ACCOM_OVERVIEW";
         }
      }
      else if (laplandAccommodation(accommodationModel) && OVERVIEW.equalsIgnoreCase(subPageType))
      {
         subPageType = "LAPLAND_ACCOM_OVERVIEW";
      }

      return subPageType;
   }

   /**
    * @param accommodationModel
    * @return boolean
    */
   private boolean laplandAccommodation(final AccommodationModel accommodationModel)
   {
      LocationModel locationModel = null;
      for (final CategoryModel category : accommodationModel.getSupercategories())
      {
         if (category instanceof LocationModel)
         {
            locationModel = (LocationModel) category;
            for (final CategoryModel loc : locationModel.getAllSupercategories())
            {
               if (loc instanceof LocationModel)
               {
                  final LocationModel location = (LocationModel) loc;
                  if (LAPLAND.equalsIgnoreCase(location.getName()))
                  {
                     return true;
                  }
               }
            }
         }
      }
      return false;
   }

   public AbstractPageModel getPageForAccommodation(final AccommodationModel accommodationModel,
      final String subPageTypeParam) throws CMSItemNotFoundException
   {

      final Map<String, Object> params = new HashMap<String, Object>();
      String subPageType = subPageTypeParam;
      subPageType = getSubPageTypeForDiffrentAccommTypes(subPageType, accommodationModel);

      params.put(PAGE_TYPE, cleansePageSubType(subPageType));

      final String queryString =
         FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
            ACCOMMODATION_PAGE_FOR_PAGE_TYPE, ACTIVE_CATALOG_VERSION, "OR",
            catalogVersionService.getSessionCatalogVersions(), params);

      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
      final SearchResult<AbstractPageModel> result =
         flexibleSearchService.<AbstractPageModel> search(query);
      final List<AbstractPageModel> pages = result.getResult();
      // Check that we have a page to display at all:
      if (pages.isEmpty())
      {
         throw new CMSItemNotFoundException("");
      }
      else
      {
         // If we have more than one before restrictions were applied, warn:
         if (pages.size() > 1)
         {
            LOG.debug("More than one page found matching accommodation '"
               + accommodationModel.getName() + "' and PageType " + cleansePageSubType(subPageType)
               + RETURNING_FIRST_PAGE);
            return pages.get(0);
         }
         // Return the first page that was retrieved before restrictions were applied.
         else
         {
            LOG.debug(RETURNING + pages.get(0).getName());
            return pages.get(0);
         }
      }

   }

   public AbstractPageModel getPageForBookFlowAccommodation(
      final AccommodationModel accommodationModel, final String subPageTypeParam)
      throws SearchResultsSystemException
   {
      final Map<String, Object> params = new HashMap<String, Object>();
      String subPageType = subPageTypeParam;
      subPageType = getSubPageTypeForDiffrentAccommTypes(subPageType, accommodationModel);

      params.put(PAGE_TYPE, cleansePageSubType(subPageType));

      final String queryString =
         FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
            BOOKFLOW_ACCOMMODATION_PAGE_FOR_PAGE_TYPE, "activeCatalogVersions", "OR",
            catalogVersionService.getSessionCatalogVersions(), params);

      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
      final SearchResult<AbstractPageModel> result =
         flexibleSearchService.<AbstractPageModel> search(query);
      final List<AbstractPageModel> pages = result.getResult();
      // Check that we have a page to display at all:
      if (pages.isEmpty())
      {
         return null;
      }
      else
      {
         // If we have more than one before restrictions were applied, warn:
         if (pages.size() > 1)
         {
            LOG.debug("More than one page found matching accommodation '"
               + accommodationModel.getName() + "' and PageType " + cleansePageSubType(subPageType)
               + RETURNING_FIRST_PAGE);
            if (viewSelector.checkIsMobile())
            {
               for (final AbstractPageModel page : pages)
               {
                  if (page.getUid().contains("Mobile"))
                  {
                     return page;
                  }
               }
            }
            return pages.get(0);

         }
         // Return the first page that was retrieved before restrictions were
         // applied.
         else
         {
            LOG.debug(RETURNING + pages.get(0).getName());
            return pages.get(0);
         }
      }

   }

   public AbstractPageModel getPageForExcursion(final ExcursionModel excursionModel)
      throws SearchResultsSystemException
   {
      final Map<String, Object> params = new HashMap<String, Object>();

      final String queryString =
         FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
            PAGE_FOR_ATTRACTION_OR_EXCURSION, ACTIVE_CATALOG_VERSION, "OR",
            catalogVersionService.getSessionCatalogVersions(), params);

      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
      final SearchResult<AbstractPageModel> result =
         flexibleSearchService.<AbstractPageModel> search(query);
      final List<AbstractPageModel> pages = result.getResult();
      // Check that we have a page to display at all:
      if (pages.isEmpty())
      {
         throw new SearchResultsSystemException(ERRORCODE);
      }
      else
      {
         // If we have more than one before restrictions were applied, warn:
         if (pages.size() > 1)
         {
            LOG.debug("More than one page found matching excursion '" + excursionModel.getName()
               + "'. returning first page");
            return pages.get(0);
         }
         // Return the first page that was retrieved before restrictions were
         // applied.
         else
         {
            LOG.debug(RETURNING + pages.get(0).getName());
            return pages.get(0);
         }
      }

   }

   public AbstractPageModel getPageForAttraction(final ItemModel itemModel)
      throws SearchResultsSystemException
   {
      final Map<String, Object> params = new HashMap<String, Object>();

      final String queryString =
         FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
            PAGE_FOR_ATTRACTION_OR_EXCURSION, ACTIVE_CATALOG_VERSION, "OR",
            catalogVersionService.getSessionCatalogVersions(), params);

      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
      final SearchResult<AbstractPageModel> result =
         flexibleSearchService.<AbstractPageModel> search(query);
      final List<AbstractPageModel> pages = result.getResult();
      // Check that we have a page to display at all:
      if (pages.isEmpty())
      {
         throw new SearchResultsSystemException(ERRORCODE);
      }
      else
      {
         // If we have more than one before restrictions were applied, warn:
         if (pages.size() > 1)
         {
            String name = null;
            if (itemModel instanceof AttractionModel)
            {
               name = ((AttractionModel) itemModel).getName();
            }
            else if (itemModel instanceof ExcursionModel)
            {
               name = ((ExcursionModel) itemModel).getName();
            }
            LOG.debug("More than one page found matching attraction '" + name
               + "'. returning first page");
            return pages.get(0);
         }
         // Return the first page that was retrieved before restrictions were
         // applied.
         else
         {
            LOG.debug(RETURNING + pages.get(0).getName());
            return pages.get(0);
         }
      }

   }

   /**
    * Gets the matching catalog page for the give page Uid.<br/>
    *
    *
    * @param categoryModel
    * @param subPageType
    */
   public AbstractPageModel getPageForPageUID(final String pageUID) throws CMSItemNotFoundException
   {
      final Map<String, Object> params = new HashMap<String, Object>();

      params.put("pageUID", pageUID);

      final String queryString =
         "SELECT{PK} FROM {CatalogPage} WHERE {uid}=?pageUID AND "
            + FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
               "{catalogVersion} in (?activeCatalogVersions)", ACTIVE_CATALOG_VERSION, "OR",
               catalogVersionService.getSessionCatalogVersions(), params);
      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);

      final SearchResult<AbstractPageModel> result =
         flexibleSearchService.<AbstractPageModel> search(query);
      final List<AbstractPageModel> pages = result.getResult();
      // Check that we have a page to display at all:
      if (pages.isEmpty())
      {
         throw new CMSItemNotFoundException("");
      }
      else
      {
         // If we have more than one before restrictions were applied, warn:
         if (pages.size() > 1)
         {
            LOG.debug("More than one page found for page '" + pageUID + RETURNING_FIRST_PAGE);
            return pages.get(0);
         }
         // Return the first page that was retrieved before restrictions were applied.
         else
         {
            LOG.debug(RETURNING + pages.get(0).getName());
            return pages.get(0);
         }
      }

   }

   public AbstractPageModel getBookFlowPageForAttraction(final ItemModel itemModel)
      throws SearchResultsSystemException
   {
      final Map<String, Object> params = new HashMap<String, Object>();

      final String queryString =
         FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(
            BOOKFLOW_PAGE_FOR_ATTRACTION_OR_EXCURSION, ACTIVE_CATALOG_VERSION, "OR",
            catalogVersionService.getSessionCatalogVersions(), params);

      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
      final SearchResult<AbstractPageModel> result =
         flexibleSearchService.<AbstractPageModel> search(query);
      final List<AbstractPageModel> pages = result.getResult();
      // Check that we have a page to display at all:
      if (pages.isEmpty())
      {
         throw new SearchResultsSystemException(ERRORCODE);
      }
      else
      {
         // If we have more than one before restrictions were applied, warn:
         if (pages.size() > 1)
         {
            String name = null;
            if (itemModel instanceof AttractionModel)
            {
               name = ((AttractionModel) itemModel).getName();
            }
            else if (itemModel instanceof ExcursionModel)
            {
               name = ((ExcursionModel) itemModel).getName();
            }
            LOG.debug("More than one page found matching attraction '" + name
               + "'. returning first page");
            return pages.get(0);
         }
         // Return the first page that was retrieved before restrictions were
         // applied.
         else
         {
            LOG.debug(RETURNING + pages.get(0).getName());
            return pages.get(0);
         }
      }

   }

   private String cleansePageSubType(final String input)
   {

      return input.replaceAll("-", "").toUpperCase();
   }

   public boolean isControllerExist(final String controllerName)
   {
      return Registry.getApplicationContext().containsBean(controllerName);
   }

   /**
    * This method resolves ABTest name based on ABTestComponent
    *
    * @param abTestCompUID
    * @return ABTestCode
    * @throws CMSItemNotFoundException
    */
   public String resolveABTestcode(final String abTestCompUID) throws CMSItemNotFoundException
   {
      if (((ABTestComponentModel) cmsComponentService.getAbstractCMSComponent(abTestCompUID))
         .getAbTest() != null)
      {
         return ((ABTestComponentModel) cmsComponentService.getAbstractCMSComponent(abTestCompUID))
            .getAbTest().getTestCode();
      }

      return "";
   }

}