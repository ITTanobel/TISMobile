package com.tanobel.it_yoga.tis_mobile.model;

import android.content.Context;
import android.graphics.Typeface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanobel.it_yoga.tis_mobile.R;

import java.util.ArrayList;

/**
 * Created by IT_Yoga on 14/11/2018.
 */

public class CRM_SalesOrder_PageAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();
    SparseArray<Fragment> registeredFragments = new SparseArray<>();
    String title = "";

    private int[] tabIcons = {
            R.drawable.ic_so_master,
            R.drawable.ic_so_detail,
    };

    public CRM_SalesOrder_PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment f) {
        fragments.add(f);
    }

    public View getCustomView(Context context, int position) {
        View mView = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView mTextView = mView.findViewById(R.id.txttab);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/fontawesome-webfont.ttf"));
        mTextView.setText(getPageTitle(position));
        mTextView.setCompoundDrawablesWithIntrinsicBounds(tabIcons[position], 0, 0, 0);

        return mView;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String fragname = String.valueOf(fragments.get(position).getClass().getSimpleName()).trim();

        if (fragname.indexOf("Master") != -1) {
            title = "Order Master";
        } else {
            title = "Order Detail";
        }

        return title;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
