/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.util.Config;

import java.util.Arrays;

import javax.annotation.Resource;

import uk.co.tui.book.converters.PackageOption;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.book.view.data.PackageViewData;

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

    @Resource(name = "defaultPackageConfiguredPopulator")
    private ConfigurablePopulator<BasePackage, PackageViewData, PackageOption> packageConfiguredPopulator;

    /**
     * This method populates packageViewData object from InclusivePackageModel
     * object.
     *
     * @param packageModel
     *            - InclusivePackageModel
     * @param target
     *            - PackageViewData object
     */

    @Override
    public void populate(final BasePackage packageModel,
            final PackageViewData target) {

        packageConfiguredPopulator.populate(packageModel, target, Arrays
                .asList(PackageOption.CONTENT, PackageOption.FLIGHT,
                        PackageOption.PRICE_BREAKDOWN,
                        PackageOption.ACCOMMODATION,
                        PackageOption.PAX_COMPOSITION, PackageOption.MEMO,
                        PackageOption.PRICE));
        target.setSaved(packageModel.isSaved());
        target.setDisplaySaveButton(Boolean.parseBoolean(Config
                .getParameter("displaySaveButton")));
        target.setMulticomThirdPartyFlight(PackageUtilityService
                .isMulticomThirdPartyFlight(packageComponentService
                        .getFlightItinerary(packageModel)));
        target.setTracsThirdPartyFlight(PackageUtilityService
                .isTRACSThirdPartyFlight(packageComponentService
                        .getFlightItinerary(packageModel)));
    }

}
