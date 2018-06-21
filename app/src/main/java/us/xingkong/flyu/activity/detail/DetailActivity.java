package us.xingkong.flyu.activity.detail;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.adapter.DetailAdapter;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.util.DateUtil;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.avatar)
    AppCompatImageView avatar;
    @BindView(R.id.name)
    AppCompatTextView name;
    @BindView(R.id.time)
    AppCompatTextView time;
    @BindView(R.id.content)
    AppCompatTextView content;

    private DownloadModel.Message message;

    @Override
    protected BasePresenter newPresenter() {
        return null;
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbar.setTitle(" ");
        toolbar.setTitle("");
    }

    @Override
    protected void initData() {
        message = (DownloadModel.Message) getIntent().getSerializableExtra("Message");

        LinearLayoutManager manager =
                new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        DetailAdapter adapter = new DetailAdapter(DetailActivity.this, message.getImg());
        recyclerView.setAdapter(adapter);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.avatar)
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(RequestOptions.circleCropTransform())
                .into(avatar);

        name.setText(message.getName());
        time.setText(DateUtil.formatTime(message.getTime()));
        content.setText(message.getText());
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.activity_exit);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.activity_exit);
    }

    @Override
    public void showMessage(String message) {

    }
}
