package edu.odu.cs.zomp.dietapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.ui.character.CharacterFragment;
import edu.odu.cs.zomp.dietapp.ui.home.HomeFragment;
import edu.odu.cs.zomp.dietapp.ui.profile.ProfileFragment;
import edu.odu.cs.zomp.dietapp.ui.quests.QuestsFragment;
import edu.odu.cs.zomp.dietapp.ui.store.StoreFragment;

import static android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends BaseActivity implements OnNavigationItemSelectedListener {

    @BindView(R.id.view_main_viewpager) ViewPager pager;
    @BindView(R.id.view_main_nav) BottomNavigationView nav;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pager.setAdapter(new NavAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                nav.getMenu().getItem(position).setChecked(true);
            }

            @Override public void onPageSelected(int position) {}
            @Override public void onPageScrollStateChanged(int state) {}
        });

        nav.setOnNavigationItemSelectedListener(this);
        nav.getMenu().getItem(0).setChecked(true);
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                pager.setCurrentItem(0);
                return true;
            case R.id.nav_profile:
                pager.setCurrentItem(1);
                return true;
            case R.id.nav_quests:
                pager.setCurrentItem(2);
                return true;
            case R.id.nav_equip:
                pager.setCurrentItem(3);
                return true;
            case R.id.nav_store:
                pager.setCurrentItem(4);
                return true;
            default:
                return false;
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
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
                    return QuestsFragment.newInstance();
                case 3:
                    return CharacterFragment.newInstance();
                case 4:
                    return StoreFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override public int getCount() {
            return 5;
        }
    }
}
