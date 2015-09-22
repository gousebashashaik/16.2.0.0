package uk.co.portaltech.tui.helper;

/**
 *
 */


import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.constants.NumberConstants;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author
 *
 */
@Component("webCacheHelper")
public class WebCacheHelper
{

    private static final String UNDERSCORE = "_";

    private static final TUILogUtils LOG = new TUILogUtils("WebCacheHelper");

    @Resource(name = "sessionService")
    private SessionService sessionService;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;

    @Resource(name = "cmsSiteService")
    private CMSSiteService cmsSiteService;

    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;

    @Resource(name = "tuiUtilityService")
    private TuiUtilityService tuiUtilityService;

    /**
     * Used to make sure we don't cache a preview session
     *
     * @return
     */
    public boolean isPreviewSession(final HttpServletRequest request)
    {
        String previewId = request.getParameter(CMSFilter.PREVIEW_TICKET_ID_PARAM);
        if (StringUtils.isBlank(previewId))
        {
            previewId = sessionService.getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM);
        }

        return StringUtils.isNotEmpty(previewId);
    }

    public String getURL(final HttpServletRequest request)
    {
        return (String) request.getAttribute("javax.servlet.forward.request_uri");
    }

    public String generateKey(final String id, final HttpServletRequest request, final boolean page, final boolean language,
            final String scope)
    {
        final StringBuilder keyBuffer = new StringBuilder();

        keyBuffer.append(id).append('/');
        // if page=true we create a unique key for the page
        if (page)
        {
            final String url = getURL(request);

            // add the server path
            keyBuffer.append(url).append('/');
            // normalise the query string
            keyBuffer.append(parseQueryString(request)).append('/');
        }
        // if language=true we create a unique key for the session language
        if (language)
        {
            keyBuffer.append(commonI18NService.getCurrentLanguage().getIsocode()).append('/');
        }
        // the URI and language and be the same for different sites so add the site as well
        final CMSSiteModel cmsSite = cmsSiteService.getCurrentSite();
        if (cmsSite != null)
        {
            keyBuffer.append(cmsSite.getUid()).append('/');
        }

        // ensure we distinguish between Staged and Online
        final Collection<CatalogVersionModel> catalogVersions = catalogVersionService.getSessionCatalogVersions();
        for (final CatalogVersionModel cv : catalogVersions)
        {
            keyBuffer.append(cv.getPk()).append(':');
        }
        keyBuffer.append('/');

        // if it's session scope we need to add the users session
        if ("session".equalsIgnoreCase(scope))
        {
            keyBuffer.append(request.getSession(true).getId());
        }

        LOG.debug("Generated key [" + keyBuffer + "]");

        return keyBuffer.toString();
    }

    /**
     * Normalises the query string by sorting the parameters by name and removing the jsessionid
     *
     * @return
     */
    protected String parseQueryString(final HttpServletRequest request)
    {
        final Map paramMap = request.getParameterMap();

        if (paramMap.isEmpty())
        {
            return null;
        }

        final Set paramSet = new TreeMap(paramMap).entrySet();
        final StringBuilder buf = new StringBuilder();
        boolean first = true;

        for (final Iterator it = paramSet.iterator(); it.hasNext();)
        {
            final Map.Entry entry = (Map.Entry) it.next();
            final String[] values = (String[]) entry.getValue();

            for (int i = 0; i < values.length; i++)
            {
                final String key = (String) entry.getKey();

                if ((key.length() != NumberConstants.TEN) || !getJSessionIdNameWithBrand().equals(key))
                {
                    if (first)
                    {
                        first = false;
                    }
                    else
                    {
                        buf.append('&');
                    }

                    buf.append(key).append('=').append(values[i]);
                }
            }
        }

        // We get a 0 length buffer if the only parameter was a jsessionid
        if (buf.length() == 0)
        {
            return null;
        }
        else
        {
            return buf.toString();
        }
    }

    /**
     * This is done as per recent change.
     *
     * @return JSESSIONID
     */
    private String getJSessionIdNameWithBrand()
    {
        final StringBuilder jsessionIdName = new StringBuilder("jsessionid");
        jsessionIdName.append(UNDERSCORE);
        jsessionIdName.append(tuiUtilityService.getSiteBrand());
        return jsessionIdName.toString();
    }

}