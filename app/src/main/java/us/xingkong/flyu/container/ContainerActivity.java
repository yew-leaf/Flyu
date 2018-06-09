package us.xingkong.flyu.container;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.home.HomeFragment;
import us.xingkong.flyu.main.MainActivity;
import us.xingkong.flyu.mine.MineFragment;
import us.xingkong.flyu.profile.ProfileFragment;

public class ContainerActivity extends BaseActivity<ContainerContract.Presenter>
        implements ContainerContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.post)
    AppCompatImageView post;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;
    private MenuItem menuItem;
    private static UserModel userModel;

    @Override
    protected int bindLayout() {
        return R.layout.activity_container;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void initData() {
        String username = getIntent().getStringExtra("username");
        UserModelDao dao = App.getInstance().getDaoSession().getUserModelDao();
        userModel = dao.load(username);
    }

    public static UserModel getUserModel() {
        return userModel;
    }

    @Override
    protected void initListener() {
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContainerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        toolbar.setVisibility(View.VISIBLE);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.mine:
                        toolbar.setVisibility(View.VISIBLE);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.profile:
                        toolbar.setVisibility(View.GONE);
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2)
                    toolbar.setVisibility(View.GONE);
                else
                    toolbar.setVisibility(View.VISIBLE);
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setViewPager(viewPager);
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(HomeFragment.newInstance());
        adapter.add(MineFragment.newInstance());
        adapter.add(ProfileFragment.newInstance());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragmentList;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void add(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}
