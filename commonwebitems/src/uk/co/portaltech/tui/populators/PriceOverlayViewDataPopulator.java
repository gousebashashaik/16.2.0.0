/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.breadcrumb.impl.BreadcrumbBuilder;
import uk.co.portaltech.tui.web.view.data.PriceOverlayViewData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author sunilkumar.sahu
 *
 */
public class PriceOverlayViewDataPopulator implements Populator<AccommodationModel, PriceOverlayViewData>
{

    @Resource
    private FeatureService featureService;

    @Resource
    private BreadcrumbBuilder breadcrumbBuilder;

    @Resource(name = "sessionService")
    private SessionService sessionService;

    /**
     * Retrieves a target(PriceOverlayViewData), from source (ProductModel).
     *
     * @param source
     * @param target
     */
    @Override
    public void populate(final AccommodationModel source, final PriceOverlayViewData target) throws ConversionException
    {
        target.settRating(featureService.getFirstFeatureValueAsString("tRating", source, new Date(), null));
        target.setAccomName(source.getName());
        target.setCode(source.getCode());
        target.setLocationMap(breadcrumbBuilder.getLocationMap(source));

        final BrandDetails brandDetails = (BrandDetails) sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
        for (final BrandType brandType : source.getBrands())
        {
            if (StringUtils.equalsIgnoreCase(brandType.getCode(), brandDetails.getSiteUid()))
            {
                target.settRatingCss("falcon");
                break;
            }
            target.settRatingCss("");
        }
    }
}
