/**
 *
 */
package uk.co.portaltech.tui.connections;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

/**
 * @author veena.pn
 *
 */
public class BaynoteConnector
{

   private static final Logger LOG = Logger.getLogger(BaynoteConnector.class);

   @Resource
   private ConfigurationService configurationService;

   // It makes baynote connection for the requested url and gets the response from baynote
   public String doHttpUrlConnectionAction(final String url)
   {
      final StopWatch baynoteResponseTime = new StopWatch();

      URL urlupdated = null;
      BufferedReader reader = null;
      StringBuilder stringBuilder;
      HttpURLConnection connection = null;

      try
      {

         urlupdated = new URL(url);
         baynoteResponseTime.start();
         // Gets baynote Conncetion
         connection = getBaynoteConnection(urlupdated);
         reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         baynoteResponseTime.stop();
         LOG.debug("Total baynote process time in ms" + baynoteResponseTime.getTotalTimeMillis()
            + " ms");
         stringBuilder = new StringBuilder();

         String line = null;
         while ((line = reader.readLine()) != null)
         {
            stringBuilder.append(line + "\n");
         }
         return stringBuilder.toString();
      }
      catch (final IOException e)
      {
         LOG.error("Error baynote connection creation ", e);
      }
      finally
      {
         // Close the connection
         closeConnection(reader, connection);
      }
      return null;
   }

   /**
    * Create HttpURLConnection object for a given url.
    *
    * @param url The String URL.
    * @return HttpURLConnection connection object.
    */
   private HttpURLConnection getBaynoteConnection(final URL urlupdated) throws IOException
   {
      HttpURLConnection connection = null;
      try
      {
         connection = (HttpURLConnection) urlupdated.openConnection();

         connection.setRequestMethod("GET");

         connection.setConnectTimeout(configurationService.getConfiguration().getInt(
            "baynote.connectionTimeout"));
         connection.setReadTimeout(configurationService.getConfiguration().getInt(
            "baynote.readTimeout"));
         connection.connect();
      }
      catch (final MalformedURLException malFormedURLException)
      {
         LOG.error("Error baynote url creation ", malFormedURLException);
      }
      catch (final IOException ioException)
      {
         LOG.error("Error baynote connection creation ", ioException);
      }
      return connection;
   }

   /**
    * Close the created connection.
    *
    * @param reader The BufferedReader object.
    * @param connection The HttpURLConnection Object.
    */
   private void closeConnection(final BufferedReader reader, final HttpURLConnection connection)
   {
      if (connection != null)
      {
         connection.disconnect();
      }

      if (reader != null)
      {
         try
         {
            reader.close();
         }
         catch (final IOException ioe)
         {
            LOG.error("Error while connecting to baynote", ioe);
         }
      }
   }
}