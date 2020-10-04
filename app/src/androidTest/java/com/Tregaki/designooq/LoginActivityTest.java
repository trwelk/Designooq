package com.Tregaki.designooq;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class LoginActivityTest {
////------------------------------------IT19187006---------------------------------------------------------
    private final String validEmail = "Trewon@gmail.com";
    private final String inValidEmail = "Trewon@gmail";
    private final String validPassword = "Trewon@gmail123";
    private final String inValidPassword = "Trewon";

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private  LoginActivity loginActivity;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(CustomerHomeActivity.class.getName(),null,false);
    Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(RegisterOptionActivity.class.getName(),null,false);
    @Before
    public void setUp() throws Exception {
        loginActivity = loginActivityRule.getActivity();
    }
    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void loadUi() {
        View loginButton = loginActivity.findViewById(R.id.login_login_btn);
        View registerButton = loginActivity.findViewById(R.id.login_register_button);
        View emailText = loginActivity.findViewById(R.id.login_email);
        View passWordText = loginActivity.findViewById(R.id.login_password);
        assertNotNull(loginButton);
        assertNotNull(registerButton);
        assertNotNull(emailText);
        assertNotNull(passWordText);
    }
    @Test
    public void loginSuccess(){
        onView(withId(R.id.login_email)).perform(replaceText("test@test.com"));
        onView(withId(R.id.login_password)).perform(replaceText("test@test.com"));

        onView(withId(R.id.login_login_btn)).perform(click());
        Activity customerHomeActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(customerHomeActivity);
    }

    @Test
    public void register(){
        onView(withId(R.id.login_register_button)).perform(click());
        Activity registerActivity = getInstrumentation().waitForMonitorWithTimeout(monitor1,5000);
        assertNotNull(registerActivity);

    }

    @Test
    public void checkTyping() {
        onView(withId(R.id.login_email)).perform(replaceText("test"));
        onView(withId(R.id.login_password)).perform(replaceText("test"));
        onView(withId(R.id.login_email)).check(matches(withText("test")));
        onView(withId(R.id.login_password)).check(matches(withText("test")));
    }

    @Test
    public void checkHints() {

        onView(withId(R.id.login_email)).check(matches(withHint("Email")));
        onView(withId(R.id.login_password)).check(matches(withHint("password")));
    }

    public void testEmailValidation(){
        assertEquals(false,loginActivity.isEmailFaulty(validEmail));
        assertEquals(true,loginActivity.isEmailFaulty(inValidEmail));
    }

    public void testPasswordValidation(){
        assertEquals(false,loginActivity.isPasswordFaulty(validPassword));
        assertEquals(true,loginActivity.isPasswordFaulty(inValidPassword));
    }
}