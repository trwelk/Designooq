package com.Tregaki.designooq;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ChatActivityTest {

    private String message;
    private String user;
    private String friend;
    DatabaseReference rootDatabase;
    @Before
    public void setUp() throws Exception {
         message = "This is a message";
         user = "user1";
         friend = "friend1";


    }

    @After
    public void tearDown() throws Exception {
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