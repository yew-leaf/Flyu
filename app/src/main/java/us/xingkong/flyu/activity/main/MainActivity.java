package us.xingkong.flyu.activity.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.R;
import us.xingkong.flyu.activity.container.ContainerActivity;
import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.adapter.TouchHelperCallback;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.model.PhotoModel;
import us.xingkong.flyu.util.FileUtil;
import us.xingkong.flyu.util.GifSizeFilter;
import us.xingkong.flyu.util.L;
import us.xingkong.flyu.util.MatisseEngine;
import us.xingkong.flyu.util.S;
import us.xingkong.flyu.util.UIUtil;

import static us.xingkong.flyu.app.Constants.ALBUM_REQUEST;
import static us.xingkong.flyu.app.Constants.CAMERA_REQUEST;

/**
 * @作者: Xuer
 * @描述:
 * @更新日志:
 * @目前的问题: (1) popupWindow的动画还未改成material design风格、外边距
 * (2) 拖拽排序、删除√
 * (3) 点击图片后预览√
 * (4) 上传(后端)
 * (5) grid里面item的边距
 * (6) test的adapter
 * (7) base64
 * (8) 清除缓存
 */

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.content)
    AppCompatEditText content;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private final static int Browse_REQUEST = 3;
    private float alpha = 1f;
    private MenuItem ok;
    private MenuItem submit;
    private View root;
    private View footer;
    private PopupWindow popupWindow;
    private Uri cameraUri;
    private List<PhotoModel> photos;
    //private List<String> base64List;
    private PhotosAdapter mAdapter;
    private TouchHelperCallback callback;

    @Override
    protected MainContract.Presenter newPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        photos = new ArrayList<>();
        //base64List = new ArrayList<>();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        toolbar.setTitle(R.string.send);

        initPopupWindow();

        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new PhotosAdapter(MainActivity.this, new ArrayList<PhotoModel>());
        recyclerView.setAdapter(mAdapter);

        footer = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.item_footer, recyclerView, false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }

    private void initPopupWindow() {
        root = LayoutInflater.from(MainActivity.this).inflate(R.layout.popupwindow, null);
        CardView takePhoto = root.findViewById(R.id.take_photo);
        CardView choosePhoto = root.findViewById(R.id.choose_photo);
        CardView cancel = root.findViewById(R.id.cancel);

        popupWindow = new PopupWindow(root);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
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
            File imageFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            if (Build.VERSION.SDK_INT >= 24) {
                cameraUri = FileProvider.getUriForFile(MainActivity.this, "us.xingkong.flyu", imageFile);
            } else {
                cameraUri = Uri.fromFile(imageFile);
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            startActivityForResult(intent, CAMERA_REQUEST);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void choosePhoto() {
        Matisse.from(MainActivity.this)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        ok = menu.findItem(R.id.ok);
        submit = menu.findItem(R.id.submit);
        ok.setVisible(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.submit:
                        //parseBitmapToBase64();
                        UIUtil.closeKeyboard(MainActivity.this);
                        List<File> files = new ArrayList<>();
                        for (PhotoModel model : photos) {
                            String uri = model.getUri();
                            File file = new File(FileUtil.convertPath(MainActivity.this, Uri.parse(uri)));
                            files.add(file);
                        }
                        mPresenter.uploadImageAndText(files);
                        break;
                    case R.id.ok:
                        //callback.setSelected();
                        ok.setVisible(false);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
        return true;
    }

    /*private void parseBitmapToBase64() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < photos.size(); i++) {
                    try {
                        Bitmap bitmap = Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(photos.get(i))
                                .thumbnail(0.8f)
                                .into(300, 300)
                                .get();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        baos.flush();
                        baos.close();
                        String base = Base64.encodeToString(bytes, Base64.NO_WRAP);
                        if (!base.equals("") && !base64List.contains(base)) {
                            base64List.add(base);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.e("uriList", photos.size() + "");
                for (int i = 0; i < base64List.size(); i++) {
                    Log.e("base" + i, base64List.get(i));
                    Log.i("size", base64List.size() + "");
                }
            }
        }).start();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    PhotoModel photoModel = new PhotoModel();
                    photoModel.setUri(cameraUri.toString());
                    photos.add(photoModel);
                    mPresenter.displayPhotos(photos);
                }
                break;
            case ALBUM_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<Uri> list = Matisse.obtainResult(data);
                    for (Uri uri : list) {
                        PhotoModel bean = new PhotoModel();
                        bean.setUri(uri.toString());
                        photos.add(bean);
                    }
                    mPresenter.displayPhotos(photos);
                }
                break;
            case Browse_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<PhotoModel> list = (List<PhotoModel>) data.getSerializableExtra("Photos");
                    photos.clear();
                    photos.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    S.shortSnackbar(findViewById(R.id.root), getString(R.string.forget_it));
                }
                break;
            case ALBUM_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    S.shortSnackbar(findViewById(R.id.root), getString(R.string.forget_it));
                }
                break;
        }
    }


    @Override
    public String getUsername() {
        //return ContainerActivity.getUserModel().getUsername();
        return ContainerActivity.getBmobUserModel().getUsername();
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
                UIUtil.closeKeyboard(MainActivity.this);
                UIUtil.setEnterAlpha(MainActivity.this);
                popupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        UIUtil.setExitAlpha(MainActivity.this);
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
    public void setVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void setEnable(boolean enable) {
        submit.setEnabled(enable);
        recyclerView.setEnabled(enable);
    }

    @Override
    public void showProgress(int progress) {
        progressBar.setProgress(progress);
        L.i("MainActivity", progress + "");
    }

    @Override
    public void toOtherActivity(Intent intent) {
        String name = getString(R.string.translate);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(MainActivity.this, recyclerView, name);
        ActivityCompat.startActivityForResult(MainActivity.this, intent, Browse_REQUEST, options.toBundle());
    }

    @Override
    public void showMessage(String message) {
        S.shortSnackbar(findViewById(R.id.root), message);
    }

    @Override
    public void finishActivity() {
        finish();
        overridePendingTransition(0, R.anim.activity_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    protected void onDestroy() {
        App.getInstance().getOkUtil().cancel(this);
        super.onDestroy();
    }
}
