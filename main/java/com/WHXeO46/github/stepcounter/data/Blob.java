package com.WHXeO46.github.stepcounter.data;

import androidx.annotation.Nullable;
import com.WHXeO46.github.stepcounter.utils.SHA1;
import com.WHXeO46.github.stepcounter.user.Account;
import java.io.*;
import android.content.Context;
import java.io.File;

public class Blob implements Serializable {

    private final Date date;
    private final int steps;
    private final String hashcode;
    // For creating new Blob
    public Blob(int steps) {
        this.steps = steps;

        this.date = new Date();
        this.hashcode = SHA1.hash(SHA1.hash(""+steps)+SHA1.hash(date));
    }

    // For loading from storage
    public Blob(int step, Date date, String hash) {
        this.steps = step;
        this.date = date;
        this.hashcode = hash;
    }

    public int getStep() {
        return steps;
    }

    public Date getDate() {
        return date;
    }

    // Serialize this blob into your storage
    public void save(Context context, boolean isTemp) throws IOException {
        if (isTemp) {
            File tempDir = new File(context.getFilesDir(), "Temp/"+Account.getUserName()+"/"+Account.getPassword());
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            File blobFile = new File(tempDir, "tmp.blob");

            try (FileOutputStream fos = new FileOutputStream(blobFile);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(this);
            }
        } else {
            File userDir = new File(context.getFilesDir(), Account.getUserName() + "/" + Account.getPassword() + "/userdata");
            if (!userDir.exists()) {
                userDir.mkdirs();
            }
            File blobFile = new File(userDir, hashcode+".blob");

            try (FileOutputStream fos = new FileOutputStream(blobFile);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(this);
            }
        }
    }

    // reload this blob into your application
    public static Blob load(Context context, @Nullable String hash, boolean isTemp) throws IOException, ClassNotFoundException {
        if (isTemp) {
            File userDir = new File(context.getFilesDir(), "Temp/"+Account.getUserName() + "/" + Account.getPassword());
            File blobFile = new File(userDir, "tmp.blob");
            if (blobFile.exists()) {
                try (FileInputStream fis = new FileInputStream(blobFile);
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    return (Blob) ois.readObject();
                }
            } else {
                return new Blob(0);
            }
        } else {
            File userDir = new File(context.getFilesDir(), Account.getUserName() + "/" + Account.getPassword() + "/userdata");
            File blobFile = new File(userDir, hash+".blob");

            try (FileInputStream fis = new FileInputStream(blobFile);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                return (Blob) ois.readObject();
            }
        }


    }
}
