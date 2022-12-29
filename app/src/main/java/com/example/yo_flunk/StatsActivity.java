package com.example.yo_flunk;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yo_flunk.ui.main.SectionsPagerAdapter_stats;

public class StatsActivity extends AppCompatActivity {

    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        SectionsPagerAdapter_stats sectionsPagerAdapter = new SectionsPagerAdapter_stats(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Set Drawable as a tab option. Actually, I am not sure whether we should
        //tabs.getTabAt(0).setIcon(R.drawable.spiele);
        //tabs.getTabAt(1).setIcon(R.drawable.treffer);
        //tabs.getTabAt(2).setIcon(R.drawable.saison);

        String[] tabNames = {"Spiele", "Treffer", "Saison"};
        int[] imId = {R.drawable.spiele, R.drawable.treffer, R.drawable.saison};
        for(int i=0; i<tabs.getTabCount(); i++){
            // for each tab, create a layout containing an image view and a description (txtview)
            LinearLayout layout = new LinearLayout(tabs.getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams layout_params = (LinearLayout.LayoutParams) layout.getLayoutParams();
            layout_params.setMargins(0, 10, 0, 10);
            layout.setLayoutParams(layout_params);
            layout.setOrientation(LinearLayout.VERTICAL);

            // add image view:
            ImageView imView = new ImageView(tabs.getContext());
            imView.setImageResource(imId[i]);
            imView.setColorFilter(getResources().getColor(R.color.textcolor_notransparency), PorterDuff.Mode.SRC_ATOP);


            LinearLayout.LayoutParams params_im = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            params_im.weight = 8;
            layout.addView(imView, params_im);
            // add text view:
            TextView txtView = new TextView(tabs.getContext());
            txtView.setText(tabNames[i]);
            txtView.setGravity(Gravity.CENTER_HORIZONTAL);
            txtView.setTextColor(ContextCompat.getColor(tabs.getContext(), R.color.textcolor));
            txtView.setTypeface(ResourcesCompat.getFont(tabs.getContext(), R.font.nunito_semibold));
            LinearLayout.LayoutParams params_txt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            params_txt.weight = 3;
            layout.addView(txtView, params_txt);

            tabs.getTabAt(i).setCustomView(layout);
        }

    }

}