/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;

import uk.co.portaltech.tui.web.view.data.ViewData;

/**
 * @author l.furrer
 *
 */
public interface AttractionFacade {

    /**
     * Gets an attraction according to its code populating it with a list of its Usps
     *
     * @param attractionCode
     *            the code of the attraction
     * @return a dto for attraction
     */
     ViewData getAttractionUspsByCode(String attractionCode);

    /**
     * Get the editorial content for an Attraction
     *
     * @param attractionCode
     *            The code of the attraction
     * @return {@link AttractionViewData} The view data for this attraction
     */
     AttractionViewData getEditorialContent(String attractionCode);

    /**
     * Get the result pane/overview content for an Attraction
     *
     * @param attractionCode
     *            The code of the attraction
     * @return {@link AttractionViewData} The view data for this attraction
     */

    /**
     *
     * @param resultData
     * @return list of AttractionviewData
     */
    List<AttractionViewData> getAttractionEnrichedData(List<ResultData> resultData);

    /**
     * @param attractionCode
     * @return
     */
    AttractionViewData getAttractionData(String attractionCode);

    /**
     * @param locationModel
     * @return
     */
    ItineraryViewData getNonBookableAttractions(LocationModel locationModel);
}
