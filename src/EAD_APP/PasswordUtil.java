package EAD_APP;

import org.mindrot.jbcrypt.BCrypt;      // securely store passwords  jBcrptjar used

public class PasswordUtil {   // in database password is not clearly viewed
    public static String hashPassword(String plainPassword) { 
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}