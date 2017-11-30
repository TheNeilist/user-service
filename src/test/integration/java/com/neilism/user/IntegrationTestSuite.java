package com.neilism.user;

import com.neilism.user.controller.AuthControllerIntTest;
import com.neilism.user.controller.UserControllerIntTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*
set the following env vars:
TEST_ADMIN_USERNAME
TEST_ADMIN_PASSWORD
TEST_ADMIN_AUTHTOKEN
*/
@Suite.SuiteClasses({
        AuthControllerIntTest.class,
        UserControllerIntTest.class
})
@RunWith(Suite.class)
public class IntegrationTestSuite {
}
