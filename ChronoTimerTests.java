package Sprint3;

//import Sprint3.*;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Owner on 5/6/2017.
 */
//@RunWith(Arquillian.class)
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
        loop(a);
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
        loop(a);
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
        loop(a);

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
    public void triggerStartFinish() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();

        // for individual event
        a.event("IND");
        a.newRun();
        loop(a);
        a.num(11);
        a.num(12);
        a.num(13);
        a.num(14);
        assertEquals(a.totRacers, a.racers.size());
        assertEquals(0, a.toFinish.size());
        assertEquals(0, a.completed.size());

        // testing start, should move all racers from racers list to toFinish list
        a.trigChannel(1);
        a.trigChannel(3);
        a.trigChannel(5);
        a.trigChannel(7);
        assertEquals(0, a.racers.size());
        assertEquals(a.totRacers, a.toFinish.size());
        assertEquals(0, a.completed.size());

        // testing finish, should move all racers from toFinish list to completed list
        a.trigChannel(2);
        a.trigChannel(4);
        a.trigChannel(6);
        a.trigChannel(8);
        assertEquals(0, a.racers.size());
        assertEquals(0, a.toFinish.size());
        assertEquals(a.totRacers, a.completed.size());

        // for parallel individual event
        a.reset();
        a.event("PARIND");
        a.newRun();
        a.num(1);
        a.num(2);
        a.num(3);
        a.num(4);
        a.num(5);

        // testing start for PARIND
        a.trigChannel(1);
        a.trigChannel(1);
        assertEquals(3, a.racers.size());
        assertEquals(2, a.toFinish.size());
        assertEquals(0, a.completed.size());
        a.trigChannel(3);
        a.trigChannel(3);
        assertEquals(1, a.racers.size());
        assertEquals(4, a.toFinish.size());
        assertEquals(0, a.completed.size());
        a.trigChannel(1);
        assertEquals(0, a.racers.size());
        assertEquals(a.totRacers, a.toFinish.size());
        assertEquals(0, a.completed.size());

        // testing finish for PARIND
        a.trigChannel(2);
        a.trigChannel(2);
        assertEquals(0, a.racers.size());
        assertEquals(3, a.toFinish.size());
        assertEquals(2, a.completed.size());
        a.trigChannel(4);
        a.trigChannel(4);
        assertEquals(0, a.racers.size());
        assertEquals(1, a.toFinish.size());
        assertEquals(4, a.completed.size());
        a.trigChannel(2);
        assertEquals(0, a.racers.size());
        assertEquals(0, a.toFinish.size());
        assertEquals(a.totRacers, a.completed.size());

        // for group event
        a.reset();
        a.event("GRP");
        a.newRun();
        a.num(1);
        a.num(2);
        a.num(3);
        a.num(4);
        a.num(5);
        assertEquals(a.totRacers, a.racers.size());
        assertEquals(0, a.toFinish.size());
        assertEquals(0, a.completed.size());

        // testing start for group event
        a.trigChannel(1);
        assertEquals(0, a.racers.size());
        assertEquals(a.totRacers, a.toFinish.size());
        assertEquals(0, a.completed.size());

        // testing finish for group event
        a.trigChannel(2);
        a.trigChannel(2);
        a.trigChannel(2);
        a.trigChannel(2);
        a.trigChannel(2);
        a.updGRPF("5");
        a.updGRPF("2");
        a.updGRPF("3");
        a.updGRPF("4");
        a.updGRPF("1");
        assertEquals(0, a.racers.size());
        assertEquals(0, a.toFinish.size());
        assertEquals(a.totRacers, a.completed.size());

        // for Parallel group event
        a.reset();
        a.event("PARGRP");
        a.newRun();
        a.num(1);
        a.num(2);
        a.num(3);
        a.num(4);
        a.num(5);
        a.num(6);
        a.num(7);
        a.num(8);

        // testing start for PARGRP
        a.trigChannel(1);
        loop(a);
        assertEquals(0, a.racers.size());
        assertEquals(a.totRacers, a.toFinish.size());
        assertEquals(0, a.completed.size());

        // testing finish for PARGRP
        a.trigChannel(4);
        a.trigChannel(5);
        assertEquals(0, a.racers.size());
        assertEquals(6, a.toFinish.size());
        assertEquals(2, a.completed.size());
        a.trigChannel(1);
        a.trigChannel(8);
        a.trigChannel(2);
        assertEquals(0, a.racers.size());
        assertEquals(3, a.toFinish.size());
        assertEquals(5, a.completed.size());
        a.trigChannel(3);
        a.trigChannel(6);
        a.trigChannel(7);
        assertEquals(0, a.racers.size());
        assertEquals(0, a.toFinish.size());
        assertEquals(a.totRacers, a.completed.size());
    }
    @Test
    public void swap() throws Exception{
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

        int temp = 11;

        a.trigChannel(1);
        a.trigChannel(3);
        assertEquals(a.totRacers, a.toFinish.size());
        assertEquals(temp, a.toFinish.getFirst().racerNum);

        a.swap();
        assertEquals(temp, a.toFinish.getLast().racerNum);
    }

    public void loop (ChronoTimerFORTESTSONLY a) {
        for(int i = 0; i < 8; ++i) {
            a.togChannel(i);
            assertTrue(a.channels[i]);
        }
    }
}
