package us.xingkong.flyu;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @作者: Xuer
 * @描述:
 * @更新日志:
 * @目前的问题: (1) popupWindow的动画还未改成material design风格、外边距
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
    private PhotosAdapter mAdapter;
    private PopupWindow popupWindow;
    private View sheet;
    private CardView choosePhoto, takePhoto, cancel;
    private ArrayList<String> mBaseList = new ArrayList<>();
    private ImageView test;

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
        mAdapter = new PhotosAdapter(MainActivity.this, null);
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
        test = findViewById(R.id.test);
        String base = "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RQAefP8A89m/76NHnz/89m/76NFFAB58/wDz2b/vo0efP/z2b/vo0UUAHnz/APPZv++jR58//PZv++jRRQAefP8A89m/76NHnz/89m/76NFFAB58/wDz2b/vo0efP/z2b/vo0UUAHnz/APPZv++jR58//PZv++jRRQAefP8A89m/76NHnz/89m/76NFFAB58/wDz2b/vo1+dv/B0siXn/BIrW4rtBKv/AAsLQflkG4ffn9aKKAP5j/7I0n/oF2//AH4X/Cj+yNJ/6Bdv/wB+F/woooA/pu/4NZ0Sz/4JF6LFaIsS/wDCwte+WMbR96D0r9FPPn/57N/30aKKADz5/wDns3/fRo8+f/ns3/fRoooAPPn/AOezf99Gjz5/+ezf99GiigA8+f8A57N/30aPPn/57N/30aKKADz5/wDns3/fRo8+f/ns3/fRoooAPPn/AOezf99Gjz5/+ezf99GiigA8+f8A57N/30aPPn/57N/30aKKADz5/wDns3/fRo8+f/ns3/fRoooAPPn/AOezf99Gjz5/+ezf99GiigA8+f8A57N/30aPPn/57N/30aKKADz5/wDns3/fRo8+f/ns3/fRoooAPPn/AOezf99GpLaaZpdrSt91v4vY0UUAf//Z";
        Bitmap bitmap;
        byte[] bytes = Base64.decode(base, Base64.NO_WRAP);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        test.setImageBitmap(bitmap);
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
        //popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
                for (int i = 0; i < mUriList.size(); i++) {
                    try {
                        Bitmap bitmap = Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(mUriList.get(i))
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
                Log.e("uriList", mUriList.size() + "");
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
                    mUriList.add(imageUri);
                    String uri = imageUri.toString();
                    String base64 = new String(Base64.encode(uri.getBytes(), Base64.DEFAULT));
                    //Toast.makeText(this, "uri>>>" + uri + "base64>>>" + base64, Toast.LENGTH_LONG).show();
                    Log.e("details", "uri>>>" + uri + "base64>>>" + base64);
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

        mAdapter.setItemClickListener(new PhotosAdapter.onItemClickListener() {
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
        mAdapter = new PhotosAdapter(MainActivity.this, list);
        mRecyclerView.setAdapter(mAdapter);
        setFooterView(mRecyclerView);
        mAdapter.notifyDataSetChanged();
        Log.e("mList", mAdapter.getItemCount() + "");
        ItemTouchHelper.Callback callback = new TouchHelperCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
