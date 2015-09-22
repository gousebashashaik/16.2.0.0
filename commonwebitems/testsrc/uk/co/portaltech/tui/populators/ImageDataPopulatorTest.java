/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertNotNull;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;


/**
 * @author sureshbabu.rn
 * 
 */
public class ImageDataPopulatorTest
{
	@InjectMocks
	private final ImageDataPopulator dataPopulator = new ImageDataPopulator();
	@Mock
	ImageDataPopulator dataPopulator1;
	@Mock
	ImageFormatMapping imageFormatMapping;

	@Mock
	MediaService mediaService;

	@Mock
	MediaContainerService mediaContainerService;

	@Mock
	Converter<MediaModel, ImageData> imageConverter;

	@Mock
	ImageData imageDataMock;

	private Collection<MediaContainerModel> mediaCollectionModelList;
	private List<ImageData> imageDataList;
	MediaFormatModel mediaFormatModel;
	MediaModel mediaModel;
	ImageData imageData;

	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		mediaCollectionModelList = new ArrayList<MediaContainerModel>();
		imageDataList = new ArrayList<ImageData>();
		final MediaContainerModel containerModel = new MediaContainerModel();
		mediaFormatModel = new MediaFormatModel();
		mediaModel = new MediaModel();
		imageData = new ImageData();


		containerModel.setCaption("Media Caption");
		containerModel.setBrands(Arrays.asList(BrandType.TH, BrandType.FC));
		containerModel.setCronSchedule("Cron Schedule");
		containerModel.setMediaMogulId("TH");
		containerModel.setMediaType("Mpeg");
		containerModel.setQualifier("Qualifier;");

		mediaCollectionModelList.add(containerModel);
	}

	@Test
	public void testPopulate()
	{



		final List<String> imageFormats = new ArrayList<String>();

		imageFormats.add(".png");
		imageFormats.add("Jpeg");
		imageFormats.add("gif");
		dataPopulator.setImageFormats(imageFormats);

		Mockito.when(imageFormatMapping.getMediaFormatQualifierForImageFormat(Mockito.anyString())).thenReturn("media");
		Mockito.when(mediaService.getFormat(Mockito.anyString())).thenReturn(mediaFormatModel);
		Mockito.when(
				mediaContainerService.getMediaForFormat(Mockito.any(MediaContainerModel.class), Mockito.any(MediaFormatModel.class)))
				.thenReturn(mediaModel);
		Mockito.when(imageConverter.convert(Mockito.any(MediaModel.class))).thenReturn(imageData);
		Mockito.doNothing().when(imageDataMock).setFormat(Mockito.anyString());
		Mockito.when(imageDataMock.getImageType()).thenReturn("imageType");
		Mockito.when(dataPopulator1.getImageFormats()).thenReturn(imageFormats);

		try
		{
			dataPopulator.populate(mediaCollectionModelList, imageDataList);
			System.out.println(imageDataList + "imageF");
			assertNotNull(imageDataList);


		}
		catch (final ConversionException e)
		{
			e.getMessage();
		}
	}

}
