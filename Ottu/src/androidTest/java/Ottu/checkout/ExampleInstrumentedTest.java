<<<<<<< HEAD:Ottu/src/androidTest/java/Ottu/ExampleInstrumentedTest.java
package Ottu;
=======
package ottu.checkout;
>>>>>>> 45e955ee8ca4f3ddeb4f4b36b3d6ff2021930a94:Ottu/src/androidTest/java/ottu/checkout/ExampleInstrumentedTest.java

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ottu.payment.test", appContext.getPackageName());
    }
}