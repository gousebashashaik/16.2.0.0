/**
 *
 */
package uk.co.portaltech.tui.jackson.databind;

import java.io.IOException;
import java.util.Arrays;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.async.logging.TUILogUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;


/**
 * @author
 *
 */
public class HTMLCharacterEscapes<T> extends CharacterEscapes
{
    private static final String DEPRECATION = "deprecation";

    private static final TUILogUtils LOG = new TUILogUtils("HTMLCharacterEscapes");

    private static final String JSON_EXCEPTION = "JsonProcessingException";


    private static ObjectMapper objectMapper;

    private final int[] asciiEscapes;

    static
    {
        objectMapper = new ObjectMapper();
        objectMapper.getJsonFactory().setCharacterEscapes(new HTMLCharacterEscapes());
    }

    /**
     *
     */
    public HTMLCharacterEscapes()
    {

        // start with set of characters known to require escaping (double-quote,
        // backslash etc)
        final int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
        // and force escaping of a few others:
        esc['<'] = CharacterEscapes.ESCAPE_STANDARD;
        esc['>'] = CharacterEscapes.ESCAPE_STANDARD;
        esc['&'] = CharacterEscapes.ESCAPE_STANDARD;
        esc['\''] = CharacterEscapes.ESCAPE_STANDARD;
        asciiEscapes = esc;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fasterxml.jackson.core.io.CharacterEscapes#getEscapeCodesForAscii() this method gets called for character
     * codes 0 - 127
     */
    @Override
    public int[] getEscapeCodesForAscii()
    {
        return Arrays.copyOf(asciiEscapes,asciiEscapes.length);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fasterxml.jackson.core.io.CharacterEscapes#getEscapeSequence(int)
     */
    @Override
    public SerializableString getEscapeSequence(final int arg0)
    {
        // no further escaping (beyond ASCII chars) needed:
        return null;
    }

    /**
     * API used to convert JAVA object to JSON string.
     *
     * @param obj
     * @return String
     */
    public static String writeValue(final Object obj)
    {
        final ObjectWriter objectWriter = objectMapper.writer();
        try
        {
            return objectWriter.writeValueAsString(obj);
        }
        catch (final IOException e)
        {
            LOG.error("IO exception", e);
        }

        return StringUtils.EMPTY;
    }

    /**
     * API to convert JSON String to JAVA object.
     *
     * @param input
     * @param clazz
     * @return T
     */
    public static <T extends Object> T readValue(final String input, final Class clazz)
    {
        final ObjectReader objectReader = objectMapper.reader(clazz);
        try
        {
            return (T) objectReader.readValue(input);
        }
        catch (final JsonProcessingException e)
        {
            LOG.error(JSON_EXCEPTION, e);
        }
        catch (final IOException e)
        {
            LOG.error(JSON_EXCEPTION, e);
        }

        return null;
    }

    /**
     * API to convert JSON string to any custom List/Map object.
     *
     * @param input
     * @param typeReference
     * @return T
     */
    public static <T extends Object> T readValueWithTypeReference(final String input, final TypeReference typeReference)
    {
        final ObjectReader objectReader = objectMapper.reader().withType(typeReference);
        try
        {
            return (T) objectReader.readValue(input);
        }
        catch (final JsonProcessingException e)
        {
            LOG.error(JSON_EXCEPTION, e);
        }
        catch (final IOException e)
        {
            LOG.error(JSON_EXCEPTION, e);
        }

        return null;

    }
    /**
     * API to convert JSON String to JAVA object.
     *
     * @param input
     * @param dateFormat
     * @param clazz
     * @return T
     */
    @SuppressWarnings(DEPRECATION)
    public static <T extends Object> T readValueWithDateFormat(final String input,
       final String dateFormat, final Class clazz)
    {
       final ObjectMapper dateFormatObjectMapper = new ObjectMapper();
       dateFormatObjectMapper.getJsonFactory().setCharacterEscapes(new HTMLCharacterEscapes());
       final DateFormat df = new SimpleDateFormat(dateFormat);
       dateFormatObjectMapper.setDateFormat(df);
       final ObjectReader objectReader = dateFormatObjectMapper.reader(clazz);
       try
       {
          return (T) objectReader.readValue(input);
       }
       catch (final JsonProcessingException e)
       {
          LOG.error(JSON_EXCEPTION, e);
       }
       catch (final IOException e)
       {
          LOG.error(JSON_EXCEPTION, e);
       }
       return null;
    }
    // and then an example of how to apply it
    @SuppressWarnings("deprecation")
    public ObjectMapper getEscapingMapper()
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.getJsonFactory().setCharacterEscapes(new HTMLCharacterEscapes());
        return mapper;
    }

    // so we could do:
    public String serializeWithEscapes(final Object obj) throws JsonProcessingException
    {
        try
        {
            return getEscapingMapper().writeValueAsString(obj);
        }
        catch (final IOException e)
        {
            LOG.error("IO exception", e);
            LOG.error(e.getMessage());
        }

        return StringUtils.EMPTY;
    }
}
