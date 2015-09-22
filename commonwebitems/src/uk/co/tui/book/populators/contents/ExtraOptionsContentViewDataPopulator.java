/**
 *
 */
package uk.co.tui.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.store.PackageExtraFacilityStore;
import uk.co.tui.book.view.data.ContentViewData;
import uk.co.tui.domain.model.DynamicContentConfigModel;



/**
 * @author thyagaraju.e
 *
 */
public class ExtraOptionsContentViewDataPopulator
        implements
            Populator<Object, ContentViewData> {

    @Resource
    private GenericContentService genericContentService;

    /** The package cart service. */
    @Resource
    private PackageCartService packageCartService;

    /** The session service. */
    @Resource
    private SessionService sessionService;

    /**
     * Populates the Extra Options Content
     */
    @Override
    public void populate(final Object source, final ContentViewData target)
            throws ConversionException {

        final BasePackage packageModel = packageCartService.getBasePackage();
        final PackageExtraFacilityStore packageExtraFacilityStore = (PackageExtraFacilityStore) sessionService
                .getAttribute("PackageExtraFacilityStore");

        final String packageCode = packageModel.getId();

        final List<ExtraFacilityCategory> categoryList = packageExtraFacilityStore
                .getExtraFacilityLite(packageCode);
        for (final ExtraFacilityCategory categoryModel : categoryList) {
            for (final ExtraFacility extraFacility : categoryModel
                    .getExtraFacilities()) {
                if (SyntacticSugar.isNotNull(categoryModel.getInventoryCode())) {
                    final List<DynamicContentConfigModel> dynamicContents = genericContentService
                            .getDynamicContentConfig(
                                    categoryModel.getInventoryCode(),
                                    extraFacility.getExtraFacilityCode(),
                                    extraFacility.getCorporateCode());
                    populatePackageExtraContent(target, dynamicContents);
                }
            }
        }

        populateInsuranceContent(target);
    }

    /**
     * Populate insurance content.
     *
     * @param target
     *            the target
     */
    private void populateInsuranceContent(final ContentViewData target) {
        final List<String> insuranceCodes = new ArrayList<String>();
        insuranceCodes.add("LP");
        insuranceCodes.add("LPFLM");
        insuranceCodes.add("LPIND");
        for (final String code : insuranceCodes) {
            final List<DynamicContentConfigModel> dynamicContents = genericContentService
                    .getDynamicContentConfig(code, StringUtils.EMPTY,
                            StringUtils.EMPTY);
            for (final DynamicContentConfigModel dynamicContent : dynamicContents) {
                target.getContentMap().putAll(
                        genericContentService
                                .getGenericDynamicContentValue(dynamicContent));
            }
        }
    }

    /**
     * Populate package extra content.
     *
     * @param target
     *            the target
     * @param dynamicContents
     *            the dynamic contents
     */
    private void populatePackageExtraContent(final ContentViewData target,
            final List<DynamicContentConfigModel> dynamicContents) {
        for (final DynamicContentConfigModel dynamicContent : dynamicContents) {
            target.getContentMap().putAll(
                    genericContentService
                            .getGenericDynamicContentValue(dynamicContent));
        }
    }
}
