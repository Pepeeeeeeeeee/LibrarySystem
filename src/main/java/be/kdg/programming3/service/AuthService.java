package be.kdg.programming3.service;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.dto.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class AuthService {
    private static final String COOKIE_PREFIX = "test_userToken_";
    private UserService service;
    private boolean generateToken = false;

    public AuthService(UserService service){
        this.service = service;
    }

    /**
     * Hashes the given password using the SHA-256 algorithm and encodes it as a Base64 string.
     * This ensures passwords are never stored or compared in plaintext.
     *
     * @param password the plaintext password to hash
     * @return a Base64-encoded SHA-256 hash of the password, or {@code null} if the algorithm is not found
     */
    private String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] hashedBytes = messageDigest.digest();
            return Base64.getEncoder().encodeToString(hashedBytes); // Encoding to Base64 for safe string representation
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
            return null;
        }
    }

    /**
     * Generates a new authentication token, stores it as an HTTP-only cookie,
     * and persists the token in the database for the given user.
     *
     * @param response HTTP response used to add the cookie
     * @param id       user ID for which the token is generated
     * @return the generated token string, or {@code null} if user ID is invalid
     */
    private String storeToken(HttpServletResponse response, int id) {
        if(id == -1) return null;
        UUID token = UUID.randomUUID();

        Cookie newToken = new Cookie(COOKIE_PREFIX, token.toString());
        newToken.setHttpOnly(true);
        newToken.setPath("/");
        newToken.setMaxAge(60 * 60 * 24 * 7); // 7 days

        response.addCookie(newToken);
        service.updateToken(id, token.toString());

        return token.toString();
    }

    /**
     * Stores authentication state cookies indicating whether the user
     * is authenticated and what their user ID is.
     *
     * @param response HTTP response used to add cookies
     * @param isAuth   whether the user is authenticated
     * @param id       authenticated user ID
     */
    private void addIsAuthenticated(HttpServletResponse response, boolean isAuth, int id) {
        Cookie isUserAuth = new Cookie("isAuth", Boolean.toString(isAuth));
        Cookie uid = new Cookie("uid", Integer.toString(id));
        isUserAuth.setHttpOnly(true);
        isUserAuth.setPath("/");
        isUserAuth.setMaxAge(60 * 60 * 24 * 7); // 7 days

        response.addCookie(isUserAuth);
        response.addCookie(uid);
    }

    /**
     * Retrieves and validates authentication tokens from cookies.
     *
     * If a valid token is found:
     * - It may be refreshed depending on {@code generateToken}
     * - Authentication cookies are updated
     *
     * @param request  incoming HTTP request
     * @param response HTTP response for updating cookies
     * @return a map entry where:
     *         key = 0 if authenticated, 1 otherwise
     *         value = refreshed token (or null)
     */
    public Map.Entry<Integer, String> getTokensFromCookies(HttpServletRequest request, HttpServletResponse response) {
        String oldToken = checkValidToken(request);
        String token = null;
        boolean validToken = oldToken != null;

        if(generateToken){
            token = storeToken(response, service.getUserByToken(oldToken).getId());
            addIsAuthenticated(response, true, service.getUserByToken(token).getId());
        }

        if(validToken){
            return new AbstractMap.SimpleEntry<>(0, token);
        } else{
            return new AbstractMap.SimpleEntry<>(1, token);
        }
    }


    /**
     * Checks whether the current request contains a valid authentication flag.
     *
     * @param request HTTP request containing cookies
     * @return {@code true} if authenticated, otherwise {@code false}
     */
    public boolean checkIsAuth(HttpServletRequest request){
        boolean valid = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().startsWith("isAuth") && c.getValue() != null) {
                    valid = Boolean.parseBoolean(c.getValue());
                    break;
                }
            }
        }
        return valid;
    }

    /**
     * Validates the authentication token found in cookies against
     * the token stored in the database.
     *
     * @param request HTTP request containing cookies
     * @return valid token string, or {@code null} if invalid
     */
    private String checkValidToken(HttpServletRequest request){
        boolean valid = false;
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().startsWith(COOKIE_PREFIX) && c.getValue() != null) {
                    if(service.getUserByToken(c.getValue()) == null){
                        break;
                    }
                    valid = authToken(c.getValue(), service.getUserByToken(c.getValue()).getId());
                    generateToken = valid;
                    token = c.getValue();
                    break;
                }
            }
        }
        return valid ? token : null;
    }

    /**
     * Compares a provided token with the token stored for the user.
     *
     * @param token provided token
     * @param id    user ID
     * @return {@code true} if tokens match
     */
    private boolean authToken(String token, int id){
        return token.equals(service.getToken(id));
    }

    /**
     * Validates login credentials, sets authentication cookies,
     * and generates a new token if authentication succeeds.
     *
     * @param username user username
     * @param password plaintext password
     * @param response HTTP response to store cookies
     * @return {@code true} if login is successful
     */
    public boolean validateLogin(String username, String password, HttpServletResponse response) {
        System.err.println(hashPassword(password));
        User user = service.authUser(username, hashPassword(password));
        if(user == null){
            return false;
        }
        boolean res =  user.getUsername().equals(username) && user.getPassword().equals(hashPassword(password));
        addIsAuthenticated(response, res, user.getId());
        generateToken = res;
        storeToken(response, user.getId());
        return res;
    }

    /**
     * Validates registration data and temporarily stores credentials in cookies.
     *
     * @param username      desired username
     * @param password      password
     * @param passwordConf  password confirmation
     * @param response      HTTP response to store cookies
     * @return {@code true} if data is valid
     */
    public boolean checkRegisterDataValidity(String username, String password, String passwordConf, HttpServletResponse response){
        if(!password.equals(passwordConf)) return false;
        if(service.checkIfUsernameExists(username)) return false;
        storeUserCred(response, new UserDTO(null, null, username, hashPassword(password), null, null));
        return true;
    }

    /**
     * Stores temporary user credentials in HTTP-only cookies
     * for use during the registration flow.
     */
    private void storeUserCred(HttpServletResponse response, UserDTO user) {
        Cookie userName = new Cookie("userName", user.getUsername());
        Cookie pass = new Cookie("pass", user.getPassword());

        userName.setHttpOnly(true);
        userName.setPath("/");
        userName.setMaxAge(60 * 60 * 24); // 1 day
        pass.setHttpOnly(true);
        pass.setPath("/");
        pass.setMaxAge(60 * 60 * 24);

        response.addCookie(userName);
        response.addCookie(pass);
    }

    /**
     * Retrieves and clears temporary registration credentials from cookies.
     */
    private Map.Entry<String, String> getUserCred(HttpServletRequest request){
        String userName = null;
        String pass = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if(c.getName().startsWith("userName") && c.getValue() != null) {
                    userName = c.getValue();
                    c.setMaxAge(0);
                    continue;
                }
                if(c.getName().startsWith("pass") && c.getValue() != null){
                    pass = c.getValue();
                    c.setMaxAge(0);
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(userName, pass);
    }

    /**
     * Completes the registration process by creating a user
     * and authenticating them immediately.
     */
    public void handleRegister(HttpServletResponse response, HttpServletRequest request, int id){
        Map.Entry<String, String> userCred = getUserCred(request);
        User user = service.registerUser(id, userCred.getKey(), userCred.getValue());
        addIsAuthenticated(response, user != null, user != null ? user.getId() : -1);
        generateToken = user != null;
        storeToken(response, user != null ? user.getId() : -1);
    }

    /**
     * Retrieves the authenticated user ID from cookies.
     */
    public int getUIDFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().startsWith("uid") && c.getValue() != null) {
                    return Integer.parseInt(c.getValue());
                }
            }
        }
        return -1;
    }

    /**
     * Logs out the user by clearing all authentication-related cookies
     * and invalidating the stored token.
     */
    public void logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        this.generateToken = false;
        service.updateToken(service.getUserByToken(getTokensFromCookies(request, response).getValue()).getId(), null);
        if (cookies != null) {
            for (Cookie c : cookies) {
                if(c.getName().startsWith("userName") && c.getValue() != null) {
                    c.setMaxAge(0);
                    continue;
                }
                if(c.getName().startsWith("pass") && c.getValue() != null){
                    c.setMaxAge(0);
                    continue;
                }
                if(c.getName().startsWith(COOKIE_PREFIX) && c.getValue() != null){
                    c.setMaxAge(0);
                    continue;
                }
                if(c.getName().startsWith("isAuth") && c.getValue() != null){
                    c.setMaxAge(0);
                    continue;
                }
                if(c.getName().startsWith("uid") && c.getValue() != null){
                    c.setMaxAge(0);
                }
            }
        }
    }

    /**
     * Updates the user's password after validating the old password.
     *
     * @param oldPassword current password
     * @param newPassword new password
     * @param request     HTTP request containing user ID
     * @return 1 if successful, 0 otherwise
     */
    public int updatePassword(String oldPassword, String newPassword, HttpServletRequest request){
        int id = getUIDFromCookies(request);
        User user = service.getUserById(id);
        User userAuth = service.authUser(user.getUsername(), hashPassword(oldPassword));
        if(userAuth != null){
            service.updatePassword(id, hashPassword(newPassword));
            return 1;
        }

        return 0;
    }
}