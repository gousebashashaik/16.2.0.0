/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.DealCategoryModel;
import uk.co.portaltech.travel.model.DealCollectionModel;
import uk.co.portaltech.travel.model.InlineHeaderComponentModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.results.DealsCollectionEndecaResults;
import uk.co.portaltech.travel.model.results.MerchandisedHoliday;
import uk.co.portaltech.travel.model.results.MerchandisedResult;
import uk.co.portaltech.travel.model.results.SmerchConfiguration;
import uk.co.portaltech.travel.services.DealsService;
import uk.co.portaltech.travel.thirdparty.endeca.MerchandiserSearchContext;
import uk.co.portaltech.travel.thirdparty.endeca.services.MerchandiserService;
import uk.co.portaltech.tui.components.model.ProductPromoComponentModel;
import uk.co.portaltech.tui.facades.MerchandiserFacade;
import uk.co.portaltech.tui.helper.Pagination;
import uk.co.portaltech.tui.model.SortCriteriaModel;
import uk.co.portaltech.tui.populators.DealsCollectionMerchandiserResultsPopulator;
import uk.co.portaltech.tui.populators.InvGroupsFilterSelectorPopulator;
import uk.co.portaltech.tui.populators.MerchandiserResultsPopulator;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.DealsCategoryViewData;
import uk.co.portaltech.tui.web.view.data.DealsCollectionResult;
import uk.co.portaltech.tui.web.view.data.DealsResult;
import uk.co.portaltech.tui.web.view.data.MerchandiserRequest;
import uk.co.portaltech.tui.web.view.data.MerchandiserResponse;
import uk.co.portaltech.tui.web.view.data.MerchandiserViewData;
import uk.co.portaltech.tui.web.view.data.ProductPromoViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.EndecaSearchException;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.util.MerchandisedHolidayComparator;
import uk.co.tui.web.common.enums.SortParameters;

public class DefaultMerchandiserFacade implements MerchandiserFacade
{

   private static final int DEFAULT_OFFSET = 10;

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private MerchandiserService merchandiserService;

   @Resource
   private MerchandiserResultsPopulator merchandiserResultsPopulator;

   @Resource
   private DealsService dealsService;

   @Resource
   private Populator<DealCollectionModel, DealsCategoryViewData> dealsCategoryPopulator;

   @Resource
   private DealsCollectionMerchandiserResultsPopulator dealsCollectionMerchandiserPopulator;

   @Resource
   private Populator<ProductRangeModel, ProductPromoViewData> productPromoPopulator;

   @Resource
   private Populator<MerchandiserRequest, MerchandiserSearchContext> merchandisedRequestPopulator;

   @Resource
   private Pagination pagination;

   @Resource
   private InvGroupsFilterSelectorPopulator invfilterPopulator;

   @Resource
   private SessionService sessionService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private ViewSelector viewSelector;

   private ConfigurationService configurationService;

   private List<DealsCategoryViewData> dealCollectionViewDatas;

   private DealsCategoryViewData dealCollectionViewData;

   private static final String COMPONENT_ID = "WF_COM_306-3";

   private static final String SORT = "sort";

   private static final String FILTER = "Filter";

   private static final String INS = "ins";

   private static final String PAGINATE = "paginate";

   private static final String LATEST_INV_CRITERIA = "latestInvCriteria";

   private static final String INV_RESULTS = "invResults";

   private static final String INV_OFFSET = "holiday.inventoryGroups.offset";

   private static final String DEALS_OFFSET = "holiday.deals.offset";

   private static final String LATEST_DEALS_CRITERIA = "latestdealsCriteria";

   private static final String DEALS_RESULTS = "dealsResults";

   private static final String DEFAULT = "Default";

   private static final TUILogUtils LOG = new TUILogUtils("DefaultMerchandiserFacade");

   @Override
   public SmerchConfiguration getSmerchConfiguration(final MerchandiserRequest dealsRequest)
      throws SearchResultsBusinessException
   {
      try
      {
         return getSmerchConfigurationFromEndecaCache(dealsRequest);
      }
      catch (final EndecaSearchException ex)
      {
         throw new SearchResultsBusinessException("3005", ex);
      }
   }

   /**
    * Get all deals for particular page .
    */

   @Override
   public DealsResult getDeals(final MerchandiserRequest dealsRequest)
      throws SearchResultsBusinessException
   {

      dealsRequest.setOffset(configurationService.getConfiguration().getInt(DEALS_OFFSET,
         DEFAULT_OFFSET));

      try
      {
         if (StringUtils.equalsIgnoreCase("paginate", dealsRequest.getSearchRequestType())
            && !dealsRequest.isCategorizedPage())
         {
            return getPaginationDealsResult(dealsRequest);
         }

         sessionService.setAttribute(LATEST_DEALS_CRITERIA, dealsRequest);
         return getDealsResult(dealsRequest);

      }
      catch (final EndecaSearchException ex)
      {
         throw new SearchResultsBusinessException("3005", ex);
      }

   }

   /**
    * This method returns Inventory Groups results for a particular page
    *
    * @param invGroupsRequest
    * @return EndecaSearchResult
    * @throws SearchResultsBusinessException
    */

   @Override
   public MerchandiserResponse getInvGroups(final MerchandiserRequest invGroupsRequest)
      throws SearchResultsBusinessException
   {

      invGroupsRequest.setOffset(configurationService.getConfiguration().getInt(INV_OFFSET,
         DEFAULT_OFFSET));
      final MerchandiserResponse viewData = new MerchandiserResponse();
      viewData.setMerchandiserRequest(invGroupsRequest);
      try
      {

         if (StringUtils.equalsIgnoreCase(invGroupsRequest.getSearchRequestType(), "paginate")
            || StringUtils.equalsIgnoreCase("Sort", invGroupsRequest.getSearchRequestType()))
         {
            viewData.setMerchandiserResult(getInvPaginationSortData(invGroupsRequest));
            return viewData;
         }
         if (isFollowOnSearch(invGroupsRequest))
         {
            // latest search criteria is added because if we do any room change and go to accom
            // details page we take latest criteria from session to get the holiday.
            sessionService.setAttribute(LATEST_INV_CRITERIA, invGroupsRequest);
            viewData.setMerchandiserResult(getHolidayPackagesResultDataForFacets(invGroupsRequest));
            return viewData;
         }

         sessionService.setAttribute(LATEST_INV_CRITERIA, invGroupsRequest);
         viewData.setMerchandiserResult(getInvPackages(invGroupsRequest));

      }
      catch (final EndecaSearchException ex)
      {
         throw new SearchResultsBusinessException("3005", ex);
      }
      return viewData;
   }

   /**
    * Paginated and Sort result for inventory group
    *
    * @param invGroupsRequest
    * @throws SearchResultsBusinessException
    */

   public MerchandiserViewData getInvPaginationSortData(final MerchandiserRequest invGroupsRequest)
      throws SearchResultsBusinessException
   {

      LOG.info("This search Criteria is ignored except for page no's and sort criteria. First: "
         + invGroupsRequest.getFirst() + " Offset: " + invGroupsRequest.getOffset() + "Sort By: "
         + invGroupsRequest.getSortBy());

      final MerchandisedResult endecaSearchResults = sessionService.getAttribute(INV_RESULTS);
      // checking previous search is Main Search / Follow On Search
      if (checkIsFollowSearchResult(endecaSearchResults))
      {

         if (StringUtils.equalsIgnoreCase(SORT, invGroupsRequest.getSearchRequestType()))
         {

            // sorting is applied on raw results
            sort(invGroupsRequest, endecaSearchResults);
         }
         // paginate and populate the vew data.
         return getPaginatedInvViewData(invGroupsRequest, endecaSearchResults);

      }
      else
      {
         MerchandiserRequest searchParameterInSession =
            sessionService.getAttribute(LATEST_INV_CRITERIA);

         if (StringUtils.equalsIgnoreCase(PAGINATE, invGroupsRequest.getSearchRequestType())
            && searchParameterInSession != null)
         {
            // to update the new page
            searchParameterInSession.setFirst(invGroupsRequest.getFirst());
            searchParameterInSession.setOffset(invGroupsRequest.getOffset());
            // If its an initial search pagination...use the View cache route and If sort is not
            // happened on previous results
            if (StringUtils.equalsIgnoreCase(INS, searchParameterInSession.getSearchRequestType())
               && (StringUtils.isBlank(searchParameterInSession.getSortBy()))
               || (StringUtils.equalsIgnoreCase(DEFAULT, searchParameterInSession.getSortBy())))
            {
               // pagination on main search results with out sorting from View Cache
               return getInvPackages(searchParameterInSession);

            }
            else
            {

               // Else block will execute when pagination on top of sorted Main search results.
               MerchandisedResult endecaSearchResult = sessionService.getAttribute(INV_RESULTS);
               if (endecaSearchResult == null)
               {
                  endecaSearchResult = getInvPackagesFromEndecaCache(invGroupsRequest);
               }

               // paginate and populate the view data.
               return getPaginatedInvViewData(invGroupsRequest, endecaSearchResult);
            }
         }
         else if (StringUtils.equalsIgnoreCase(SORT, invGroupsRequest.getSearchRequestType())
            && searchParameterInSession != null)
         {
            // to update the current search Criteria with any new sort
            // parameter
            searchParameterInSession.setSortBy(invGroupsRequest.getSortBy());
         }
         // getting results from Raw Cache / Endeca
         if (!(searchParameterInSession != null))
         {
            // if session is empty
            searchParameterInSession = invGroupsRequest;
         }

         final MerchandisedResult endecaSearchResult =
            getInvPackagesFromEndecaCache(searchParameterInSession);
         // sorting is applied on raw results
         sort(searchParameterInSession, endecaSearchResult);
         // Storing Sorted main Results into Session.
         sessionService.setAttribute(INV_RESULTS, endecaSearchResult);
         // paginate and populate the vew data.
         return getPaginatedInvViewData(invGroupsRequest, endecaSearchResult);
      }
   }

   /**
    * Facet search for inventory groups
    *
    * @param invGroupsRequest
    * @return MerchandiserViewData
    */
   private MerchandiserViewData getHolidayPackagesResultDataForFacets(
      final MerchandiserRequest invGroupsRequest)
   {

      final MerchandiserViewData viewData = new MerchandiserViewData();
      final MerchandisedResult searchResult = getInvPackagesForFilters(invGroupsRequest);

      if (!isEndecaResultEmpty(searchResult))
      {

         // setting search type when facet search is invoked.
         searchResult.setSearchRequestType(invGroupsRequest.getSearchRequestType());
         if (StringUtils.isNotEmpty(invGroupsRequest.getSortBy()))
         {
            sort(invGroupsRequest, searchResult);
         }
         sessionService.setAttribute(INV_RESULTS, searchResult);
         invfilterPopulator.populate(searchResult, viewData);

         if (CollectionUtils.isNotEmpty(searchResult.getHolidays()))
         {
            viewData.setEndecaResultsCount(searchResult.getHolidays().size());
         }

         final List<MerchandisedHoliday> paginatedHolidays =
            pagination.paginateMerchandiserResults(searchResult, invGroupsRequest);
         merchandiserResultsPopulator.populate(paginatedHolidays, viewData);
      }
      return viewData;
   }

   /**
    * Paginate the inventory results
    *
    * @param invGroupsRequest
    * @param endecaSearchResults
    * @return MerchandiserViewData
    * @throws SearchResultsBusinessException
    */
   @SuppressWarnings("boxing")
   public MerchandiserViewData getPaginatedInvViewData(final MerchandiserRequest invGroupsRequest,
      final MerchandisedResult endecaSearchResults) throws SearchResultsBusinessException
   {
      // Paginate Results
      final List<MerchandisedHoliday> paginatedHolidays =
         pagination.paginateMerchandiserResults(endecaSearchResults, invGroupsRequest);

      final MerchandiserViewData merchandiserViewData = new MerchandiserViewData();
      // Total num of records returned from Endeca in parameter TotalNumofRecs is not correct hence
      // calcualtion from Hybris end
      if (CollectionUtils.isNotEmpty(endecaSearchResults.getHolidays()))
      {
         merchandiserViewData.setEndecaResultsCount(endecaSearchResults.getHolidays().size());
      }

      merchandiserResultsPopulator.populate(paginatedHolidays, merchandiserViewData);
      return merchandiserViewData;
   }

   /**
    * check if its a follow on result
    *
    * @param endecaSearchResults
    * @return boolean
    */
   private boolean checkIsFollowSearchResult(final MerchandisedResult endecaSearchResults)
   {
      return endecaSearchResults != null
         && StringUtils.isNotBlank(endecaSearchResults.getSearchRequestType())
         && isFollowOnSearchResult(endecaSearchResults);
   }

   /**
    * check if its a follow on request
    *
    * @param searchParameter
    * @return boolean
    */
   private boolean isFollowOnSearch(final MerchandiserRequest searchParameter)
   {
      return StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), FILTER);
   }

   /**
    * check for follow on search
    *
    * @param searchResults
    * @return boolean
    */
   private boolean isFollowOnSearchResult(final MerchandisedResult searchResults)
   {
      return StringUtils.equalsIgnoreCase(searchResults.getSearchRequestType(), FILTER);
   }

   /**
    * This method returns collection list for each category.
    */
   @Override
   public Map<String, List<DealsCategoryViewData>> getDealsCategoryCollection()
   {

      final Map<String, List<DealsCategoryViewData>> dealsCategoryCollection =
         new TreeMap<String, List<DealsCategoryViewData>>();
      final List<String> brandCodes = tuiUtilityService.getSiteReleventBrands();
      final List<DealCategoryModel> dealCategories =
         dealsService.getDealCategories(tuiUtilityService.getSiteReleventBrandPks());
      if (CollectionUtils.isNotEmpty(dealCategories))
      {

         for (final DealCategoryModel dealCategory : dealCategories)
         {
            final List<DealCollectionModel> dealCollections = dealCategory.getDealCollection();
            dealCollectionViewDatas = new ArrayList<DealsCategoryViewData>();
            for (final DealCollectionModel dealCollection : dealCollections)
            {
               if (BrandUtils.brandCodesExistInBrandTypes(dealCollection.getBrands(), brandCodes))
               {
                  dealCollectionViewData = new DealsCategoryViewData();
                  dealsCategoryPopulator.populate(dealCollection, dealCollectionViewData);
                  dealCollectionViewDatas.add(dealCollectionViewData);
               }

            }
            dealsCategoryCollection.put(dealCategory.getName(), dealCollectionViewDatas);
         }
      }
      return dealsCategoryCollection;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.MerchandiserFacade#getProductPromoViewData
    * (uk.co.portaltech.tui.components.model.ProductPromoComponentModel)
    */
   @Override
   public List<ProductPromoViewData> getProductPromoViewData(
      final ProductPromoComponentModel component)
   {
      final List<ProductRangeModel> productRanges = new ArrayList<ProductRangeModel>();
      final List<ItemModel> prItems = component.getProductRanges().getItems();
      if (CollectionUtils.isNotEmpty(prItems))
      {
         for (final ItemModel item : prItems)
         {
            if (item instanceof ProductRangeModel)
            {
               productRanges.add((ProductRangeModel) item);
            }
         }
      }
      final List<ProductPromoViewData> viewDatas = new ArrayList<ProductPromoViewData>();
      for (final ProductRangeModel productRange : productRanges)
      {
         final ProductPromoViewData viewData = new ProductPromoViewData();
         productPromoPopulator.populate(productRange, viewData);
         viewDatas.add(viewData);
      }
      return viewDatas;
   }

   /**
    * This method returns Deals results from Endeca
    *
    * @param request -dealsRequest
    * @return EndecaSearchResult
    * @throws SearchResultsBusinessException
    */
   @SuppressWarnings("boxing")
   @Override
   public DealsResult getDealsResult(final MerchandiserRequest request)
      throws SearchResultsBusinessException
   {

      final DealsCollectionEndecaResults dealsCollectionEndecaResults =
         getDealsFromEndecaCache(request);
      final DealsResult dealsResult = new DealsResult();
      final DealsCollectionResult viewData = dealsResult.getDealsCollectionResult();
      viewData.setMerchandiserRequest(request);
      dealsCollectionMerchandiserPopulator.populate(dealsCollectionEndecaResults, viewData);
      return dealsResult;
   }

   /**
    * Paginated and Sort result for inventory group --Deals
    *
    * @param dealsRequest
    * @throws SearchResultsBusinessException
    */
   public DealsResult getPaginationDealsResult(final MerchandiserRequest dealsRequest)
      throws SearchResultsBusinessException
   {

      LOG.info("This search Criteria is ignored except for page no's : " + dealsRequest.getFirst()
         + " Offset: " + dealsRequest.getOffset());
      MerchandiserRequest searchParameterInSession =
         sessionService.getAttribute(LATEST_DEALS_CRITERIA);

      if (StringUtils.equalsIgnoreCase(PAGINATE, dealsRequest.getSearchRequestType())
         && searchParameterInSession != null)
      {
         // to update the new page
         searchParameterInSession.setFirst(dealsRequest.getFirst());
         searchParameterInSession.setOffset(dealsRequest.getOffset());
         // If its an initial search pagination...use the View cache route and If sort is not
         // happened on previous results
         if (StringUtils.equalsIgnoreCase(INS, searchParameterInSession.getSearchRequestType())
            && StringUtils.isBlank(searchParameterInSession.getSortBy()))
         {
            // pagination on main search results with out sorting from View Cache
            return getDealsResult(searchParameterInSession);
         }
      }
      // getting results from Raw Cache / Endeca
      if (!(searchParameterInSession != null))
      {
         // if session is empty
         searchParameterInSession = dealsRequest;
      }

      final DealsCollectionEndecaResults endecaSearchResult =
         getDealsFromEndecaCache(searchParameterInSession);
      // Storing Sorted main Results into Session.
      sessionService.setAttribute(DEALS_RESULTS, endecaSearchResult);
      // paginate and populate the vew data.
      return getpaginatedDealsViewData(dealsRequest, endecaSearchResult);
   }

   /**
    * Deals population for pagination
    *
    * @param request
    * @param dealsCollectionEndecaResults
    * @return DealsResult
    * @throws SearchResultsBusinessException
    */
   public DealsResult getpaginatedDealsViewData(final MerchandiserRequest request,
      final DealsCollectionEndecaResults dealsCollectionEndecaResults)
      throws SearchResultsBusinessException
   {

      final DealsResult dealsResult = new DealsResult();
      final DealsCollectionResult viewData = dealsResult.getDealsCollectionResult();
      viewData.setMerchandiserRequest(request);
      if (!request.isCategorizedPage())
      {
         dealsCollectionMerchandiserPopulator.populate(dealsCollectionEndecaResults, viewData);
      }
      return dealsResult;
   }

   /**
    * This method will return the packages from Endeca
    */
   public DealsCollectionEndecaResults getDealsFromEndecaCache(final MerchandiserRequest request)
   {

      final MerchandiserSearchContext context = new MerchandiserSearchContext();

      if (null != request)
      {
         context.setPageLabel(request.getPageLabel());
      }
      return merchandiserService.getMerchandisedDealsPackages(context);
   }

   /**
    * This method returns Inv Groups results from Endeca which is cached for view cache
    *
    * @param request -dealsRequest
    * @return EndecaSearchResult
    * @throws SearchResultsBusinessException
    */
   @SuppressWarnings("boxing")
   @Override
   public MerchandiserViewData getInvPackages(final MerchandiserRequest request)
      throws SearchResultsBusinessException
   {
      final MerchandiserViewData viewData = new MerchandiserViewData();
      final MerchandisedResult endecaResult = getInvPackagesFromEndecaCache(request);

      // When user does "sorting" on basic search
      sort(request, endecaResult);

      viewData.setPageLabel(request.getPageLabel());

      // Removing previously searched facetResults from user session when again doing main Search
      sessionService.removeAttribute(INV_RESULTS);
      // Paginate Results
      final List<MerchandisedHoliday> paginatedHolidays =
         pagination.paginateMerchandiserResults(endecaResult, request);

      // Total num of records returned from Endeca in parameter TotalNumofRecs is not correct hence
      // calcualtion from Hybris end
      if (CollectionUtils.isNotEmpty(endecaResult.getHolidays()))
      {
         viewData.setEndecaResultsCount(endecaResult.getHolidays().size());
      }
      if (StringUtils.isNotEmpty(request.getPageName()))
      {
         viewData.setName(request.getPageName());
      }
      else
      {
         viewData.setName(endecaResult.getName());
      }

      invfilterPopulator.populate(endecaResult, viewData);
      merchandiserResultsPopulator.populate(paginatedHolidays, viewData);

      return viewData;
   }

   /**
    * This method will return the packages from Endeca for Inventory
    */
   public MerchandisedResult getInvPackagesFromEndecaCache(final MerchandiserRequest request)
   {

      final MerchandiserSearchContext context = new MerchandiserSearchContext();

      if (null != request)
      {
         merchandisedRequestPopulator.populate(request, context);
      }
      if (viewSelector.checkIsMobile())
      {
         context.setMobile(true);
      }
      context.setBrand(tuiUtilityService.getSiteBrand());
      return merchandiserService.getMerchandisedInvGroupsPackages(context);

   }

   /**
    * This method will return the packages from Endeca for Deals
    */
   public MerchandisedResult getInvPackagesForFilters(final MerchandiserRequest request)
   {

      final MerchandiserSearchContext context = new MerchandiserSearchContext();

      if (null != request)
      {
         merchandisedRequestPopulator.populate(request, context);
      }
      if (viewSelector.checkIsMobile())
      {
         context.setMobile(true);
      }
      context.setBrand(tuiUtilityService.getSiteBrand());
      return merchandiserService.invEndecaResponse(context);

   }

   /**
    * This method will return the Smerch Configuration from Endeca
    */
   public SmerchConfiguration getSmerchConfigurationFromEndecaCache(
      final MerchandiserRequest request)
   {

      final MerchandiserSearchContext context = new MerchandiserSearchContext();

      if (null != request)
      {
         context.setPageLabel(request.getPageLabel());
      }
      context.setBrand(tuiUtilityService.getSiteBrand());
      return merchandiserService.getMerchandisedSmerchConfigurations(context);
   }

   /**
    * This method checks if the Result returned by endeca is empty or not
    *
    * @param merchandisedResult
    * @return boolean
    */
   private boolean isEndecaResultEmpty(final MerchandisedResult merchandisedResult)
   {
      return !((null != merchandisedResult) && (CollectionUtils.isNotEmpty(merchandisedResult
         .getHolidays())));

   }

   /**
    * Comparator call to Sort the result based on sortby criteria
    *
    * @param searchParameter
    * @param merchandisedResult
    */
   private void sort(final MerchandiserRequest searchParameter,
      final MerchandisedResult merchandisedResult)
   {

      if (!isEndecaResultEmpty(merchandisedResult)
         && StringUtils.isNotEmpty(searchParameter.getSortBy()))
      {
         // Apply sorting here
         Collections.sort(merchandisedResult.getHolidays(), MerchandisedHolidayComparator
            .getComparator(getHolidayComparatorList(searchParameter.getSortBy())));
      }
   }

   /**
    * This method returns list of Comparators for sorting the SearchResultViewData based on the
    * defined Sort Criteria
    *
    * @param sortCriteriaCode
    * @return List<SearchResultViewDataComparator>
    */

   private List<MerchandisedHolidayComparator> getHolidayComparatorList(
      final String sortCriteriaCode)
   {

      final List<MerchandisedHolidayComparator> comparators =
         new ArrayList<MerchandisedHolidayComparator>();

      for (final SortParameters parameter : getSortParameters(sortCriteriaCode))
      {

         comparators.add(MerchandisedHolidayComparator.valueOf(parameter.getCode()));
      }
      return comparators;
   }

   /**
    * Get Sort configuration from Inline header component
    *
    * @param sortCriteriaCode
    * @return
    */
   private SortParameters[] getSortParameters(final String sortCriteriaCode)
   {
      SortParameters[] sortParameters = null;
      InlineHeaderComponentModel component = null;
      try
      {
         component =
            (InlineHeaderComponentModel) cmsComponentService.getAbstractCMSComponent(COMPONENT_ID);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOG.error("Component not found", e);
      }
      List<SortParameters> listofParams = null;

      if (null != component)
      {
         for (final SortCriteriaModel sortCriteria : component.getSortCriteria())
         {
            if (sortCriteria.getCode().equalsIgnoreCase(sortCriteriaCode))
            {
               listofParams = sortCriteria.getSortParameters();
               sortParameters = listofParams.toArray(new SortParameters[listofParams.size()]);
               return sortParameters;
            }
         }
      }

      return sortParameters;
   }

   @Override
   public boolean isDealsApplicable(final CategoryModel location)
   {
      return dealsService.isDealsApplicable(location);
   }

   /**
    * @return the configurationService
    */
   public ConfigurationService getConfigurationService()
   {
      return configurationService;
   }

   /**
    * @param configurationService the configurationService to set
    */
   public void setConfigurationService(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;
   }
}
