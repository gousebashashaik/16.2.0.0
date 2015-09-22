/**
 *
 */
package uk.co.portaltech.tui.utils;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;



/**
 *
 * This class is used to generate cache key during search
 *
 * @author chandrasekhar.v
 *
 */
public class SearchCacheKeyGenerator implements CacheKeyGenerator<Serializable>
{

    @Override
    public Serializable generateKey(final Object... arg0)
    {
        return returnKey(arg0);
    }

    /**
     *
     */
    @SuppressWarnings("boxing")
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
        if (params[0] != null)
        {
            return params[0].hashCode();

        }
        return "";
    }
}
