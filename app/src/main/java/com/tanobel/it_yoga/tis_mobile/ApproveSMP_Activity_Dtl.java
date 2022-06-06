package com.tanobel.it_yoga.tis_mobile;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tanobel.it_yoga.tis_mobile.model.ApproveSMP_PageAdapter;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;


public class ApproveSMP_Activity_Dtl extends AppCompatActivity {

    ViewPager vp;
    ApproveSMP_PageAdapter pagerAdapter;
    TabLayout tabLayout;
    String title, subtitle, type, menutype, chanelcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvedtl_smp);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_smp_dtl);
        setSupportActionBar(toolbar);

        TextView textTitle = findViewById(R.id.txt_smp_dtl_title);
        TextView textSubTitle = findViewById(R.id.txt_smp_dtl_subtitle);
        TextView textMenuType = findViewById(R.id.txt_smp_dtl_tipe);
        TextView textChannelcode = findViewById(R.id.txt_smp_dtl_chanelcode);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            title = (String) bd.get("Code");
            subtitle = (String) bd.get("Name");
            type = (String) bd.get("Type");
            menutype = (String) bd.get("MenuType");
            chanelcode = (String) bd.get("Chanelcode");
        }

        if (getSupportActionBar() != null) {
            textTitle.setText(title);
            textSubTitle.setText(subtitle);
            textMenuType.setText(menutype);
            textChannelcode.setText(chanelcode);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //VIEWPAGER
        vp = findViewById(R.id.pager_smp_dtl);
        this.addPages();

        //TABLAYOUT
        tabLayout = findViewById(R.id.tab_layout_smp_dtl);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        setupTabIcons();

    }

    private void setupTabIcons() {
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            View customView = pagerAdapter.getCustomView(this, i);
            tabLayout.getTabAt(i).setCustomView(customView);
        }
    }

    private void addPages() {
        pagerAdapter = new ApproveSMP_PageAdapter(this.getSupportFragmentManager());

        if (type.indexOf("H") != -1) {
            pagerAdapter.addFragment(new ApproveSMP_FragmentHrg(), "Dtl");
        }

        if (type.indexOf("D") != -1) {
            pagerAdapter.addFragment(new ApproveSMP_FragmentDisc(), "Dtl");
        }

        if (type.indexOf("A") != -1) {
            pagerAdapter.addFragment(new ApproveSMP_FragmentAddCost(), "Dtl");
        }

        if (type.indexOf("B") != -1) {
            pagerAdapter.addFragment(new ApproveSMP_FragmentBnsPrd(), "Dtl");
        }

        if (type.indexOf("T") != -1) {
            pagerAdapter.addFragment(new ApproveSMP_FragmentBnsTmbhn(), "Dtl");
        }

        //SET ADAPTER TO VP
        vp.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainModule) this.getApplication()).setPurpose("");
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
