/**
 *
 */
package uk.co.tui.fj.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.tui.domain.model.DynamicContentConfigModel;
import uk.co.tui.fj.book.view.data.DataProtectionViewData;


/**
 * @author thyagaraju.e
 *
 */
public class DataProtectionViewDataPopulator implements Populator<Object, DataProtectionViewData>
{

    @Resource
    private GenericContentService genericContentService;

    /**
     * Populates the data protection content
     */
    @Override
    public void populate(final Object source, final DataProtectionViewData target) throws ConversionException
    {
        final String dataProtectionCode1 = "4524C70C862ABD7F";
        final String dataProtectionCode2 = "919243E04F2AC1B0";
        final List<DynamicContentConfigModel> dynamicContents = genericContentService.getDynamicContentConfig(dataProtectionCode1,
                dataProtectionCode2, StringUtils.EMPTY);
        for (final DynamicContentConfigModel dynamicContent : dynamicContents)
        {
            final Map<Object, Object> dynamicContentMap = genericContentService.getGenericDynamicContentValue(dynamicContent);
            for (final Entry<Object, Object> content : dynamicContentMap.entrySet())
            {
                final String key = "dpn" + content.getKey();
                target.getDataProtectionMap().put(key, content.getValue());
            }
        }
    }

}
