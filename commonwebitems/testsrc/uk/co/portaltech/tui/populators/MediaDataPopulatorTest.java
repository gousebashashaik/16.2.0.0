/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.List;
import java.util.Locale;

import org.junit.After;
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
public class MediaDataPopulatorTest {
	@InjectMocks
	private MediaDataPopulator mediaDataPopulator=new MediaDataPopulator();
	@Mock
	private MediaModel mediaModel1;
	private MediaModel mediaModel,mediaModel2;
	private MediaViewData mediaViewData;
	@Mock
	private MediaFormatModel formatModel;
	
	private static final String URL="holiday/attraction/Alcudia/Palma-City-Sightseeing-980352";
	private static final String DESC="Media Description";
	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		mediaModel=new MediaModel();
		mediaViewData=new MediaViewData();
		

		
		mediaModel.setAlttext("AltText");
		mediaModel.setDescription(DESC);
		mediaModel.setMime("audio/mpeg");
		mediaModel.setURL(URL);
		mediaModel.setMediaFormat(formatModel);
		
		mediaModel2=new MediaModel();
		
		mediaModel2.setAlttext(null);
		mediaModel2.setDescription(null);
		mediaModel2.setMime(null);
		mediaModel2.setURL(null);
		mediaModel2.setMediaFormat(formatModel);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPopulate() {
		assertNotNull(mediaModel);
		assertNotNull(mediaViewData);
		Mockito.when(mediaModel1.getMediaFormat()).thenReturn(formatModel);
		Mockito.when(formatModel.getName()).thenReturn("Palma-City");
		mediaDataPopulator.populate(mediaModel, mediaViewData);
		
		assertNotNull(mediaViewData);
		assertThat(mediaViewData.getAltText(),is("AltText"));
		assertThat(mediaViewData.getDescription(), is(DESC));
		assertThat(mediaViewData.getMime(), is("audio/mpeg"));
		assertThat(mediaViewData.getSize(), is("Palma-City"));
		assertThat(mediaViewData.getMainSrc(), is(URL));
		
	}
	@Test
	public void testPopulateNull() {
		
		String val=null;
		Mockito.when(mediaModel1.getMediaFormat()).thenReturn(formatModel);
		Mockito.when(formatModel.getName()).thenReturn(null);
		
		mediaDataPopulator.populate(mediaModel2, mediaViewData);
		
		assertThat(mediaViewData.getAltText(),is(val));
		assertThat(mediaViewData.getDescription(), is(val));
		assertThat(mediaViewData.getMime(), is(val));
		assertThat(mediaViewData.getSize(), is(val));
		assertThat(mediaViewData.getMainSrc(), is(val));	
	}
	@SuppressWarnings("boxing")
	@Test
	public void testConvert(){
		
		Mockito.when(mediaModel1.getURL()).thenReturn(URL);
		Mockito.when(mediaModel1.getDescription()).thenReturn(DESC);
		
		final List<MediaViewData> expectedResult=mediaDataPopulator.convert(mediaModel1);
		
		assertNotNull(expectedResult);
		
		assertThat(expectedResult.size(), is(1));
		
			for(MediaViewData data:expectedResult){
				assertThat(data.getMainSrc(), is(URL));
				assertThat(data.getDescription(), is(DESC));
			}
	}
	
	@SuppressWarnings("boxing")
	@Test
	public void testConvertNull(){
		
		String val=null;
		
		Mockito.when(mediaModel1.getURL()).thenReturn(val);
		Mockito.when(mediaModel1.getDescription()).thenReturn(val);
		
		final List<MediaViewData> expectedResult=mediaDataPopulator.convert(mediaModel2);
		
		assertNotNull(expectedResult);
		
		
			for(MediaViewData data:expectedResult){
				assertThat(data.getMainSrc(), is(val));
				assertThat(data.getDescription(), is(val));
			}
	}

}
