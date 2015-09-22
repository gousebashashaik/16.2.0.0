package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.jfree.util.Log;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AssertThrows;

import uk.co.portaltech.tui.components.model.PromoComponentModel;
import uk.co.portaltech.tui.facades.impl.DefaultFacilityFacade;
import uk.co.portaltech.tui.facades.impl.DefaultPromoComponentFacade;
import uk.co.portaltech.tui.web.view.data.PromoComponentData;

/**
 *
 */

/**
 * @author kavita.na
 *
 */
public class DefaultPromoComponentFacadeTest {
	 @Mock
	    private CMSComponentService cmsComponentService;

	 @InjectMocks
		private final DefaultPromoComponentFacade Facade= new DefaultPromoComponentFacade();


	 @Mock
	    private Converter<PromoComponentModel, PromoComponentData> promoComponentConverter;

	 private final static String TEST_COMP_ID="promo component";
	 private PromoComponentData promoComponentData,promoComponentData1;
	 private final static String PROMO_TEXT="summer holidays";
	 private PromoComponentModel promoComponentModel;
	 private final static String NAME="PromoComponent";
	 private final static String URL="/holidays";
	 private final static String FORMAT="small";

	 public PromoComponentModel createDummayDataModel()
	 {
		PromoComponentModel model=new PromoComponentModel();
		model.setPromoText(PROMO_TEXT);
		model.setName(NAME);
		return model;
	 }
    public PromoComponentData createDummyData()
    {
    	PromoComponentData data=new PromoComponentData();
    	data.setUrl(URL);
    	data.setPromoText(PROMO_TEXT);
    	data.setComponentID(TEST_COMP_ID);
    	data.setMediaFormat(FORMAT);
    	return data;
    }

    @Test
    public void gePromoComonentViewData() throws CMSItemNotFoundException
    {
    	PromoComponentData componentData=createDummyData();
    	PromoComponentModel componentModel=createDummayDataModel();

			try {
			Mockito.when((PromoComponentModel)cmsComponentService.getSimpleCMSComponent(TEST_COMP_ID)).thenReturn(componentModel);
			Mockito.when(promoComponentConverter.convert(componentModel)).thenReturn(componentData);
			PromoComponentData expectedResult=Facade.gePromoComonentViewData(TEST_COMP_ID);
			assertThat(expectedResult.getMediaFormat(),is(componentData.getMediaFormat()));
			assertThat(expectedResult.getPromoText(),is(componentData.getPromoText()));
			assertThat(expectedResult.getUrl(), is(componentData.getUrl()));
			assertThat(expectedResult.getComponentID(), is(componentData.getComponentID()));
			} catch (NullPointerException e) {
				// YTODO Auto-generated catch block
				Log.error("Null pointe exception thrown");
			}
    }
}
