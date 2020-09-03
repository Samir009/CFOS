package com.samir.cfos.helpers;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class AppFragment extends Fragment {
    abstract protected void initializeView(View view);
    abstract protected void initializeListeners();
}
