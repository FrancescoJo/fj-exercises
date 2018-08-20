/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.main;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui._base.AbstractBaseFragment;
import com.fj.android.mediamonkey.ui._base.ActivityLifecycle;
import com.fj.android.mediamonkey.ui.common.ViewHolderClickListener;
import com.fj.android.mediamonkey.util.ResourceUtils;
import com.fj.android.mediamonkey.util.UiUtils;
import com.mediamonkey.android.lib.annotation.DisplayType;
import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.CategoryList;
import com.mediamonkey.android.lib.dto.contents.ThumbnailSpec;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

import static com.fj.android.mediamonkey.ui.main.MainUiHelper.createLayoutManager;
import static com.fj.android.mediamonkey.ui.main.MainUiHelper.createRootView;

/**
 * This class is not reusable but public, due to restrictions on {@link android.app.Fragment}s.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Dec - 2016
 */
public class CategoryFragment extends AbstractBaseFragment {
    private static final String ARG_CTGS  = "argCtgs";
    private static final String ARG_TITLE = "argTitle";

    private MainViewPresenter presenter;

    private CharSequence oldTitle;
    private String       title;
    private CategoryList ctgList;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctgList = getArguments().getParcelable(ARG_CTGS);
        this.title   = getArguments().getString(ARG_TITLE);
    }

    @Override
    public void onActivityCreated(final @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.presenter = getPresenter(MainViewPresenter.class);
        this.oldTitle  = presenter.getView().getTitleText();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getView().setTitleText(title);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        RecyclerView rootView = createRootView(getActivity(), ctgList.getDisplayType());
        rootView.setBackgroundResource(R.color.grey_ee);
        final CategoryAdapter adapter = new CategoryAdapter(ctgList.getCategoryList(), ctgList.getDisplayType());
        adapter.setItemClickListener(new ViewHolderClickListener<CategoryItemViewHolder, Category>() {
            @Override
            public void onClick(final CategoryItemViewHolder viewHolder, final Category drawingItem) {
                drawingItem.setBrowseCount(drawingItem.getBrowseCount() + 1);
                presenter.updateCategory(drawingItem).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean flag) throws Exception {
                        adapter.notifyItemChanged(drawingItem);
                    }
                });
                presenter.loadContentList(drawingItem, 0, ((MainActivity) getActivity())
                        .until(ActivityLifecycle.ON_DESTROY));
            }
        });
        adapter.setBookmarkClickListener(new ViewHolderClickListener<CategoryItemViewHolder, Category>() {
            @Override
            public void onClick(final CategoryItemViewHolder viewHolder, final Category drawingItem) {
                drawingItem.setBookmarked(!drawingItem.isBookmarked());
                presenter.updateCategory(drawingItem).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean flag) throws Exception {
                        adapter.notifyItemChanged(drawingItem);
                    }
                });
            }
        });

        rootView.setLayoutManager(createLayoutManager(rootView, ctgList.getDisplayType()));
        rootView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        presenter.getView().setTitleText(oldTitle);
        super.onDestroyView();
    }

    static CategoryFragment newInstance(final CategoryList categoryList, final CharSequence title) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CTGS, categoryList);
        args.putString(ARG_TITLE, title.toString());
        fragment.setArguments(args);
        return fragment;
    }
}

class CategoryAdapter extends AbstractMainUiAdapterTemplate<Category, CategoryItemViewHolder> {
    private ViewHolderClickListener<CategoryItemViewHolder, Category> bookmarkClickListener;
    private @DisplayType int displayType;

    CategoryAdapter(final List<Category> items, final @DisplayType int displayType) {
        super(items);
        this.displayType = displayType;
    }

    @Override
    @SuppressLint("SwitchIntDef")
    public CategoryItemViewHolder onNewViewHolder(final ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final CategoryItemViewHolder viewHolder;
        switch(displayType) {
            default:
            case DisplayType.LIST:
                viewHolder = new CategoryItemViewHolder(inflater.inflate(R.layout.item_main_category_list, parent, false));
                break;
            case DisplayType.GRID:
                viewHolder = new CategoryItemViewHolder(inflater.inflate(R.layout.item_main_category_grid, parent, false));
                break;
        }

        viewHolder.setBookmarkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View bookMarkView) {
                CategoryItemViewHolder vh = (CategoryItemViewHolder) bookMarkView.getTag();
                Category category = vh.getDrawingData();

                bookmarkClickListener.onClick(vh, category);
            }
        });
        return viewHolder;
    }

    void setBookmarkClickListener(final ViewHolderClickListener<CategoryItemViewHolder, Category> listener) {
        this.bookmarkClickListener = listener;
    }

    void notifyItemChanged(final Category categoryDto) {
        List<Category> items = getItems();
        for (int i = 0, limit = items.size(); i < limit; i++) {
            Category item = items.get(i);
            if (item.getId() == categoryDto.getId()) {
                items.set(i, categoryDto);
                notifyItemChanged(i);
                break;
            }
        }
    }
}

class CategoryItemViewHolder extends AbstractMainUiAdapterTemplate.ViewHolder<Category> {
    private final DraweeView imgCategory;
    private final TextView   tvTitle;
    private final TextView   tvDescription;
    private final ImageView  imgBookmarked;
    private final TextView   tvBrowseCount;
    private final TextView   tvBrowseDate;

    private Category drawingItem;

    CategoryItemViewHolder(final View itemView) {
        super(itemView);
        this.imgCategory   = ButterKnife.findById(itemView, R.id.img_main_category_item);
        this.tvTitle       = ButterKnife.findById(itemView, R.id.txt_main_category_item_title);
        this.tvDescription = ButterKnife.findById(itemView, R.id.txt_main_category_item_desc);
        this.imgBookmarked = ButterKnife.findById(itemView, R.id.img_main_category_item_bookmarked);
        this.tvBrowseCount = ButterKnife.findById(itemView, R.id.txt_main_category_item_browse_count);
        this.tvBrowseDate  = ButterKnife.findById(itemView, R.id.txt_main_category_item_browse_date);
    }

    @Override
    public Category getDrawingData() {
        return drawingItem;
    }

    @Override
    public void bind(final Category category) {
        itemView.setTag(this);
        imgBookmarked.setTag(this);
        this.drawingItem = category;

        Uri thumbnailUri = ThumbnailSpec.getThumbnailUri(category.getThumbnailSpec());
        if (null != thumbnailUri) {
            tvDescription.setVisibility(View.VISIBLE);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(thumbnailUri)
                    .setOldController(imgCategory.getController())
                    .build();
            imgCategory.setController(controller);
        }

        tvTitle.setText(category.getTitleText());

        if (!TextUtils.isEmpty(category.getDescriptionText())) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(category.getDescriptionText());
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        imgBookmarked.setSelected(category.isBookmarked());
        tvBrowseCount.setText(ResourceUtils.getString(R.string.text_times_count, category.getBrowseCount()));
        if (category.getLastAccessDate() == 0) {
            tvBrowseDate.setText("");
        } else {
            tvBrowseDate.setText(UiUtils.getRelativeDateText(System.currentTimeMillis(), category.getLastAccessDate()));
        }
    }

    void setBookmarkClickListener(final View.OnClickListener listener) {
        imgBookmarked.setOnClickListener(listener);
    }
}
