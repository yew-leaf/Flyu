package us.xingkong.flyu.activity.browse;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.activity.main.MainActivity;
import us.xingkong.flyu.adapter.BrowseAdapter;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.model.PhotoModel;
import us.xingkong.flyu.view.ViewPager;

public class BrowseActivity extends BaseActivity<BrowseContract.Presenter>
        implements BrowseContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.hint)
    AppCompatTextView hint;

    private int currentPosition;
    private List<PhotoModel> mList;
    private BrowseAdapter mAdapter;
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
        PhotoModel bean = (PhotoModel) getIntent().getSerializableExtra("PhotoModel");
        mList = (List<PhotoModel>) getIntent().getSerializableExtra("Photos");
        currentPosition = bean.getPosition();

        mAdapter = new BrowseAdapter(BrowseActivity.this, mList);
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

        mAdapter.setOnPhotoViewClickListener(new BrowseAdapter.OnPhotoViewClickListener() {
            @Override
            public void onClick() {
                goBack();
            }
        });
    }

    @Override
    public void goBack() {
        Intent intent = new Intent(BrowseActivity.this, MainActivity.class);
        intent.putExtra("Photos", (Serializable) mList);
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
                        new AlertDialog.Builder(BrowseActivity.this)
                                .setTitle("提示")
                                .setMessage("要删除这张照片吗？")
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mList.remove(currentPosition);
                                        mAdapter.notifyDataSetChanged();
                                        hint.setText((currentPosition + 1) + "/" + mList.size());
                                        if (mList.size() == 0) {
                                            hint.setText("0/0");
                                        }
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        break;
                }
                return true;
            }
        });
        return true;
    }
}
