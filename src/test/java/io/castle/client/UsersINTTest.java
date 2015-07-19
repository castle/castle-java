package io.castle.client;

import io.castle.client.exceptions.CastleException;
import io.castle.client.objects.User;
import io.castle.client.objects.UserCollection;
import org.junit.*;

import java.util.UUID;

import static org.junit.Assert.*;

@Ignore
public class UsersINTTest extends GenericINTTest {

    private static UUID testId;


    @BeforeClass
    public static void setupTest() {
        testId = UUID.randomUUID();
    }

    @Test
    public void createAndThenDeleteUser() {
	User user = new User();
        user.setId(testId.toString());
        user.setEmail("p@noonday.se");
        user.setFirstName("Patrick");
        user.setLastName("Gilmore");
        User.setUserInfoHeaders(userHeader).create(user);
        UserCollection users = User.setUserInfoHeaders(userHeader).listUsers();
        boolean contains = false;
        for(User u : users.getPage()) {
            if(u.getId().equals(testId.toString())) {
                contains = true;
            }
        }
        assertTrue(contains);
        User.setUserInfoHeaders(userHeader).delete(user);
        users = User.setUserInfoHeaders(userHeader).listUsers();
        contains = false;
        for(User u : users.getPage()) {
            if(u.getId().equals(testId.toString())) {
                contains = true;
            }
        }
        assertFalse(contains);
    }

    @Test
    public void findAllUsers() {
	UserCollection users = User.listUsers();
	int i = 0;
	do {
	    for(User u : users.getPage()) {
		try {
		    //User.delete(u);
		    i++;
		}catch(CastleException e) {
		    System.err.println("User id: " + u.getId());
		    e.printStackTrace();
		}
	    }
	}
	while(users.fetchNextPage());
	System.out.println("Found " + i + " users");
    }

    @Test
    public void lockUnlockUser() {
        assertNull(testUser.getLockedAt());
        User updatedUser = User.setUserInfoHeaders(userHeader).lock(testUser);
        assertNotNull(updatedUser.getLockedAt());
        User fetchedUser = User.setUserInfoHeaders(userHeader).find(testUser.getId());
        assertNotNull(fetchedUser.getLockedAt());
        updatedUser = User.setUserInfoHeaders(userHeader).unlock(testUser);
        assertNull(updatedUser.getLockedAt());
    }

    @Test
    public void updateUser() {
        testUser.setFirstName("Testatest");
        testUser.setUsername("tatestsson");
        User updatedUser = User.setUserInfoHeaders(userHeader).update(testUser);
        assertEquals("Testatest", updatedUser.getFirstName());
        assertEquals("tatestsson", updatedUser.getUsername());
    }

}
