/**
 *
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;


/**
 * @author yuvarani.v Resolves the Accomodation and Location type urls for component controllers
 */
public class ThomsonURLResolver extends TUIUrlResolver<ItemModel>
{

    @Resource
    private CrdToUrlMappingFacade crdToUrlMapFacade;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.url.UrlResolver#resolve(java.lang.Object)
     */
    @Override
    public String resolve(final ItemModel source)
    {

        String url = "/destinations/holiday-destinations.html";
        SearchResultType searchType = SearchResultType.ACCOMMODATION;
        String crd = StringUtils.EMPTY;
        if (source != null)
        {
            if (source instanceof CategoryModel)
            {

                searchType = SearchResultType.LOCATION;
                crd = ((CategoryModel) source).getCode();
                url = crdToUrlMapFacade.getUrlForCRD(crd, searchType);
                if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(overrideSubPageType))
                {
                    url = getTabUrl(url, overrideSubPageType);
                }

            }
            else if (source instanceof ProductModel)
            {

                crd = ((ProductModel) source).getCode();
                url = crdToUrlMapFacade.getUrlForCRD(crd, searchType);
                if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(overrideSubPageType))
                {
                    url = getTabUrl(url, overrideSubPageType);
                }
            }
        }
        return url;
    }

}
