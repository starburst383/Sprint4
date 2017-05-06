package Sprint3;

//import Sprint3.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Owner on 5/6/2017.
 */
@RunWith(Arquillian.class)
public class ChronoTimerTests {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    //test initial values are what they should be
    public void main() throws Exception {

        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        assertNotEquals(a,null);
        assertFalse(a.getPower());
        assertFalse(a.getRun());
        assertEquals("", a.getEvent());
        assertEquals(8, a.channels.length);
    }

    @Test
    //turn on an off
    public void power() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        assertNotEquals(a,null);
        assertFalse(a.getPower());
        a.setPower();
        assertTrue(a.getPower());
        a.setPower();
        assertFalse(a.getPower());
    }

     @Test
    public void event() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
        assertEquals("", a.getEvent());
        a.event("XXXXXXXXXX");
        assertEquals("", a.getEvent());
        a.event("IND");
        assertEquals("IND", a.getEvent());
        a.reset();
        a.event("PARIND");
        assertEquals("PARIND", a.getEvent());
        a.reset();
        a.event("GRP");
        assertEquals("GRP", a.getEvent());
        a.reset();
        a.event("PARGRP");
        assertEquals("PARGRP", a.getEvent());

    }

    @Test
    public void run() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
        assertFalse(a.getRun());
        a.newRun();
        assertTrue(a.getRun());
        a.endrun();
        assertFalse(a.getRun());
    }

    @Test
    public void runAndEvent() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
        assertFalse(a.getRun());
        assertEquals("", a.getEvent());
        a.event("IND");
        assertEquals("IND", a.getEvent());
        a.newRun();
        assertTrue(a.getRun());
        assertEquals("IND", a.getEvent());
        a.endrun();
        assertFalse(a.getRun());
        assertEquals("IND", a.getEvent());
    }

    @Test
    public void toggle() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
        //turn all channel on
        for(int i = 0; i < 8; ++i) {
            a.togChannel(i);
            assertTrue(a.channels[i]);
        }
        //turn all channels off
        for(int i = 0; i < 8; ++i) {
            a.togChannel(i);
            assertFalse(a.channels[i]);
        }
    }

    @Test
    public void reset() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
        //turn all channles on
        for(int i = 0; i < 8; ++i) {
            a.togChannel(i);
            assertTrue(a.channels[i]);
        }
        //reset all channels
        a.reset();
        //test their value
        for(int i = 0; i < 8; ++i) {
            assertFalse(a.channels[i]);
        }
        //set up run
        a.event("IND");
        a.newRun();
        a.num(11);
        // reset all values
        a.reset();
        //test
        assertFalse(a.getRun());
        assertEquals("", a.getEvent());
        assertEquals(0, a.totRacers);

    }

    
    @Test
    public void num() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
        a.event("IND");
        a.newRun();
        for(int i = 0; i < 8; ++i) {
            a.togChannel(i);
            assertTrue(a.channels[i]);
        }

        a.num(11);
        a.num(12);
        a.num(13);
        a.num(14);

        assertEquals(4, a.totRacers);
        assertEquals(4, a.racers.size());
        for(int i = 0; i < 4; ++i) {
            assertEquals(11 + i, a.racers.get(i).racerNum);
        }
    }

    @Test
    public void trigger() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
        a.event("IND");
        a.newRun();
        for(int i = 0; i < 8; ++i) {
            a.togChannel(i);
            assertTrue(a.channels[i]);
        }
        a.num(11);
        a.num(12);
        a.num(13);
        a.num(14);

        for(int i = 0; i < 4; ++i) {


        }
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Sprint3.GUI.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
