package com.asaphmwangi.studentattendace.login;

import java.util.regex.Pattern;

public class Authentication {
    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public String studLogin(String email, String password)
    {
        if (email.isEmpty() || password.isEmpty())
        {
            return "Fill in the empty fields";

        }
        if (!isValidEmail(email))
        {
            return "Invalid email format";
        }

        return "success";
    }
    public String lecLogin(String email, String password)
    {
        if (email.isEmpty() || password.isEmpty())
        {
            return "Fill in the empty fields";

        }
        if (!isValidEmail(email))
        {
            return "Invalid email format";
        }

        return "success";
    }
    public String studSignUp(String name, String email, String password, String studID)
    {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || studID.isEmpty())
        {
            return "Fill in the empty fields";

        }
        if (!isValidEmail(email))
        {
            return "Invalid email format";
        }
        boolean isPasswordValid = true;

        if (password.length() < 8) {
            System.out.println("❌ Password must be at least 8 characters long.");
            isPasswordValid = false;
        }

        if (!password.matches(".*[A-Z].*")) {
            isPasswordValid = false;
            return "❌ Password must contain at least one uppercase letter.";

        }

        if (!password.matches(".*[a-z].*")) {
            isPasswordValid = false;
            return "❌ Password must contain at least one lowercase letter.";

        }

        if (!password.matches(".*\\d.*")) {
            isPasswordValid = false;
            return "❌ Password must contain at least one digit.";

        }




        return "true";
    }
    public String lecSignUp(String name, String email, String password, String lecID)
    {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || lecID.isEmpty())
        {
            return "Fill in the empty fields";

        }
        if (!isValidEmail(email))
        {
            return "Invalid email format";
        }
        boolean isPasswordValid = true;

        if (password.length() < 8) {
            System.out.println("❌ Password must be at least 8 characters long.");
            isPasswordValid = false;
        }

        if (!password.matches(".*[A-Z].*")) {
            isPasswordValid = false;
            return "❌ Password must contain at least one uppercase letter.";

        }

        if (!password.matches(".*[a-z].*")) {
            isPasswordValid = false;
            return "❌ Password must contain at least one lowercase letter.";

        }

        if (!password.matches(".*\\d.*")) {
            isPasswordValid = false;
            return "❌ Password must contain at least one digit.";

        }

        return "true";
    }

    private static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }


}
