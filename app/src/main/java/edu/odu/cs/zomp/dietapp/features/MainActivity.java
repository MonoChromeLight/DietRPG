package edu.odu.cs.zomp.dietapp.features;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.features.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_main_viewpager) ViewPager pager;
    @BindView(R.id.view_main_nav) BottomNavigationView nav;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pager.setAdapter(new NavAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        pager.setCurrentItem(0);
                        return true;
                    case R.id.nav_profile:
                        pager.setCurrentItem(1);
                        return true;
                    case R.id.nav_equip:
                        pager.setCurrentItem(2);
                        return true;
                    case R.id.nav_store:
                        return true;
                    default:
                        return false;
                }
            }
        });
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
