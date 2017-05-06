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
    public void run() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
        assertFalse(a.getRun());
        a.newRun();
        assertTrue(a.getRun());
    }

    @Test
    public void event() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
    }

    @Test
    public void toggle() throws Exception {
        ChronoTimerFORTESTSONLY a = new ChronoTimerFORTESTSONLY();
        a.setPower();
    }

    @Test
    public void reset() throws Exception {
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Sprint3.GUI.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
