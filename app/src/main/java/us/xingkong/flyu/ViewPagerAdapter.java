package us.xingkong.flyu;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: ViewPagerAdapter
 * @创建时间: 2018/5/13 1:06
 * @最后修改于:
 * @版本: 1.0
 * @描述:
 * @更新日志:
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<PhotoBean> mList;
    private AppCompatActivity mActivity;

    public ViewPagerAdapter(AppCompatActivity context, List<PhotoBean> list) {
        mActivity = context;
        mList = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoBean bean = mList.get(position);
        PhotoView photoView = new PhotoView(mActivity);
        Glide.with(mActivity)
                .asBitmap()
                .load(Uri.parse(bean.getUri()))
                .into(photoView);
        container.addView(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        return photoView;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        } else
            return 0;
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
