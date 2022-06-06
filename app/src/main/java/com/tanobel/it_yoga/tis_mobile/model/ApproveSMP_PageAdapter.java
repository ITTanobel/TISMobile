package com.tanobel.it_yoga.tis_mobile.model;

import android.content.Context;
import android.graphics.Typeface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tanobel.it_yoga.tis_mobile.R;

import java.util.ArrayList;

public class ApproveSMP_PageAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();
    String title = "";
    String Id = "";
    int fragpos;

    private int[] tabIcons = {
            R.drawable.ic_customer,
            R.drawable.ic_sales_area,
            R.drawable.ic_price,
            R.drawable.ic_discount,
            R.drawable.ic_addcost,
            R.drawable.ic_bonus,
            R.drawable.ic_bonus_extra,
    };

    public ApproveSMP_PageAdapter(FragmentManager fm) {
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

    public void addFragment(Fragment f, String id) {
        fragments.add(f);
        Id = id;
    }

    public View getCustomView(Context context, int position) {
        String fragname = String.valueOf(fragments.get(position).getClass().getSimpleName()).trim() ;

        View mView = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView mTextView = mView.findViewById(R.id.txttab);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/fontawesome-webfont.ttf"));
        mTextView.setText(getPageTitle(position));

        if (Id == "Mst") {
            if (fragname.indexOf("Cust")!=-1) {
                fragpos = 0;
            } else {
                fragpos = 1;
            }
        } else if (Id == "Dtl") {
            if (fragname.indexOf("Hrg")!=-1) {
                fragpos = 2;
            } else if (fragname.indexOf("Disc")!=-1) {
                fragpos = 3;
            } else if (fragname.indexOf("AddCost")!=-1) {
                fragpos = 4;
            } else if (fragname.indexOf("BnsPrd")!=-1) {
                fragpos = 5;
            } else if (fragname.indexOf("BnsTmbhn")!=-1) {
                fragpos = 6;
            }
        }

        mTextView.setCompoundDrawablesWithIntrinsicBounds(tabIcons[fragpos], 0, 0, 0);

        return mView;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String fragname = String.valueOf(fragments.get(position).getClass().getSimpleName()).trim() ;

        if (Id == "Mst") {
            if (fragname.indexOf("Cust")!=-1) {
                title = "Customer Khusus";
            } else {
                title = "Sales Organization";
            }
        } else if (Id == "Dtl") {
            if (fragname.indexOf("Hrg")!=-1) {
                title = "Harga";
            } else if (fragname.indexOf("Disc")!=-1) {
                title = "Diskon";
            } else if (fragname.indexOf("AddCost")!=-1) {
                title = "Add Cost";
            } else if (fragname.indexOf("BnsPrd")!=-1) {
                title = "Bonus Produk";
            } else if (fragname.indexOf("BnsTmbhn")!=-1) {
                title = "Bonus Tambahan";
            }
        }

        return title;
    }

}
