/**
 *
 */
package uk.co.portaltech.tui.utils;

import de.hybris.platform.core.Registry;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.services.TuiUtilityService;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;


/**
 * @author kandipr
 *
 */
public class GenericKeyGenerator implements CacheKeyGenerator<Serializable>
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
        final StringBuilder key = new StringBuilder();

        for (final Object obj : params)
        {
            if (obj != null)
            {
                key.append(obj.toString());
            }
        }
        final TuiUtilityService tuiUtilityService = (TuiUtilityService) Registry.getApplicationContext().getBean(
                "tuiUtilityService");
        final String brand = tuiUtilityService.getSiteBrand();
        if (StringUtils.isNotBlank(brand))
        {
            key.append(brand);
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
        final Object[] params = arg0;

        return returnKey(params);
    }
}
