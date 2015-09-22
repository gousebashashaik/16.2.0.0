/**
 *
 */
package uk.co.tui.th.book.populators;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.th.book.view.data.GenericStaticContentViewData;

/**
 * The Class PageContentViewDataPopulator.
 *
 * @author thyagaraju.e
 */
public class PageContentViewDataPopulator {

    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    /**
     * Populate generic content.
     *
     * @param genericViewData
     *            the generic view data
     */
    protected void populateGenericContent(
            final GenericStaticContentViewData genericViewData) {
        genericViewData.setGenericContentMap(staticContentServ
                .getGenericContents());

    }
}