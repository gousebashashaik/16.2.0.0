/**
 *
 */
package uk.co.portaltech.tui.services.productfinder;

import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.travel.thirdparty.endeca.SearchContext;
import uk.co.portaltech.travel.thirdparty.endeca.SearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.services.SearchService;
import uk.co.portaltech.tui.components.model.VillaAvailabilityComponentModel;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.VillaAvailabilitySearchRequestData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.CarouselLookupType;


/**
 * @author ext
 *
 */
public class EndecaVillaAvailabilityFinder extends AbstractEndecaFinder implements ProductFinder
{



    private static final TUILogUtils LOG = new TUILogUtils("EndecaVillaAvailabilityFinder");

    @Resource
    private SearchService searchService;


    @Override
    public SearchResultData<ResultData> search(final SearchRequestData request)
    {
        LOG.debug("Searching for Villa Availablity.");
        final VillaAvailabilityComponentModel villaAvailabilityModel = (VillaAvailabilityComponentModel) request.getRelevantItem();

        final SearchResultData<ResultData> result = new SearchResultData<ResultData>();
        final SearchContext context = new SearchContext();
        // get the configured endeca component reference

        final String componentReference = Config.getString(villaAvailabilityModel.getLookupType().toString(), "");
        context.getComponentReferences().add(componentReference);
        final String componentTrigger = Config.getString(villaAvailabilityModel.getLookupType().toString() + "_TRIGGER", "");
        context.getComponentTriggers().add(componentTrigger);
        context.setProductCode(request.getProductCode());

        context.setPageType(request.getPageType());
        context.setSeoPageType(request.getSeoPageType());
        context.setSortBy(request.getSortBy());
        context.setFacetOptionNames(request.getFacetOptionNames());
        context.setFilterParams(request.getFilterParams());
        context.setBreadCrumbRequired(request.isBreadCrumbRequired());
        context.setTabbedResult(request.isTabbedResult());


        setBrand(context);

        if (request instanceof VillaAvailabilitySearchRequestData)
        {
            populateAdditionalParameters((VillaAvailabilitySearchRequestData) request, context);
        }
        final SearchResult searchResult = searchService.search(context);
        final Map<String, List<ResultData>> results = searchResult.getResults();
        if (results.containsKey(componentReference))
        {
            result.setResults(results.get(componentReference));
        }
        result.setPagination(searchResult.getPaginationData());
        result.setSortOptions(searchResult.getSortOptions());
        result.setHeading(searchResult.getHeading());
        result.setTabResults(searchResult.getTabResults());
        return result;



    }

    // Retail Changes
    private void setBrand(final SearchContext context)
    {
        //setting brand
        setBrandDetails(context);
        if (context.getBrand() == null)
        {
            context.setBrand("TH");
        }
    }

    /**
     * @param request
     * @param context
     */
    private void populateAdditionalParameters(final VillaAvailabilitySearchRequestData request, final SearchContext context)
    {
        final Map<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("stay", request.getStay() + StringUtils.EMPTY);
        additionalParams.put("sdate", request.getStartDate());
        additionalParams.put("edate", request.getEndDate());
        additionalParams.put("accommodationCode", request.getProductCode() + "|" + context.getBrand().charAt(0));
        additionalParams.put("adults", request.getAdults() + StringUtils.EMPTY);
        final int children = request.getChildren();
        addChildren(additionalParams, children);

        setAdditionalParameters(context, additionalParams);
    }

    /**
     * @param additionalParams
     * @param children
     */
    private void addChildren(final Map<String, String> additionalParams, final int children)
    {
        if (children > 0)
        {
            final String paramName = "childAge";
            final String defParamValue = "5";

            //populating paramName 'children' no. of times...
            final StringBuilder childParamAndValue = new StringBuilder();
            populateChildAgeParam(children, paramName, defParamValue, childParamAndValue);
            additionalParams.put(paramName, childParamAndValue.toString());
        }
    }

    /**
     * This method exist only because Endeca uses duplicate parameter names in case of Child ages for a search.
     *
     * @param childrenParam
     * @param paramName
     * @param defParamValue
     * @param childParamAndValue
     */
    private void populateChildAgeParam(final int childrenParam, final String paramName, final String defParamValue,
            final StringBuilder childParamAndValue)
    {
        int children = childrenParam;
        childParamAndValue.append(defParamValue);
        children--;
        if (children > 0)
        {
            do
            {
                childParamAndValue.append("&");
                childParamAndValue.append(paramName);
                childParamAndValue.append("=");
                childParamAndValue.append(defParamValue);
                children--;
            }
            while (children > 0);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#accomSearch(uk.co.portaltech.tui.web.view.data.
     * SearchRequestData)
     */
    @Override
    public SearchResultData<? extends Object> accomSearch(final SearchRequestData searchRequestData,
            final CarouselLookupType carouselLookupType)
    {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#searchHolidays(uk.co.portaltech.tui.web.view.data.
     * SearchRequestData)
     */
    @Override
    public EndecaSearchResult searchHolidays(final SearchRequestData request)
    {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.services.productfinder.ProductFinder#priceForRecSearch(uk.co.portaltech.tui.web.view.data
     * .SearchRequestData)
     */
    @Override
    public SearchResultData<ResultData> priceForRecSearch(final SearchRequestData requestData)
    {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.productfinder.ProductFinder#search(uk.co.portaltech.tui.web.view.data.
     * SearchResultsRequestData)
     */
    @Override
    public SearchResultData<? extends Object> searchRecPack(final SearchResultsRequestData request)
    {

        return null;
    }
}
