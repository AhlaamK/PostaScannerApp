package TabsPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.postaplus.postascannerapp.Pickup_accept_fragment;
import com.postaplus.postascannerapp.Pickup_transfer_accept_fragment;

public class TabsPagerAdapter_Pickup extends FragmentPagerAdapter {
 
    public TabsPagerAdapter_Pickup(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
    	 switch (index) {
         case 0:
             // Transfer WC activity
             return new Pickup_accept_fragment();
         case 1:
             // Transfer hold activity
             return new Pickup_transfer_accept_fragment();
                 }
  
         return null;
     }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
 
}
