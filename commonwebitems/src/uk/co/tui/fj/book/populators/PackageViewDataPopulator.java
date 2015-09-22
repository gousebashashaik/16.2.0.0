/**
 *
 */
package uk.co.tui.fj.book.populators;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.util.Config;

import java.util.Arrays;

import javax.annotation.Resource;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.fj.book.converters.PackageOption;
import uk.co.tui.fj.book.view.data.PackageViewData;

/**
 * The Class PackageViewDataPopulator. populator class used to populating the
 * package view data from the inclusive package.
 *
 * @author munisekhar.k
 */
public class PackageViewDataPopulator
        implements
            Populator<BasePackage, PackageViewData> {

    /** The package component service. */
    @Resource
    private PackageComponentService packageComponentService;

    @Resource(name = "fjDefaultPackageConfiguredPopulator")
    private ConfigurablePopulator<BasePackage, PackageViewData, PackageOption> packageConfiguredPopulator;

    /**
     * This method populates packageViewData object from InclusivePackageModel
     * object.
     *
     * @param source
     *            - InclusivePackageModel
     * @param target
     *            - PackageViewData object
     */

    @Override
    public void populate(final BasePackage source, final PackageViewData target) {

        packageConfiguredPopulator.populate(source, target, Arrays.asList(
                PackageOption.CONTENT, PackageOption.FLIGHT,
                PackageOption.PRICE_BREAKDOWN, PackageOption.ACCOMMODATION,
                PackageOption.LAPLAND, PackageOption.PAX_COMPOSITION,
                PackageOption.MEMO, PackageOption.PRICE,
                PackageOption.MULTI_CENTRE));
        target.setDisplaySaveButton(Boolean.parseBoolean(Config
                .getParameter("displaySaveButton")));
        target.setMulticomThirdPartyFlight(PackageUtilityService
                .isMulticomThirdPartyFlight(packageComponentService
                        .getFlightItinerary(source)));
        target.setTracsThirdPartyFlight(PackageUtilityService
                .isTRACSThirdPartyFlight(packageComponentService
                        .getFlightItinerary(source)));
        populateBrandInformation(source,target);
    }

    /**
     * Populate brand information.
     *
     * @param source the source
     * @param target the target
     */
    private void populateBrandInformation(BasePackage source,
            PackageViewData target) {
        target.setBrandType(source.getBrandType().toString());
        if (source.getCrossBrandType() != null) {
            target.setCrossBrandType(source.getCrossBrandType().toString());
        }
    }
}
