package com.androftp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;

public class UploadFile extends AsyncTask<String, Void, String> {
    private int port = 21;
    private File file;
    private String server = "", username = "", password = "", localPath = "", remotePath = "";

    public UploadFile(File file,
                      int port,
                      String server,
                      String username,
                      String password,
                      String remotePath) {
        this.file = file;
        this.port = port;
        this.server = server;
        this.username = username;
        this.password = password;
        this.remotePath = remotePath;
    }

    @Override
    protected String doInBackground(String... params) {
        FTPClient ftpClient = new FTPClient();
        String newDirectory = "/atkafasi";
        try {
            ftpClient.setType(FTPClient.TYPE_BINARY);
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.changeDirectory(remotePath);
            ftpClient.createDirectory(newDirectory);
            ftpClient.changeDirectory(newDirectory);
            ftpClient.upload(file);
            ftpClient.logout();
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return "0";
        }
        return "1";
    }
}
