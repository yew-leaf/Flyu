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

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import us.xingkong.flyu.R;
import us.xingkong.flyu.adapter.DetailAdapter;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.model.BmobUserModel;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.util.DateUtil;
import us.xingkong.flyu.util.L;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.page)
    AppCompatTextView page;
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

        name.setText(message.getName());
        time.setText(DateUtil.formatTime(message.getTime()));
        content.setText(message.getText());

        BmobQuery<BmobUserModel> query = new BmobQuery<>();
        query.addWhereEqualTo("username", message.getName());
        query.findObjects(new FindListener<BmobUserModel>() {
            @Override
            public void done(List<BmobUserModel> list, BmobException e) {
                if (e == null) {
                    if (!list.isEmpty() && list.get(0).getAvatar() != null) {
                        Glide.with(DetailActivity.this)
                                .load(list.get(0).getAvatar().getUrl())
                                .thumbnail(0.5f)
                                .transition(new DrawableTransitionOptions().crossFade())
                                .apply(RequestOptions.circleCropTransform())
                                .into(avatar);
                    } else {
                        Glide.with(DetailActivity.this)
                                .asDrawable()
                                .load(R.drawable.default_avatar)
                                .apply(RequestOptions.circleCropTransform())
                                .into(avatar);
                    }
                } else {
                    L.e("DetailActivity", e.getMessage());
                    L.e("ErrorCode", e.getErrorCode() + "");
                    showMessage("加载头像失败：" + e.getMessage());
                }
            }
        });

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = l.findFirstVisibleItemPosition() + 1;
                String s = position+"/"+l.getItemCount();
                L.i("onScrolled",s);
                page.setText(s);
            }
        });*/
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
    public void showMessage(String message) {

    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.activity_exit);
    }
}
