package TabsPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.postaplus.postascannerapp.Transfer_hold_fragment;
import com.postaplus.postascannerapp.Transfer_pickup_fragment;
import com.postaplus.postascannerapp.Transfer_wc_fragment;

public class TabsPagerAdapter_Transfer extends FragmentPagerAdapter {
 
    public TabsPagerAdapter_Transfer(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Transfer WC activity
            return new Transfer_wc_fragment();
        case 1:
            // Transfer hold activity
            return new Transfer_hold_fragment();
        case 2:
            // Transfer pickup activity
            return new Transfer_pickup_fragment();
                }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}
