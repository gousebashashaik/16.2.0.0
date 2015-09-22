/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.web.view.data.MediaViewData;
import static org.hamcrest.Matchers.is;
/**
 * @author sureshbabu.rn
 *
 */
public class MediaViewDataPopulatorTest {
	
	@InjectMocks
	private MediaViewDataPopulator mediaViewDataPopulator=new MediaViewDataPopulator();
	@Mock
	private MediaFormatModel formatModel;
	private MediaModel sourceData;
	private MediaViewData targetData;
	private final String DESCRIPTION="From its atmospheric old town to the leafy boulevards of its modern shopping district, Palma is one of those got-it-all cities. Today, you’ll get to know Majorca’s capital from the comfort of our sightseeing bus. Best of all, you can hop on and off to explore the bits you fancy on foot. You’ll see the city’s impressive Gothic cathedral, which casts a shadow over the harbour. You’ll tick off the stylish waterfront, with its glossy yachts and open-air restaurants. And you’ll check out the main shopping boulevards, where the local ladies do lunch in designer sunglasses. The city’s showpiece, though, is its cobbled Medieval quarter. Hop off the bus here to discover tiny treasure-trove jewellery and antique shops, and sunny squares where tapas joints advertise their specials on outdoor chalk boards. When you need to refuel, stop for a coffee and an ensaimada – a delicious local pastry – at one of the cubbyhole patisseries.";
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		sourceData=new  MediaModel();
		targetData=new MediaViewData();
				
		sourceData.setCode("002240");
		sourceData.setDescription(DESCRIPTION);
		sourceData.setURL("/holiday/accommodation/location/Benidorm/Flamingo-Benidorm-002240");
		sourceData.setAltText("AltText");
		sourceData.setMime("MP4V-ES");
		sourceData.setMediaFormat(formatModel);
		
	}

	
	/*The test method that coverts the source data to  target data*/
	@Test
	public void testPopulate() {
		try {
			
			assertNotNull(sourceData);
			assertNotNull(targetData);
			
			mediaViewDataPopulator.populate(sourceData, targetData);
			
			assertNotNull(targetData);
			
			assertThat(targetData.getCode(),is("002240") );
			assertThat(targetData.getDescription(), is(DESCRIPTION));
			assertThat(targetData.getMainSrc(), is("/holiday/accommodation/location/Benidorm/Flamingo-Benidorm-002240"));
			assertThat(targetData.getAltText(), is("AltText"));
			assertThat(targetData.getMime(), is("MP4V-ES"));
			assertThat(targetData.getSize(), is(formatModel.getName()));
			
			
		} catch (ConversionException conversionException) {
			conversionException.printStackTrace();
		}
	}

}
