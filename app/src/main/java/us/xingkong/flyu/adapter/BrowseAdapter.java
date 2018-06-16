package us.xingkong.flyu.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import us.xingkong.flyu.model.PhotoModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/13 1:06
 * @描述:
 * @更新日志:
 */
public class BrowseAdapter extends PagerAdapter {

    private List<PhotoModel> mList;
    private AppCompatActivity mActivity;
    private PhotoView mPhotoView;
    private OnPhotoViewClickListener mListener;

    public BrowseAdapter(AppCompatActivity activity, List<PhotoModel> list) {
        mActivity = activity;
        mList = list;
    }

    public interface OnPhotoViewClickListener {
        void onClick();
    }

    public void setOnPhotoViewClickListener(OnPhotoViewClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoModel model = mList.get(position);
        mPhotoView = new PhotoView(mActivity);

        Glide.with(mActivity)
                .load(Uri.parse(model.getUri()))
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(mPhotoView);

        container.addView(mPhotoView);

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick();
            }
        });
        return mPhotoView;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
