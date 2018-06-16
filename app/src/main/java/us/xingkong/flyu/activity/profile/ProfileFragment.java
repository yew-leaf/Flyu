package us.xingkong.flyu.activity.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.login.LoginActivity;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseFragment;
import us.xingkong.flyu.model.EventModel;
import us.xingkong.flyu.util.DialogUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 15:21
 * @描述:
 * @更新日志:
 */
public class ProfileFragment extends BaseFragment<ProfileContract.Presenter>
        implements ProfileContract.View, View.OnClickListener {

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
    AppCompatTextView toolbar_name;
    @BindView(R.id.big_avatar)
    AppCompatImageView big_avatar;
    @BindView(R.id.username)
    AppCompatTextView username;
    @BindView(R.id.dynamic)
    AppCompatButton dynamic;
    @BindView(R.id.about)
    AppCompatButton about;
    @BindView(R.id.logout)
    AppCompatButton logout;

    private UserModel userModel;
    //private ProfileContract.Presenter mPresenter;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View root) {
        EventBus.getDefault().register(this);

        ((AppCompatActivity) mActivity).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        collapsingToolbar.setTitle(" ");
        toolbar.setTitle("");

        new ProfilePresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {
        userModel = ContainerActivity.getUserModel();
        toolbar_name.setText(userModel.getUsername());
        username.setText(userModel.getUsername());
    }

    @Subscribe
    public void getDynamic(EventModel<String> model) {
        if (!model.getPublisher().equals("DynamicFragment")) {
            return;
        }
        String s = getString(R.string.dynamic) + model.getSubscriber();
        dynamic.setText(s);
    }

    @Override
    protected void initListener() {
        //dynamic.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);

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
                } else {
                    if (big_avatar.getAlpha() != 0f) {
                        big_avatar.setAlpha(0f);
                    }
                    if (big_avatar.getScaleX() != 0f) {
                        big_avatar.setScaleY(0f);
                    }
                    if (big_avatar.getScaleY() != 0f) {
                        big_avatar.setScaleY(0f);
                    }
                }

                if (currentOffset >= restOffset - 8 && currentOffset <= totalOffset) {
                    alpha = (currentOffset - restOffset) / 8f;

                    small_avatar.setAlpha(alpha);
                    toolbar_name.setAlpha(alpha);
                } else {
                    if (small_avatar.getAlpha() != 0f) {
                        small_avatar.setAlpha(0f);
                    }
                    if (toolbar_name.getAlpha() != 0f) {
                        toolbar_name.setAlpha(0f);
                    }
                }
            }
        });
    }

    @Override
    public void display() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new BlurTransformation(25));

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.avatar)
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(options)
                .into(background);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.avatar)
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(RequestOptions.circleCropTransform())
                .into(small_avatar);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.avatar)
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(RequestOptions.circleCropTransform())
                .into(big_avatar);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void toOtherActivity(UserModel user) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about:
                DialogUtil.aboutDialog(mActivity).show();
                break;
            case R.id.logout:
                new AlertDialog.Builder(mActivity)
                        .setTitle("")
                        .setMessage("确定退出吗？")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserModelDao dao = App.getInstance().getDaoSession().getUserModelDao();
                                UserModel user = dao.load(userModel.getUsername());
                                user.setIsLogged(false);
                                dao.update(user);
                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                startActivity(intent);
                                mActivity.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }
}