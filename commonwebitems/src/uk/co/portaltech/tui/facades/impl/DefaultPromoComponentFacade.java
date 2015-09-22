/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.components.model.PromoComponentModel;
import uk.co.portaltech.tui.facades.PromoComponentFacade;
import uk.co.portaltech.tui.web.view.data.PromoComponentData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.ExtraFacilityUpdator;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.populators.PackageViewDataPopulator;
import uk.co.tui.book.services.PromotionalCodeValidationService;
import uk.co.tui.book.view.data.PassengerDetailsStaticContentViewData;
import uk.co.tui.book.view.data.PassengerDetailsViewData;


/**
 * @author pts
 *
 */
public class DefaultPromoComponentFacade implements PromoComponentFacade
{

    @Resource
    private CMSComponentService cmsComponentService;

    @Resource
    private Converter<PromoComponentModel, PromoComponentData> promoComponentConverter;

    @Resource
    private PackageViewDataPopulator packageViewDataPopulator;

    /** The promotional code validation service. */
    @Resource
    private PromotionalCodeValidationService promotionalCodeValidationService;

    /** The passenger static content view data populator. */
    @Resource
    private Populator<Object, PassengerDetailsStaticContentViewData> passengerStaticContentViewDataPopulator;

    /** The extra facility updator. */
    @Resource
    private ExtraFacilityUpdator extraFacilityUpdator;

    /** The package cart service. */
    @Resource
    private PackageCartService packageCartService;

    private static final TUILogUtils LOG = new TUILogUtils("DefaultPromoComponentFacade");

    @Override
    public PromoComponentData gePromoComonentViewData(final String componentUID)
    {
        PromoComponentData promoComonentViewData = null;
        try
        {
            if (StringUtils.isNotEmpty(componentUID))
            {
                final PromoComponentModel promoComponentModel = (PromoComponentModel) cmsComponentService
                        .getSimpleCMSComponent(componentUID);
                promoComonentViewData = promoComponentConverter.convert(promoComponentModel);
            }
        }
        catch (final CMSItemNotFoundException e)
        {

            LOG.error(" Error occured while creating promo component", e);
        }

        return promoComonentViewData;
    }

    /**
     * Removes the existing promo code.
     *
     * @return the passenger details view data
     */
    @Override
    public PassengerDetailsViewData removeExistingPromoCode()
    {
        final PassengerDetailsViewData passengerDetailsViewData = new PassengerDetailsViewData();
        final BasePackage packageModel = packageCartService.getBasePackage();
        promotionalCodeValidationService.removePromotionalCode(packageModel);
        packageViewDataPopulator.populate(packageModel, passengerDetailsViewData.getPackageViewData());
        extraFacilityUpdator.updatePackageViewData(packageModel, passengerDetailsViewData.getPackageViewData());
        populatePassengerStaticContentViewData(passengerDetailsViewData);
        return passengerDetailsViewData;
    }

    /**
     * Populate passenger static content view data.
     *
     * @param viewData
     *           the view data
     */
    private void populatePassengerStaticContentViewData(final PassengerDetailsViewData viewData)
    {
        final PassengerDetailsStaticContentViewData passengerDetailsStaticContentViewData = new PassengerDetailsStaticContentViewData();
        passengerStaticContentViewDataPopulator.populate(new Object(), passengerDetailsStaticContentViewData);
        viewData.setPassengerDetailsStaticContentViewData(passengerDetailsStaticContentViewData);
    }

}
