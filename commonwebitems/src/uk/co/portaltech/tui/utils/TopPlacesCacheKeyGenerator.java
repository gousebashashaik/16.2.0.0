/**
 *
 */
package uk.co.portaltech.tui.utils;

import de.hybris.platform.core.Registry;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;

import uk.co.portaltech.tui.components.model.DestinationQSAndTopPlacesComponentModel;
import uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.constants.NumberConstants;
import uk.co.portaltech.tui.services.TuiUtilityService;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;


/**
 * @author kandipr
 *
 */
public class TopPlacesCacheKeyGenerator implements CacheKeyGenerator<Serializable>
{

    /*
     * (non-Javadoc)
     *
     * @see
     * com.googlecode.ehcache.annotations.key.CacheKeyGenerator#generateKey(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Serializable generateKey(final MethodInvocation arg0)
    {
        final Object[] params = arg0.getArguments();
        return returnKey(params);
    }

    /**
     * @param params
     * @return
     */
    private Serializable returnKey(final Object[] params)
    {
        final StringBuilder key = new StringBuilder("");
        checkNumberConstants(params, key);

        if (params[NumberConstants.THREE] != null)
        {
            String lookupType = "";
            if (params[NumberConstants.THREE] instanceof TopPlacesToStayCarouselComponentModel)
            {
                lookupType = TopPlacesToStayCarouselComponentModel.class.cast(params[NumberConstants.THREE]).getTopPlacesCarousels().get(0)
                        .getLookupType().getCode();
            }
            else if (params[NumberConstants.THREE] instanceof DestinationQSAndTopPlacesComponentModel)
            {
                lookupType = DestinationQSAndTopPlacesComponentModel.class.cast(params[NumberConstants.THREE]).getTopPlacesCarousels().get(0)
                        .getLookupType().getCode();
            }
            key.append(lookupType.hashCode());
        }
        final TuiUtilityService tuiUtilityService = (TuiUtilityService) Registry.getApplicationContext().getBean(
                "tuiUtilityService");

        key.append(tuiUtilityService.getSiteBrand().hashCode());


        return key.toString();
    }

    /**
     * @param params
     * @param key
     */
    private void checkNumberConstants(final Object[] params, final StringBuilder key)
    {
        if (params[NumberConstants.ZERO] != null)
        {
            key.append(params[0].hashCode());
        }
        if (params[NumberConstants.ONE] != null)
        {
            key.append(params[NumberConstants.ONE].hashCode());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.ehcache.annotations.key.CacheKeyGenerator#generateKey(java.lang.Object[])
     */
    @Override
    public Serializable generateKey(final Object... arg0)
    {
        return returnKey(arg0);
    }
}
