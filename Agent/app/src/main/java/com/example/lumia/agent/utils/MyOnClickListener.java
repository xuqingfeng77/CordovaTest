package com.example.lumia.agent.utils;

import android.view.View;

/**
 * Created by Lumia on 2016-01-13.
 */
public class MyOnClickListener {

    private View view;
    private View.OnClickListener l;
    public MyOnClickListener(){}

    public void setOnClickListener(View view,View.OnClickListener l){
        view.setOnClickListener(l);
    }

    public MyOnClickListener(View view,View.OnClickListener l){
        view.setOnClickListener(l);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public View.OnClickListener getL() {
        return l;
    }

    public void setL(View.OnClickListener l) {
        this.l = l;
    }
}
