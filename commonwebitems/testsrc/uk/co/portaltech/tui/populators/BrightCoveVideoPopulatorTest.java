/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.core.model.media.MediaContainerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.tui.web.view.data.MediaViewData;


/**
 * @author sureshbabu.rn
 * 
 */
public class BrightCoveVideoPopulatorTest
{

	BrightCoveVideoPopulator brightCoveVideoPopulator = new BrightCoveVideoPopulator();
	@Mock
	MediaContainerModel containerModel;
	MediaContainerModel mediaContainerModel;
	Collection<MediaContainerModel> galleryVideos;
	List<MediaViewData> videoDataList;

	@Before
	public void setUp() throws Exception
	{
		mediaContainerModel = new MediaContainerModel();
		galleryVideos = new ArrayList<MediaContainerModel>();

		mediaContainerModel.setQualifier("Qualifier");
		mediaContainerModel.setBrands(Arrays.asList(BrandType.FC, BrandType.TH));
		videoDataList = new ArrayList<MediaViewData>();

		mediaContainerModel.setQualifier("qualifier");
		galleryVideos.add(mediaContainerModel);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.BrightCoveVideoPopulator#populate(java.util.Collection, java.util.List)}.
	 */
	@Test
	public void testPopulate()
	{
		assertNotNull(galleryVideos);
		
		brightCoveVideoPopulator.populate(galleryVideos, videoDataList);

		assertNotNull(videoDataList);

		assertEquals("qualifier", videoDataList.get(0).getCode());
	}

}
