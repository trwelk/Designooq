package com.Tregaki.designooq;

import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {
    private String message;
    private String user;
    private String friend;
    @Rule
    public ActivityTestRule<ChatActivity> chatActivityRule = new ActivityTestRule<ChatActivity>(ChatActivity.class);
    private  ChatActivity chatActivity;

    @Before
    public void setUp() throws Exception {
        chatActivity = chatActivityRule.getActivity();
        message = "This is a message";
        user = "user1";
        friend = "friend1";

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void sendMessage() {

        //assertEquals(false,chatActivity.sendMessageLogic("testUser","testFriend","testMessage"));
    }

    @Test
    public void loadUi() {
        View sendButton = chatActivity.findViewById(R.id.chat_send_button);
        View msgEditText = chatActivity.findViewById(R.id.chat_edit_test);

        assertNotNull(sendButton);
        assertNotNull(msgEditText);

        onView(withId(R.id.chat_send_button)).perform(click());

    }

    @Test
    public void typeMessage() {
        onView(withId(R.id.chat_edit_test)).perform(replaceText("message"));
        onView(withId(R.id.chat_edit_test)).check(matches(withText("message")));
    }

    @Test
    public void friendIsEmpty() {
        assertEquals(false,new ChatActivity().friendIsEmpty(friend));
        assertEquals( true,new ChatActivity().friendIsEmpty(null));

    }

    @Test
    public void senderIsEmpty() {
        assertEquals(false,new ChatActivity().senderIsEmpty(user));
        assertEquals(true,new ChatActivity().senderIsEmpty(null));
    }

    @Test
    public void messageIsEmpty() {
        assertEquals(false,new ChatActivity().messageIsEmpty(message));
        assertEquals( true,new ChatActivity().messageIsEmpty(null));
    }



}