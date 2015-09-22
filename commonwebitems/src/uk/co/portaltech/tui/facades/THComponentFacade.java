/**
 *
 */
package uk.co.portaltech.tui.facades;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.CarHireViewData;
import uk.co.portaltech.tui.web.view.data.DreamLinerCarouselViewData;
import uk.co.portaltech.tui.web.view.data.GenericContentViewData;
import uk.co.portaltech.tui.web.view.data.SafariBusPlanData;


/**
 * THComponentFacade.java
 * <p>
 * Represents a high level interface that makes client easier to access the subsystem.
 *
 * @author narendra.bm
 *
 */
public interface THComponentFacade
{
    /**
     * Represents to get CarHireViewData object that consists of description and logo of the car hire supplier based on
     * the accommodation code.
     * <p>
     * If accommodation code is null, this method returns null.
     * <p>
     *
     * @param accommodationCode
     *           represents either villa or accommodation code.
     * @return returns CarHireViewData if accommodation code and contentValueModel are not null, otherwise null.
     */
    CarHireViewData getCarHireViewData(String accommodationCode);

    /**
     * Represents to Return DreamLinerCarouselViewData object that consists of images and dream liner usps and associated
     * usp's description and media. If accommodation code is passed, then it fetches location for that accommodation. If
     * location is available, then it checks either this location is associated with any dreamliner. If yes, then fetch
     * media and usps data for the dream liner.
     * <p>
     *
     * @param code
     *           it can be either product code or category code.
     * @param type
     *           it can be either accommodation or location.
     * @return DreamLinerCarouselViewData consists of media and usps data for the dream liner.
     * @throws BusinessException
     *            Throws {@link BusinessException} when type is invalid.
     */
    DreamLinerCarouselViewData getDreamLinerCarouselViewData(final String code, final String type) throws BusinessException;

    AccommodationViewData getNonGeoHeroCarouselData(String componentUid);

    GenericContentViewData getNonGeoEditorialContent(String noncoreHolidayTypeCode);

    /**
     *
     * @param productCode
     * @return
     */
    SafariBusPlanData getSafariBusPlanComponentData(String productCode);

    GenericContentViewData getNonCoreProductName(String noncoreHolidayTypeCode, String categoryCode);

    /**
     * Retrieves price data provided by Endeca related to an accommodation for booking
     *
     * @param componentUid
     *           UID of Booking Component
     * @param accommodationCode
     *           The accommodation code
     * @return Accommodation view data containing the result searching performed on Endeca
     */
    AccommodationViewData getAccommodationPrice(String componentUid, String accommodationCode);

    List<GenericContentViewData> getNeedToKnowContent(String holidayTypeCode);
}
