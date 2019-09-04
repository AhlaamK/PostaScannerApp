package TabsPagerAdapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.postaplus.postascannerapp.Delivery_print_fragment;
import com.postaplus.postascannerapp.Delivery_ta_fragment;
import com.postaplus.postascannerapp.Delivery_wc_fragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // WC activity
            return new Delivery_wc_fragment();
        case 1:
            // Print activity
            return new Delivery_print_fragment();
        case 2:
            // TransferAccept activity
            return new Delivery_ta_fragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}
