package com.aelliott.samplegooglecalendarview;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Testing extends AppCompatActivity
{
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ActionBar actionBar;
    //@BindView(R.id.collapsingToolbarLayout)
    //CollapsingToolbarLayout collapsingToolbarLayout;
    //@BindView(R.id.button10)
    //Button button10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        // Initialize all variables annotated with @BindView and other variants
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        //collapsingToolbarLayout.lock();
    }

    /*@OnClick(R.id.button10)
    public void button10_onClick(View v)
    {
        appBarLayout.setExpanded(true, true);
        //appBarLayout.setExpanded(false, true);
        //collapsingToolbarLayout.setExpa
    }

    @OnClick(R.id.button11)
    public void button11_onClick(View v)
    {
        appBarLayout.setExpanded(false, true);
        //appBarLayout.setExpanded(false, true);
        //collapsingToolbarLayout.setExpa
    }*/
}
