package com.Tregaki.designooq;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddNewPostActivityTest {
    private AddNewPostActivity addNewPostActivity = new AddNewPostActivity();
    final private String title = "This is title";
    final private String descriptionPositive = "This is description";
    final private String descriptionNegative = "This is a long fescripion which has more than 100 characters,which is the character limit,lorem ipsum"
            + "lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum,lorem ipsum";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
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