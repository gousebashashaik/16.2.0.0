/**
 *
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;


/**
 * @author omonikhide
 *
 */
public class TuiProductUrlResolver extends TUIUrlResolver<ProductModel>
{

    @Resource
    private CrdToUrlMappingFacade crdToUrlMapFacade;

    @Resource
    private TuiUtilityService tuiUtilityService;


    @Resource
    private TypeService typeService;

    @Override
    public String resolve(final ProductModel source)
    {


        String url = "";

        final String siteBrand = tuiUtilityService.getSiteBrand();
        if ((BrandType.TH.getCode().equals(siteBrand) || BrandType.FJ.getCode().equals(siteBrand))
                && source instanceof AccommodationModel)
        {
            url = crdToUrlMapFacade.getUrlForCRD(source.getCode(),
                    typeService.getEnumerationValue("SearchResultType", SearchResultType.ACCOMMODATION.getCode()).getPk().toString(),
                    typeService.getEnumerationValue("BrandType", BrandType.valueOf(siteBrand).getCode()).getPk().toString());
            if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(overrideSubPageType))
            {
                url = getTabUrl(url, overrideSubPageType);
            }
            overrideSubPageType = null;
            return url;
        }
        else
        {
            return getFCProductUrl(source);
        }
    }
}
