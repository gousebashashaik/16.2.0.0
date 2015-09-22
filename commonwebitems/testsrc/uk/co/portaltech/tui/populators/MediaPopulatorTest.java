/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.LocMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.web.view.data.MediaViewData;

/**
 * @author sureshbabu.rn
 *
 */
public class MediaPopulatorTest {
	@InjectMocks
	private MediaPopulator mediaPopulator=new MediaPopulator();
	
	private Collection<MediaContainerModel> galleryImages;
	private List<MediaViewData> imageDataList;
	@Mock
	private ImageFormatMapping  imageFormatMapping;
	@Mock
	private  MediaService                mediaService;
	@Mock
	private MediaContainerService       mediaContainerService;
	@Mock
	private MediaModel mediaModel;
	@Mock
	private MediaFormatModel mediaFormatModelMock;
    
    private List<String>        imageFormats;
    
    private MediaContainerModel  containerModel;
    private String mediaFormatQualifier="658x370";
    private String URL="http://newmedia.thomson.co.uk/live/vol/0/280597cd3a96d2b0dcf6ac7b7185195b0a8ac3d8/658x370/web/EUROPEMEDITERRANEANSPAINCON_ESPMAJORCAPALMARES_000282.jpg";
    private MediaFormatModel mediaFormat;
    @Mock
    private MediaFormatModel mediaFormat1;
    
    private MediaModel media;
    LocMap<String> locMap;
    
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		galleryImages=new ArrayList<MediaContainerModel>();
		imageDataList=new ArrayList<MediaViewData>();
		
		 containerModel=new MediaContainerModel();
		containerModel.setCaption("Gothic Cathedral, Palma");
		containerModel.setMediaType("IMAGE");
		containerModel.setQualifier("280597cd3a96d2b0dcf6ac7b7185195b0a8ac3d8");
		
		galleryImages.add(containerModel);
		
		imageFormats=new ArrayList<String>();
		imageFormats.add("Main Pic");
		
		mediaFormat=new MediaFormatModel();
		mediaFormat.setQualifier(mediaFormatQualifier);
		mediaFormat.setName("CANADA",Locale.CANADA);
		media=new MediaModel();
		media.setCode("280597cd3a96d2b0dcf6ac7b7185195b0a8ac3d8_658x370");
		media.setMime("image/jpeg");
		media.setURL(URL);
		media.setMediaFormat(mediaFormat);
		 locMap=new LocMap<String>();
	
		locMap.put(Locale.CANADA, "CANADA");
	}
	
	@Test
	public void testPopulate()throws ConversionException {
		
		List<String> imageFormats=new ArrayList<String>();
		imageFormats.add("jpeg");
		Mockito.when(imageFormatMapping.getMediaFormatQualifierForImageFormat(Mockito.anyString())).thenReturn("658x370");
		Mockito.when(mediaService.getFormat(mediaFormatQualifier)).thenReturn(mediaFormat);
		Mockito.when(mediaModel.getMediaFormat()).thenReturn(mediaFormat);
		Mockito.when(mediaFormat1.getName()).thenReturn(locMap.get(locMap));
		
		mediaPopulator.setImageFormats(imageFormats);
		mediaPopulator.populate(galleryImages, imageDataList);
		
		assertNotNull(imageDataList);
		assertNotNull(galleryImages);
		
	
	}

}
