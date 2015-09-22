/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StopWatch;

import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

public class RecommendationsRESTConnector
{

   private static final TUILogUtils LOG = new TUILogUtils("RecommendationsRESTConnector");

   private static final String TH_HOME_REC_URL = "baynote.server.th.HomeRec.url";

   private static final String FC_HOME_REC_URL = "baynote.server.fc.HomeRec.url";

   private static final String TH_ACCOM_REC_URL = "baynote.server.th.AccomRec.url";

   private static final String FC_ACCOM_REC_URL = "baynote.server.fc.AccomRec.url";

   private static final String PAGE_ACCOM = "AccomodationRec";

   private static final String PAGE_HOME = "HomeRec";

   private static final String TYPE_SEGMENT = "segment";

   /** The HTTPclient object. */
   // @Autowired

   @Resource
   private TUIConfigService tuiConfigService;

   /** The configuration service. */
   @Resource
   private ConfigurationService configurationService;

   /**
    * @return the configurationService
    */
   public ConfigurationService getConfigurationService()
   {
      return configurationService;
   }

   /**
    * @param configurationService the configurationService to set
    */
   public void setConfigurationService(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;
   }

   public String getRESTResponse(final String pageType, final String brand,
      final Map<String, String> urlMap) throws IOException
   {
      configurationService =
         (ConfigurationService) Registry.getApplicationContext().getBean("configurationService");

      tuiConfigService =
         (TUIConfigService) Registry.getApplicationContext().getBean("tuiConfigService");

      HttpGet requestURI = null;

      String url = null;

      HttpClient client;

      if (brand != null && "TH".equalsIgnoreCase(brand))
      {

         if (pageType != null && pageType.equalsIgnoreCase(PAGE_HOME))
         {

            final String domain =
               configurationService.getConfiguration().getString(TH_HOME_REC_URL);
            url = buildUrlFromMap(domain, urlMap);
            requestURI = new HttpGet(url);
         }
         else if (pageType != null && pageType.equalsIgnoreCase(PAGE_ACCOM))
         {
            final String domain =
               configurationService.getConfiguration().getString(TH_ACCOM_REC_URL);
            url = buildUrlFromMap(domain, urlMap);

            requestURI = new HttpGet(url);
         }
      }
      else if (brand != null && "FC".equalsIgnoreCase(brand))
      {

         if (pageType != null && pageType.equalsIgnoreCase(PAGE_HOME))
         {
            final String domain =
               configurationService.getConfiguration().getString(FC_HOME_REC_URL);
            url = buildUrlFromMap(domain, urlMap);

            requestURI = new HttpGet(url);
         }
         else if (pageType != null && pageType.equalsIgnoreCase(PAGE_ACCOM))
         {
            final String domain =
               configurationService.getConfiguration().getString(FC_ACCOM_REC_URL);
            url = buildUrlFromMap(domain, urlMap);

            requestURI = new HttpGet(url);
         }

      }

      String xmlResponse = "";
      String timeoutSeconds = null;
      Integer timeout = Integer.valueOf(0);
      if (tuiConfigService != null)
      {
         timeoutSeconds =
            tuiConfigService.getConfigValue(brand + ".baynoteConnectionTimeout", "1000");
      }
      if (timeoutSeconds != null)
      {
         timeout = Integer.parseInt(timeoutSeconds);
      }

      final HttpParams params = new BasicHttpParams();

      HttpConnectionParams.setConnectionTimeout(params, timeout.intValue());

      HttpConnectionParams.setSoTimeout(params, timeout.intValue());

      client = new DefaultHttpClient(params);

      HttpResponse response;
      try
      {
         LOG.debug("Requested Baynote Url " + requestURI);

         final StopWatch bnResponseTime = new StopWatch();

         bnResponseTime.start();

         response = client.execute(requestURI);

         bnResponseTime.stop();

         LOG.info("Time took for Baynote to respond: " + bnResponseTime.getTotalTimeMillis()
            + " ms");

         final HttpEntity resEntityGet = response.getEntity();

         xmlResponse = EntityUtils.toString(resEntityGet);
         if (response.getStatusLine().getStatusCode() == CommonwebitemsConstants.FIVE_ZERO_ZERO)
         {
            LOG.info("Baynote Server Unavailable.." + response.getStatusLine().getStatusCode());
            return "Server Unavailable";
         }
         EntityUtils.consumeQuietly(resEntityGet);
      }
      catch (final ClientProtocolException cpe)
      {

         LOG.debug("Baynote Server ClientProtocolException", cpe);

      }
      catch (final Exception io)
      {

         LOG.debug("Baynote Server IOException", io);

      }
      finally
      {
         if (requestURI != null)
         {
            requestURI.releaseConnection();
         }
      }

      return xmlResponse;

   }

   public String buildUrlFromMap(final String domain, final Map<String, String> urlmap)
   {

      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(domain);

      for (final Map.Entry<String, String> entry : urlmap.entrySet())
      {
         if (stringBuilder.length() > 0)
         {
            stringBuilder.append("&");
         }
         final String value = entry.getValue();
         final String key = entry.getKey();
         try
         {
            if (!("v".equals(key)))
            {
               if (key != null && !key.equalsIgnoreCase(TYPE_SEGMENT))
               {
                  stringBuilder.append(URLEncoder.encode(key, "UTF-8"));
                  stringBuilder.append("=");
                  stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");

               }
               else if (key != null && key.equalsIgnoreCase(TYPE_SEGMENT))
               {
                  stringBuilder.append(URLEncoder.encode(key, "UTF-8"));
                  stringBuilder.append("=");
                  stringBuilder.append(value);
               }
               else
               {
                  stringBuilder.append(value);
               }
            }

         }
         catch (final UnsupportedEncodingException e)
         {

            LOG.error(" Error has occured due to unsupported encoding of URL ", e);

         }
      }
      stringBuilder.append("&");
      stringBuilder.append("v");
      stringBuilder.append("=");
      stringBuilder.append("1");

      return stringBuilder.toString();

   }
}
