package com.samir.cfos.helpers;

import androidx.appcompat.app.AppCompatActivity;

public abstract class AppActivity extends AppCompatActivity {
    abstract protected void initializeView();
    abstract protected void initializeListeners();
}
