package us.xingkong.flyu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class Browse extends AppCompatActivity {

    private Toolbar mToolbar;
    private List<PhotoBean> mList;
    private ViewPager mViewPager;
    private int currentPosition;
    private ViewPagerAdapter mAdapter;
    private TextView hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        initView();
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        PhotoBean bean = (PhotoBean) getIntent().getSerializableExtra("bean");
        mList = (List<PhotoBean>) getIntent().getSerializableExtra("photo");
        Log.e("list", mList.size() + "");
        currentPosition = bean.getPosition();
        mAdapter = new ViewPagerAdapter(Browse.this, mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(currentPosition);
        hint.setText((currentPosition + 1) + "/" + mList.size());
        mViewPager.addOnPageChangeListener(new android.support.v4.view.ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                hint.setText((currentPosition + 1) + "/" + mList.size());
            }
        });
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.viewPager);
        hint = findViewById(R.id.hint);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_menu, menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return true;
            }
        });
        return true;
    }
}
