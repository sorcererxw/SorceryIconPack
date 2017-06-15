package com.sorcerer.sorcery.iconpack.iconShowCase.overview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;
import com.mikepenz.materialize.util.KeyboardUtil;
import com.mikepenz.materialize.util.UIUtils;
import com.sorcerer.sorcery.iconpack.MainActivity;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.IconBean;
import com.sorcerer.sorcery.iconpack.iconShowCase.detail.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.BaseRecyclerViewAdapter;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.BaseRecyclerViewHolder;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.turingtechnologies.materialscrollbar.ICustomAdapter;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.mthli.slice.Slice;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/19 0019
 */
public class IconAdapter extends BaseRecyclerViewAdapter<IconAdapter.IconItemViewHolder>
        implements ICustomAdapter, INameableAdapter {

    private static final int TYPE_HEADER = 0x0;
    private static final int TYPE_ICON_CENTER = 0x1;
    private static final int TYPE_ICON_TOP = 0x2;
    private static final int TYPE_ICON_TOP_LEFT = 0x3;
    private static final int TYPE_ICON_TOP_RIGHT = 0x4;
    private static final int TYPE_ICON_BOTTOM = 0x5;
    private static final int TYPE_ICON_BOTTOM_LEFT = 0x6;
    private static final int TYPE_ICON_BOTTOM_RIGHT = 0x7;
    private static final int TYPE_ICON_LEFT = 0x8;
    private static final int TYPE_ICON_RIGHT = 0x9;
    private static final int TYPE_ICON_SINGLE_LINE_LEFT = 0xA;
    private static final int TYPE_ICON_SINGLE_LINE_CENTER = 0xB;
    private static final int TYPE_ICON_SINGLE_LINE_RIGHT = 0xC;
    private static final int TYPE_FOOTER = 0xD;
    private static boolean sLock = false;
    private Activity mActivity;
    private boolean mCustomPicker = false;
    private Context mContext;
    private List<List<IconBean>> mIconBeanLists = new ArrayList<>();
    private List<Pair<IconBean, Integer>> mShowList;
    private RequestManager mGlideRequestManager;
    private int mSpan;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private boolean mLessAnim = false;

    IconAdapter(Activity activity, Context context,
                List<IconBean> iconBeanList, int span,
                RequestManager glideRequestManager,
                RecyclerView recyclerView,
                GridLayoutManager slowGridLayoutManager) {
        super();

        mActivity = activity;
        mContext = context;
        mSpan = span;
        mGlideRequestManager = glideRequestManager;

        mRecyclerView = recyclerView;
        mLayoutManager = slowGridLayoutManager;

        mPrefs.lessAnim().asObservable().subscribe(lessAnim -> {
            mLessAnim = lessAnim;
        });

        List<IconBean> tmpList = new ArrayList<>();
        for (int i = 0; i < iconBeanList.size(); i++) {
            if (isLabel(iconBeanList.get(i).getName()) && !tmpList.isEmpty()) {
                mIconBeanLists.add(tmpList);
                tmpList = new ArrayList<>();
            }
            tmpList.add(iconBeanList.get(i));
        }
        mIconBeanLists.add(tmpList);
        generateShowList(span);
    }

    void setSpan(int span) {
        if (span == mSpan) {
            return;
        }
        mSpan = span;
        generateShowList(span);
    }

    private void generateShowList(int span) {
        mShowList = new ArrayList<>();
        Stream.of(mIconBeanLists)
                .filter(value -> value != null && value.size() > 0)
                .forEach(list -> {
                    boolean withHead = false;
                    if (isLabel(list.get(0).getName())) {
                        withHead = true;
                        mShowList.add(new Pair<>(list.get(0), TYPE_HEADER));
                    }
                    int itemCount = withHead ? list.size() - 1 : list.size();
                    if (itemCount % span != 0) {
                        itemCount = (itemCount + span) / span * span;
                    }
                    for (int position = 0; position < itemCount; position++) {
                        int realPos = withHead ? position + 1 : position;
                        IconBean bean = null;
                        if (realPos < list.size()) {
                            bean = list.get(realPos);
                        }
                        if (itemCount <= span) {
                            if (position == 0) {
                                mShowList.add(new Pair<>(bean, TYPE_ICON_SINGLE_LINE_LEFT));
                            } else if (position == itemCount - 1) {
                                mShowList.add(new Pair<>(bean, TYPE_ICON_SINGLE_LINE_RIGHT));
                            } else {
                                mShowList.add(new Pair<>(bean, TYPE_ICON_SINGLE_LINE_CENTER));
                            }
                            continue;
                        }
                        if (position == 0) {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_TOP_LEFT));
                        } else if (position == span - 1) {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_TOP_RIGHT));
                        } else if (position == itemCount - span) {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_BOTTOM_LEFT));
                        } else if (position == itemCount - 1) {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_BOTTOM_RIGHT));
                        } else if (position < span) {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_TOP));
                        } else if (position + span >= itemCount) {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_BOTTOM));
                        } else if ((position + 1) % span == 0) {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_RIGHT));
                        } else if (position % span == 0) {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_LEFT));
                        } else {
                            mShowList.add(new Pair<>(bean, TYPE_ICON_CENTER));
                        }
                    }
                });
        mShowList.add(new Pair<>(null, TYPE_FOOTER));

    }

    @Override
    public int getItemViewType(int position) {
        return mShowList.get(position).second;
    }

    @Override
    public IconItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(new View(parent.getContext()));
        }
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_icon_header_new, parent, false);
            return new HeaderViewHolder(view);
        }
        int layout;
        switch (viewType) {
            case TYPE_ICON_SINGLE_LINE_CENTER:
                layout = R.layout.item_icon_single_line_center;
                break;
            case TYPE_ICON_SINGLE_LINE_LEFT:
                layout = R.layout.item_icon_single_line_left;
                break;
            case TYPE_ICON_SINGLE_LINE_RIGHT:
                layout = R.layout.item_icon_single_line_right;
                break;
            case TYPE_ICON_CENTER:
                layout = R.layout.item_icon_center;
                break;
            case TYPE_ICON_TOP:
                layout = R.layout.item_icon_top;
                break;
            case TYPE_ICON_TOP_LEFT:
                layout = R.layout.item_icon_top_left;
                break;
            case TYPE_ICON_TOP_RIGHT:
                layout = R.layout.item_icon_top_right;
                break;
            case TYPE_ICON_BOTTOM:
                layout = R.layout.item_icon_bottom;
                break;
            case TYPE_ICON_BOTTOM_LEFT:
                layout = R.layout.item_icon_bottom_left;
                break;
            case TYPE_ICON_BOTTOM_RIGHT:
                layout = R.layout.item_icon_bottom_right;
                break;
            case TYPE_ICON_LEFT:
                layout = R.layout.item_icon_left;
                break;
            case TYPE_ICON_RIGHT:
                layout = R.layout.item_icon_right;
                break;
            default:
                layout = R.layout.item_icon_center;
        }
        View content = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new IconViewHolder(content);
    }

    @Override
    public void onBindViewHolder(IconItemViewHolder holder, int position) {
        final int type = mShowList.get(position).second;
        final IconBean iconBean = mShowList.get(position).first;
        if (type == TYPE_FOOTER) {
        } else if (type == TYPE_HEADER) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.mHeader.setText(getLabel(iconBean.getName()));
        } else {
            IconViewHolder iconHolder = (IconViewHolder) holder;
            if (iconBean != null) {
                RxView.clicks(iconHolder.itemView)
                        .filter(click ->
                                mShowList.get(iconHolder.getAdapterPosition()).first.getRes() != 0
                                        && !sLock)
                        .subscribe(click -> {
                            sLock = true;
                            new Handler().postDelayed(() -> sLock = false, 1000);

                            KeyboardUtil.hideKeyboard((Activity) mContext);
                            if (!mCustomPicker) {
                                prepareShowIconDialog(iconHolder, iconHolder.getAdapterPosition());
                            } else {
                                ((MainActivity) mActivity).onReturnCustomPickerRes(
                                        mShowList.get(iconHolder.getAdapterPosition()).first
                                                .getRes());
                            }
                        });
                RequestBuilder<Drawable> request = mGlideRequestManager
                        .load(iconBean.getRes());
                if (mLessAnim) {
                    request.apply(RequestOptions.noAnimation());
                } else {
                    request.transition(new DrawableTransitionOptions().crossFade());
                }
                request.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(iconHolder.mIcon);
            } else {
                iconHolder.itemView.setOnClickListener(null);
                iconHolder.mIcon.setImageBitmap(null);
            }

            boolean leftBottomRect = false,
                    rightBottomRect = false,
                    leftTopRect = false,
                    rightTopRect = false;

            switch (type) {
                case TYPE_ICON_CENTER:
                    break;
                case TYPE_ICON_TOP:
                    break;
                case TYPE_ICON_TOP_LEFT:
                    leftTopRect = true;
                    break;
                case TYPE_ICON_TOP_RIGHT:
                    rightTopRect = true;
                    break;
                case TYPE_ICON_BOTTOM:
                    break;
                case TYPE_ICON_BOTTOM_LEFT:
                    leftBottomRect = true;
                    break;
                case TYPE_ICON_BOTTOM_RIGHT:
                    rightBottomRect = true;
                    break;
                case TYPE_ICON_LEFT:
                    break;
                case TYPE_ICON_RIGHT:
                    break;
                case TYPE_ICON_SINGLE_LINE_LEFT:
                    leftTopRect = true;
                    leftBottomRect = true;
                    break;
                case TYPE_ICON_SINGLE_LINE_CENTER:
                    break;
                case TYPE_ICON_SINGLE_LINE_RIGHT:
                    rightBottomRect = true;
                    rightTopRect = true;
                    break;
            }

            Slice slice = new Slice(iconHolder.mFrame);
            slice.setRadius(2);
            slice.setRipple(0);

            slice.showLeftBottomRect(!leftBottomRect);
            slice.showRightBottomRect(!rightBottomRect);
            slice.showLeftTopRect(!leftTopRect);
            slice.showRightTopRect(!rightTopRect);

            slice.setColor(ResourceUtil.getAttrColor(mContext, R.attr.colorCard));

//            mThemeManager.cardColor().subscribe(slice::setColor);
        }
    }

    @Override
    public int getItemCount() {
        return mShowList.size();
    }

    void setCustomPicker(Activity activity, boolean customPicker) {
        mActivity = activity;
        mCustomPicker = customPicker;
    }

    boolean isItemFooter(int position) {
        return position == getItemCount() - 1;
    }

    boolean isItemHead(int position) {
        return mShowList.get(position).second == TYPE_HEADER;
    }

    private boolean isLabel(String s) {
        return s.startsWith("**") && s.endsWith("**");
    }

    private String getLabel(String s) {
        return s.substring(2, s.length() - 2);
    }

    private void prepareShowIconDialog(IconViewHolder holder, final int position) {
        if (position >= mLayoutManager.findFirstCompletelyVisibleItemPosition()
                && position <= mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            showIconDialog(holder, position);
        } else {
            RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        showIconDialog(holder, position);
                        mRecyclerView.removeOnScrollListener(this);
                    }
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        mRecyclerView.removeOnScrollListener(this);
                    }
                }
            };
            mRecyclerView.addOnScrollListener(onScrollListener);
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    private void showIconDialog(IconViewHolder holder, final int position) {
        Intent intent = new Intent(mContext, IconDialogActivity.class);
        intent.putExtra(IconDialogActivity.EXTRA_RES,
                mShowList.get(position).first.getRes());
        intent.putExtra(IconDialogActivity.EXTRA_NAME,
                mShowList.get(position).first.getName());
        intent.putExtra(IconDialogActivity.EXTRA_LABEL, mShowList.get(position).first.getLabel());
        intent.putExtra(IconDialogActivity.EXTRA_LESS_ANIM, mLessAnim);
        if (mLessAnim) {
            mActivity.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else {
            mActivity.startActivityForResult(
                    intent,
                    MainActivity.Companion.getREQUEST_ICON_DIALOG(),
                    ActivityOptions.makeSceneTransitionAnimation(
                            mActivity,
                            holder.mIcon,
                            "icon"
                    ).toBundle()
            );
        }

    }

    @Override
    public String getCustomStringForElement(int position) {
        int type = mShowList.get(position).second;
        IconBean iconBean = mShowList.get(position).first;
        if (iconBean == null) {
            return "";
        }
        if (type == TYPE_HEADER) {
            return getLabel(iconBean.getName());
        } else {
            return iconBean.getName().substring(0, 1).toUpperCase();
        }
    }

    @Override
    public Character getCharacterForElement(int position) {
        int type = mShowList.get(position).second;
        IconBean iconBean = mShowList.get(position).first;
        if (iconBean == null) {
            return ' ';
        }
        if (type == TYPE_HEADER) {
            return getLabel(iconBean.getName()).charAt(0);
        } else {
            return iconBean.getName().charAt(0);
        }
    }

    static class IconItemViewHolder extends BaseRecyclerViewHolder {
        IconItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class IconViewHolder extends IconItemViewHolder {

        @BindView(R.id.frameLayout_item_icon_container)
        FrameLayout mFrame;

        @BindView(R.id.imageView_item_icon)
        ImageView mIcon;

        IconViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class HeaderViewHolder extends IconItemViewHolder {
        @BindView(R.id.textView_icon_header_new)
        TextView mHeader;

        HeaderViewHolder(final View itemView) {
            super(itemView);
        }
    }

    private static class FooterViewHolder extends IconItemViewHolder {

        View mSpace;

        FooterViewHolder(View itemView) {
            super(itemView);
            mSpace = itemView;
            mPrefs.enableTransparentNavBar().asObservable()
                    .map(enable -> enable ?
                            UIUtils.getNavigationBarHeight(itemView.getContext()) : 0)
                    .subscribe(height -> {
                        ViewGroup.LayoutParams layoutParams = mSpace.getLayoutParams();
                        if (layoutParams == null) {
                            layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, height);
                        } else {
                            layoutParams.height = height;
                        }
                        mSpace.setLayoutParams(layoutParams);
                    });
        }
    }
}

