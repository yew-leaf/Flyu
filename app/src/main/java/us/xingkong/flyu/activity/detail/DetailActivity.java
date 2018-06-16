package us.xingkong.flyu.activity.detail;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.SoftReference;

import butterknife.BindView;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.R;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.util.DateUtil;
import us.xingkong.flyu.util.GlideUtil;

public class DetailActivity extends BaseActivity<DetailContract.Presenter>
        implements DetailContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.image)
    AppCompatImageView image;
    @BindView(R.id.avatar)
    AppCompatImageView avatar;
    @BindView(R.id.name)
    AppCompatTextView name;
    @BindView(R.id.time)
    AppCompatTextView time;
    @BindView(R.id.content)
    AppCompatTextView content;

    private DownloadModel.Message message;
    private SoftReference<String> softReference;
    private DetailContract.Presenter mPresenter;

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

        String url = message.getImg().get(0);
        softReference = new SoftReference<>(DownloadModel.formatUrl(url));

        if (softReference.get() != null) {
            Glide.with(this)
                    .load(softReference.get())
                    .thumbnail(0.5f)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .listener(GlideUtil.listener(image))
                    .into(image);
        }

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
}
