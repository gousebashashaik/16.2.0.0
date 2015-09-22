package uk.co.tui.validator;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.jfree.util.Log;

import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.constants.RegexPatterns;
import uk.co.tui.exception.InvalidParamException;

public class RequestParamRegexValidator implements Validator<HttpServletRequest>
{
   private static final TUILogUtils LOGGER = new TUILogUtils("RequestParamRegexValidator");

   private final Properties validParams = new Properties();

   public RequestParamRegexValidator(final String propertyFile, final Class extensionManager)
   {
      InputStream inStream = null;
      try
      {
         inStream = extensionManager.getResourceAsStream(propertyFile);
         this.validParams.load(inStream);
      }
      catch (final IOException e)
      {
         LOGGER.error("Exception while loading properties::" + e.getMessage(), e);
      }
      finally
      {
         closeMe(inStream);
      }
   }

   @Override
   public void validate(final HttpServletRequest... sources) throws InvalidParamException
   {
      final Iterator i = sources[0].getParameterMap().entrySet().iterator();
      while (i.hasNext())
      {
         final Map.Entry<String, String[]> entry = (Entry<String, String[]>) i.next();
         final String parameter = entry.getKey();
         if (validParams.containsKey(parameter))
         {
            findMatcher(parameter, (entry.getValue())[0], validParams.getProperty(parameter));
         }
         else
         {
            Log.error("Unregistered Parameter present in request. Name : "
               + parameter
               + "It can be a fishing attempt / if you have added a new parameter but not added validation for that could be the cause as well.Add new validation entries in AbstractValidator. ");
            throw new InvalidParamException("Unregistered Parameter present in request. ");
         }
      }
   }

   /**
    * @param source
    * @param regexPattern
    * @throws InvalidParamException
    * @description This Method Check Whether Any Source is Inline With The Regex .
    */
   private void findMatcher(final String key, final String source, final String regexPattern)
      throws InvalidParamException
   {
      if (isNotEmpty(source))
      {
         getFindMatcher(key, source, regexPattern);
      }
   }

   private void getFindMatcher(final String key, final String source, final String regexPattern)
      throws InvalidParamException
   {
      try
      {
         if (!Pattern.compile(regexPattern).matcher(URLDecoder.decode(source, RegexPatterns.UTF_8))
            .matches())
         {
            throw new InvalidParamException("Bad Param Came For Request Param :: " + key
               + "::Bad Value Is::" + source);
         }
      }
      catch (final UnsupportedEncodingException e)
      {
         LOGGER.error("> Error While decoding the Source..", e);
         LOGGER.error(e.getMessage());
      }
   }

   /**
    * @param inStream
    */
   private void closeMe(final InputStream inStream)
   {
      try
      {
         if (inStream != null)
         {
            inStream.close();
         }
      }
      catch (final IOException e)
      {
         LOGGER.error("Unable to close the input stream ::" + e.getMessage(), e);
      }
   }

}
