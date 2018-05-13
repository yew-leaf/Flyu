package us.xingkong.flyu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
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

        PhotoBean bean = (PhotoBean) getIntent().getSerializableExtra("bean");
        mList = (List<PhotoBean>) getIntent().getSerializableExtra("photo");
        currentPosition = bean.getPosition();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

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

        mAdapter.setOnPhotoViewClickListener(new ViewPagerAdapter.OnPhotoViewClickListener() {
            @Override
            public void onClick() {
                goBack();
            }
        });
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.viewPager);
        hint = findViewById(R.id.hint);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void goBack() {
        Intent intent = new Intent(Browse.this, MainActivity.class);
        intent.putExtra("photo", (Serializable) mList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_menu, menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
