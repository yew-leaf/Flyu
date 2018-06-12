package us.xingkong.flyu.container;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import us.xingkong.flyu.EventModel;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.dynamic.DynamicFragment;
import us.xingkong.flyu.home.HomeFragment;
import us.xingkong.flyu.main.MainActivity;
import us.xingkong.flyu.profile.ProfileFragment;
import us.xingkong.flyu.util.SnackbarUtil;

public class ContainerActivity extends BaseActivity<ContainerContract.Presenter>
        implements ContainerContract.View, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.post)
    AppCompatImageView post;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    private long exitTime;
    private MenuItem menuItem;
    private static UserModel userModel;

    @Override
    protected int bindLayout() {
        return R.layout.activity_container;
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();
    }

    @Override
    protected void init() {

    }

    @BindColor(R.color.black)
    int textColor;
    @BindColor(R.color.text_color)
    int hintColor;

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        viewPager.setOffscreenPageLimit(3);
        setViewPager(viewPager);

        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        SearchView.SearchAutoComplete complete = searchView.findViewById(R.id.search_src_text);
        complete.setHintTextColor(hintColor);
        complete.setTextColor(textColor);
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(HomeFragment.newInstance());
        adapter.add(DynamicFragment.newInstance());
        adapter.add(ProfileFragment.newInstance());
        viewPager.setAdapter(adapter);
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
        post.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
                EventBus.getDefault().post(new EventModel<>("Home", query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    toolbar.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                }
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

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        toolbar.setVisibility(View.VISIBLE);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.dynamic:
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post:
                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                }
                Intent intent = new Intent(ContainerActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
               SnackbarUtil.shortSnackbar(findViewById(R.id.root),"再按一次退出").show();
                exitTime = System.currentTimeMillis();
            } else {
                App.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

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
