package us.xingkong.flyu.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import us.xingkong.flyu.browse.Browse;
import us.xingkong.flyu.PhotoBean;
import us.xingkong.flyu.R;
import us.xingkong.flyu.adapter.PhotosAdapterTest;
import us.xingkong.flyu.adapter.TouchHelperCallbackTest;
import us.xingkong.flyu.util.GifSizeFilter;
import us.xingkong.flyu.util.MatisseEngine;

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

public class MainActivityTest extends AppCompatActivity {

    private Uri imageUri;
    private List<PhotoBean> mPhotoList = new ArrayList<>();
    private final static int GENERAL_REQUEST = 0;
    private final static int CAMERA_REQUEST = 1;
    private final static int ALBUM_REQUEST = 2;
    private final static int Browse_REQUEST = 3;
    private ArrayList<String> mPermissionList = new ArrayList<>();
    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private Toolbar mToolbar;
    private EditText words;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridManager;
    private PhotosAdapterTest mAdapter;
    private PopupWindow popupWindow;
    private View sheet;
    private CardView choosePhoto, takePhoto, cancel;
    private ArrayList<String> mBaseList = new ArrayList<>();
    private ImageView test;
    private float alpha = 1f;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        //Glide.get(this).clearDiskCache();
        //Glide.get(this).clearMemory();
        initView();
        initPopupWindow();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }*/

        //applyPermissions();

        mGridManager = new GridLayoutManager(MainActivityTest.this, 3);
        mGridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(mGridManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new PhotosAdapterTest(MainActivityTest.this, new ArrayList<PhotoBean>());
        mRecyclerView.setAdapter(mAdapter);
        setHeaderView(mRecyclerView);
        setFooterView(mRecyclerView);
    }

    private void initView() {
        words = findViewById(R.id.words);
        mRecyclerView = findViewById(R.id.recyclerView);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void initPopupWindow() {
        sheet = LayoutInflater.from(MainActivityTest.this).inflate(R.layout.activity_pop_window, null);
        takePhoto = sheet.findViewById(R.id.take_photo);
        choosePhoto = sheet.findViewById(R.id.choose_photo);
        cancel = sheet.findViewById(R.id.cancel);
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
                    if (ContextCompat.checkSelfPermission(MainActivityTest.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivityTest.this,
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
                    if (ContextCompat.checkSelfPermission(MainActivityTest.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivityTest.this,
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
                imageUri = FileProvider.getUriForFile(MainActivityTest.this, "us.xingkong.flyu", imageFile);
            } else {
                imageUri = Uri.fromFile(imageFile);
            }
            Log.i("imageUri", String.valueOf(imageUri));
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_REQUEST);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void choosePhoto() {
        Matisse.from(MainActivityTest.this)
                .choose(MimeType.allOf())
                .countable(true)
                .capture(false)
                .captureStrategy(new CaptureStrategy(true, "us.xingkong.flyu"))
                .maxSelectable(3 - mPhotoList.size())
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MatisseEngine())
                .forResult(ALBUM_REQUEST);
    }

    @TargetApi(23)
    public void applyPermissions() {
        mPermissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(MainActivityTest.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }

        if (!mPermissionList.isEmpty()) {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(MainActivityTest.this, permissions, GENERAL_REQUEST);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.login:
                        parseUriToBase64();
                        break;
                }
                return true;
            }
        });
        return true;
    }

    private void parseUriToBase64() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mPhotoList.size(); i++) {
                    try {
                        Bitmap bitmap = Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(mPhotoList.get(i))
                                .thumbnail(0.8f)
                                .into(300, 300)
                                .get();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        baos.flush();
                        baos.close();
                        String base = Base64.encodeToString(bytes, Base64.NO_WRAP);
                        if (!base.equals("") && !mBaseList.contains(base)) {
                            mBaseList.add(base);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.e("uriList", mPhotoList.size() + "");
                for (int i = 0; i < mBaseList.size(); i++) {
                    Log.e("base" + i, mBaseList.get(i));
                    Log.i("size", mBaseList.size() + "");
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    PhotoBean bean = new PhotoBean();
                    bean.setUri(imageUri.toString());
                    mPhotoList.add(bean);
                    //String uri = imageUri.toString();
                    //String base64 = new String(Base64.encode(uri.getBytes(), Base64.DEFAULT));
                    //Toast.makeText(this, "uri>>>" + uri + "base64>>>" + base64, Toast.LENGTH_LONG).show();
                    //Log.e("details", "uri>>>" + uri + "base64>>>" + base64);
                }
                break;
            case ALBUM_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<Uri> list = Matisse.obtainResult(data);
                    for (Uri uri : list) {
                        PhotoBean bean = new PhotoBean();
                        bean.setUri(uri.toString());
                        mPhotoList.add(bean);
                    }
                }
                break;
            case Browse_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<PhotoBean> list = (List<PhotoBean>) data.getSerializableExtra("photo");
                    mPhotoList.clear();
                    mPhotoList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
        loadPictures(mPhotoList);
        mAdapter.print();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GENERAL_REQUEST:
                for (int i = 0; i < grantResults.length; i++) {
                    boolean reRequest = ActivityCompat.shouldShowRequestPermissionRationale(MainActivityTest.this, permissions[i]);
                    if (reRequest) {
                        Toast.makeText(MainActivityTest.this, "没有权限我会死的......", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(MainActivityTest.this, "那就算了吧......", Toast.LENGTH_SHORT).show();
                }
                break;
            case ALBUM_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    Toast.makeText(MainActivityTest.this, "那就算了吧......", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void setHeaderView(RecyclerView recyclerView) {
        View header = LayoutInflater.from(MainActivityTest.this).inflate(R.layout.item_header, recyclerView, false);
        mAdapter.setHeaderView(header);
    }

    private void setFooterView(RecyclerView recyclerView) {
        View footer = LayoutInflater.from(MainActivityTest.this).inflate(R.layout.item_footer, recyclerView, false);

        if (mPhotoList.size() >= 3) {
            Toast.makeText(MainActivityTest.this, "最多支持三张照片哦", Toast.LENGTH_SHORT).show();
        }

        mAdapter.setOnAddClickListener(new PhotosAdapterTest.onAddClickListener() {
            @Override
            public void onAddClick() {
                closeKeyboard();
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

    private void loadPictures(final List<PhotoBean> list) {
        mAdapter = new PhotosAdapterTest(MainActivityTest.this, list);
        mRecyclerView.setAdapter(mAdapter);
        setHeaderView(mRecyclerView);
        setFooterView(mRecyclerView);
       // mAdapter.print();
        mAdapter.notifyDataSetChanged();
        ItemTouchHelper.Callback callback = new TouchHelperCallbackTest(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
        mAdapter.setItemClickListener(new PhotosAdapterTest.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position < list.size()) {
                    PhotoBean bean = list.get(position);
                    bean.setPosition(position);
                    Intent intent = new Intent(MainActivityTest.this, Browse.class);
                    intent.putExtra("photo", (Serializable) list);
                    intent.putExtra("bean", bean);
                    String name = getString(R.string.translate);
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(MainActivityTest.this, mRecyclerView, name);
                    ActivityCompat.startActivityForResult(MainActivityTest.this, intent, Browse_REQUEST, options.toBundle());
                }
            }
        });
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

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
