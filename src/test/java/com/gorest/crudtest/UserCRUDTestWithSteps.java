package com.gorest.crudtest;

import com.gorest.gorestinfo.UserSteps;
import com.gorest.testbase.TestBase;
import com.gorest.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class UserCRUDTestWithSteps extends TestBase {
    static String name = TestUtils.getRandomValue() + "Prime";
    static String email = TestUtils.getRandomValue() + "PrimeTesting@gmail.com";
    static String gender = "Male";
    static String status = "Active";
    static int userId;
    @Steps
    UserSteps steps;

    @Title("Get all Users Details")
    @Test
    public void test001() {
        ValidatableResponse response = steps.getAllUsers();
        response.log().all().statusCode(200);
    }

    @Title("This will create a new user")
    @Test
    public void test002() {
        ValidatableResponse response = steps.createUser(name, email, gender, status);
        response.log().all().statusCode(201);
        userId = response.log().all().extract().path("id");
        System.out.println(userId);
    }

    @Title("Verify User Added successfully")
    @Test
    public void test003() {
        HashMap<String, Object> userMap = steps.getUserInfoByUserID(userId);

        Assert.assertThat(userMap, hasValue(name));

    }

    @Title("Update the user information and verify the updated information")
    @Test
    public void test004() {
        name = "Nikhil" + TestUtils.getRandomValue();
        email = TestUtils.getRandomValue() + "Nikhil@gmail.com";
        steps.updateUser(userId, name, email, gender, status).statusCode(200);

        HashMap<String, Object> userMap = steps.getUserInfoByUserID(userId);
        Assert.assertThat(userMap, hasValue(userId));
    }

    @Title("Delete the user and verify if the user is deleted")
    @Test
    public void test005() {
        steps.deleteUser(userId).statusCode(204);

        steps.getUserById(userId).statusCode(404);
    }
}
