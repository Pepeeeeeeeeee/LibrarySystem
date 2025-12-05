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
     * @return 0 -> Token authenticated successfully, regenerated lifetime <br/> 1 - > Token authenticated unsuccessfully (possibly missing), regenerated token, send to login*/
    public Map.Entry<Integer, String> getTokensFromCookies(HttpServletRequest request, HttpServletResponse response) {
        String oldToken = checkValidToken(request);
        String token = null;
        boolean validToken = oldToken != null;
        //service.getUserByToken(Arrays.stream(request.getCookies())
        //                .filter(c -> c.getName().startsWith(COOKIE_PREFIX) && c.getValue() != null)
        //                .map(Cookie::getValue)
        //                .findFirst()
        //                .orElse(null)
        //        ).getId()

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

    private String checkValidToken(HttpServletRequest request){
        boolean valid = false;
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().startsWith(COOKIE_PREFIX) && c.getValue() != null) {
                    valid = authToken(c.getValue(), service.getUserByToken(c.getValue()).getId());
                    generateToken = valid;
                    token = c.getValue();
                    break;
                }
            }
        }
        return valid ? token : null;
    }

    private boolean authToken(String token, int id){
        return token.equals(service.getToken(id));
    }

    public boolean validateLogin(String username, String password, HttpServletResponse response) {
        System.err.println(hashPassword(password));
        User user = service.authUser(username, hashPassword(password));
        System.out.println(user.toString());
        boolean res =  user.getUsername().equals(username) && user.getPassword().equals(hashPassword(password));
        addIsAuthenticated(response, res, user.getId());
        generateToken = res;
        storeToken(response, user.getId());
        return res;
    }

    public boolean checkRegisterDataValidity(String username, String password, String passwordConf, HttpServletResponse response){
        if(!password.equals(passwordConf)) return false;
        if(service.checkIfUsernameExists(username)) return false;
        storeUserCred(response, new UserDTO(null, null, username, hashPassword(password), null, null));
        return true;
    }

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

    public void handleRegister(HttpServletResponse response, HttpServletRequest request, int id){
        Map.Entry<String, String> userCred = getUserCred(request);
        User user = service.registerUser(id, userCred.getKey(), userCred.getValue());
        addIsAuthenticated(response, user != null, user != null ? user.getId() : -1);
        generateToken = user != null;
        storeToken(response, user != null ? user.getId() : -1);
    }

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
}