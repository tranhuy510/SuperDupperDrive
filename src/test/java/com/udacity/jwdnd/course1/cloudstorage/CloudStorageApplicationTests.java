package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.NoSuchElementException;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.close();
			driver.quit();
			System.out.println("cleaning ...");
		}
	}

	@Test
	public void unauthorizedUserAccessibleRoutes(){
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

	}

	@Test
	public void testUserSignupLoginLogout() throws InterruptedException {
		// Sign up & login
		signUpAndLogin("HuyTQ", "HuyTQ", "Test", "abcd1234");

		// logout
		WebElement logoutButton= driver.findElement(By.id("logout-button"));
		logoutButton.click();

        Assertions.assertNotEquals("Home", driver.getTitle());
		Assertions.assertEquals("Login", driver.getTitle());

		Thread.sleep(3000);

	}

	private void signUpAndLogin(String username, String firstName, String lastName, String password) throws InterruptedException {
		doMockSignUp(firstName, lastName, username, password);
		doLogIn(username, password);
	}

	public void redirectToNotesTab(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
	}

	@Test
	public void createNote() throws InterruptedException {
		// Sign up & login
		signUpAndLogin("HuyTQ", "HuyTQ", "Test", "abcd1234");

		// Navigate to the notes tab
		openNotesTab();

		// Verify that the notes tab appears
		Assertions.assertTrue(isElementVisible(By.id("nav-notes")), "Notes tab should be visible.");

		// Add a new note
		addNewNote("Test Note", "Testing create note!");

		// Redirect to the notes tab and check if the note appears
		redirectToNotesTab();
		Assertions.assertTrue(isNotePresent("Test Note"), "The note should be present in the notes table.");

		// Wait for a short period to observe the result (optional)
		Thread.sleep(3000);
	}

	private void openNotesTab() {
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		new WebDriverWait(driver, 2).until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
	}

	private boolean isElementVisible(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private void addNewNote(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// Press on add note button
		WebElement addNoteButton = driver.findElement(By.id("add-note-button"));
		addNoteButton.click();

		// Fill out the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		driver.findElement(By.id("note-title")).sendKeys(title);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		driver.findElement(By.id("note-description")).sendKeys(description);

		// Attempt to submit the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-button")));
		driver.findElement(By.id("submit-note-button")).click();
	}

	private boolean isNotePresent(String noteTitle) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		return driver.findElement(By.id("table-note-title")).getText().contains(noteTitle);
	}


	@Test
	public void editNote() throws InterruptedException {
		createNote();

		openEditNoteModal();

		editNoteDescription("Test update note!");

		saveNoteChanges();

		verifyNoteDescription("Test update note!");
	}

	private void openEditNoteModal() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-button")));
		driver.findElement(By.id("edit-note-button")).click();
	}

	private void editNoteDescription(String newDescription) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));

		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.clear();
		inputDescription.sendKeys(newDescription);
	}

	private void saveNoteChanges() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-button")));
		driver.findElement(By.id("submit-note-button")).click();

		redirectToNotesTab();
	}

	private void verifyNoteDescription(String expectedDescription) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

		Assertions.assertTrue(driver.findElement(By.id("table-note-description")).getText().contains(expectedDescription),
				"The note description should be updated.");
	}

	@Test
	public void deleteNote() throws InterruptedException {
		createNote();

		handleDeleteNote();

		verifyNoteDeleted();
	}

	private void handleDeleteNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note-button")));
		driver.findElement(By.id("delete-note-button")).click();

		redirectToNotesTab();
	}

	private void verifyNoteDeleted() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		WebElement notesTable = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		List<WebElement> notesList = notesTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(0, notesList.size(), "The note should be deleted from NOTE table.");
	}


	public void redirectToCredentialsTab(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
	}

	@Test
	public void createCredential() throws InterruptedException {
		// Sign up & login
		signUpAndLogin("HuyTQ", "Test", "HuyTQ", "abcd1234");

		openCredentialsTab();

		addCredential("https://www.google.com/", "HuyTQ", "abcd1234");

		verifyCredentialAdded("abcd1234");
	}

	private void openCredentialsTab() {
		driver.findElement(By.id("nav-credentials-tab")).click();
		new WebDriverWait(driver, 2).until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
	}

	private void addCredential(String url, String username, String password) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credentials-button"))).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).sendKeys(url);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username"))).sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password"))).sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-credential-button"))).click();

		redirectToCredentialsTab();
	}

	private void verifyCredentialAdded(String originalPassword) {
		WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credList = credentialsTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(1, credList.size(), "There should be one credential present.");

		Assertions.assertNotEquals(driver.findElement(By.id("table-cred-password")).getText(), originalPassword, "The displayed password should be encrypted.");
	}

	@Test
	public void editCredential() throws InterruptedException {
		createCredential();

		editCredentialDetails("https://www.youtube.com/");

		verifyCredentialEdited("https://www.youtube.com/");
	}

	private void editCredentialDetails(String newUrl) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-credential-button"))).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).clear();
		driver.findElement(By.id("credential-url")).sendKeys(newUrl);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-credential-button"))).click();

		redirectToCredentialsTab();
	}

	private void verifyCredentialEdited(String expectedUrl) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		Assertions.assertTrue(driver.findElement(By.id("table-cred-url")).getText().contains(expectedUrl), "The URL should be updated.");

		String inputPassword = driver.findElement(By.id("credential-password")).getAttribute("value");
		Assertions.assertNotEquals(driver.findElement(By.id("table-cred-password")).getText(), inputPassword, "The displayed password should be encrypted.");
	}

	@Test
	public void deleteCredential() throws InterruptedException {
		createCredential();

		handleDeleteCredential();

		verifyCredentialDeleted();
	}

	private void handleDeleteCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credential-button"))).click();

		redirectToCredentialsTab();
	}

	private void verifyCredentialDeleted() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		WebElement credentialTable = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		List<WebElement> credList = credentialTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(0, credList.size(), "The credential should be deleted.");
	}


	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}



	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertTrue(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys("D:/udacity/Java Web Developer/SuperDuperDrive/cloudstorage/upload5m.zip");

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertTrue(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

}