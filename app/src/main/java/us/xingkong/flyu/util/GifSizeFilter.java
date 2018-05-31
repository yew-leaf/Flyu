package us.xingkong.flyu.util;

import android.content.Context;
import android.graphics.Point;

import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.UncapableCause;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.util.HashSet;
import java.util.Set;

import us.xingkong.flyu.R;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: GifSizeFilter
 * @创建时间: 2018/5/9 20:45
 * @最后修改于:
 * @版本: 1.0
 * @描述: Matisse的gif过滤器
 * @更新日志:
 */
public class GifSizeFilter extends Filter {

    private int mMinWidth;
    private int mMinHeight;
    private int mMaxSize;

    public GifSizeFilter(int minWidth, int minHeight, int maxSize) {
        mMinWidth = minWidth;
        mMinHeight = minHeight;
        mMaxSize = maxSize;
    }

    @Override
    protected Set<MimeType> constraintTypes() {
        return new HashSet<MimeType>() {{
            add(MimeType.GIF);
        }};
    }

    @Override
    public UncapableCause filter(Context context, Item item) {
        if (!needFiltering(context, item))
            return null;

        Point size = PhotoMetadataUtils.getBitmapBound(context.getContentResolver(), item.getContentUri());
        if (size.x < mMinWidth || size.y < mMinHeight || item.size > mMaxSize) {
            return new UncapableCause(UncapableCause.DIALOG,
                    context.getString(R.string.error_gif, mMinWidth, String.valueOf(PhotoMetadataUtils.getSizeInMB(mMaxSize))));
        }
        return null;
    }
}
