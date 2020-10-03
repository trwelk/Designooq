package com.Tregaki.designooq;
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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class AddNewPostActivityTest {
    private AddNewPostActivity addNewPostActivity = new AddNewPostActivity();
    final private String title = "This is title";
    final private String descriptionPositive = "This is description";
    final private String descriptionNegative = "This is a long fescripion which has more than 100 characters,which is the character limit,lorem ipsum"
            + "lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum";


    @Rule
    public ActivityTestRule<AddNewPostActivity> chatActivityRule = new ActivityTestRule<AddNewPostActivity>(AddNewPostActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadUi() {
        View sendButton = addNewPostActivity.findViewById(R.id.chat_send_button);
        View msgEditText = addNewPostActivity.findViewById(R.id.chat_edit_test);

        assertNotNull(sendButton);
        assertNotNull(msgEditText);

        onView(withId(R.id.chat_send_button)).perform(click());

    }

    @Test
    public void isValidTitle() {
        assertEquals("Pleas enter a title",addNewPostActivity.isValidTitle(null));
        assertEquals(null,addNewPostActivity.isValidTitle(title));
    }

    @Test
    public void isDescriptionValid() {
        assertEquals("The description should be less than 100 characters",addNewPostActivity.isDescriptionValid(descriptionNegative));
        assertEquals("Pleas enter a description",addNewPostActivity.isDescriptionValid(null));
        assertEquals(null,addNewPostActivity.isDescriptionValid(descriptionPositive));
    }
}