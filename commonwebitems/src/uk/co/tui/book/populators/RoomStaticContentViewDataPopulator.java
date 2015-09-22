/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.view.data.GenericStaticContentViewData;
import uk.co.tui.book.view.data.RoomOptionsStaticContentViewData;

/**
 * @author thyagaraju.e
 *
 */
public class RoomStaticContentViewDataPopulator implements Populator<Object, RoomOptionsStaticContentViewData>{

    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    @Resource
    private PageContentViewDataPopulator pageContentViewDataPopulator;

    /**
     * Populate room content.
     *
     * @param roomViewData the room view data
     */
    public void populateRoomContent(
            final RoomOptionsStaticContentViewData roomViewData) {
        final GenericStaticContentViewData genericViewData = new GenericStaticContentViewData();

        roomViewData.setRoomContentMap(staticContentServ
                    .getRoomContents());

        pageContentViewDataPopulator.populateGenericContent(genericViewData);
        roomViewData.setGenericContentViewData(genericViewData);
    }


    /**
     * Calls the room content population
     */
    @Override
    public void populate(final Object source, final RoomOptionsStaticContentViewData target)
            throws ConversionException {
        populateRoomContent(target);

    }

}
