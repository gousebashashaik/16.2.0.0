/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author sreenivasulu.v
 *
 */
public class JsonTripadvisorFetchResults
{
   // private static final Logger LOGGER =

   private static final TUILogUtils LOGGER = new TUILogUtils("JsonTripadvisorFetchResults");

   private JsonadvisorUserReviewData jsonreviwsviewdata;

   private JsonadvisorUserRatingData userrating;

   @Resource
   private ConfigurationService configurationService;

   @Resource
   private HttpClient httpClient;

   public static final int THREE_ZERO_ZERO_ZERO = 3000;

   private static final String RATINGIMAGEURL = "ratingImageUrl";

   private static final String TEXT = "text";

   private static final String EXCEPTION_MESSAGE =
      "Exception in gettripadvisorJsondata(final String url) ";

   private static final int EIGHT = 8;

   public Object getTripAdvisorRating(final String tripadvisorreviwid, final int maxreviews)
   {
      final String tripadvisorratingurl =
         configurationService.getConfiguration().getString("tripadvisorratingurl")
            .replace("tripadvisorreviewsid", tripadvisorreviwid);
      LOGGER.info("Tripadvisor user review rating Data to be Fetched from Json using the URL : "
         + tripadvisorratingurl);

      final JSONObject jsonreviwratingobject = gettripadvisorJsondata(tripadvisorratingurl);

      userrating = new JsonadvisorUserRatingData();

      /* Start for trip advisor User rating */

      if (jsonreviwratingobject != null && !jsonreviwratingobject.isEmpty())
      {

         if (jsonreviwratingobject.get("num_reviews") != null)
         {

            final String numreviews = jsonreviwratingobject.get("num_reviews").toString();
            final int numreviewscount = Integer.parseInt(numreviews);
            userrating.setReviewsCount(numreviewscount);

         }

         if (jsonreviwratingobject.get(RATINGIMAGEURL) != null)
         {
            final String ratingbarimage = jsonreviwratingobject.get(RATINGIMAGEURL).toString();
            userrating.setRatingBar(ratingbarimage);
         }
         if (jsonreviwratingobject.get("web_url") != null)
         {
            final String ratingreviwsurl = jsonreviwratingobject.get("web_url").toString();
            userrating.setRatingReviewsUrl(ratingreviwsurl);
         }

         if (jsonreviwratingobject.get("rating") != null)
         {
            final String avgrating = jsonreviwratingobject.get("rating").toString();
            final double averagerating = Double.parseDouble(avgrating);
            final Double medianrating = new Double(averagerating);
            userrating.setAverageRating(medianrating);
         }

      }
      return userrating;
   }

   public Object getTripAdvisorreviews(final String tripadvisorreviwid, final Integer maxUserReviews)
   {
      final String tripadvisorreviewsurl =
         configurationService.getConfiguration().getString("tripadvisoruserviewsurl")
            .replace("tripadvisorreviewsid", tripadvisorreviwid);

      LOGGER.info("Tripadvisor user reviews Data to be Fetched from Json using the URL : "
         + tripadvisorreviewsurl);
      userrating = new JsonadvisorUserRatingData();
      final List<JsonadvisorUserReviewData> reviews = new ArrayList();

      final JSONObject jsonobject = gettripadvisorJsondata(tripadvisorreviewsurl);
      try
      {
         if (jsonobject != null && !jsonobject.isEmpty())
         {
            final JSONArray jsondatasets = (JSONArray) jsonobject.get("data");
            /* Start for adding trip advisor user reviews */
            for (int i = 0; i <= maxUserReviews.intValue(); i++)
            {
               jsonreviwsviewdata = new JsonadvisorUserReviewData();

               final JSONObject indexjsonobject = (JSONObject) jsondatasets.get(i);
               if (indexjsonobject.get("url") != null)
               {
                  jsonreviwsviewdata.setReviewsUrl(indexjsonobject.get("url").toString());
               }
               if (indexjsonobject.get(RATINGIMAGEURL) != null)
               {
                  jsonreviwsviewdata.setRatingImage(indexjsonobject.get(RATINGIMAGEURL).toString());
               }

               final String publishedDate = indexjsonobject.get("published_date").toString();
               if (indexjsonobject.get(TEXT) != null)
               {
                  jsonreviwsviewdata.setContent(indexjsonobject.get(TEXT).toString());
               }
               if (indexjsonobject.get(TEXT) != null)
               {

                  jsonreviwsviewdata.setContentSummary((indexjsonobject.get(TEXT).toString())
                     .substring(0, CommonwebitemsConstants.NINE_ZERO).concat("..."));
               }
               if (indexjsonobject.get("title") != null)
               {
                  jsonreviwsviewdata.setTitle(indexjsonobject.get("title").toString());
               }
               final JSONObject userinfobject = (JSONObject) indexjsonobject.get("user");
               if (userinfobject.get("username") != null)
               {
                  jsonreviwsviewdata.setAuthor(userinfobject.get("username").toString());
               }
               final JSONObject authorlocationobject =
                  (JSONObject) userinfobject.get("user_location");

               if (authorlocationobject != null && !authorlocationobject.isEmpty())
               {
                  final Set keys = authorlocationobject.keySet();
                  final Iterator itr = keys.iterator();
                  while (itr.hasNext())
                  {
                     final String idkey = (String) itr.next();
                     final String locationkey = (String) itr.next();
                     if (authorlocationobject.get(idkey) != null)
                     {
                        jsonreviwsviewdata.setAuthorID(authorlocationobject.get(idkey).toString());
                     }
                     if (authorlocationobject.get(locationkey) != null)
                     {
                        jsonreviwsviewdata.setAuthorLocation(authorlocationobject.get(locationkey)
                           .toString());
                     }

                  }
               }
               final SimpleDateFormat sourcedateFormat =
                  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-S");
               final SimpleDateFormat targetdispalyFormat = new SimpleDateFormat("MMM dd, yyyy");
               // date format
               if (publishedDate != null)
               {
                  try
                  {
                     final Date sourcedate = sourcedateFormat.parse(publishedDate);
                     final String destinationformat = targetdispalyFormat.format(sourcedate);
                     jsonreviwsviewdata.setPublishedDate(destinationformat);
                  }
                  catch (final ParseException e)
                  {
                     LOGGER
                        .info(
                           "Exception in getTripAdvisorreviews(final String tripadvisorreviwid, final int maxreviews)",
                           e);
                  }
               }
               reviews.add(jsonreviwsviewdata);
            }
         }
      }
      catch (final IndexOutOfBoundsException e)
      {
         LOGGER
            .info(
               "Exception in getTripAdvisorreviews(final String tripadvisorreviwid, final int maxreviews)",
               e);
      }
      userrating.setUserReviews(reviews);
      return userrating;
   }

   private JSONObject gettripadvisorJsondata(final String url)
   {

      JSONObject jsonObj = null;
      String jsoncontent = "";
      BufferedReader reader = null;

      // Making HTTP request
      try
      {
         // defaultHttpClient

         final HttpPost httpPost = new HttpPost(url);

         final HttpResponse httpResponse = getClient().execute(httpPost);
         final HttpEntity httpEntity = httpResponse.getEntity();

         reader =
            new BufferedReader(new InputStreamReader(httpEntity.getContent(), "iso-8859-1"), EIGHT);
         final StringBuilder sb = new StringBuilder();
         String line = null;
         while ((line = reader.readLine()) != null)
         {
            sb.append(line);
         }

         jsoncontent = sb.toString();
         EntityUtils.consumeQuietly(httpEntity);
      }
      catch (final UnsupportedEncodingException e)
      {
         LOGGER.info(EXCEPTION_MESSAGE, e);

      }
      catch (final ClientProtocolException e)
      {
         LOGGER.info(EXCEPTION_MESSAGE, e);

      }
      catch (final IOException e)
      {
         LOGGER.info(EXCEPTION_MESSAGE, e);

      }
      finally
      {
         if (reader != null)
         {
            try
            {
               reader.close();
            }
            catch (final IOException e)
            {
               LOGGER.info("Exception while closing the reader ", e);
            }

         }
      }

      // try parse the string to a JSON object

      jsonObj = new JSONObject();
      final JSONParser jsonParser = new JSONParser();
      try
      {
         jsonObj = (JSONObject) jsonParser.parse(jsoncontent);
      }
      catch (final org.json.simple.parser.ParseException e)
      {
         LOGGER.info("Exception while parsing the String to JSON object : ", e);
      }

      // return JSON String
      return jsonObj;

   }

   @SuppressWarnings("deprecation")
   private HttpClient getClient()
   {
      final int timeout =
         Config.getInt("tripadvisor.server.connection.timeout", THREE_ZERO_ZERO_ZERO);
      final HttpParams params = httpClient.getParams();
      HttpConnectionParams.setConnectionTimeout(params, timeout);
      HttpConnectionParams.setSoTimeout(params, timeout);
      return httpClient;
   }

}
