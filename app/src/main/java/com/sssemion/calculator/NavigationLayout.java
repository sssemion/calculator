package com.sssemion.calculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class NavigationLayout extends RelativeLayout
{
    public NavigationLayout(Context context, RelativeLayout parent)
    {
        super(context);
        initView(context, parent);
    }

    public void initView(final Context context, RelativeLayout parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.drawer, parent,true);
    }
}