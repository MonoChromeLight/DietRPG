package edu.odu.cs.zomp.dietapp.features;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import edu.odu.cs.zomp.dietapp.R;

public class MainActivity extends AppCompatActivity {



    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public class NavAdapter extends FragmentStatePagerAdapter {

        public NavAdapter(FragmentManager fm) { super(fm); }

        @Override public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return ProfileFragment.newInstance();
                case 2:
                    return EquipFragment.newInstance();
                case 3:
                    return StoreFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override public int getCount() {
            return 4;
        }
    }
}
