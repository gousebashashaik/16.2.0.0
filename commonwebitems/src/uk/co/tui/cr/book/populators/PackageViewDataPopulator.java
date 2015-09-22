/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.util.Config;

import java.util.Arrays;

import javax.annotation.Resource;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.cr.book.converters.PackageOption;
import uk.co.tui.cr.book.view.data.PackageViewData;

/**
 * The Class PackageViewDataPopulator. populator class used to populating the
 * package view data from the inclusive package.
 *
 * @author munisekhar.k
 */
public class PackageViewDataPopulator
        implements
            Populator<BasePackage, PackageViewData> {

    @Resource(name = "crDefaultPackageConfiguredPopulator")
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
                PackageOption.FLIGHT, PackageOption.CONTENT,
                PackageOption.PRICE_BREAKDOWN, PackageOption.STAY,
                PackageOption.PAX_COMPOSITION, PackageOption.MEMO,
                PackageOption.PRICE));

        target.setDisplaySaveButton(Boolean.parseBoolean(Config
                .getParameter("displaySaveButton")));
    }
}
