package androidsamples.java.dicegames;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.Collection;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class Instrumented_TestCases {

    @Rule
    public ActivityScenarioRule<WalletActivity> mActivityTestRule = new ActivityScenarioRule<WalletActivity>(WalletActivity.class);

    @BeforeClass
    public static void enableAccessibilityCheck() {
        AccessibilityChecks.enable();
    }

    public static Activity getActivity(){
        final Activity[] activity={null};
        getInstrumentation().runOnMainSync(new Runnable(){
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    activity[0]=(Activity)resumedActivities.iterator().next();
                }
            }
        });
        return activity[0];
    }

    @Test
    public void useAppContext() {
        Context context = getInstrumentation().getTargetContext();
        assertEquals("androidsamples.java.dicegames", context.getPackageName());
    }

    @Test
    public void testers() {
        onView(withId(R.id.btn_launch_twoormore)).perform(click());

        Activity activity = getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.btn_go)).perform(click());
    }


    @Test
    public void validateRadioButton() {
        int index = 0;
        while(index < 22) {
            onView(withId(R.id.btn_die)).perform(click());
            index++;
        }

        onView(withId(R.id.btn_launch_twoormore)).perform(click());
        onView(withId(R.id.btn_2alike)).perform(click());
        onView(withId(R.id.edit_wager)).perform(typeText("1"));
        onView(withId(R.id.btn_go)).perform(click());
    }

    @Test
    public void validateGameFlow() {
        for(int index = 0; index < 25; index++)
        {
            onView(withId(R.id.btn_die)).perform(click());
        }
        onView(withId(R.id.btn_launch_twoormore)).perform(click());
        onView(withId(R.id.btn_2alike)).perform(click());
        onView(withId(R.id.edit_wager)).perform(clearText(), typeText("5"));
        onView(withId(R.id.btn_go)).perform(click());
    }


    @Test
    public void testOrientationPersistence() {
        Activity testActivity = getActivity();
        for(int index = 0; index < 5; index++)
            onView(withId(R.id.btn_die)).perform(click());
        testActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void validateWagerInputField() {
        for(int index = 0; index < 15; index++)
        {
            onView(withId(R.id.btn_die)).perform(click());
        }

        onView(withId(R.id.btn_launch_twoormore)).perform(click());
        onView(withId(R.id.edit_wager)).perform(typeText("20"));
    }



}