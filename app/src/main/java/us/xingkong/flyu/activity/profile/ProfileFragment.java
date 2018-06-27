package us.xingkong.flyu.activity.profile;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import jp.wasabeef.glide.transformations.BlurTransformation;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.activity.login.LoginActivity;
import us.xingkong.flyu.base.BaseFragment;
import us.xingkong.flyu.model.BmobUserModel;
import us.xingkong.flyu.model.EventModel;
import us.xingkong.flyu.util.DialogUtil;
import us.xingkong.flyu.util.FileUtil;
import us.xingkong.flyu.util.S;
import us.xingkong.flyu.util.T;
import us.xingkong.flyu.util.UIUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 15:21
 * @描述:
 * @更新日志:
 */
public class ProfileFragment extends BaseFragment<ProfileContract.Presenter>
        implements ProfileContract.View {

    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.background_avatar)
    AppCompatImageView background_avatar;
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
    @BindView(R.id.signature)
    AppCompatTextView signature;
    @BindView(R.id.dynamic)
    AppCompatButton dynamic;
    @BindView(R.id.about)
    AppCompatButton about;
    @BindView(R.id.night)
    AppCompatButton night;
    @BindView(R.id.logout)
    AppCompatButton logout;

    private UserModel userModel;
    private BmobUserModel bmobUserModel;
    private View root;
    private PopupWindow popupWindow;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    protected ProfileContract.Presenter newPresenter() {
        return new ProfilePresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_profile;
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

        this.root = LayoutInflater.from(mActivity).inflate(R.layout.popupwindow, null);
        popupWindow = UIUtil.photoPopupWindow(mActivity, this.root);

        collapsingToolbar.setTitle(" ");
        toolbar.setTitle("");
        dynamic.setText("动态：0");
    }

    @Override
    protected void initData() {
        //userModel = ContainerActivity.getUserModel();
        //toolbar_name.setText(userModel.getUsername());
        //username.setText(userModel.getUsername());
        bmobUserModel = ContainerActivity.getBmobUserModel();
        toolbar_name.setText(bmobUserModel.getUsername());
        username.setText(bmobUserModel.getUsername());
        signature.setText(bmobUserModel.getSignature());

        displayProfile(bmobUserModel);
    }

    @Subscribe
    public void getDynamic(EventModel<String> eventModel) {
        if (!eventModel.getPublisher().equals("DynamicFragment")) {
            return;
        }
        String s = getString(R.string.dynamic) + eventModel.getSubscriber();
        dynamic.setText(s);
    }

    @Subscribe
    public void getAvatarUri(EventModel<String> eventModel) {
        if (!eventModel.getPublisher().equals("AvatarRequest")) {
            return;
        }
        showMessage("上传中，请稍等");
        setEnable(false);
        String uri = eventModel.getSubscriber();
        File file = new File(FileUtil.convertPath(mActivity, Uri.parse(uri)));
        BmobFile bmobFile = new BmobFile(file);
        bmobUserModel.setAvatar(bmobFile);
        mPresenter.updateAvatar(bmobUserModel);
    }

    @Override
    protected void initListener() {
        //我希望能获取到attr里toolbar的高度，但是好像不行
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

    @OnClick({R.id.small_avatar, R.id.background_avatar, R.id.big_avatar
            , R.id.signature, R.id.about, R.id.night, R.id.logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.small_avatar:
            case R.id.background_avatar:
            case R.id.big_avatar:
                UIUtil.setEnterAlpha(mActivity);
                popupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        UIUtil.setExitAlpha(mActivity);
                    }
                });
                break;
            case R.id.signature:
                mPresenter.updateSignature(bmobUserModel, signature.getText().toString());
                break;
            case R.id.about:
                DialogUtil.aboutDialog(mActivity).show();
                break;
            case R.id.night:
                //RippleAnimation.create(v).setDuration(200).start();
                //mActivity.findViewById(R.id.root).setBackgroundColor(getResources().getColor(R.color.browse));
                //mActivity.findViewById(R.id.viewPager).setBackgroundColor(getResources().getColor(R.color.black));
                //night.setBackgroundColor(getResources().getColor(R.color.browse));
                break;
            case R.id.logout:
                mPresenter.logout(userModel);
                break;
        }
    }

    @Override
    public void displayProfile(BmobUserModel bmobUserModel) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_error)
                .transform(new BlurTransformation(25));

        if (bmobUserModel.getAvatar() != null) {
            String uri = bmobUserModel.getAvatar().getUrl();
            Glide.with(this)
                    .load(uri)
                    .thumbnail(0.5f)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(small_avatar);

            Glide.with(this)
                    .load(uri)
                    .thumbnail(0.5f)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(options)
                    .into(background_avatar);

            Glide.with(this)
                    .load(uri)
                    .thumbnail(0.5f)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(big_avatar);
        } else {
            //加载默认头像
            Glide.with(this)
                    .asDrawable()
                    .load(R.drawable.default_avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(small_avatar);

            Glide.with(this)
                    .asDrawable()
                    .load(R.drawable.default_avatar)
                    .apply(options)
                    .into(background_avatar);

            Glide.with(this)
                    .asDrawable()
                    .load(R.drawable.default_avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(big_avatar);
        }
    }

    @Override
    public void showSignature(String sign) {
        signature.setText(sign);
    }

    @Override
    public void setEnable(boolean enable) {
        small_avatar.setEnabled(enable);
        background_avatar.setEnabled(enable);
        big_avatar.setEnabled(enable);
    }

    @Override
    public void toOtherActivity(UserModel userModel) {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void toOtherActivity(BmobUserModel bmobUserModel) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showToast(String message) {
        T.shortToast(mActivity, message);
    }

    @Override
    public void showMessage(String message) {
        S.shortSnackbar(mActivity.findViewById(R.id.root), message);
    }
}
