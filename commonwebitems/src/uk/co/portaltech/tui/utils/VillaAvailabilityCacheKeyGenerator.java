/**
 *
 */
package uk.co.portaltech.tui.utils;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.tui.web.view.data.wrapper.VillaAvailabilityRequest;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;


/**
 * @author kandipr
 *
 */
public class VillaAvailabilityCacheKeyGenerator implements CacheKeyGenerator<Serializable>
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

        if (params[1] != null)
        {

            final VillaAvailabilityRequest request = VillaAvailabilityRequest.class.cast(params[1]);
            key.append(request.getCode()).append(request.getDirection()).append(request.getNoOfAdults())
                    .append(request.getNoOfChildren()).append(request.getRequestType())
                    .append(DateUtils.format(request.getStartDate()));
        }

        return Integer.valueOf(key.hashCode());
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
