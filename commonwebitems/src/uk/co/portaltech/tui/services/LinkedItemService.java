/**
 *
 */
package uk.co.portaltech.tui.services;

import java.util.List;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;
import uk.co.portaltech.tui.web.view.data.MultiCentreData;
import uk.co.portaltech.tui.web.view.data.wrapper.TwoCentreSelectorViewData;

/**
 * @author
 *
 */
public interface LinkedItemService
{
    /**
     * @param multiCentreCode
     * @return linked items list
     */
    List<AccommodationModel> getLinkedItemsOfAccommodationType(String multiCentreCode);

    /**
     * @param multiCentreCode
     * @return itinerary
     */
    ItineraryViewData getItinerary(String multiCentreCode);

    /**
     * @param multiCentreCode
     * @return itinerary
     */
    ItineraryViewData getDetailedItinerary(String multiCentreCode);

    /**
     * @param multiCentreCode
     * @return two centre selector
     */
    TwoCentreSelectorViewData getTwoCentreSelectorData(String multiCentreCode);

    /**
     * @param code
     * @return
     */
    List<AttractionModel> getLinkedItemsOfAttractionModel(String multiCentrecode);

    /**
     * This method checks either any accommodation from the list is of type SHIP exists or not. If yes,
     * it will return that accommodation. This will be the accommodation of cruise. Otherwise it will return the first accommodation.
     * the accommodation of type SHIP.
     * If any
     * @param multiCentreCode
     * @return accommodation code
     */
    AccommodationModel getAccommodationOfTypeShip(String multiCentreCode);

    /**
     *
     * @param accommodationModel
     * @return
     */
    boolean isMultiCentre(AccommodationModel accommodationModel);

    /**
     * @param code
     * @return List<MultiCentreData>
     */
    List<MultiCentreData> getMultiCentreData(String code);

    /**
     * @param tab
     * @return linked accom code
     */
    String getLinkedAccomCodeByTab(String multiCentreCode, String tab);

    /**
     * @param locationModel
     * @param fdCode
     * @return
     */
    ItineraryViewData getItineraryDataForLocation(LocationModel locationModel, String fdCode);

    ItineraryViewData getItineraryDataForAttrationandExcursion(String code);

}
