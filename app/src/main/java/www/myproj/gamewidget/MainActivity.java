package www.myproj.gamewidget;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttontest;
        buttontest = (Button) findViewById(R.id.button);
        SharedPreferences userInfo = getSharedPreferences("locate", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        buttontest.setOnClickListener(new View.OnClickListener() {
        Context mcon=MainActivity.this;
            @Override
            public void onClick(View v) {
                EditText medit=findViewById(R.id.editTextTextPersonName);
                editor.putString("truelocate", String.valueOf(medit.getText()));
                editor.commit();
                System.out.println("");
                Toast.makeText(mcon, userInfo.getString("truelocate","1"), Toast.LENGTH_SHORT).show();

            }
        });
        Button intentbtn=findViewById(R.id.button3);
        intentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mintent=new Intent();
                mintent.setClass(MainActivity.this,MainActivity2.class);
                startActivity(mintent);
            }
        });


    }


}