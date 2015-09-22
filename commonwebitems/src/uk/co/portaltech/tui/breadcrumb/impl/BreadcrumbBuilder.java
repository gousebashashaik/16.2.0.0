/**
 *
 */
package uk.co.portaltech.tui.breadcrumb.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.breadcrumb.BreadCrumb;
import uk.co.portaltech.tui.breadcrumb.BrowseHistory;
import uk.co.portaltech.tui.breadcrumb.BrowseHistoryEntry;
import uk.co.portaltech.tui.components.model.BookflowProgressIndicatorComponentModel;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.components.model.BookflowStepIndicatorComponentModel;
import uk.co.tui.book.constants.SessionObjectKeys;
import uk.co.tui.book.utils.PropertyReader;

/**
 * ProductBreadcrumbBuilder implementation for ProductData
 */
public class BreadcrumbBuilder
{
   private static final String ACTIVE_OR_STATIC_LINK_CLASS = "active";

   private static final String COMPLETED_LINK_CLASS = "completed";

   private static final Object LOCATION_STR = "Location";

   private static final String NAME = ".name";

   public static final int TWO = 2;

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   @Resource
   private TUIUrlResolver<AttractionModel> attractionUrlResolver;

   @Resource
   private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;

   @Resource
   private FeatureService featureService;

   @Resource
   private SessionService sessionService;

   private BrowseHistory browseHistory;

   /** The book flow urls. */
   @Resource
   private PropertyReader bookFlowUrls;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   @Resource
   private BrandUtils brandUtils;

   /**
    * Method returns the bread crumb data at product level if productCode is passed otherwise
    * returns category level data
    *
    * @param itemModel
    * @return List<BreadCrumb>
    */

   public List<BreadCrumb> getBreadcrumbs(final ItemModel itemModel)
   {
      List<BreadCrumb> breadcrumbs = new ArrayList<BreadCrumb>();

      final List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
      final BreadCrumb last;

      last = getProductBreadcrumb(itemModel);

      if ("Attraction".equals(itemModel.getItemtype()))
      {
         final AttractionModel attractionModel = (AttractionModel) itemModel;
         categoryModels.addAll(attractionModel.getLocations());
      }
      else
      {
         categoryModels.addAll(((ProductModel) itemModel).getSupercategories());
      }
      last.setLinkClass(ACTIVE_OR_STATIC_LINK_CLASS);
      last.setActive(true);
      breadcrumbs.add(last);
      processBrowserHistory(categoryModels, breadcrumbs);
      if (breadcrumbs.size() == 1)
      {
         if ("Attraction".equals(itemModel.getItemtype()))
         {
            breadcrumbs = getCategoryTreeBreadcrumbForAttractions(categoryModels, breadcrumbs);
         }
         else
         {
            breadcrumbs = getCategoryTreeBreadcrumb(categoryModels, breadcrumbs);
         }

      }

      breadcrumbs.add(this.getBreadcrumbforDestinationLanding());
      Collections.reverse(breadcrumbs);
      return breadcrumbs;
   }

   /**
    * @param categoryModels
    * @param breadcrumbs
    */
   private void processBrowserHistory(final List<CategoryModel> categoryModels,
      final List<BreadCrumb> breadcrumbs)
   {
      // add browse history into bread crumb if browse history category
      // available
      for (final CategoryModel categoryModel : categoryModels)
      {
         final BrowseHistoryEntry history =
            getBrowseHistory().findUrlInHistory(categoryModel.getCode());
         if (history != null)
         {
            final String pageTitle = getPageTitle(history.getUrl(), categoryModels);
            if (!"".equals(pageTitle))
            {
               breadcrumbs.add(new BreadCrumb(history.getUrl(), pageTitle, null, false, false));
            }
         }
      }
   }

   /**
    * @param categoryModels
    * @return title
    */
   private String getPageTitle(final String pageUrl, final List<CategoryModel> categoryModels)
   {
      String title = "";
      String url = pageUrl;
      final String substring = url.substring(url.length() - 1, url.length());
      if ("/".equals(substring))
      {
         url = url.substring(0, url.length() - 1);

      }
      final String code = url.substring(url.lastIndexOf('/') + 1, url.length());
      for (final CategoryModel catModel : categoryModels)
      {
         if (catModel.getItemtype().equals(LOCATION_STR))
         {
            if (catModel.getCode().equals(code))
            {
               title = catModel.getName();
               break;
            }
         }
         else if (catModel.getName().equalsIgnoreCase(code))
         {
            title = catModel.getName();
            break;
         }
      }
      return title;
   }

   /**
    * This method returns the location map of the item
    *
    * @param itemModel
    * @return locationMap
    */
   public Map<String, String> getLocationMap(final ItemModel itemModel)
   {
      final Map<String, String> locationMap = new HashMap<String, String>();
      return getLocationHierarchy(((ProductModel) itemModel).getSupercategories(), locationMap);
   }

   /**
    * Method returns the location map for a product
    *
    * @param categoryModels
    * @return locationHierarchy
    */
   public Map<String, String> getLocationHierarchy(final Collection<CategoryModel> categoryModels,
      final Map<String, String> locationHierarchy)
   {
      for (final CategoryModel category : categoryModels)
      {
         final String type = category.getItemtype();
         if (!LOCATION_STR.equals(type))
         {
            continue;
         }
         if (categoryCodeCheck(category) && !"resorts".equals(category.getCode())
            && !"regions".equals(category.getCode()) && !"continents".equals(category.getCode()))
         {
            if (LocationType.RESORT.getCode()
               .equals(((LocationModel) category).getType().getCode()))
            {
               addLocationToMap(locationHierarchy, category, "resort");
            }
            else if (LocationType.DESTINATION.getCode().equals(
               ((LocationModel) category).getType().getCode()))
            {
               addLocationToMap(locationHierarchy, category, "destination");
            }
            else if (LocationType.REGION.getCode().equals(
               ((LocationModel) category).getType().getCode()))
            {
               addLocationToMap(locationHierarchy, category, "region");
            }
            else if (LocationType.COUNTRY.getCode().equals(
               ((LocationModel) category).getType().getCode()))
            {
               addLocationToMap(locationHierarchy, category, "country");
            }
         }
      }

      return locationHierarchy;
   }

   /**
    * This method adds one location to the map
    *
    * @param locationHierarchy
    * @param category
    * @param locationType
    */
   private void addLocationToMap(final Map<String, String> locationHierarchy,
      final CategoryModel category, final String locationType)
   {
      locationHierarchy.put(locationType, category.getName());
      if (!category.getSupercategories().isEmpty())
      {
         final List<CategoryModel> superCategories =
            new ArrayList<CategoryModel>(category.getSupercategories());
         getLocationHierarchy(superCategories, locationHierarchy);
      }
   }

   /**
    * Method returns the bread crumb data category level
    *
    *
    * @param itemModel
    * @return List<BreadCrumb>
    */

   public List<BreadCrumb> getBookflowAccommPageBreadcrumbs(final ItemModel itemModel)
   {

      List<BreadCrumb> breadcrumbs = new ArrayList<BreadCrumb>();
      final List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
      categoryModels.addAll(((ProductModel) itemModel).getSupercategories());

      breadcrumbs = getCategoryBreadcrumbsForBookflowAccomPage(categoryModels, breadcrumbs);

      return breadcrumbs;
   }

   /**
    * Method returns the bread crumb data for BookflowProgressIndiacator
    *
    * @return List<BreadCrumb>
    */

   public List<BreadCrumb> getBookflowProgressIndicators(final ItemModel itemModel)
   {
      final List<BreadCrumb> breadcrumbs = new ArrayList<BreadCrumb>();
      final BookflowProgressIndicatorComponentModel item =
         (BookflowProgressIndicatorComponentModel) itemModel;
      BreadCrumb first = null;
      final String currentPageName = sessionService.getAttribute("breadCrumb");
      for (final String pageName : item.getPages())
      {
         if (StringUtils.equalsIgnoreCase(currentPageName, pageName))
         {
            first = new BreadCrumb("", pageName, null, false, false);
            first.setLinkClass(ACTIVE_OR_STATIC_LINK_CLASS);
            first.setActive(true);
            breadcrumbs.add(first);
         }
         else
         {
            breadcrumbs.add(new BreadCrumb(null, pageName, null, false, false));
         }
      }
      return breadcrumbs;
   }

   /**
    * Method returns the bread crumb data for BookflowStepIndiacator
    *
    * @param brand
    *
    * @return List<BreadCrumb>
    */

   public List<BreadCrumb> getBookflowStepIndicators(final String pageLabel, final String brand)
   {
      final List<BreadCrumb> breadcrumbs = new ArrayList<BreadCrumb>();
      final List<String> pageNames = getPageNamesFromConfig();
      final String httpBaseURL =
         Config.getString(StringUtil.append(brand, ".web.http.base.url"),
            "http://www.firstchoice.co.uk");
      for (final String pageName : pageNames)
      {
         String url =
            StringUtil.append(httpBaseURL, bookFlowUrls.getValue(brand + "." + pageName + ".url"));
         if ("HO".equals(pageName))
         {
            url =
               StringUtil.append(httpBaseURL,
                  (String) sessionService.getAttribute(SessionObjectKeys.ACCOMOPTIONS_URL));
         }
         if ("HR".equals(pageName))
         {
            final Object hotelResultsUrl =
               sessionService.getAttribute(SessionObjectKeys.HOTEL_RESULTS_URL);
            String hotelResults = null;
            if (hotelResultsUrl != null)
            {
               hotelResults = (String) hotelResultsUrl;
            }
            url = StringUtil.append(httpBaseURL, hotelResults);
         }
         if ("IO".equals(pageName))
         {
            url =
               StringUtil.append(httpBaseURL,
                  (String) sessionService.getAttribute(SessionObjectKeys.ITINERARY_PAGE_URL));
         }
         if ("SR".equals(pageName))
         {
            url =
               StringUtil.append(httpBaseURL,
                  (String) sessionService.getAttribute(SessionObjectKeys.SEARCH_RESULTS_URL));
            continue;
         }
         final BreadCrumb breadcrumb =
            new BreadCrumb(url, bookFlowUrls.getValue(brand + "." + pageName + NAME), null, false,
               false);
         breadcrumbs.add(breadcrumb);
         if (StringUtils.equalsIgnoreCase(bookFlowUrls.getValue(brand + "." + pageName + ".id"),
            pageLabel))
         {
            final int index =
               breadcrumbs.indexOf(new BreadCrumb(url, bookFlowUrls.getValue(brand + "." + pageName
                  + NAME), null, false, false));
            breadcrumbs.get(index).setLinkClass(ACTIVE_OR_STATIC_LINK_CLASS);
            breadcrumbs.get(index).setActive(true);
            updateBreadCrumbs(breadcrumbs, index);
         }
      }
      return breadcrumbs;
   }

   /**
    * @param breadcrumbs
    * @param index
    */
   private void updateBreadCrumbs(final List<BreadCrumb> breadcrumbs, final int index)
   {
      int indexParam = index;
      while (indexParam != 0)
      {
         breadcrumbs.get(indexParam - 1).setLinkClass(COMPLETED_LINK_CLASS);
         breadcrumbs.get(indexParam - 1).setActive(true);
         indexParam--;
      }
   }

   /**
    * Gets the page names.
    *
    * @return the page names
    */
   private List<String> getPageNamesFromConfig()
   {
      final String pageOrder =
         bookFlowUrls.getValue(StringUtil.append(packageCartService.getBasePackage()
            .getPackageType().toString(), ".pageOrder"));
      final List<String> pageNames = new ArrayList<String>();
      final String[] pages = StringUtils.split(pageOrder, ',');
      for (final String page : pages)
      {
         pageNames.add(page);
      }
      return pageNames;
   }

   /**
    * Gets the page names for lapland.
    *
    * @return the page names for lapland
    */
   private List<String> getPageNamesForLapland()
   {
      final String pageOrder = bookFlowUrls.getValue("lapland" + ".pageOrder");
      final List<String> pageNames = new ArrayList<String>();
      final String[] pages = StringUtils.split(pageOrder, ',');
      for (final String page : pages)
      {
         pageNames.add(page);
      }
      return pageNames;
   }

   public List<BreadCrumb> getLaplandDayTripStepIndicators(final String pageLabel,
      final String brand)
   {

      final List<BreadCrumb> breadcrumbs = new ArrayList<BreadCrumb>();
      final List<String> pageNames = getPageNamesForLapland();
      final String httpBaseURL =
         Config.getString(StringUtil.append(brand, ".web.http.base.url"),
            "http://www.firstchoice.co.uk");
      for (final String pageName : pageNames)
      {
         String url =
            StringUtil.append(httpBaseURL, bookFlowUrls.getValue(brand + "." + pageName + ".url"));
         if ("HO".equals(pageName))
         {
            url =
               StringUtil.append(httpBaseURL,
                  (String) sessionService.getAttribute(SessionObjectKeys.ACCOMOPTIONS_URL));
         }
         final BreadCrumb breadcrumb =
            new BreadCrumb(url, bookFlowUrls.getValue(brand + "." + pageName + NAME), null, false,
               false);
         breadcrumbs.add(breadcrumb);
         if (StringUtils.equalsIgnoreCase(bookFlowUrls.getValue(brand + "." + pageName + ".id"),
            pageLabel))
         {
            final int index =
               breadcrumbs.indexOf(new BreadCrumb(url, bookFlowUrls.getValue(brand + "." + pageName
                  + NAME), null, false, false));
            breadcrumbs.get(index).setLinkClass(ACTIVE_OR_STATIC_LINK_CLASS);
            breadcrumbs.get(index).setActive(true);
            updateBreadCrumbs(breadcrumbs, index);
         }
      }
      return breadcrumbs;
   }

   /**
    * Adds two breadcrumbs starting from resort level and above and returns the breadcrumbs list.
    *
    * @param categoryModels
    * @return breadcrumbs
    */
   private List<BreadCrumb> getCategoryBreadcrumbsForBookflowAccomPage(
      final List<CategoryModel> categoryModels, final List<BreadCrumb> breadcrumbs)
   {

      for (final CategoryModel category : categoryModels)
      {
         final String type = category.getItemtype();
         if (!LOCATION_STR.equals(type))
         {
            continue;
         }
         if (categoryCodeCheck(category) && !"resorts".equals(category.getCode())
            && !"regions".equals(category.getCode()) && !"continents".equals(category.getCode()))
         {
            if (LocationType.PORT.getCode().equals(((LocationModel) category).getType().getCode()))
            {
               addCategoryBreadcrumbs(breadcrumbs, category);
            }
            else if (LocationType.RESORT.getCode().equals(
               ((LocationModel) category).getType().getCode()))
            {
               addCategoryBreadcrumbs(breadcrumbs, category);
            }
            else if (LocationType.DESTINATION.getCode().equals(
               ((LocationModel) category).getType().getCode()))
            {
               addCategoryBreadcrumbs(breadcrumbs, category);
            }
            else if (LocationType.REGION.getCode().equals(
               ((LocationModel) category).getType().getCode()))
            {
               addCategoryBreadcrumbs(breadcrumbs, category);
            }
            else if (LocationType.COUNTRY.getCode().equals(
               ((LocationModel) category).getType().getCode()))
            {
               addCategoryBreadcrumbs(breadcrumbs, category);
            }
            if (breadcrumbs.size() == TWO)
            {
               break;
            }
         }
      }
      return breadcrumbs;
   }

   private boolean categoryCodeCheck(final CategoryModel category)
   {
      return !"accommodations".equals(category.getCode())
         && !"destinations".equals(category.getCode()) && !"countries".equals(category.getCode());
   }

   private void addCategoryBreadcrumbs(final List<BreadCrumb> breadcrumbs,
      final CategoryModel category)
   {
      final BreadCrumb breadCrumb = new BreadCrumb(null, category.getName(), null, false, false);
      if (!breadcrumbs.contains(breadCrumb))
      {
         breadcrumbs.add(breadCrumb);
      }
      if ((breadcrumbs.size() <= 1) && !category.getSupercategories().isEmpty())
      {
         final List<CategoryModel> superCategories =
            new ArrayList<CategoryModel>(category.getSupercategories());
         getCategoryBreadcrumbsForBookflowAccomPage(superCategories, breadcrumbs);
      }
   }

   public List<BreadCrumb> getBreadcrumbsForCategory(final CategoryModel categoryModel)
   {
      List<BreadCrumb> breadcrumbs = new ArrayList<BreadCrumb>();
      BreadCrumb first = null;
      if ("DestinationLanding".equals(categoryModel.getItemtype()))
      {
         first = getBreadcrumbforDestinationLanding();
         first.setLinkClass(ACTIVE_OR_STATIC_LINK_CLASS);
         first.setActive(true);
      }
      else
      {
         final List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
         final BreadCrumb last;

         last = getCategoryBreadcrumb(categoryModel);
         categoryModels.addAll(categoryModel.getSupercategories());
         last.setLinkClass(ACTIVE_OR_STATIC_LINK_CLASS);
         last.setActive(true);
         breadcrumbs.add(last);
         breadcrumbs = getCategoryTreeBreadcrumb(categoryModels, breadcrumbs);
         if ("ProductRange".equals(categoryModel.getItemtype()))
         {
            first = new BreadCrumb("", "Holidays with the difference", null, false, false);
            first.setLinkClass(ACTIVE_OR_STATIC_LINK_CLASS);
            first.setActive(true);
         }
         else
         {
            // first =

            first = this.getBreadcrumbforDestinationLanding();
         }
      }
      breadcrumbs.add(first);
      Collections.reverse(breadcrumbs);
      return breadcrumbs;
   }

   private BreadCrumb getBreadcrumbforDestinationLanding()
   {
      // final String categoryUrl =

      final BrandDetails brandDetails = sessionService.getAttribute("brandDetails");
      return new BreadCrumb(Config.getString(brandDetails.getSiteUid()
         + ".breadcrumb.destinations.url", ""), "Destinations", null, false, false);
   }

   protected BreadCrumb getProductBreadcrumb(final ItemModel itemModel)
   {
      String productUrl = "";
      String itemName = "";
      String brand = null;
      if ("Attraction".equals(itemModel.getItemtype()))
      {
         productUrl = attractionUrlResolver.resolve((AttractionModel) itemModel);
      }
      else
      {
         productUrl = tuiProductUrlResolver.resolve((ProductModel) itemModel);

      }
      if (itemModel instanceof AccommodationModel)
      {
         brand = brandUtils.getFeatureServiceBrand(((AccommodationModel) itemModel).getBrands());
      }
      itemName = featureService.getFirstFeatureValueAsString("name", itemModel, new Date(), brand);

      return new BreadCrumb(productUrl, itemName, null, false, false);
   }

   /**
    * For a given category this method loops through recursively gets the supercategories until the
    * top level cetegory and returns the breadcrumb data
    *
    * @param categoryModels
    * @param breadcrumbs
    * @return List<BreadCrumb> breadcrumbs
    */
   protected List<BreadCrumb> getCategoryTreeBreadcrumb(final List<CategoryModel> categoryModels,
      final List<BreadCrumb> breadcrumbs)
   {
      for (final CategoryModel category : categoryModels)
      {
         final String type = category.getItemtype();
         if (!LOCATION_STR.equals(type))
         {
            continue;
         }

         if (categoryCodeCheck(category) && !"resorts".equals(category.getCode())
            && !"regions".equals(category.getCode()) && !"continents".equals(category.getCode()))
         {
            getbreadcrumbs(category, breadcrumbs);
         }
      }

      return breadcrumbs;
   }

   private void getbreadcrumbs(final CategoryModel category, final List<BreadCrumb> breadcrumbs)
   {
      if (!category.getSupercategories().isEmpty())
      {
         if (LocationType.CONTINENT.getCode()
            .equals(((LocationModel) category).getType().getCode()))
         {
            if ("CCC".equalsIgnoreCase(((LocationModel) category).getCode()))
            {
               final BreadCrumb breadCrumb = getCategoryBreadcrumb(category);
               if (!breadcrumbs.contains(breadCrumb))
               {
                  breadcrumbs.add(breadCrumb);
               }
            }
         }
         else
         {
            final BreadCrumb breadCrumb = getCategoryBreadcrumb(category);
            if (!breadcrumbs.contains(breadCrumb))
            {
               breadcrumbs.add(breadCrumb);
            }
         }
         if (!category.getSupercategories().isEmpty())
         {
            final List<CategoryModel> superCategories =
               new ArrayList<CategoryModel>(category.getSupercategories());
            getCategoryTreeBreadcrumb(superCategories, breadcrumbs);
         }
      }
   }

   /**
    * For a given categoryModel list this method loops through all the elements and finds the lowest
    * one in the hierarchy and continues in creation of the bread crumb list for attractions.
    *
    * @param categoryModels
    * @param breadcrumbs
    * @return List<BreadCrumb> breadcrumbs
    */
   protected List<BreadCrumb> getCategoryTreeBreadcrumbForAttractions(
      final List<CategoryModel> categoryModels, final List<BreadCrumb> breadcrumbs)
   {

      // fetch the lowest one from the list of Category Models
      CategoryModel lowest = null;
      for (final CategoryModel category : categoryModels)
      {
         if ((lowest == null)
            || (CollectionUtils.size(lowest.getAllSubcategories()) > CollectionUtils.size(category
               .getAllSubcategories())))
         {
            lowest = category;
         }
      }
      if (lowest != null)
      {
         final BreadCrumb lowestBreadCrumb = getCategoryBreadcrumb(lowest);
         breadcrumbs.add(lowestBreadCrumb);
         getCategoryTreeBreadcrumbForAttractionsFromChild(lowest.getSupercategories(), breadcrumbs);
      }
      return breadcrumbs;
   }

   /**
    * For a given category Model List this method loops through recursively gets the supercategories
    * until the top level cetegory. and returns the breadcrumb data. This method also checks if the
    * breadcrumb is already added into the list and adds it based on that.
    *
    * @param categoryModels
    * @param breadcrumbs
    * @return List<BreadCrumb> breadcrumbs
    */

   protected List<BreadCrumb> getCategoryTreeBreadcrumbForAttractionsFromChild(
      final List<CategoryModel> categoryModels, final List<BreadCrumb> breadcrumbs)
   {
      final Iterator<CategoryModel> iterateCategoryModels = categoryModels.iterator();
      while (iterateCategoryModels.hasNext())
      {
         final CategoryModel category = iterateCategoryModels.next();
         if (category instanceof LocationModel)
         {
            final LocationModel location = (LocationModel) category;
            if (!LocationType.CONTINENT.getCode().equals(location.getType().getCode()))
            {
               final BreadCrumb breadCrumb = getCategoryBreadcrumb(category);
               if (!breadcrumbs.contains(breadCrumb))
               {
                  breadcrumbs.add(breadCrumb);
               }
               if (!location.getSupercategories().isEmpty())
               {
                  getCategoryTreeBreadcrumbForAttractionsFromChild(location.getSupercategories(),
                     breadcrumbs);
               }
            }
         }
      }
      return breadcrumbs;
   }

   protected BreadCrumb getCategoryBreadcrumb(final CategoryModel categoryModel)
   {
      final String categoryUrl = tuiCategoryModelUrlResolver.resolve(categoryModel);
      return new BreadCrumb(categoryUrl, categoryModel.getName(), null, false, false);
   }

   /**
    * @return the browseHistory
    */
   public BrowseHistory getBrowseHistory()
   {
      return browseHistory;
   }

   /**
    * @param browseHistory the browseHistory to set
    */
   @Required
   public void setBrowseHistory(final BrowseHistory browseHistory)
   {
      this.browseHistory = browseHistory;
   }

   /**
    * @param component
    * @param pageLabel
    * @param urlMap
    * @param restrictedPages
    * @return List<BreadCrumb>
    */
   @SuppressWarnings("deprecation")
   public List<BreadCrumb> getCruiseBookflowStepIndicators(
      final BookflowStepIndicatorComponentModel component, final String pageLabel,
      final Map<String, String> urlMap, final List<String> restrictedPages)
   {
      final List<BreadCrumb> breadcrumbs = new ArrayList<BreadCrumb>();
      for (final String pageName : component.getPages())
      {
         if (!restrictedPages.contains(pageName))
         {
            final BreadCrumb breadcrumb = new BreadCrumb(null, pageName, null);
            breadcrumb.setUrl(urlMap.get(pageName.toLowerCase()));
            breadcrumbs.add(breadcrumb);
         }
      }

      for (final BreadCrumb breadcrumb : breadcrumbs)
      {
         if (breadcrumb.getName().equalsIgnoreCase(pageLabel))
         {
            breadcrumb.setLinkClass(ACTIVE_OR_STATIC_LINK_CLASS);
            break;
         }
         else
         {
            breadcrumb.setLinkClass(COMPLETED_LINK_CLASS);
         }
      }

      return breadcrumbs;
   }

}