/**
 *
 */
package uk.co.tui.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.SessionObjectKeys;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.store.FlightExtraFacilityStore;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.book.view.data.ContentViewData;
import uk.co.tui.domain.model.DynamicContentConfigModel;


/**
 * @author amaresh.d
 *
 */
public class FlightOptionsContentViewDataPopulator
        implements
            Populator<Object, ContentViewData> {

    @Resource
    private GenericContentService genericContentService;

    /** The package cart service. */
    @Resource
    private PackageCartService packageCartService;

    /** The session service. */
    @Resource
    private SessionService sessionService;

    /**
     * Populates the flight options content
     */
    @Override
    public void populate(final Object source, final ContentViewData target) {

        final BasePackage packageModel = packageCartService.getBasePackage();
        final FlightExtraFacilityStore flightExtraStore = sessionService
                .getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);
        String flightMealCode = StringUtils.EMPTY;

        final Map<String, List<ExtraFacility>> validExtraFacilitiesMap = flightExtraStore
                .getExtraFacilityFromAllLegsBasedOnCabinClass(packageModel.getId(),PackageUtilityService.getCabinClass(packageModel));
        for (final List<ExtraFacility> eachEntry : validExtraFacilitiesMap
                .values()) {
            final String inventoryCode = eachEntry.get(0)
                    .getExtraFacilityCategory().getInventoryCode();

            final String extraCode = eachEntry.get(0).getExtraFacilityCode();
            final String corporateCode = eachEntry.get(0)
                    .getCorporateCode();
            final List<DynamicContentConfigModel> dynamicContents = genericContentService
                    .getDynamicContentConfig(inventoryCode, extraCode,
                            corporateCode);

            for (final DynamicContentConfigModel dynamicContent : dynamicContents) {
                target.getContentMap().putAll(
                        genericContentService
                                .getGenericDynamicContentValue(dynamicContent));
            }

            if (StringUtils.equalsIgnoreCase(inventoryCode, "FM")) {
                flightMealCode = inventoryCode;
            }
        }
        populateMealContent(flightMealCode, target);
    }

    /**
     * Populate meal content.
     *
     * @param target
     *            the target
     */
    private void populateMealContent(final String flightMealCode,
            final ContentViewData target) {
        if (StringUtils.isEmpty(flightMealCode)) {
            final List<DynamicContentConfigModel> dynamicContents = genericContentService
                    .getDynamicContentConfig("FM", StringUtils.EMPTY,
                            StringUtils.EMPTY);
            for (final DynamicContentConfigModel dynamicContent : dynamicContents) {
                target.getContentMap().putAll(
                        genericContentService
                                .getGenericDynamicContentValue(dynamicContent));
            }
        }
    }

}
