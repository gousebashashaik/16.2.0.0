package uk.co.portaltech.tui.tags;

/**
 *
 */

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import net.sf.ehcache.Element;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.helper.WebCacheHelper;
import uk.co.portaltech.tui.utils.BeanUtil;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author
 *
 */
public class EhCacheTag extends BodyTagSupport implements TryCatchFinally
{

   /**
     *
     */
   private static final String IO_EXCEPTION = "IOException";

   private final CacheUtil cacheUtil = BeanUtil.getSpringBean("cacheUtil", CacheUtil.class);

   private final WebCacheHelper webCacheHelper = BeanUtil.getSpringBean("webCacheHelper",
      WebCacheHelper.class);

   private final TUIConfigService tuiConfigService = BeanUtil.getSpringBean("tuiConfigService",
      TUIConfigService.class);

   private static final String CACHING_ENABLED = "fragmentcaching.enabled";

   private static final String APPLICATION = "application";

   /** time to live, overrides the time to live set for the cache in ehcache.xml */
   private Integer ttl;

   /** the name if the cache, matches what's in ehcache.xml */
   private String cache;

   /**
    * scope for the cache, default is application, be careful with session because we will have
    * cached content per user which could result in high memory usage
    */
   private String scope = APPLICATION;

   // application, session

   /**
    * cache the content by language? if yes the the session language as used as part of the cache
    * key
    */
   private boolean language = true;

   private String actualKey;

   private String cachedContent;

   private static final TUILogUtils LOG = new TUILogUtils("EhCacheTag");

   private boolean page;

   private String id;

   private final SessionService sessionService = (SessionService) Registry.getApplicationContext()
      .getBean("sessionService");

   private ContentSlotModel cacheContentslot;

   /**
    * @return the cacheContentslot
    */
   public ContentSlotModel getCacheContentslot()
   {
      return cacheContentslot;
   }

   /**
    * @param cacheContentslot the cacheContentslot to set
    */
   public void setCacheContentslot(final ContentSlotModel cacheContentslot)
   {
      this.cacheContentslot = cacheContentslot;
   }

   /**
    * @return the scope
    */
   public String getScope()
   {
      return scope;
   }

   /**
    * @param scope the scope to set
    */
   public void setScope(final String scope)
   {
      // should never happen but just to be sure there is no empty string passed in
      if (StringUtils.isEmpty(scope))
      {
         this.scope = APPLICATION;
      }

      // if we have a scope that is not session or application then throw an exception
      if (!APPLICATION.equalsIgnoreCase(scope) && !"session".equalsIgnoreCase(scope))
      {
         throw new IllegalArgumentException(
            "Expecting \"session\" or \"application\" for the scope of the cache tag");
      }
      // lowercase it
      this.scope = scope.toLowerCase();
   }

   /**
    * @return the language
    */
   public boolean isLanguage()
   {
      return language;
   }

   /**
    * @param language the language to set
    */
   public void setLanguage(final boolean language)
   {
      this.language = language;
   }

   @Override
   public int doStartTag() throws JspTagException
   {

      final List<String> cachedComponentUIds = getFragmentCachedComponentIds();

      addComponentIdsToSession(cachedComponentUIds);

      int returnCode = EVAL_BODY_BUFFERED;

      final CacheWrapper<String, String> fragmentCache = cacheUtil.getFragmentCacheWrapper();

      // if time to live is specified as Zero then we short circuit here are process as
      // a normal request without caching

      if (((ttl != null) && (ttl.intValue() == 0))
         || !Boolean.parseBoolean(tuiConfigService.getConfigValue(CACHING_ENABLED, "false")))
      {
         LOG.debug("TTL of Zero specified so cached content not used");
         return returnCode;
      }

      if (webCacheHelper.isPreviewSession((HttpServletRequest) pageContext.getRequest()))
      {
         LOG.debug("In preview session so cached content not used");
         return returnCode;
      }

      // generate a key based on the context parameters
      actualKey =
         webCacheHelper.generateKey(id, (HttpServletRequest) pageContext.getRequest(), page,
            language, scope);

      cachedContent = fragmentCache.get(actualKey);

      if (StringUtils.isNotBlank(cachedContent))
      {

         LOG.debug("Using cached content for key [" + actualKey + "]");
         try
         {
            pageContext.getOut().write(cachedContent);
            // cached content written so we skip the body
            returnCode = SKIP_BODY;
            return returnCode;
         }
         catch (final IOException e)
         {
            LOG.error(IO_EXCEPTION, e);
            throw new JspTagException("IO Exception: " + e.getMessage());
         }

      }

      if (returnCode == EVAL_BODY_BUFFERED)
      {

         LOG.debug("Cached content not used [" + actualKey + "]");

      }

      return returnCode;
   }

   @Override
   public int doAfterBody() throws JspTagException
   {
      String body = null;
      // if we have no cached content and the body content
      // then put it in the cache
      if ((bodyContent != null) && (cachedContent == null))
      {
         LOG.debug("No cached content for [" + id + "]");

         body = bodyContent.getString();

         final CacheWrapper<String, String> fragmentCache = cacheUtil.getFragmentCacheWrapper();

         if (fragmentCache != null)
         {
            // we need to get the native EhCache object so we can add an object with a custom TTL
            final Element element = new Element(actualKey, body);
            if (ttl != null)
            {
               element.setTimeToLive(ttl.intValue());
            }

            fragmentCache.put(element);
         }
      }
      else if (cachedContent != null)
      {
         body = cachedContent;
      }

      bodyContent.clearBody();
      try
      {
         bodyContent.write(body);
         bodyContent.writeOut(bodyContent.getEnclosingWriter());
      }
      catch (final IOException e)
      {
         LOG.error(IO_EXCEPTION, e);
         throw new JspTagException("IO Error: " + e.getMessage());
      }

      return SKIP_BODY;

   }

   @Override
   public void doFinally()
   {
      ttl = null;
      cache = null;
      actualKey = null;
      scope = APPLICATION;
      // application, session
      language = true;
      cachedContent = null;
      page = false;
   }

   /**
    * @return the cache name
    */
   public String getCache()
   {
      return cache;
   }

   /**
    * @param cache the cache name to set
    */
   public void setCache(final String cache)
   {
      this.cache = cache;
   }

   /**
    * @return the ttl
    */
   public Integer getTtl()
   {
      return ttl;
   }

   /**
    * @param ttl the ttl to set
    */
   public void setTtl(final Integer ttl)
   {
      this.ttl = ttl;
   }

   @Override
   public int doEndTag() throws JspTagException
   {
      return EVAL_PAGE;
   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   @Override
   public void doCatch(final Throwable throwable) throws Throwable
   {
      throw throwable;
   }

   /**
    * @return the page
    */
   public boolean isPage()
   {
      return page;
   }

   /**
    * @param page the page to set
    */
   public void setPage(final boolean page)
   {
      this.page = page;
   }

   /**
    * @return the id
    */
   @Override
   public String getId()
   {
      return id;
   }

   /**
    * @param id the id to set
    */
   @Override
   public void setId(final String id)
   {
      this.id = id;
   }

   /**
    * @return
    */
   private List<String> getFragmentCachedComponentIds()
   {
      final List<String> componentUids = new ArrayList<String>();
      final ContentSlotModel contentSlotModel = getCacheContentslot();
      if (contentSlotModel != null)
      {
         final List<AbstractCMSComponentModel> cmsComponentModel =
            contentSlotModel.getCmsComponents();
         for (final AbstractCMSComponentModel components : cmsComponentModel)
         {
            final String compUid = components.getUid();
            componentUids.add(compUid);
         }

      }

      return componentUids;
   }

   public void addComponentIdsToSession(final List<String> componentId)
   {
      final List<String> newListOfComponents = new ArrayList<String>();
      if (CollectionUtils.isNotEmpty(componentId))
      {
         final List<String> listOfComponentIds =
            sessionService.getCurrentSession().getAttribute("componentIds");
         if (null != listOfComponentIds && !listOfComponentIds.containsAll(componentId))
         {
            newListOfComponents.addAll(listOfComponentIds);
            newListOfComponents.addAll(componentId);
            sessionService.getCurrentSession().removeAttribute("componentIds");
            sessionService.setAttribute("componentIds", newListOfComponents);

         }
      }
   }

}