package us.xingkong.flyu.main;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.xingkong.flyu.PhotoModel;
import us.xingkong.flyu.R;
import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.adapter.TouchHelperCallback;
import us.xingkong.flyu.base.BaseActivity;
import us.xingkong.flyu.container.ContainerActivity;
import us.xingkong.flyu.util.GifSizeFilter;
import us.xingkong.flyu.util.MatisseEngine;
import us.xingkong.flyu.util.SnackbarUtil;
import us.xingkong.flyu.util.UiUtil;

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
    @BindView(R.id.content)
    AppCompatEditText content;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private final static int GENERAL_REQUEST = 0;
    private final static int CAMERA_REQUEST = 1;
    private final static int ALBUM_REQUEST = 2;
    private final static int Browse_REQUEST = 3;
    private float alpha = 1f;
    private View sheet;
    private View footer;
    private PopupWindow popupWindow;
    private Uri uri;
    private List<PhotoModel> photos;
    private List<String> permissionList;
    //private List<String> base64List;
    private PhotosAdapter mAdapter;
    private MenuItem ok;
    private TouchHelperCallback callback;
    private MainContract.Presenter mPresenter;
    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private MenuItem submit;

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
        permissionList = new ArrayList<>();
        //base64List = new ArrayList<>();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        toolbar.setTitle("发送");

        initPopupWindow();

        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new PhotosAdapter(MainActivity.this, new ArrayList<PhotoModel>());
        recyclerView.setAdapter(mAdapter);

        footer = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_footer, recyclerView, false);

        new MainPresenter(this);
        mPresenter = getPresenter();
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
        sheet = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_window, null);
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
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
                uri = FileProvider.getUriForFile(MainActivity.this, "us.xingkong.flyu", imageFile);
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

    @TargetApi(23)
    public void applyPermissions() {
        permissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, GENERAL_REQUEST);
        }
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
                        UiUtil.closeKeyboard(MainActivity.this);
                        List<File> files = new ArrayList<>();
                        for (PhotoModel bean : photos) {
                            String uri = bean.getUri();
                            File file = new File(bean.getPath(Uri.parse(uri), MainActivity.this));
                            files.add(file);
                        }
                        mPresenter.upload(files);
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
                    PhotoModel bean = new PhotoModel();
                    bean.setUri(uri.toString());
                    photos.add(bean);
                    mPresenter.display(photos);
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
                    mPresenter.display(photos);
                }
                break;
            case Browse_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<PhotoModel> list = (List<PhotoModel>) data.getSerializableExtra("photo");
                    photos.clear();
                    photos.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GENERAL_REQUEST:
                for (int i = 0; i < grantResults.length; i++) {
                    boolean reRequest = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (reRequest) {
                        Toast.makeText(MainActivity.this, "没有权限我会死的......", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(MainActivity.this, "那就算了吧......", Toast.LENGTH_SHORT).show();
                }
                break;
            case ALBUM_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    Toast.makeText(MainActivity.this, "那就算了吧......", Toast.LENGTH_SHORT).show();
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = alpha;
                            getWindow().setAttributes(lp);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = alpha;
                            getWindow().setAttributes(lp);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public String getUsername() {
        return ContainerActivity.getUserModel().getUsername();
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
                UiUtil.closeKeyboard(MainActivity.this);
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
    public void setEnable(boolean enable) {
        submit.setEnabled(enable);
        recyclerView.setEnabled(enable);
    }

    @Override
    public void showMessage(String message) {
        SnackbarUtil.shortSnackbar(findViewById(R.id.root), message);
    }

    @Override
    public void toOtherActivity(Intent intent) {
        String name = getString(R.string.translate);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(MainActivity.this, recyclerView, name);
        ActivityCompat.startActivityForResult(MainActivity.this, intent, Browse_REQUEST, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    @Override
    public void finishActivity() {
        finish();
        overridePendingTransition(0, R.anim.activity_exit);
    }
}
