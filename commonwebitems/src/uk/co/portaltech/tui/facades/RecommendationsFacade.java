/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.RecommendationsData;


/**
 * @author Raja Rao.R
 *
 */
public interface RecommendationsFacade
{

    /**
     * @param productCodes
     * @return
     */
    @SuppressWarnings("javadoc")
    RecommendationsData getProductProductRanges(List<String> productCodes, String siteBrand);

    /**
     * @param productCodes
     * @param cspSorting
     * @return
     */
    @SuppressWarnings("javadoc")
    RecommendationsData getRecommendedAccomPriceInfoBrowse(List<String> productCodes, Boolean cspSorting, String siteBrand);

    /**
     * @param brandtype
     * @param productCodes
     * @param cspSorting
     * @return
     */
    @SuppressWarnings("javadoc")
    RecommendationsData getReccommendedHolidayPackageDataBook(String brandtype, List<String> productCodes, Boolean cspSorting);

    String baynoteStubStatus();

}
