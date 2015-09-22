/**
 *
 */

package uk.co.tui.th.book.impex.util;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;

/**
 * @author extps3
 *  This class is intended to convert Static Content Value into Object
 */
public class StaticContentValueTranslator extends AbstractValueTranslator
{

    @Override
    public Object importValue(final String cellValue, final Item toItem) throws JaloInvalidParameterException
    {
        return cellValue;
    }

    @Override
    public String exportValue(final Object value) throws JaloInvalidParameterException
    {
        throw new UnsupportedOperationException("Exporting has not been implemented for this translator.");
    }
}


