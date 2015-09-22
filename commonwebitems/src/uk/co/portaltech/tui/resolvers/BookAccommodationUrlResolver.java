/**
 *
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.brand.BrandDetails;



/**
 *
 *
 */
public class BookAccommodationUrlResolver extends TUIUrlResolver<ProductModel>
{

    @Resource
    private SessionService sessionService;

    @Resource
    private ConfigurationService configurationService;



    private static final String PRODUCT_CODE = "productCode";

    private static final String TAB = "tab";

    private static final String AND = "&";

    private static final String EQUALS = "=";

    private static final String QUESTION = "?";

    @Override
    public String resolve(final ProductModel source)
    {


        final StringBuilder url = new StringBuilder(pattern);
        if (source != null)
        {
            url.append(QUESTION).append(PRODUCT_CODE).append(EQUALS).append(source.getCode()).append(AND);
        }
        if (StringUtils.isNotBlank(overrideSubPageType))
        {

            url.append(TAB).append(EQUALS).append(overrideSubPageType).append(AND);
        }
        else
        {
            url.append(TAB).append(EQUALS).append(defaultSubPageType).append(AND);
        }

        overrideSubPageType = null;
        return getContext() + url.toString();
    }

    @Override
    public String getContext()
    {

        final BrandDetails brandDetails = sessionService.getCurrentSession().getAttribute("brandDetails");

        if ("thomson".equalsIgnoreCase(brandDetails.getSiteName()))
        {
            context = configurationService.getConfiguration().getString("thweb.webroot");
        }
        else if ("falcon".equalsIgnoreCase(brandDetails.getSiteName()))
        {

            context = configurationService.getConfiguration().getString("fjweb.webroot");
        }
        else if ("firstchoice".equalsIgnoreCase(brandDetails.getSiteName()))
        {

            context = configurationService.getConfiguration().getString("tuiweb.webroot");
        }

        if ("retail".equalsIgnoreCase(brandDetails.getSiteName()))
        {

            context = configurationService.getConfiguration().getString("rtweb.webroot");
        }

        return context;
    }


}
