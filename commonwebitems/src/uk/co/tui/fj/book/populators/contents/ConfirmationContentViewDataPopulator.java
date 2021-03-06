/**
 *
 */
package uk.co.tui.fj.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.tui.domain.model.DynamicContentConfigModel;
import uk.co.tui.fj.book.view.data.ContentViewData;


/**
 * @author thyagaraju.e
 *
 */
public class ConfirmationContentViewDataPopulator implements Populator<Object, ContentViewData>
{

    /** The generic content service. */
    @Resource
    private GenericContentService genericContentService;

    private static final String ONLINE_CHECKIN_CODE = "077857EF418C45B0";

    /**
     * Populates the Online Checkin Content
     */
    @Override
    public void populate(final Object source, final ContentViewData target) throws ConversionException
    {
        final List<DynamicContentConfigModel> dynamicContents = genericContentService.getDynamicContentConfig(ONLINE_CHECKIN_CODE,
                StringUtils.EMPTY, StringUtils.EMPTY);
        for (final DynamicContentConfigModel dynamicContent : dynamicContents)
        {
            target.getContentMap().putAll(genericContentService.getGenericDynamicContentValue(dynamicContent));
        }
    }
}
