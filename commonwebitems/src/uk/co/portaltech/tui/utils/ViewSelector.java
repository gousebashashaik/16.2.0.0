/**
 *
 */
package uk.co.portaltech.tui.utils;

import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

/**
 * @author akhileshvarma.d
 *
 */
public class ViewSelector
{

   @Resource
   private SessionService sessionService;

   private static final String MOBILE = "Mobile";

   public String getViewName(final String baseName)
   {

      String tempBaseName = baseName;
      if (checkIsMobile())
      {
         tempBaseName = tempBaseName + "_mobile";
      }
      return tempBaseName;

   }

   public boolean checkIsMobile()
   {
      final Object o = sessionService.getAttribute(MOBILE);
      return o != null ? ((String) o).equalsIgnoreCase(MOBILE) ? true : false : false;
   }

}
