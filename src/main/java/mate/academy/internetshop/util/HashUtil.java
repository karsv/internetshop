package mate.academy.internetshop.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HashUtil {
    private static final Logger LOGGER = LogManager.getLogger(HashUtil.class);

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) {
        StringBuilder hashedPassword = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(salt);
            byte[] digest = messageDigest.digest(Base64.getDecoder().decode(password));
            for (byte b : digest) {
                hashedPassword.append(b);
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("trouble with hashing password", e);
        }
        return hashedPassword.toString();
    }
}
