package com.androftp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.ExFilePickerActivity;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;

public class MainActivity extends AppCompatActivity {
    private int port = 21;
    private File file;
    private String server = "", username = "", password = "", remotePath = "", localPath = "";

    private EditText etServer, etUsername, etPassword, etRemotePath, etLocalPath;
    private Button btnUpload;

    private static final int EX_FILE_PICKER_RESULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etServer = (EditText) findViewById(R.id.etServer);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRemotePath = (EditText) findViewById(R.id.etRemotePath);
        etLocalPath = (EditText) findViewById(R.id.etLocalPath);
        btnUpload = (Button) findViewById(R.id.btnUpload);


        etLocalPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExFilePickerActivity.class);
                intent.putExtra(ExFilePicker.SET_CHOICE_TYPE, ExFilePicker.CHOICE_TYPE_FILES);
                startActivityForResult(intent, EX_FILE_PICKER_RESULT);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server = etServer.getText().toString();
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                remotePath = etRemotePath.getText().toString();
                if (remotePath.equals(""))
                    remotePath = "/";
                localPath = etLocalPath.getText().toString();

                ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Uploading...");

                if (server.equals("") || username.equals("") || password.equals("") || localPath.equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    file = new File(localPath);
                    pd.show();

                    if ("1".equals(new UploadFile(file, port, server, username, password, remotePath).execute())) {
                        Toast.makeText(MainActivity.this, "It is done", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "There was a problem", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EX_FILE_PICKER_RESULT) {
            if (data != null) {
                ExFilePickerParcelObject object = (ExFilePickerParcelObject) data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
                if (object.count > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < object.count; i++) {
                        buffer.append(object.names.get(i));
                        if (i < object.count - 1) buffer.append(", ");
                    }
                    etLocalPath.setText(object.path + buffer.toString());
                }
            }
        }
    }
}