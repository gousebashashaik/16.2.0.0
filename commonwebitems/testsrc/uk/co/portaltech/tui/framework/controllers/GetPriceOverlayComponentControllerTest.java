/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.populators.PriceOverlayViewDataPopulator;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.PriceOverlayViewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.ServiceInputValidationException;

import com.enterprisedt.util.debug.Logger;

/**
 * @author veena.pn
 *
 */
public class GetPriceOverlayComponentControllerTest {
	

	

	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	
	private final TUILogUtils LOG = new TUILogUtils("GetPriceOverlayComponentControllerTest");
	
	private MediaViewData mediaviewdata;
	
	private AccommodationViewData accommodationViewData;	
	
	private ProductModel productModel;
	
	@Mock
	private CatalogVersionModel model;
	
	@Mock
	private PriceOverlayViewData priceOverlayViewData;
	
	
	@Mock
	 private PriceOverlayViewDataPopulator priceOverlayViewDataPopulator;
	
	@Mock
	private CMSSiteService           cmsSiteService;

	@Mock
   private ProductService           productService;
	

	@Mock
	private CatalogModel catalogmodel;
	
	@Mock
	private ProductData productdata;
	
	@Mock
	private TuiUtilityService tuiUtilityService;
	


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		accommodationViewData=new AccommodationViewData();
		priceOverlayViewData = new PriceOverlayViewData();
		productdata = new ProductData();
		 model = new CatalogVersionModel();
		CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
	    model.setCatalog(catalogmodel);
		productModel= new ProductModel();
		dummyValuesForAccommodationViewData();
		dummyPriceOverlayViewData();
		dummyProductModel();
		dummyProductData();
	}
	
	
	public ProductModel dummyProductModel()
	{
		
		productModel.setCode("123");
		productModel.setCatalogVersion(model);
		return productModel;
		
	}
	
	
	public ProductData dummyProductData()
	{
		productdata.setCode("123");
		productdata.setDescription("description1");
		productdata.setName("Holiday-Village-Costa-del");
        productdata.setUrl("url1");
		return productdata;
		
	}
	public PriceOverlayViewData dummyPriceOverlayViewData()
	{
		priceOverlayViewData.setAccomName("accomName");
		priceOverlayViewData.setCode("code");
		priceOverlayViewData.settRating("tRating");
		return priceOverlayViewData;
	}

	public AccommodationViewData dummyValuesForAccommodationViewData()
	{  
		
		mediaviewdata =new MediaViewData();
		mediaviewdata.setCode("0987"); 
		mediaviewdata.setMime("jpg");
		mediaviewdata.setSize("Small");
		mediaviewdata.setDescription("small image");
		List<MediaViewData> galleryImages =  new ArrayList<MediaViewData>();
		galleryImages.add(mediaviewdata);
		accommodationViewData.setPriceFrom("$100");
		accommodationViewData.setStayPeriod("7 Days");
		accommodationViewData.setAccommodationType("Hotel");
		accommodationViewData.setDeparturePoint("Bangalore");
		accommodationViewData.setDepartureCode("BGLR");
		accommodationViewData.setRoomOccupancy("4");
		accommodationViewData.setReviewRating("4.5");
		accommodationViewData.setLocationMapUrl("http://www.firstchoice.co.uk");
		accommodationViewData.setGalleryImages(galleryImages);
		accommodationViewData.setUrl("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sarigerme/Holiday-Village-Turkey-030164");
		return accommodationViewData;
	}
	
	
	@Test
	public void testgetPriceOverlayComponent() 
	{		
		  
	     
		 
	       try {
			when(tuiUtilityService.getProductByCode("256")).thenReturn(productdata);
			when(productService.getProductForCode(cmsSiteService.getCurrentCatalogVersion(), "027942")).thenReturn(productModel);
			 Assert.assertEquals(productModel.getCode(), "123");
			 Assert.assertEquals(productdata.getCode(), "123");
				} 
	       catch (ServiceInputValidationException e) {
			e.printStackTrace();
		}
	}

	
}
