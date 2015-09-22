/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import de.hybris.bootstrap.annotations.UnitTest;


/**
 * @author geethanjali.k
 * 
 */
@UnitTest
public class BrowseRecommendationsCarouselComponentControllerTest
{/*
  * private BrowseRecommendationsCarouselComponentModel browsePageRecComponent;
  * 
  * @Mock private ComponentFacade componentFacade;
  * 
  * @Mock private RecommendationsFacade recommendationsFacade;
  * 
  * Boolean cspSorting=false;
  * 
  * private RecommendationsData recommendationsData;
  * 
  * private final Logger LOG = Logger.getLogger(BrowseRecommendationsCarouselComponentControllerTest.class);
  * 
  * String pageType="AccomodationRec"; String brandtype="firstchoice"; String
  * attributeList="BRAND,NAME,COUNTRY,RESORT,T_RATING_AF0104,FEATURED_IMAGE,DEEPLINK_URL,HOTEL_ID,PRODUCT_RANGE";
  * Map<String,String> urlmap= new HashMap<String,String>(); String
  * elementsId="BN_PersonalizedAccomodations,BN_AccomodationRec"; private final String
  * BOOK_ELEMENTS_ID="BN_PersonalizedPackages,BN_PackageDetailsRec";
  * 
  * @Before public void setUp() { MockitoAnnotations.initMocks(this); browsePageRecComponent = new
  * BrowseRecommendationsCarouselComponentModel(); dummyDataforBrowsePageRecommmendationsComponent();
  * 
  * }
  * 
  * public void dummyDataforBrowsePageRecommmendationsComponent() {
  * 
  * browsePageRecComponent.setUid("WF_COM_001.4"); browsePageRecComponent.setName("Browse Recommendations Component");
  * 
  * }
  * 
  * 
  * @Test public void testViewBrowsepageRecommendationsComponent() {
  * 
  * String componentUid = "WF_COM_001.4"; urlmap=getMapUrl(); urlmap.put("elementIds", elementsId);
  * 
  * try { Mockito.when(componentFacade.getComponent(componentUid)).thenReturn(browsePageRecComponent); } catch
  * (NoSuchComponentException e) { LOG.error("No Such component available"); e.printStackTrace(); }
  * 
  * Mockito.when( recommendationsFacade.getRecommendations(pageType,brandtype,urlmap)).thenReturn(recommendationsData);
  * Assert.assertEquals("WF_COM_001.4", browsePageRecComponent.getUid());
  * Assert.assertEquals("Browse Recommendations Component", browsePageRecComponent.getName());
  * 
  * 
  * }
  * 
  * @Test public void testViewBookPageRecommendationsComponent(){
  * 
  * String componentUid = "WF_COM_001.4"; urlmap=getMapUrl(); urlmap.put("elementIds", BOOK_ELEMENTS_ID);
  * urlmap.put("depAirport", "LGW,LTN,STN"); urlmap.put("depDate","17-04-2014"); urlmap.put("rating", "4");
  * urlmap.put("BEST_FOR", "Adults"); SearchResultsRequestData searchRequestData =new SearchResultsRequestData();
  * ArrayList<String> airportList=new ArrayList<String>(); airportList.add("LGW"); airportList.add("LTN");
  * airportList.add("STN"); searchRequestData.setAirportList(airportList); List<Integer> ages = new
  * ArrayList<Integer>(); ages.add(Integer.valueOf(3)); searchRequestData.setChildAges(ages);
  * searchRequestData.setFlexibility(true); searchRequestData.setNoOfAdults(2); searchRequestData.setNoOfChildren(0);
  * searchRequestData.setDuration(14); searchRequestData.setFlexibleDays(3); searchRequestData.setNoOfSeniors(0);
  * searchRequestData.setWhen("17-04-2014"); try {
  * Mockito.when(componentFacade.getComponent(componentUid)).thenReturn(browsePageRecComponent); } catch
  * (NoSuchComponentException e) { LOG.error("No Such component available"); e.printStackTrace(); }
  * 
  * Mockito.when(
  * recommendationsFacade.getBookFlowRecommendations(searchRequestData,brandtype,pageType,urlmap,brandtype,
  * cspSorting)).thenReturn(recommendationsData); Assert.assertEquals("WF_COM_001.4", browsePageRecComponent.getUid());
  * Assert.assertEquals("Browse Recommendations Component", browsePageRecComponent.getName());
  * 
  * } public Map<String,String> getMapUrl(){ urlmap.put("cn","tui"); urlmap.put("cc","firstchoice");
  * urlmap.put("attrList",attributeList); String bnTrailBayNoteValues = null; String urlParameters =
  * "\"http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=047065"+"\""; String bnTrailCookie =
  * "[http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=029575"
  * +","+"http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=013885]";
  * 
  * String bnTrailDecodedValue = URLDecoder.decode(bnTrailCookie); String bnTrailDecodedValuesDelimeter =
  * bnTrailDecodedValue.substring(bnTrailDecodedValue.indexOf("[")+1 , bnTrailDecodedValue.lastIndexOf("]")); String[]
  * bnTrailDecodedValueList = bnTrailDecodedValuesDelimeter.split(","); List<String> bnTrailCookieList =
  * Arrays.asList(bnTrailDecodedValueList); for(String cook:bnTrailCookieList){ urlParameters = urlParameters+","+ cook;
  * }
  * 
  * bnTrailBayNoteValues = "["+urlParameters+"]"; urlmap.put("bnTrail",bnTrailBayNoteValues);
  * 
  * 
  * urlmap.put("userId","6924717449449701377"); urlmap.put("v","1"); return urlmap; }
  */
}
