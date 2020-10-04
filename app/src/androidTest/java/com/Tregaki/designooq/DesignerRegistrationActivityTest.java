package com.Tregaki.designooq;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class DesignerRegistrationActivityTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    private DesignerRegistrationActivity designerRegistrationActivity = new DesignerRegistrationActivity();
    private String phonePositive = "0778693725";
    private String phonenegative = "07786937";
    private String emailPositive = "trewon@gmail.com";
    private String emailNegative = "trewon@gmail";
    private String userName = "trewon";
    private String websitePositive = "www.abc.com";
    private String getWebsiteNegative = "www@gmail";


    @Test
    public void phoneHasErrors() {
        assertEquals(true,designerRegistrationActivity.phoneHasErrors(phonenegative) );
        assertEquals(false,designerRegistrationActivity.phoneHasErrors(phonePositive) );
    }

    @Test
    public void websiteHasErrors() {
        // assertEquals(false,designerRegistrationActivity.websiteHasErrors(websitePositive) );
        //assertEquals(true,designerRegistrationActivity.websiteHasErrors(getWebsiteNegative) );
    }

    @Test
    public void emailHasErrors() {
        assertEquals(false,designerRegistrationActivity.emailHasErrors(emailPositive) );
        assertEquals(true,designerRegistrationActivity.emailHasErrors(emailNegative) );
    }
    @Test
    public void userNameHasErrors() {
        assertEquals(false,designerRegistrationActivity.userNameHasErrors(userName) );

    }

    @Test
    public void loadUi() {
        View username = designerRegistrationActivity.findViewById(R.id.designer_registration_username);
        View website = designerRegistrationActivity.findViewById(R.id.designer_registration_website);
        View phone = designerRegistrationActivity.findViewById(R.id.designer_registration_phone);
        View email = designerRegistrationActivity.findViewById(R.id.designer_registration_email);
        View password = designerRegistrationActivity.findViewById(R.id.designer_registration_password);
        View registerBtn = designerRegistrationActivity.findViewById(R.id.designer_registration_register_button);

        assertNotNull(username);
        assertNotNull(website);
        assertNotNull(phone);
        assertNotNull(email);
        assertNotNull(password);
        assertNotNull(registerBtn);


        onView(withId(R.id.chat_send_button)).perform(click());

    }

    @Test
    public void typeInputs() {
        onView(withId(R.id.designer_registration_username)).perform(replaceText("message"));
        onView(withId(R.id.designer_registration_website)).check(matches(withText("message")));
        onView(withId(R.id.designer_registration_phone)).check(matches(withText("message")));
        onView(withId(R.id.designer_registration_email)).check(matches(withText("message")));
        onView(withId(R.id.designer_registration_password)).check(matches(withText("message")));
        onView(withId(R.id.designer_registration_register_button)).check(matches(withText("message")));
    }

}