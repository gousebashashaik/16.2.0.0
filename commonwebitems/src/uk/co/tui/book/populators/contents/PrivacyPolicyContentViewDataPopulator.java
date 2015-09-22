/**
 *
 */
package uk.co.tui.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.tui.book.view.data.TermsAndConditionsViewData;
import uk.co.tui.domain.model.DynamicContentConfigModel;

/**
 * @author thyagaraju.e
 *
 */
public class PrivacyPolicyContentViewDataPopulator
        implements
            Populator<Object, TermsAndConditionsViewData> {

    @Resource
    private GenericContentService genericContentService;

    /**
     * Populates the Privacy Policy Content
     */
    @Override
    public void populate(final Object source,
            final TermsAndConditionsViewData target) throws ConversionException {
        final List<String> privacyPolicyList = new ArrayList<String>();
        privacyPolicyList.add("DATAPR");
        privacyPolicyList.add("C20BCB40EA123807");
        privacyPolicyList.add("5C3E1DC65C478A85");
        for (final String code : privacyPolicyList) {
            final List<DynamicContentConfigModel> dynamicContents = genericContentService
                    .getDynamicContentConfig(code, StringUtils.EMPTY,
                            StringUtils.EMPTY);
            for (final DynamicContentConfigModel dynamicContent : dynamicContents) {
                target.getPrivacyPolicyMap()
                        .putAll(genericContentService
                                .getGenericDynamicContentValue(dynamicContent));
            }
        }
    }

}
