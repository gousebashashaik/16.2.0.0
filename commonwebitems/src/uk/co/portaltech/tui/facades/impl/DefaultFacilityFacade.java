/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.services.FacilityService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.FacilityConverter;
import uk.co.portaltech.tui.converters.FacilityOption;
import uk.co.portaltech.tui.facades.FacilityFacade;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 *
 * The default implementation of the {@link FacilityFacade} interface
 *
 * @author l.furrer
 *
 */
public class DefaultFacilityFacade implements FacilityFacade
{

    @Resource
    private FacilityService facilityService;

    @Resource
    private FacilityConverter facilityConverter;

    private ConfigurablePopulator<FacilityModel, FacilityViewData, FacilityOption> facilityConfiguredPopulator;

    @Resource
    private CMSSiteService cmsSiteService;

    /** New field created for fetching the brand related information. */
    @Resource
    private SessionService sessionService;

    @Required
    public void setFacilityConfiguredPopulator(
            final ConfigurablePopulator<FacilityModel, FacilityViewData, FacilityOption> facilityConfiguredPopulator)
    {
        this.facilityConfiguredPopulator = facilityConfiguredPopulator;
    }

    /**
     * Sets the {@link FacilityService}
     */
    public void setFacilityService(final FacilityService facilityService)
    {
        this.facilityService = facilityService;
    }

    /**
     * Sets the {@link FacilityConverter}
     */
    public void setFacilityConverter(final FacilityConverter facilityConverter)
    {
        this.facilityConverter = facilityConverter;
    }

    /**
     * Sets the {@link SessionService}
     */
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    /**
     * Gets the facility according to its code.
     */
    @Override
    public FacilityViewData getFacility(final String code)
    {
        /*
         * Added as part of Sprint 2(Core Travel Changes) : facilityService Layer Changes 1. Obtain the brand details from
         * sessionService stored in the attribute named brandDetails. 2. Obtain the brandPks from brandDetails. 3. Then,
         * pass these brandPks to getFacilityForCode method.
         */

        final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
        final List<String> brandPks = brandDetails.getRelevantBrands();

        //Pass the new parameter, brandPks as the third argument.
        final FacilityModel facilityModel = facilityService.getFacilityForCode(code, cmsSiteService.getCurrentCatalogVersion(),
                brandPks);

        if (facilityModel == null)
        {
            return null;
        }
        return facilityConverter.convert(facilityModel);
    }

    @Override
    public List<FacilityViewData> getFacilitiesByAccommodationAndType(final String accommodationCode, final String facilityTypeCode)
    {
        /*
         * Added as part of Sprint 2(Core Travel Changes): facilityService Layer Changes 1. Obtain the brand details from
         * sessionService stored in the attribute named brandDetails. 2. Obtain the brandPks from brandDetails. 3. Then,
         * pass these brandPks to getFacilitiesForAccommodationAndType method.
         */

        final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
        final List<String> brandPks = brandDetails.getRelevantBrands();

        //Pass the new parameter, brandPks as the third argument.
        final List<FacilityModel> facilitiesModel = facilityService.getFacilitiesForAccommodationAndType(accommodationCode,
                facilityTypeCode, cmsSiteService.getCurrentCatalogVersion(), brandPks);

        final Iterator<FacilityModel> itr = facilitiesModel.iterator();
        final List<FacilityViewData> result = new ArrayList<FacilityViewData>();
        while (itr.hasNext())
        {
            final FacilityModel facility = itr.next();
            final FacilityViewData facilityData = facilityConverter.convert(facility);
            facilityConfiguredPopulator.populate(facility, facilityData, Arrays.asList(FacilityOption.FACILITY_BASIC));
            result.add(facilityData);
        }
        return result;
    }

}
