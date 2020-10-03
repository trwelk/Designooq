package com.Tregaki.designooq;

import org.junit.Test;

import static org.junit.Assert.*;

public class DesignerRegistrationActivityTest {
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
        assertEquals(true,designerRegistrationActivity.phoneHasErrors("asdasd") );
        assertEquals(false,designerRegistrationActivity.phoneHasErrors(phonePositive));
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
        assertEquals(true,designerRegistrationActivity.emailHasErrors("trewon") );

    }

    @Test
    public void userNameHasErrors() {
        assertEquals(false,designerRegistrationActivity.userNameHasErrors(userName) );

    }
}