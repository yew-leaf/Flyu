package us.xingkong.flyu.profile;


import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BaseActivity;

public class ProfileActivity extends BaseActivity<ProfileContract.Presenter>
        implements ProfileContract.View {

    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.background)
    AppCompatImageView background;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.small_avatar)
    AppCompatImageView small_avatar;
    @BindView(R.id.toolbar_name)
    AppCompatTextView toolbar_username;
    @BindView(R.id.big_avatar)
    AppCompatImageView big_avatar;
    @BindView(R.id.username)
    AppCompatTextView username;

    private ProfileContract.Presenter mPresenter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(" ");
        toolbar.setTitle("");

        new ProfilePresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void initListener() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalOffset = appBarLayout.getTotalScrollRange();
                int currentOffset = Math.abs(verticalOffset);
                int restOffset = totalOffset - toolbar.getHeight() / 3;
                float alpha;
                if (currentOffset >= 0 && currentOffset <= totalOffset / 3) {
                    alpha = currentOffset / (totalOffset / 3f);
                    big_avatar.setAlpha(1f - alpha);
                    big_avatar.setScaleX(1f - alpha);
                    big_avatar.setScaleY(1f - alpha);
                }

                if (currentOffset >= restOffset - 8 && currentOffset <= totalOffset) {
                    alpha = (currentOffset - restOffset) / 8f;
                    small_avatar.setAlpha(alpha);
                    toolbar_username.setAlpha(alpha);
                }
            }
        });

    }

    @Override
    public void display() {
        Glide.with(this)
                .load(R.drawable.jager)
                .apply(RequestOptions.circleCropTransform())
                .into(small_avatar);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new BlurTransformation(25));
        Glide.with(this)
                .load(R.drawable.jager)
                .apply(options)
                .into(background);
        Glide.with(this)
                .load(R.drawable.jager)
                .apply(RequestOptions.circleCropTransform())
                .into(big_avatar);
        small_avatar.setAlpha(0f);
        toolbar_username.setAlpha(0f);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void toOtherActivity(UserModel user) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
