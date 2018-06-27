package us.xingkong.flyu.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;

import us.xingkong.flyu.R;

import static us.xingkong.flyu.app.Constants.ALBUM_REQUEST;
import static us.xingkong.flyu.app.Constants.CAMERA_REQUEST;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 13:46
 * @描述:
 * @更新日志:
 */
public class UIUtil {

    public static Uri cameraUri;
    private static float alpha = 1f;

    public static void closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static PopupWindow photoPopupWindow(final Activity activity, View root) {
        return photoPopupWindow(activity, root, 1);
    }

    public static PopupWindow photoPopupWindow(final Activity activity, View root, final int maxSelectable) {
        CardView takePhoto = root.findViewById(R.id.take_photo);
        CardView choosePhoto = root.findViewById(R.id.choose_photo);
        CardView cancel = root.findViewById(R.id.cancel);

        final PopupWindow popupWindow = new PopupWindow(root);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOutsideTouchable(true);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                    } else {
                        takePhoto(activity);
                    }
                } else {
                    takePhoto(activity);
                }
                popupWindow.dismiss();
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALBUM_REQUEST);
                    } else {
                        choosePhoto(activity, maxSelectable);
                    }
                } else {
                    choosePhoto(activity, maxSelectable);
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

        return popupWindow;
    }

    public static void takePhoto(Activity activity) {
        try {
            File imageFile = new File(activity.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");

            if (Build.VERSION.SDK_INT >= 24) {
                cameraUri = FileProvider.getUriForFile(activity, "us.xingkong.flyu", imageFile);
            } else {
                cameraUri = Uri.fromFile(imageFile);
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            activity.startActivityForResult(intent, CAMERA_REQUEST);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /*public static void choosePhoto(Activity activity) {
        Matisse.from(activity)
                .choose(MimeType.allOf())
                .countable(true)
                .capture(false)
                .captureStrategy(new CaptureStrategy(true, "us.xingkong.flyu"))
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MatisseEngine())
                .forResult(ALBUM_REQUEST);
    }*/

    public static void choosePhoto(Activity activity, int maxSelectable) {
        Matisse.from(activity)
                .choose(MimeType.allOf())
                .countable(true)
                .capture(false)
                .captureStrategy(new CaptureStrategy(true, "us.xingkong.flyu"))
                .maxSelectable(maxSelectable)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MatisseEngine())
                .forResult(ALBUM_REQUEST);
    }

    public static void setEnterAlpha(final Activity activity) {
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                            lp.alpha = alpha;
                            activity.getWindow().setAttributes(lp);
                        }
                    });
                }
            }
        }).start();
    }

    public static void setExitAlpha(final Activity activity) {
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                            lp.alpha = alpha;
                            activity.getWindow().setAttributes(lp);
                        }
                    });
                }
            }
        }).start();
    }
}
