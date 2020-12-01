package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    CRUD crud;
    EditText ET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ET = findViewById(R.id.ET);
    }

    public void add(View view)
    {
        crud = new CRUD(getApplicationContext());
        crud.open();
        long i = crud.add();
        Log.v("Tag","add"+i);
        crud.close();
    }

    public void delete(View view)
    {
        crud = new CRUD(getApplicationContext());
        crud.open();
        crud.delete(ET.getText().toString());
        crud.close();
    }

    public void update(View view)
    {
        crud = new CRUD(getApplicationContext());
        crud.open();
        crud.update(ET.getText().toString());
        crud.close();
    }

    public void check(View view) {
        crud = new CRUD(getApplicationContext());
        crud.open();
        crud.add();
        crud.check();
    }
}