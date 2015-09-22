/**
 *
 */
package uk.co.portaltech.tui.utils;

import de.hybris.platform.core.Registry;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;

import uk.co.portaltech.tui.services.TuiUtilityService;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;

/**
 * @author kandipr
 *
 */
public class CrossSellCacheKeyGenerator implements CacheKeyGenerator<Serializable>
{
   TuiUtilityService tuiUtilityService = (TuiUtilityService) Registry.getApplicationContext()
      .getBean("tuiUtilityService");

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see
    * com.googlecode.ehcache.annotations.key.CacheKeyGenerator#generateKey(org.aopalliance.intercept
    * .MethodInvocation)
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

         key.append(params[1]);

      }
      else
      {
         key.append(params[TWO]);
      }

      key.append(tuiUtilityService.getSiteBrand());
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
