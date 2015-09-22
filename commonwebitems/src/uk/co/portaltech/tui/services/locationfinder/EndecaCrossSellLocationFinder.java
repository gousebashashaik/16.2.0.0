/**
 *
 */
package uk.co.portaltech.tui.services.locationfinder;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.travel.thirdparty.endeca.SearchContext;
import uk.co.portaltech.travel.thirdparty.endeca.SearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.services.SearchService;
import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.tui.async.logging.TUILogUtils;



/**
 * @author abi
 *
 */
public class EndecaCrossSellLocationFinder implements LocationFinder
{

    @Resource
    private FlexibleSearchService flexibleSearchService;

    @Resource
    private SearchService searchService;

    @Resource
    private TuiUtilityService tuiUtilityService;

    @Resource
    private ViewSelector viewSelector;


    private static final TUILogUtils LOG = new TUILogUtils("EndecaCrossSellLocationFinder");

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.services.locationfinder.LocationFinder#search(uk.co.portaltech.tui.catalog.Data.SearchRequestData
     * )
     */
    @Override
    public SearchResultData<LocationModel> search(final SearchRequestData request)
    {
        LOG.debug("Searching endeca location cross sell result.");
        final CrossSellCarouselComponentModel carouselModel = (CrossSellCarouselComponentModel) request.getRelevantItem();
        final SearchResultData<LocationModel> result = new SearchResultData<LocationModel>();
        final String lookType = carouselModel.getLookupType().toString();
        final String componentReference = Config.getString(lookType, "");
        if (StringUtils.isBlank(request.getCategoryCode()))
        {
            return (SearchResultData<LocationModel>) Collections.<AccommodationModel> emptyList();
        }
        final Map<String, List<ResultData>> results = find(request, lookType, componentReference);
        if (results.containsKey(componentReference))
        {
            final List<String> codes = new ArrayList<String>(results.size());
            for (final ResultData resultData : results.get(componentReference))
            {
                codes.add(resultData.getCode());
            }
            result.setResults(getAllLocationsForCarousel(codes));
        }
        return result;

    }

    //    @Cacheable(cacheName = "endecaBrowseCache")
    private Map<String, List<ResultData>> find(final SearchRequestData request, final String lookType,
            final String componentReference)
    {



        final SearchContext context = new SearchContext();


        context.getComponentReferences().add(componentReference);
        final String componentTrigger = Config.getString(lookType + "_TRIGGER", "");
        context.getComponentTriggers().add(componentTrigger);

        context.setCategoryCode(request.getCategoryCode());
        if (viewSelector.checkIsMobile())
        {
            context.setMobile(true);
        }
        context.setBrand(tuiUtilityService.getSiteBrand());
        final SearchResult searchResult = searchService.search(context);
        return searchResult.getResults();
    }

    private List<LocationModel> getAllLocationsForCarousel(final List<String> codes)
    {
        final String queryString = "SELECT {PK} FROM {Location} WHERE {code} in (?codes)";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("codes", codes);
        return flexibleSearchService.<LocationModel> search(query).getResult();
    }


}
