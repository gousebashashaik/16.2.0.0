/**
 *
 */
package uk.co.portaltech.tui.utils;

import de.hybris.platform.core.Registry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author ext
 *
 */
public final class BeanUtil
{

   private static final TUILogUtils LOG = new TUILogUtils("BeanUtil");

   private BeanUtil()
   {

   }

   public static <T> T getSpringBean(final String beanName, final Class<T> beanClass)
   {
      T bean = null;

      if (StringUtils.isNotBlank(beanName))
      {
         try
         {
            bean = (T) Registry.getApplicationContext().getBean(beanName);
         }
         catch (final NoSuchBeanDefinitionException ex)
         {
            LOG.warn("No bean found with the specified name. Trying to resolve bean using type...",
               ex);
         }
      }
      if (bean == null)
      {
         processBean(beanClass);
      }
      return bean;
   }

   /**
    * @param beanClass
    */
   private static <T> void processBean(final Class<T> beanClass)
   {
      if (beanClass == null)
      {
         LOG.warn("No bean could be resolved. Reason: No type specified.");
      }
      else
      {
         Registry.getApplicationContext().getBean(beanClass);
      }
   }
}
