package us.xingkong.flyu.browse;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.PhotoBean;
import us.xingkong.flyu.R;
import us.xingkong.flyu.view.ViewPager;
import us.xingkong.flyu.adapter.ViewPagerAdapter;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.main.MainActivity;

public class Browse extends BaseActivity<BrowseContract.Presenter>
        implements BrowseContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.hint)
    AppCompatTextView hint;

    private int currentPosition;
    private List<PhotoBean> mList;
    private ViewPagerAdapter mAdapter;
    private BrowseContract.Presenter mPresenter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_browse;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        new BrowsePresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {
        PhotoBean bean = (PhotoBean) getIntent().getSerializableExtra("bean");
        mList = (List<PhotoBean>) getIntent().getSerializableExtra("photo");
        currentPosition = bean.getPosition();

        mAdapter = new ViewPagerAdapter(Browse.this, mList);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(currentPosition);
        hint.setText((currentPosition + 1) + "/" + mList.size());
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        viewPager.addOnPageChangeListener(new android.support.v4.view.ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                hint.setText((currentPosition + 1) + "/" + mList.size());
            }
        });

        mAdapter.setOnPhotoViewClickListener(new ViewPagerAdapter.OnPhotoViewClickListener() {
            @Override
            public void onClick() {
                goBack();
            }
        });
    }

    @Override
    public void goBack() {
        Intent intent = new Intent(Browse.this, MainActivity.class);
        intent.putExtra("photo", (Serializable) mList);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, R.anim.activity_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_menu, menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_browse:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Browse.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("要删除这张照片吗？");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("currentPosition", currentPosition + "");
                                mList.remove(currentPosition);
                                mAdapter.notifyDataSetChanged();
                                hint.setText((currentPosition + 1) + "/" + mList.size());
                                if (mList.size() == 0) {
                                    hint.setText("0/0");
                                }
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return true;
    }
}
