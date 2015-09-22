/**
 *
 */
package uk.co.portaltech.tui.services.productfinder;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.travel.thirdparty.endeca.SearchContext;
import uk.co.portaltech.travel.thirdparty.endeca.SearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.services.SearchService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.tui.web.common.enums.LocationPageType;


/**
 * @author omonikhide
 *
 */
public abstract class AbstractEndecaFinder
{

    @Resource
    private SearchService searchService;

    @Resource
    private SessionService sessionService;

    @Resource
    private ViewSelector viewSelector;



    public SearchResultData<? extends Object> processRequest(final SearchRequestData request, final String lookupType)
    {
        final SearchResultData<ResultData> result = new SearchResultData<ResultData>();
        final SearchContext context = new SearchContext();
        final List<String> productCodes = request.getProductCodes();
        // get the configured endeca component reference

        final String componentReference = Config.getString(lookupType, "");
        context.getComponentReferences().add(componentReference);
        final String componentTrigger = Config.getString(lookupType + "_TRIGGER", "");
        context.getComponentTriggers().add(componentTrigger);
        context.setProductCode(request.getProductCode());
        context.setCategoryCode(request.getCategoryCode());
        context.setPageType(request.getPageType());
        context.setSeoPageType(request.getSeoPageType());
        context.setSortBy(request.getSortBy());
        context.setFacetOptionNames(request.getFacetOptionNames());
        context.setFilterParams(request.getFilterParams());
        context.setBreadCrumbRequired(request.isBreadCrumbRequired());
        context.setTabbedResult(request.isTabbedResult());
        context.setAccommodationTypeContext(request.getAccommodationTypeContext());
        context.setAdditionalParams(request.getAdditionalParams());
        if (productCodes != null && !CollectionUtils.isEmpty(productCodes))
        {
            context.setProductCodes(productCodes);
        }
        if (StringUtils.isNotBlank(request.getNonCoreHolidayType()))
        {
            context.setNonCoreHolidayType(request.getNonCoreHolidayType());
        }

        if (avoidSetBrand(request, componentTrigger))
        {
            //setting brand
            setBrandDetails(context);

        }
        if (viewSelector.checkIsMobile())
        {
            context.setMobile(true);
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

    public EndecaSearchResult createRequest(final SearchRequestData request, final String lookupType)
    {
        final SearchContext context = new SearchContext();
        final String componentReference = Config.getString(lookupType, "");
        context.getComponentReferences().add(componentReference);
        final String componentTrigger = Config.getString(lookupType + "_TRIGGER", "");
        context.getComponentTriggers().add(componentTrigger);
        context.setProductCode(request.getProductCode());
        context.setCategoryCode(request.getCategoryCode());
        //setting brand
        setBrandDetails(context);
        final Map<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("accommodationCode", request.getProductCode() + "|" + context.getBrand().charAt(0));


        context.setAdditionalParams(additionalParams);
        final EndecaSearchResult endecaSearchResultWithoutChild = searchService.searchHolidays(context);
        // Second endeca request to calculate the child price
        additionalParams.put("accommodationCode", request.getProductCode() + "|" + context.getBrand().charAt(0));

        additionalParams.put("infants", "1");
        additionalParams.put("childAge", "7");
        final EndecaSearchResult endecaSearchResultWithChild = searchService.searchHolidays(context);
        for (int i = 0; i < endecaSearchResultWithChild.getHolidays().size(); i++)
        {
            if (endecaSearchResultWithChild.getHolidays().get(i).getTotalPrice() != null
                    && endecaSearchResultWithoutChild.getHolidays().get(i).getTotalPrice() != null)
            {
                endecaSearchResultWithoutChild
                        .getHolidays()
                        .get(i)
                        .setChildPrice(
                                BigDecimal.valueOf((Float.parseFloat(endecaSearchResultWithChild.getHolidays().get(i).getTotalPrice()))
                                        - (Float.parseFloat(endecaSearchResultWithoutChild.getHolidays().get(i).getTotalPrice()))));
            }
        }
        return endecaSearchResultWithoutChild;
    }

    /**
     * For things to do we should not set the brand.
     *
     * @param request
     * @param componentTrigger
     * @return boolean
     */
    private boolean avoidSetBrand(final SearchRequestData request, final String componentTrigger)
    {
        return !(StringUtils.equalsIgnoreCase(LocationPageType.THINGSTODO.getCode(), request.getPageType()) && StringUtils
                .equalsIgnoreCase(componentTrigger, Config.getString("ENDECA_INTERACTIVE_MAP_TRIGGER", "COM_002_InteractiveMap")));
    }

    /**
     * @param context
     */
    protected void setBrandDetails(final SearchContext context)
    {
        final BrandDetails brandDetails = sessionService.getAttribute("brandDetails");
        context.setBrand(brandDetails.getSiteUid());
        context.setSiteId(brandDetails.getSiteUid());
    }




    /**
     * @param context
     */
    protected void setAdditionalParameters(final SearchContext context, final Map<String, String> additionalParams)
    {
        context.setAdditionalParams(additionalParams);

    }
}
