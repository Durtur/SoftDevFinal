package test;

import controllers.OfferManager;
import database.DatabaseManager;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

public class OffersTest {

    OffersMock om;

    @Before
    public void setUp() {
        ArrayList<String> offers = new ArrayList<String>();
        om = new OffersMock();
    }

    @After
    public void tearDown() {
        ArrayList<String> offers = null;
        om = null;
    }

    @Test //when there's offers
    public void offersMockTest() {
        ArrayList<String[]> offers = om.getOffers();
        assertTrue(offers.size() != 0);
    }

    @Test //when there's no offers
    public void noOffersMockTest() {
        ArrayList<String[]> offers = om.noOffers();
        assertNotNull(offers);
        assertEquals(0, offers.size());
    }

    @Test(expected = Exception.class) //when connection to db fails
    public void noConnectionMockTest() throws Exception {
          ArrayList<String[]> offers = om.noDBConnection();
        
    }
    /*
    public static void main(String[] args) throws Exception{
        OffersTest ot = new OffersTest();
        ot.setUp();
        ot.offersMockTest();
        ot.noOffersMockTest();
        ot.noConnectionMockTest();
    }*/
}
