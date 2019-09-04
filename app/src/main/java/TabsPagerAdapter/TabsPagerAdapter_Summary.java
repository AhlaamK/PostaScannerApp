package TabsPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.postaplus.postascannerapp.Summary_ack_fragment;
import com.postaplus.postascannerapp.Summary_pickup_fragment;
import com.postaplus.postascannerapp.Summary_return_fragment;
import com.postaplus.postascannerapp.Summary_wc_fragment;

/**
 * Created by ahlaam.kazi on 9/27/2016.
 */

public class TabsPagerAdapter_Summary extends FragmentPagerAdapter {

    public TabsPagerAdapter_Summary(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Summary WC activity
                return new Summary_wc_fragment();
            case 1:
                // Pickup  activity
                return new Summary_pickup_fragment();
            case 2:
                // Ack  activity
                return new Summary_ack_fragment();
            case 3:
            // Return  activity
            return new Summary_return_fragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
}
