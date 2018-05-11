package us.xingkong.flyu;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.PopupWindow;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @作者: Xuer
 * @描述:
 * @更新日志:
 * @目前的问题: (1) popupWindow的动画还未改成material design风格
 * (2) 拖拽排序、删除
 * (3) 点击图片后预览
 * (4) 上传
 */
public class MainActivity extends AppCompatActivity {

    private Uri imageUri;
    private List<Uri> mUriList = new ArrayList<>();
    private final static int GENERAL_REQUEST = 0;
    private final static int CAMERA_REQUEST = 1;
    private final static int ALBUM_REQUEST = 2;
    private final static int CROP_REQUEST = 3;
    private ArrayList<String> mPermissionList = new ArrayList<>();
    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private Toolbar mToolbar;
    private EditText words;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridManager;
    private PicturesAdapter mAdapter;
    private PopupWindow popupWindow;
    private View sheet;
    private CardView choosePhoto, takePhoto, cancel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPopupWindow();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }*/

        //applyPermissions();

        mGridManager = new GridLayoutManager(MainActivity.this, 3);
        mRecyclerView.setLayoutManager(mGridManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new PicturesAdapter(MainActivity.this, null);
        mRecyclerView.setAdapter(mAdapter);
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
        sheet = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_pop_window, null);
        takePhoto = sheet.findViewById(R.id.take_photo);
        choosePhoto = sheet.findViewById(R.id.choose_photo);
        cancel = sheet.findViewById(R.id.cancel);
        popupWindow = new PopupWindow(sheet);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.SheetAnimation);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
                imageUri = FileProvider.getUriForFile(MainActivity.this, "us.xingkong.flyu", imageFile);
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
        Matisse.from(MainActivity.this)
                .choose(MimeType.allOf())
                .countable(true)
                .capture(false)
                .captureStrategy(new CaptureStrategy(true, "us.xingkong.flyu"))
                .maxSelectable(3 - mUriList.size())
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
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }

        if (!mPermissionList.isEmpty()) {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, GENERAL_REQUEST);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.submit:
                        OkUtils.post("", null, new OkUtils.DataCallBack() {
                            @Override
                            public void onSuccess(String result) throws Exception {

                            }

                            @Override
                            public void onFailure(Request request, IOException e) {

                            }
                        });
                        break;
                }
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    mUriList.add(imageUri);
                    String uri = imageUri.toString();
                    String base64 = new String(Base64.encode(uri.getBytes(), Base64.DEFAULT));
                    //Toast.makeText(this, "uri>>>" + uri + "base64>>>" + base64, Toast.LENGTH_LONG).show();
                    Log.e("details","uri>>>" + uri + "base64>>>" + base64);
                    loadPictures(mUriList);
                }
                break;
            case ALBUM_REQUEST:
                if (resultCode == RESULT_OK) {
                    mUriList.addAll(Matisse.obtainResult(data));
                    loadPictures(mUriList);
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

    private void setFooterView(RecyclerView recyclerView) {
        View footer = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_photo_add, recyclerView, false);

        if (mUriList.size() >= 3) {
            Toast.makeText(MainActivity.this, "最多支持三张照片哦", Toast.LENGTH_SHORT).show();
            footer.setEnabled(false);
        }

        mAdapter.setItemClickListener(new PicturesAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                closeKeyboard();
                if (position == mUriList.size()) {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.5f;
                    getWindow().setAttributes(lp);
                    popupWindow.showAtLocation(sheet, Gravity.BOTTOM, 0, 0);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 1f;
                            getWindow().setAttributes(lp);
                        }
                    });
                }
            }
        });
        mAdapter.setFooterView(footer);
    }

    private void loadPictures(List<Uri> list) {
        mAdapter = new PicturesAdapter(MainActivity.this, list);
        mRecyclerView.setAdapter(mAdapter);
        setFooterView(mRecyclerView);
        mAdapter.notifyDataSetChanged();
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
