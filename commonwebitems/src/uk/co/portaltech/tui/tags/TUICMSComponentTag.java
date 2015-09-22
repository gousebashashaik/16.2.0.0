/**
 *
 */
package uk.co.portaltech.tui.tags;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2lib.cmstags.CMSComponentTag;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.co.tui.async.logging.TUILogUtils;

/**
 * I have overridden this method so that we can choose the prefix for controller URLs. Before the
 * change, we were forced to use /view/.
 *
 * I have highlighted the two changed lines below to enable easy updates in future.
 *
 * @author cxw
 */
public class TUICMSComponentTag extends CMSComponentTag
{

   private static final TUILogUtils LOG = new TUILogUtils("TUICMSComponentTag");

   @Override
   public int doStartTag() throws JspException
   {
      final WebApplicationContext appContext =
         WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
      final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

      final boolean liveEdit = isLiveEdit();
      if (liveEdit)
      {
         final String init = (String) request.getAttribute(INIT_LE_JS);
         if (StringUtils.isEmpty(init))
         {
            initLiveEditJS(request);
            request.setAttribute(INIT_LE_JS, "true");
         }
      }
      try
      {
         final ContentSlotModel contentSlot =
            (ContentSlotModel) request.getAttribute("contentSlot");
         final String prefix =
            contentSlot == null ? "ceid_" : "ceid_" + contentSlot.getUid() + "___";
         for (final SimpleCMSComponentModel element : getComponents(appContext, request))
         {
            final String id = prefix + element.getUid();
            if (liveEdit)
            {
               pageContext.getOut().write("<!-- Start of liveEdit [" + id + "] -->");
               pageContext.getOut().write("<div id=\"" + id + "\">");
            }
            pageContext.setAttribute("componentUid", element.getUid(), PageContext.REQUEST_SCOPE);
            final String code = element.getTypeCode();
            String controllerName = code + "Controller";
            if (!appContext.containsBean(controllerName))
            {
               LOG.debug("No controller defined for ContentElement [" + code
                  + "]. Using default Controller");
               controllerName = DEFAULT_CONTROLLER;
            }

            // Start of changes
            final String controllerPrefix = "/phoenix/";
            try
            {
               pageContext.include(controllerPrefix + controllerName);
            }
            catch (final ServletException e1)
            {
               LOG.warn("Error processing tag: " + e1.getMessage(), e1);
            }
            // End of changes.

            if (liveEdit)
            {
               pageContext.getOut().write("</div>");
               pageContext.getOut().write("<!-- End of liveEdit [" + id + "] -->");
            }
         }
      }
      catch (final IOException e)
      {
         LOG.warn("Error processing tag: " + e.getMessage(), e);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOG.warn("Error processing tag: " + e.getMessage(), e);
      }
      return SKIP_BODY;
   }

   protected void initLiveEditJS(final HttpServletRequest request)
   {
      final JspWriter out = pageContext.getOut();

      try
      {
         out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + request.getContextPath()
            + "/stylesheets/liveEdit.css\"/>");
         out.write("<script type=\"text/javascript\" src=\"" + request.getContextPath()
            + "/js/liveedit.js\"></script>");
      }
      catch (final IOException e)
      {
         LOG.warn("Could not write initial liveEdit JavaScript: " + e.getMessage(), e);
      }
   }

}
