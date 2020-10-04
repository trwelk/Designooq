package com.Tregaki.designooq;

import android.view.View;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class CustomerRegistrationActivityTest {


    private CustomerRegistrationActivity customerRegistrationActivity = new CustomerRegistrationActivity();
    private String emailPositive = "trewon@gmail.com";
    private String emailNegative = "trewon@gmail";
    private String userName = "trewon";


    @Test
    public void emailHasErrors() {
        assertEquals(false,customerRegistrationActivity.emailHasErrors(emailPositive) );
        assertEquals(true,customerRegistrationActivity.emailHasErrors(emailNegative) );
    }
    @Test
    public void userNameHasErrors() {
        assertEquals(false,customerRegistrationActivity.userNameHasErrors(userName) );

    }

    @Test
    public void loadUi() {
        View username = customerRegistrationActivity.findViewById(R.id.customer_registration_button);
        View website = customerRegistrationActivity.findViewById(R.id.customer_registration_username);
        View phone = customerRegistrationActivity.findViewById(R.id.customer_registration_password);
        View email = customerRegistrationActivity.findViewById(R.id.customer_registration_email);


        assertNotNull(username);
        assertNotNull(website);
        assertNotNull(phone);
        assertNotNull(email);

        onView(withId(R.id.chat_send_button)).perform(click());

    }
}