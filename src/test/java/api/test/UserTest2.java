package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoits.UserEndpoints;
import api.endpoits.UserEndpoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTest2 {
	
	Faker faker;
	User userpayload;
	public Logger logger; //for log
	
	@BeforeClass
	public void setUp() {
		faker = new Faker();
		userpayload = new User();
		
		userpayload.setId(faker.idNumber().hashCode());
		userpayload.setUsername(faker.name().username());
		userpayload.setFirstName(faker.name().firstName());
		userpayload.setLastName(faker.name().lastName());
		userpayload.setEmail(faker.internet().safeEmailAddress());
		userpayload.setPassword(faker.internet().password(5,10));
		userpayload.setPhone(faker.phoneNumber().cellPhone());
		
		logger = LogManager.getLogger(this.getClass());//this.getClass() dynamically gets the runtime class of the current object. This ensures that the logger is tied to the specific class, even if the code is part of a parent class or a subclass.
	}
	//Logs
	
	@Test(priority=1)
	public void testPostUser() {
		logger.info("********** Creating user  ***************");
		Response response = UserEndpoints2.createUser(userpayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("**********User is created  ***************");
	}
	
	@Test(priority=2)
	public void testGetUserByName() {
		logger.info("********** Reading User Info ***************");
		Response response = UserEndpoints2.readUser(this.userpayload.getUsername());
		response.then().log().all();		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("**********User info  is displayed ***************");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName() {
		logger.info("********** Updating User ***************");
		//Update data using payload
		userpayload.setFirstName(faker.name().firstName());
		userpayload.setLastName(faker.name().lastName());
		userpayload.setEmail(faker.internet().safeEmailAddress());
		//
		Response response = UserEndpoints2.updateUser(this.userpayload.getUsername(),userpayload);
		response.then().log().body();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("********** User updated ***************");
		
		//checking data after update
		Response responseAferterUpdate = UserEndpoints2.readUser(this.userpayload.getUsername());
		
		Assert.assertEquals(responseAferterUpdate.getStatusCode(), 200);
		
	}
	
	@Test(priority=4)
	public void testDeleteUserByName() {
		logger.info("**********   Deleting User  ***************");
		Response response=UserEndpoints2.deleteUser(this.userpayload.getUsername());
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("********** User deleted ***************");
	}

}
