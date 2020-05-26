package com.sssemion.calculator.nav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.sssemion.calculator.R;

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