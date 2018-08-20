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
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui._base.AbstractBaseFragment;
import com.fj.android.mediamonkey.ui.common.ViewHolderClickListener;
import com.fj.android.mediamonkey.util.ResourceUtils;
import com.fj.android.mediamonkey.util.UiUtils;
import com.mediamonkey.android.lib.annotation.DisplayType;
import com.mediamonkey.android.lib.dto.contents.Chapter;
import com.mediamonkey.android.lib.dto.contents.ChapterList;
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
 * @since 26 - Dec - 2016
 */
public class ChapterFragment extends AbstractBaseFragment {
    private static final String ARG_CHPS = "argChps";
    private static final String ARG_TITLE = "argTitle";

    private MainViewPresenter presenter;

    private CharSequence oldTitle;
    private String       title;
    private ChapterList  chpList;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.chpList = getArguments().getParcelable(ARG_CHPS);
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
        RecyclerView rootView = createRootView(getActivity(), chpList.getDisplayType());
        rootView.setBackgroundResource(R.color.grey_ee);
        final ChapterAdapter adapter = new ChapterAdapter(chpList.getChapterList(), chpList.getDisplayType());
        adapter.setItemClickListener(new ViewHolderClickListener<ChapterItemViewHolder, Chapter>() {
            @Override
            public void onClick(final ChapterItemViewHolder viewHolder, final Chapter drawingItem) {
                drawingItem.setBrowseCount(drawingItem.getBrowseCount() + 1);
                presenter.updateChapter(drawingItem).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean flag) {
                        adapter.notifyItemChanged(drawingItem);
                    }
                });

                presenter.loadChapter(viewHolder.getAdapterPosition(), drawingItem);
            }
        });

        rootView.setLayoutManager(createLayoutManager(rootView, chpList.getDisplayType()));
        rootView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        presenter.getView().setTitleText(oldTitle);
        super.onDestroyView();
    }

    static ChapterFragment newInstance(final ChapterList chapterList, final CharSequence title) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CHPS, chapterList);
        args.putString(ARG_TITLE, title.toString());
        fragment.setArguments(args);
        return fragment;
    }
}

class ChapterAdapter extends AbstractMainUiAdapterTemplate<Chapter, ChapterItemViewHolder> {
    private @DisplayType int displayType;

    ChapterAdapter(final List<Chapter> items, final @DisplayType int displayType) {
        super(items);
        this.displayType = displayType;
    }

    @Override
    @SuppressLint("SwitchIntDef")
    public ChapterItemViewHolder onNewViewHolder(final ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ChapterItemViewHolder viewHolder;
        switch(displayType) {
            default:
            case DisplayType.LIST:
                viewHolder = new ChapterItemViewHolder(inflater.inflate(R.layout.item_main_chapter_list, parent, false));
                break;
            case DisplayType.GRID:
                viewHolder = new ChapterItemViewHolder(inflater.inflate(R.layout.item_main_chapter_grid, parent, false));
                break;
        }

        return viewHolder;
    }

    void notifyItemChanged(final Chapter chapter) {
        List<Chapter> items = getItems();
        for (int i = 0, limit = items.size(); i < limit; i++) {
            Chapter item = items.get(i);
            if (item.getId() == chapter.getId()) {
                items.set(i, chapter);
                notifyItemChanged(i);
                break;
            }
        }
    }
}

class ChapterItemViewHolder extends AbstractMainUiAdapterTemplate.ViewHolder<Chapter> {
    private final DraweeView imgCategory;
    private final TextView   tvTitle;
    private final TextView   tvDescription;
    private final TextView   tvBrowseCount;
    private final TextView   tvBrowseDate;

    private Chapter drawingItem;

    ChapterItemViewHolder(final View rootView) {
        super(rootView);
        this.imgCategory   = ButterKnife.findById(itemView, R.id.img_main_content_item);
        this.tvTitle       = ButterKnife.findById(itemView, R.id.txt_main_content_item_title);
        this.tvDescription = ButterKnife.findById(itemView, R.id.txt_main_content_item_desc);
        this.tvBrowseCount = ButterKnife.findById(itemView, R.id.txt_main_content_item_browse_count);
        this.tvBrowseDate  = ButterKnife.findById(itemView, R.id.txt_main_content_item_browse_date);
    }

    @Override
    public Chapter getDrawingData() {
        return drawingItem;
    }

    @Override
    public void bind(final Chapter chapter) {
        itemView.setTag(this);
        this.drawingItem = chapter;

        Uri thumbnailUri = ThumbnailSpec.getThumbnailUri(chapter.getThumbnailSpec());
        if (null != thumbnailUri) {
            tvDescription.setVisibility(View.VISIBLE);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(thumbnailUri)
                    .setOldController(imgCategory.getController())
                    .build();
            imgCategory.setController(controller);
        }

        tvTitle.setText(chapter.getTitleText());

        if (!TextUtils.isEmpty(chapter.getDescriptionText())) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(chapter.getDescriptionText());
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        tvBrowseCount.setText(ResourceUtils.getString(R.string.text_times_count, chapter.getBrowseCount()));
        if (chapter.getLastAccessDate() == 0) {
            tvBrowseDate.setText("");
        } else {
            tvBrowseDate.setText(UiUtils.getRelativeDateText(System.currentTimeMillis(), chapter.getLastAccessDate()));
        }
    }
}
