package com.WHXeO46.github.stepcounter.user;

import android.content.Context;
import com.WHXeO46.github.stepcounter.utils.ErrorMessage;
import com.WHXeO46.github.stepcounter.data.Blob;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Account {
    private static String userName = null;
    private static String password = null;
    private static List<Blob> history = new ArrayList<>();

    public static ErrorMessage login(Context context, String user, String pwd) throws IOException {
        File dataDir = context.getFilesDir();
        File folder = new File(dataDir, user+'/'+pwd+"/userdata");
        File userFolder = new File(dataDir, user);

        if (folder.exists() && folder.isDirectory()) {
            userName = user;
            password = pwd;
            save(context);
            load(context, user, pwd);
            return ErrorMessage.LOGIN_SUCCEED;
        } else {
            if (userFolder.exists() && !folder.exists()) {
                return ErrorMessage.LOGIN_ERROR;
            }

            ErrorMessage usernameMessage = isUserNameAvailable(user);
            ErrorMessage passwordMessage = isPasswordAvailable(pwd);

            // If no error, create new account, else return error message
            if (usernameMessage.equals(ErrorMessage.NO_ERROR)
                    && passwordMessage.equals(ErrorMessage.NO_ERROR)) {

                if (folder.mkdirs()) {
                    userName = user;
                    password = pwd;
                    return ErrorMessage.NEW_USER_REGISTERED;
                } else {
                    return  ErrorMessage.LOGIN_FAILED;
                }
            } else {
                if (! usernameMessage.equals(ErrorMessage.NO_ERROR)) {
                    return usernameMessage;
                }
                return passwordMessage;
            }
        }
    }



    private static ErrorMessage isUserNameAvailable(String name) {
        // username's length must be less than 8 characters and cant be empty
        if (name.length()>8 || name.isEmpty()) {
            return ErrorMessage.USERNAME_LENGTH_ERROR;
        }

        // username can only be composed with numer and alphabeta
        for (int i=0; i<name.length(); i++) {
            if (! (Character.isAlphabetic(name.charAt(i))
                    || Character.isDigit(name.charAt(i)))) {

                return ErrorMessage.USERNAME_INVALID_ERROR;
            }
        }

        return ErrorMessage.NO_ERROR;
    }
    private static ErrorMessage isPasswordAvailable(String password) {
        // password's length must be within 8-16 characters
        if (password.length()>16 || password.length()<8) {
            return ErrorMessage.PASSWORD_LENGTH_ERROR;
        }

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            // only number, alphabeta, and '_' are allowed in password
            if (!(Character.isDigit(c)
                    || Character.isAlphabetic(c)
                    || c == '_')) {

                return ErrorMessage.PASSWORD_INVALID_ERROR;
            }
        }

        return ErrorMessage.NO_ERROR;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setUserName(String user) {
        userName = user;
    }

    public static void setPassword(String pwd) {
        password = pwd;
    }

    public static void setAccount(String user, String pwd) {
        userName = user;
        password = pwd;
    }

    public static void setAccount(String[] account) {
        userName = account[0];
        password = account[1];
    }

    public static List<Blob> getHistory() {
        return history;
    }

    public static void load(Context context, String user, String pwd) {
        userName = user;
        password = pwd;
        history.clear();

        File userDir = new File(context.getFilesDir(), user + "/" + pwd + "/userdata");
        if (userDir.exists() && userDir.isDirectory()) {
            File[] blobFiles = userDir.listFiles();
            if (blobFiles != null) {
                for (File blobFile : blobFiles) {
                    try {
                        // The Blob.load method in the user's code seems to use the hash (filename)
                        // and constructs the full path inside.
                        Blob blob = Blob.load(context, blobFile.getName(),false);
                        history.add(blob);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        // Decide how to handle load failure for a single blob
                    }
                }
                // Sort the history by date
                Collections.sort(history, new Comparator<Blob>() {
                    @Override
                    public int compare(Blob o1, Blob o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });
            }
        }
    }

    public static void save(Context context) throws IOException {
        String fileName = "last_user.txt";
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            osw.write(userName + '#' + password + '#');
        }

    }
}
