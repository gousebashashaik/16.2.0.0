/**
 *
 */
package uk.co.portaltech.tui.facades;

import de.hybris.platform.core.model.ItemModel;

import java.util.List;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;
import uk.co.portaltech.tui.web.view.data.ViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.PriceAndAvailabilityWrapper;

/**
 * @author l.furrer
 *
 */
public interface ExcursionFacade {

    /**
     * Gets an excursion according to its code populating it with a list of its Usps
     *
     * @param excursionCode
     *            the code of the excursion
     * @return a dto for excursion
     */
    ExcursionViewData getExcursionUspsByCode(String excursionCode);

    ExcursionViewData getRestrictionInfo(String excursionCode);

    ViewData getExcursionNameComponentData(String excursionCode);

    List<ExcursionViewData> getExcursionsWithEndecaData(List<ResultData> list);

    PriceAndAvailabilityWrapper getLowsetPriceExcursionForLocation(LocationModel location, ItemModel item);

}
