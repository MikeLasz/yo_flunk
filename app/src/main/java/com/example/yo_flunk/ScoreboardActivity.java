package com.example.yo_flunk;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import com.example.yo_flunk.ui.main.SectionsPagerAdapter_tabelle;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreboardActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        SectionsPagerAdapter_tabelle sectionsPagerAdapter = new SectionsPagerAdapter_tabelle(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(1, false);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        String[] tabNames = {"Letzte Spiele", "Tabelle", "Letzte Turniere"};
        int[] imId = {R.drawable.letzte_spiele_trans_1, R.drawable.tabelle, R.drawable.turnier_trans};
        for(int i=0; i<tabs.getTabCount(); i++){
            // for each create a layout containing an image view and a description (txtview)
            LinearLayout layout = new LinearLayout(tabs.getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            // add image view:

            ImageView imView = new ImageView(tabs.getContext());
            imView.setImageResource(imId[i]);
            imView.setColorFilter(getResources().getColor(R.color.textcolor_notransparency), PorterDuff.Mode.SRC_ATOP);



            layout.addView(imView);
            // add text view:
            TextView txtView = new TextView(tabs.getContext());
            txtView.setText(tabNames[i]);
            txtView.setGravity(Gravity.CENTER_HORIZONTAL);
            txtView.setTextColor(ContextCompat.getColor(tabs.getContext(), R.color.textcolor));
            txtView.setTypeface(ResourcesCompat.getFont(tabs.getContext(), R.font.nunito_semibold));
            layout.addView(txtView);

            tabs.getTabAt(i).setCustomView(layout);
        }



    }
}