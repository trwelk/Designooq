package com.Tregaki.designooq;

import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {

    @Rule
    public ActivityTestRule<ChatActivity> chatActivityRule = new ActivityTestRule<ChatActivity>(ChatActivity.class);
    private  ChatActivity chatActivity;

    @Before
    public void setUp() throws Exception {
        chatActivity = chatActivityRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void sendMessage() {

        assertEquals(false,chatActivity.sendMessageLogic("testUser","testFriend","testMessage"));
    }

    @Test
    public void loadUi() {
        View sendButton = chatActivity.findViewById(R.id.chat_send_button);
        assertNotNull(sendButton);
    }

    @Test
    public void addMessageToDatabase() {
    }
}