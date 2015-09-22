/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.auth.AuthenticationService;
import de.hybris.platform.servicelayer.security.auth.InvalidCredentialsException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.tui.facades.impl.DefaultAccomodationFacade;
import uk.co.portaltech.tui.web.view.data.SustainableTourismComponentViewData;
import uk.co.tui.async.logging.TUILogUtils;



/**
 * @author niranjani.r
 *
 */
@IntegrationTest
public class DefaultAccommodationServiceIntegrationTest extends ServicelayerTransactionalTest {

   		@Resource
		private ModelService modelService;
	    @Resource
	    private DefaultAccomodationFacade accomodationFacade;
	    @Resource
	    private  CMSSiteService  cmsSiteService;
	    @Resource
	    private static UserService userService;
	    @Resource
	    private static AuthenticationService authenticationService;

		/**
		 * Value holder for catalog version
		 */
		private CatalogVersionModel catalogVersion;
		/**
		 *  value holders for feature descriptors.
		 */
		private FeatureDescriptorModel feature1,feature2,feature3,feature4,feature5;
	    /**
	     *  value holder for feature value sets to be added for accommodation.
	     */
	    private FeatureValueSetModel valueSet1,valueSet2,valueSet3,valueSet4;
	    /**
	     *  value holder for feature value model.
	     */
	    private FeatureValueModel value1, value2;
	    /**
	     * value holder for view data.
	     */
	    private SustainableTourismComponentViewData viewData;
	    private static final String GOLD= "Gold is awarded to hotels that demonstrate industry-leading ";
	    private static final String ACCOMMCODE="007854";
	    private static final String ACCOMMCODE1="027407";
	    /**
	     *  value holder for setting active catalog version.
	     */
	    @SuppressWarnings("boxing")
		private static final Boolean ACTIVE=true;
	    
	    private final TUILogUtils LOG = new TUILogUtils("DefaultAccommodationServiceIntegrationTest");


	    /**
	     * @throws Exception
	     */
	    @Before
	    public void setUp() throws Exception {

	     AccommodationModel accommodationModel1,accommodationModel2,accommodationModel3;
		//create user admin
			 createDummyUserAuthentication();

	    //create cms site
			 createDummyCmssiteWithCurrentCatalogandCatalogVersion();

		//create feature dscriptors
			 createDummyFeatureDescriptors();

	    //create featurevaluesets
			 createDummyFeatureValueSets();

		//create feature value
			 createDummyFeatureValue();

	    // adding FeatureValueToFeatureValueSet
         final List<FeatureValueModel> existingFeatureValues = new ArrayList<FeatureValueModel>();
         existingFeatureValues.add(value1);
         valueSet1.setFeatureValues(existingFeatureValues);
         value1.setFeatureValueSet(valueSet1);

         final List<FeatureValueModel> existingFeatureValues1 = new ArrayList<FeatureValueModel>();
         existingFeatureValues.add(value2);
         valueSet4.setFeatureValues(existingFeatureValues1);
         value2.setFeatureValueSet(valueSet4);

         //Create list feature Value sets
         final List<FeatureValueSetModel> featureValueSets= new  ArrayList<FeatureValueSetModel>();
         featureValueSets.add(valueSet1);
         featureValueSets.add(valueSet2);
         featureValueSets.add(valueSet3);


         final List<FeatureValueSetModel> featureValueSets1= new  ArrayList<FeatureValueSetModel>();
         featureValueSets.add(valueSet2);
         featureValueSets.add(valueSet3);
         featureValueSets.add(valueSet4);

		 // creating accommodation model with data
		 accommodationModel1= modelService.create(AccommodationModel.class);
		 accommodationModel1.setCode(ACCOMMCODE);
		 accommodationModel1.setName("Hotel Nana Beach");
		 accommodationModel1.setCatalogVersion(catalogVersion);
		 accommodationModel1.setFeatureValueSets(featureValueSets);
		 modelService.save(accommodationModel1);

		 accommodationModel2= modelService.create(AccommodationModel.class);
		 accommodationModel2.setCode(ACCOMMCODE1);
		 accommodationModel2.setName("Barcelo Costa Cancun");
		 accommodationModel2.setCatalogVersion(catalogVersion);
		 accommodationModel2.setFeatureValueSets(featureValueSets1);
		 modelService.save(accommodationModel2);


		 accommodationModel3= modelService.create(AccommodationModel.class);
		 accommodationModel3.setCode("004097");
		 accommodationModel3.setName("Hotel Orion");
		 accommodationModel3.setCatalogVersion(catalogVersion);
		 modelService.save(accommodationModel3);

		 // creating view data
		 viewData=new SustainableTourismComponentViewData();
		 viewData.setTitle("");
		 viewData.setDescription("");



	  }

	     /**
	     *  Method to create admin  user
	     */
	    private void createDummyUserAuthentication()	{
		 // To authenticate and login  as a administrator and  to overcome anonymous user account
		   //which is default .

	    	 UserModel usr= modelService.create(UserModel.class);
		    usr = userService.getUserForUID("admin");
		    modelService.save(usr);
		    final String adminusr = usr.getUid();
	        try {
				authenticationService.login(adminusr, "nimda");
			} catch (InvalidCredentialsException e) {
				LOG.error("invalid credentials",e);

			}
      	 }
         /**
         * Method to create cms site.
         */
        private void createDummyCmssiteWithCurrentCatalogandCatalogVersion(){
        	 final CatalogModel catalogModel;
        	 final CMSSiteModel site;
        	 final List<CatalogModel>catalogs=new ArrayList<CatalogModel>();
        	 final List<BaseStoreModel> baseStores= new ArrayList<BaseStoreModel>();
        	    // creating catalog version model
    		 catalogModel = modelService.create(CatalogModel.class);
    		 catalogModel.setId("fc_catalog");

    		 catalogModel.setDefaultCatalog(ACTIVE);
    		 modelService.save(catalogModel);

    		 catalogVersion = new CatalogVersionModel();
    		 catalogVersion.setVersion("Online");
    		 catalogVersion.setActive(ACTIVE);
    		 catalogVersion.setCatalog(catalogModel);
    		 modelService.save(catalogVersion);

    		 catalogs.add(catalogModel);
    		 final BaseStoreModel base= modelService.create(BaseStoreModel.class);
    		 base.setUid("tuistore");
    		 base.setCatalogs(catalogs);
    		 modelService.save(base);

    		 baseStores.add(base);


    		// creating cmssite
    		 site= modelService.create(CMSSiteModel.class);
    		 site.setUid("tui");
    		 site.setDefaultCatalog(catalogModel);
    		 site.setStores(baseStores);
    		 modelService.save(site);
    		 cmsSiteService.setCurrentSite(site);
    		 try {
				cmsSiteService.setCurrentCatalogVersion(catalogVersion);
			} catch (CMSItemNotFoundException e) {
				LOG.error("Could not find cmssite with this catalog version",e);

			}
         }
         /**
         * Method to create Feature Descriptors.
         */
        private void createDummyFeatureDescriptors()
         {
        	 // creating feature descriptor model
    		 feature1= modelService.create(FeatureDescriptorModel.class);
    		 feature1.setCode("travelLifeGold");
    		 feature1.setCatalogVersion(catalogVersion);
    		 modelService.save(feature1);

    		 feature2=modelService.create(FeatureDescriptorModel.class);
    		 feature2.setCode("name");
    		 feature2.setCatalogVersion(catalogVersion);
    		 modelService.save(feature2);

    		 feature3=modelService.create(FeatureDescriptorModel.class);
    		 feature3.setCode("strapline");
    		 feature3.setCatalogVersion(catalogVersion);
    		 modelService.save(feature3);

    		 feature4=modelService.create(FeatureDescriptorModel.class);
    		 feature4.setCode("travelLifeBronze");
    		 feature4.setCatalogVersion(catalogVersion);
    		 modelService.save(feature4);

    		 feature5=modelService.create(FeatureDescriptorModel.class);
    		 feature5.setCode("travelLifeSilver");
    		 feature5.setCatalogVersion(catalogVersion);
    		 modelService.save(feature5);
         }
         /**
         * Method to create FeatureValue sets
         */
        private void createDummyFeatureValueSets()
         {

        	// creating Feaurevalueset model
    		 valueSet1=modelService.create(FeatureValueSetModel.class);
    		 valueSet1.setCatalogVersion(catalogVersion);
             valueSet1.setFeatureDescriptor(feature1);
             valueSet1.setCode("codegen");
             modelService.attach(valueSet1);

              valueSet2=modelService.create(FeatureValueSetModel.class);
              valueSet2.setCatalogVersion(catalogVersion);
              valueSet2.setFeatureDescriptor(feature2);
              valueSet2.setCode("codegen1");
              modelService.attach(valueSet2);

              valueSet3=modelService.create(FeatureValueSetModel.class);
              valueSet3.setCatalogVersion(catalogVersion);
              valueSet3.setFeatureDescriptor(feature3);
              valueSet3.setCode("codegen2");
              modelService.attach(valueSet3);


              valueSet4=modelService.create(FeatureValueSetModel.class);
              valueSet4.setCatalogVersion(catalogVersion);
              valueSet4.setFeatureDescriptor(feature4);
              valueSet4.setCode("codegen4");
              modelService.attach(valueSet4);
         }

         /**
         * Method to create FeatureValues.
         */
        private void createDummyFeatureValue(){
             // creating Feature value model
             value1=modelService.create(FeatureValueModel.class);
             value1.setCatalogVersion(catalogVersion);
             value1.setValue(GOLD);
             value1.setCode("codegen");
             modelService.attach(value1);

             value2=modelService.create(FeatureValueModel.class);
             value2.setCatalogVersion(catalogVersion);
             value2.setValue("");
             value2.setCode("codegen5");
             modelService.attach(value2);
         }

 	    @Test
	    public void testingAccommodationSustainableComponentWithValidData() {


	    	final SustainableTourismComponentViewData view=accomodationFacade.getAccommodationTravelLifeAwardInfo(ACCOMMCODE);
	    	assertThat("TRAVELIFE GOLD", is(view.getTitle()));
	    }

        @Test
        public void testingAccommodationSustaianbleComponentWithEmptyAccommodationCode()
        {
        	final SustainableTourismComponentViewData view =accomodationFacade.getAccommodationTravelLifeAwardInfo("");
        	assertThat(viewData.getTitle(), is(view.getTitle()));

        }
        @Test
        public void testingAccommodationSustainableComponentWithInvalidData()
        {
        	final SustainableTourismComponentViewData view =accomodationFacade.getAccommodationTravelLifeAwardInfo(ACCOMMCODE1);
        	assertThat(viewData.getDescription(), is(view.getDescription()));
        }
        @Test
        public void testingAccommodationSustainableComponentWithEmptyFeatureValueSets(){
        	final SustainableTourismComponentViewData view =accomodationFacade.getAccommodationTravelLifeAwardInfo("004097");
        	assertThat(viewData.getDescription(), is(view.getDescription()));

        }
}
