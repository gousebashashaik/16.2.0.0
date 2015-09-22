/**
 *
 */
package uk.co.portaltech.tui.utils;

import de.hybris.platform.core.Registry;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;

import uk.co.portaltech.tui.components.model.BookingComponentModel;
import uk.co.portaltech.tui.services.TuiUtilityService;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;


/**
 * @author kandipr
 *
 */
public class BrowseAccomPriceKeyGenerator implements CacheKeyGenerator<Serializable>
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

        if (params[0] != null && params[1] != null)
        {
            final TuiUtilityService tuiUtilityService = (TuiUtilityService) Registry.getApplicationContext().getBean(
                    "tuiUtilityService");

            key.append(params[0]).append(BookingComponentModel.class.cast(params[1]).getLookupType().getCode())
                    .append(tuiUtilityService.getSiteBrand());

        }

        return key.toString();
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
