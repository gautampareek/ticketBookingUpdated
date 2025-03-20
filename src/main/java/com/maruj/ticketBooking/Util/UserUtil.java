package com.maruj.ticketBooking.Util;

import java.util.Base64;

public class UserUtil {
    public static String getHashedPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
    public static String setHashedPassword(String hashedPassword) {
        return Base64.getDecoder().decode(hashedPassword).toString();
    }
}
