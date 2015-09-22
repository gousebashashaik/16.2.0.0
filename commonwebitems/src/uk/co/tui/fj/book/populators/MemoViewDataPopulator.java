/**
 *
 */
package uk.co.tui.fj.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Memo;
import uk.co.tui.fj.book.view.data.MemoViewData;
import uk.co.tui.fj.book.view.data.PackageViewData;

/**
 * The Class MemoViewDataPopulator.
 *
 * @author samantha.gd
 */
public class MemoViewDataPopulator implements
        Populator<BasePackage, PackageViewData> {

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(BasePackage source, PackageViewData target)
            throws ConversionException {
        //SONAR FIX-Changing logic for "null" check (to avoid parameter reassignment in actual population)
        MemoViewData memoViewData = null;
        if (target.getMemoViewData() != null) {
            memoViewData = target.getMemoViewData();
        } else {
            memoViewData = new MemoViewData();
        }

        populate(source, memoViewData);
        target.setMemoViewData(memoViewData);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    /**
     * Populate.
     *
     * @param source
     *            the source
     * @param target
     *            the target
     * @throws ConversionException
     *             the conversion exception
     */
    public void populate(BasePackage source, MemoViewData target)
            throws ConversionException {
        final List<Memo> memoModelList = source.getMemos();
        List<String> description = new ArrayList<String>();
        if (memoModelList != null && CollectionUtils.isNotEmpty(memoModelList)) {
            for (final Memo memomodel : memoModelList) {
                description.add(String
                        .valueOf(memomodel.getDescription()));
            }
            target.setDescription(description);
            target.setAvailable(true);
        }
    }

}
