/**
 *
 */
package uk.co.tui.fj.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.domain.model.DynamicContentConfigModel;
import uk.co.tui.fj.book.store.PackageExtraFacilityStore;
import uk.co.tui.fj.book.view.data.ContentViewData;


/**
 * @author amaresh.d
 *
 */
public class RoomOptionsContentViewDataPopulator implements Populator<Object, ContentViewData>
{

    /** The generic content service. */
    @Resource
    private GenericContentService genericContentService;

    /** The package cart service. */
    @Resource
    private PackageCartService packageCartService;

    /** The session service. */
    @Resource
    private SessionService sessionService;

    /**
     * Populates the room options content
     */
    @Override
    public void populate(final Object source, final ContentViewData target) throws ConversionException
    {
        final BasePackage packageModel = packageCartService.getBasePackage();
        final PackageExtraFacilityStore packageExtraFacilityStore = (PackageExtraFacilityStore) sessionService
                .getAttribute("PackageExtraFacilityStore");
        // Checking if store is null since Item Search will not be triggered in
        // RO page if LCD is not applicable
        if (packageExtraFacilityStore != null)
        {
            final List<ExtraFacilityCategory> categoryList = packageExtraFacilityStore.getExtraFacilityLite(packageModel.getId());
            accomExtraFacility(target, categoryList);
        }

    }

    /**
     * @param target
     * @param categoryList
     */
    private void accomExtraFacility(final ContentViewData target, final List<ExtraFacilityCategory> categoryList)
    {
        if (CollectionUtils.isNotEmpty(categoryList))
        {
            for (final ExtraFacilityCategory category : categoryList)
            {
                if (StringUtils.equalsIgnoreCase(category.getCode(), ExtraFacilityConstants.ACCOMMODATION))
                {
                    populateAccommodationContentMap(category, target);
                }
            }
        }
    }

    /**
     * Populate accommodation content map.
     *
     * @param category
     *           the category
     * @param target
     *           the target
     */
    private void populateAccommodationContentMap(final ExtraFacilityCategory category, final ContentViewData target)
    {
        for (final ExtraFacility extra : category.getExtraFacilities())
        {
            final List<DynamicContentConfigModel> dynamicContents = genericContentService.getDynamicContentConfig(
                    category.getInventoryCode(), extra.getExtraFacilityCode(), extra.getCorporateCode());
            populateContentMap(target, dynamicContents);
        }
    }

    /**
     * Populates the Content Map
     *
     * @param target
     * @param dynamicContents
     */
    private void populateContentMap(final ContentViewData target, final List<DynamicContentConfigModel> dynamicContents)
    {
        for (final DynamicContentConfigModel dynamicContent : dynamicContents)
        {
            target.getContentMap().putAll(genericContentService.getGenericDynamicContentValue(dynamicContent));
        }
    }

}
