package us.xingkong.flyu.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.PhotoBean;
import us.xingkong.flyu.R;
import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.adapter.TouchHelperCallback;
import us.xingkong.flyu.base.BaseFragment;
import us.xingkong.flyu.util.GifSizeFilter;
import us.xingkong.flyu.util.MatisseEngine;
import us.xingkong.flyu.util.UiUtil;

import static android.app.Activity.RESULT_OK;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/5 23:36
 * @描述: 看样子是失败了，选择照片跳回来后会莫名其妙报错
 * @更新日志:
 */
public class MainFragment extends BaseFragment<MainContract.Presenter>
        implements MainContract.View {

    @BindView(R.id.content)
    AppCompatEditText content;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private final static int CAMERA_REQUEST = 1;
    private final static int ALBUM_REQUEST = 2;
    private final static int Browse_REQUEST = 3;
    private float alpha = 1f;
    private View sheet;
    private View footer;
    private PopupWindow popupWindow;
    private Uri uri;
    private List<PhotoBean> photos;
    private PhotosAdapter mAdapter;
    private MenuItem ok;
    private TouchHelperCallback callback;
    private MainContract.Presenter mPresenter;

    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View root) {
        initPopupWindow();

        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new PhotosAdapter(mContext, new ArrayList<PhotoBean>());
        recyclerView.setAdapter(mAdapter);

        footer = LayoutInflater.from(mContext).inflate(R.layout.item_footer, recyclerView, false);

        new MainPresenter(this);
        mPresenter = getPresenter();
    }

    @Override
    protected void initData() {
        photos = new ArrayList<>();
    }

    @Override
    protected void initListener() {

    }

    private void initPopupWindow() {
        sheet = LayoutInflater.from(mContext).inflate(R.layout.activity_pop_window, null);
        CardView takePhoto = sheet.findViewById(R.id.take_photo);
        CardView choosePhoto = sheet.findViewById(R.id.choose_photo);
        CardView cancel = sheet.findViewById(R.id.cancel);
        popupWindow = new PopupWindow(sheet);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.SheetAnimation);
        popupWindow.setOutsideTouchable(true);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mActivity,
                                new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                    } else {
                        takePhoto();
                    }
                } else {
                    takePhoto();
                }
                popupWindow.dismiss();
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mActivity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALBUM_REQUEST);
                    } else {
                        choosePhoto();
                    }
                } else {
                    choosePhoto();
                }
                popupWindow.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void takePhoto() {
        try {
            File imageFile = new File(mContext.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(mActivity, "us.xingkong.flyu", imageFile);
            } else {
                uri = Uri.fromFile(imageFile);
            }
            Log.i("uri", String.valueOf(uri));
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void choosePhoto() {
        Matisse.from(mActivity)
                .choose(MimeType.allOf())
                .countable(true)
                .capture(false)
                .captureStrategy(new CaptureStrategy(true, "us.xingkong.flyu"))
                .maxSelectable(3 - photos.size())
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MatisseEngine())
                .forResult(ALBUM_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    PhotoBean bean = new PhotoBean();
                    bean.setUri(uri.toString());
                    photos.add(bean);
                    mPresenter.display(photos);
                }
                break;
            case ALBUM_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<Uri> list = Matisse.obtainResult(data);
                    for (Uri uri : list) {
                        PhotoBean bean = new PhotoBean();
                        bean.setUri(uri.toString());
                        photos.add(bean);
                    }
                    mPresenter.display(photos);
                }
                break;
            case Browse_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<PhotoBean> list = (List<PhotoBean>) data.getSerializableExtra("photo");
                    photos.clear();
                    photos.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    private void setEnterAlpha() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        Thread.sleep(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    alpha -= 0.01f;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                            lp.alpha = alpha;
                            mActivity.getWindow().setAttributes(lp);
                        }
                    });
                }
            }
        }).start();
    }

    private void setExitAlpha() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha < 1f) {
                    try {
                        Thread.sleep(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    alpha += 0.01f;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                            lp.alpha = alpha;
                            mActivity.getWindow().setAttributes(lp);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public String getContent() {
        return content.getText().toString();
    }

    @Override
    public void setHeaderView() {

    }

    @Override
    public void setFooterView() {
        mAdapter.setOnAddClickListener(new PhotosAdapter.onAddClickListener() {
            @Override
            public void onAddClick() {
                UiUtil.closeKeyboard(mActivity);
                setEnterAlpha();
                popupWindow.showAtLocation(sheet, Gravity.BOTTOM, 0, 0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        setExitAlpha();
                    }
                });
            }
        });
        mAdapter.setFooterView(footer);
    }

    @Override
    public void setAdapter(PhotosAdapter adapter) {
        mAdapter = adapter;
        recyclerView.setAdapter(mAdapter);
        callback = new TouchHelperCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        callback.setOnSelectedListener(new TouchHelperCallback.onSelectedListener() {
            @Override
            public void onSelected() {
                ok.setVisible(true);
            }
        });
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(footer, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void toOtherActivity(Intent intent) {
        String name = getString(R.string.translate);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(mActivity, recyclerView, name);
        ActivityCompat.startActivityForResult(mActivity, intent, Browse_REQUEST, options.toBundle());
    }
}
