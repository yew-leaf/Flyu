package us.xingkong.flyu.activity.container;

import android.content.Intent;
import android.provider.MediaStore;
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

import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.activity.dynamic.DynamicFragmentX;
import us.xingkong.flyu.activity.home.HomeFragmentX;
import us.xingkong.flyu.activity.main.MainActivity;
import us.xingkong.flyu.activity.profile.ProfileFragment;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.model.BmobUserModel;
import us.xingkong.flyu.model.EventModel;
import us.xingkong.flyu.util.L;
import us.xingkong.flyu.util.S;
import us.xingkong.flyu.util.UIUtil;

import static us.xingkong.flyu.app.Constants.ALBUM_REQUEST;
import static us.xingkong.flyu.app.Constants.CAMERA_REQUEST;

public class ContainerActivity extends BaseActivity {

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
    private static BmobUserModel bmobUserModel;

    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();
    }

    @Override
    protected BasePresenter newPresenter() {
        return null;
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_container;
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
        adapter.add(HomeFragmentX.newInstance());
        adapter.add(DynamicFragmentX.newInstance());
        adapter.add(ProfileFragment.newInstance());
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        //String username = getIntent().getStringExtra("Username");
        //UserModelDao dao = App.getInstance().getDaoSession().getUserModelDao();
        //userModel = dao.load(username);
        bmobUserModel = BmobUser.getCurrentUser(BmobUserModel.class);
    }

    public static UserModel getUserModel() {
        return userModel;
    }

    public static BmobUserModel getBmobUserModel() {
        return bmobUserModel;
    }

    @Override
    protected void initListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
                UIUtil.closeKeyboard(ContainerActivity.this);
                EventBus.getDefault().post(new EventModel<>("QueryUser", query));
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

    @OnClick(R.id.post)
    public void onPostClicked() {
        if (viewPager.getCurrentItem() != 1) {
            viewPager.setCurrentItem(1);
        }
        startActivity(new Intent(ContainerActivity.this, MainActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                S.shortSnackbar(findViewById(R.id.root), getString(R.string.double_click_to_exit));
                exitTime = System.currentTimeMillis();
            } else {
                App.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.i("Container", "requestCode:" + requestCode + "resultCode:" + requestCode);
        String uri;
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    uri = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
                    EventBus.getDefault().post(new EventModel<>("AvatarRequest", uri));
                }
                break;
            case ALBUM_REQUEST:
                if (resultCode == RESULT_OK) {
                    uri = String.valueOf(Matisse.obtainResult(data).get(0));
                    EventBus.getDefault().post(new EventModel<>("AvatarRequest", uri));
                }
                break;
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

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
