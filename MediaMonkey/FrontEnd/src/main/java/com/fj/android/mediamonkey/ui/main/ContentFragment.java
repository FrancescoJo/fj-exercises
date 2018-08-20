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
import com.fj.android.mediamonkey.util.lang.CollectionUtils;
import com.mediamonkey.android.lib.annotation.DisplayType;
import com.mediamonkey.android.lib.dto.contents.Category;
import com.mediamonkey.android.lib.dto.contents.Content;
import com.mediamonkey.android.lib.dto.contents.ContentList;
import com.mediamonkey.android.lib.dto.contents.ThumbnailSpec;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

import static com.fj.android.mediamonkey.ui.main.MainUiHelper.createLayoutManager;
import static com.fj.android.mediamonkey.ui.main.MainUiHelper.createRootView;
import static com.fj.android.mediamonkey.ui.main.MainUiHelper.isLastItemShowing;

/**
 * This class is not reusable but public, due to restrictions on {@link android.app.Fragment}s.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Dec - 2016
 */
public class ContentFragment extends AbstractBaseFragment {
    private static final String ARG_CNTS  = "argCnts";
    private static final String ARG_CTG   = "argCtg";

    boolean hasMore;
    int     currentPage = 1;   // Default is 1, because '0' is already loaded upon creation

    private RecyclerView      rootView;
    private ContentAdapter    adapter;
    private MainViewPresenter presenter;
    private CharSequence      oldTitle;
    private Category          currentCategory;
    private ContentList       cntList;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.cntList         = getArguments().getParcelable(ARG_CNTS);
        this.currentCategory = getArguments().getParcelable(ARG_CTG);
        this.hasMore         = cntList.hasNext();
    }

    @Override
    public void onActivityCreated(final @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.presenter = getPresenter(MainViewPresenter.class);
        this.oldTitle  = getMainView().getTitleText();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainView().setTitleText(currentCategory.getTitleText());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        this.rootView = createRootView(getActivity(), cntList.getDisplayType());
        this.adapter = new ContentAdapter(cntList.getContentList(), cntList.getDisplayType());
        adapter.setItemClickListener(new ViewHolderClickListener<ContentItemViewHolder, Content>() {
            @Override
            public void onClick(final ContentItemViewHolder viewHolder, final Content drawingItem) {
                drawingItem.setBrowseCount(drawingItem.getBrowseCount() + 1);
                presenter.updateContent(drawingItem).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean flag) throws Exception {
                        adapter.notifyItemChanged(drawingItem);
                    }
                });
                presenter.hasChapterList(drawingItem)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(final Boolean hasChapters) throws Exception {
                                if (hasChapters) {
                                    presenter.loadChapterList(drawingItem, 0,
                                            ((MainActivity) getActivity()).until(ActivityLifecycle.ON_DESTROY));
                                } else {
                                    presenter.loadChapter(viewHolder.getAdapterPosition(), drawingItem);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(final Throwable throwable) throws Exception {
                                ((MainActivity) getActivity()).showError(throwable, null);
                            }
                        });
            }
        });
        adapter.setBookmarkClickListener(new ViewHolderClickListener<ContentItemViewHolder, Content>() {
            @Override
            public void onClick(final ContentItemViewHolder viewHolder, final Content drawingItem) {
                drawingItem.setBookmarked(!drawingItem.isBookmarked());
                presenter.updateContent(drawingItem).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean flag) throws Exception {
                        adapter.notifyItemChanged(drawingItem);
                    }
                });
            }
        });

        rootView.setLayoutManager(createLayoutManager(rootView, cntList.getDisplayType()));
        rootView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        getMainView().setTitleText(oldTitle);
        super.onDestroyView();
    }

    void onContentListAdded(final ContentList contentList) {
        getMainView().hideLoadingView();
        if (CollectionUtils.size(contentList.getContentList()) > 0) {
            currentPage++;
            this.hasMore = contentList.hasNext();
            adapter.addAll(contentList.getContentList());
        } else {
            this.hasMore = false;
        }
    }

    private MainView getMainView() {
        return presenter.getView();
    }

    static ContentFragment newInstance(final Category category, final ContentList contentList) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CTG, category);
        args.putParcelable(ARG_CNTS, contentList);
        fragment.setArguments(args);
        return fragment;
    }

    class ContentAdapter extends AbstractMainUiAdapterTemplate<Content, ContentItemViewHolder> {
        private ViewHolderClickListener<ContentItemViewHolder, Content> bookmarkClickListener;
        private @DisplayType int displayType;

        ContentAdapter(final List<Content> items, final @DisplayType int displayType) {
            super(items);
            this.displayType = displayType;
        }

        @Override
        @SuppressLint("SwitchIntDef")
        public ContentItemViewHolder onNewViewHolder(final ViewGroup parent, final int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            final ContentItemViewHolder viewHolder;
            switch(displayType) {
                default:
                case DisplayType.LIST:
                    viewHolder = new ContentItemViewHolder(inflater.inflate(R.layout.item_main_content_list, parent, false));
                    break;
                case DisplayType.GRID:
                    viewHolder = new ContentItemViewHolder(inflater.inflate(R.layout.item_main_content_grid, parent, false));
                    break;
            }

            viewHolder.setBookmarkClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View bookMarkView) {
                    ContentItemViewHolder vh = (ContentItemViewHolder) bookMarkView.getTag();
                    Content content = vh.getDrawingData();

                    bookmarkClickListener.onClick(vh, content);
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ContentItemViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            if (hasMore && isLastItemShowing(rootView)) {
                getMainView().showLoadingView();
                presenter.loadContentList(currentCategory, currentPage,
                    ((MainActivity) getActivity()).until(ActivityLifecycle.ON_DESTROY));
            }
        }

        void setBookmarkClickListener(ViewHolderClickListener<ContentItemViewHolder, Content> listener) {
            this.bookmarkClickListener = listener;
        }

        void notifyItemChanged(final Content content) {
            List<Content> items = getItems();
            for (int i = 0, limit = items.size(); i < limit; i++) {
                Content item = items.get(i);
                if (item.getId() == content.getId()) {
                    items.set(i, content);
                    notifyItemChanged(i);
                    break;
                }
            }
        }

        void addAll(final List<Content> contentList) {
            int previousSize = getItemCount() - 1;
            getItems().addAll(contentList);
            this.notifyItemRangeInserted(previousSize, contentList.size());
        }
    }
}

class ContentItemViewHolder extends AbstractMainUiAdapterTemplate.ViewHolder<Content> {
    private final DraweeView imgCategory;
    private final TextView   tvTitle;
    private final TextView   tvDescription;
    private final ImageView  imgBookmarked;
    private final TextView   tvBrowseCount;
    private final TextView   tvBrowseDate;

    private Content drawingItem;

    ContentItemViewHolder(final View rootView) {
        super(rootView);
        this.imgCategory   = ButterKnife.findById(itemView, R.id.img_main_content_item);
        this.tvTitle       = ButterKnife.findById(itemView, R.id.txt_main_content_item_title);
        this.tvDescription = ButterKnife.findById(itemView, R.id.txt_main_content_item_desc);
        this.imgBookmarked = ButterKnife.findById(itemView, R.id.img_main_content_item_bookmarked);
        this.tvBrowseCount = ButterKnife.findById(itemView, R.id.txt_main_content_item_browse_count);
        this.tvBrowseDate  = ButterKnife.findById(itemView, R.id.txt_main_content_item_browse_date);
    }

    @Override
    public Content getDrawingData() {
        return drawingItem;
    }

    @Override
    public void bind(final Content content) {
        itemView.setTag(this);
        imgBookmarked.setTag(this);
        this.drawingItem = content;

        Uri thumbnailUri = ThumbnailSpec.getThumbnailUri(content.getThumbnailSpec());
        if (null != thumbnailUri) {
            tvDescription.setVisibility(View.VISIBLE);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(thumbnailUri)
                    .setOldController(imgCategory.getController())
                    .build();
            imgCategory.setController(controller);
        }

        tvTitle.setText(content.getTitleText());

        if (!TextUtils.isEmpty(content.getDescriptionText())) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(content.getDescriptionText());
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        imgBookmarked.setSelected(content.isBookmarked());
        tvBrowseCount.setText(ResourceUtils.getString(R.string.text_times_count, content.getBrowseCount()));
        if (content.getLastAccessDate() == 0) {
            tvBrowseDate.setText("");
        } else {
            tvBrowseDate.setText(UiUtils.getRelativeDateText(System.currentTimeMillis(), content.getLastAccessDate()));
        }
    }

    void setBookmarkClickListener(final View.OnClickListener listener) {
        imgBookmarked.setOnClickListener(listener);
    }
}