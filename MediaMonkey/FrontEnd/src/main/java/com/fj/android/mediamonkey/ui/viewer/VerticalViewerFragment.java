/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui.common.ToastHelper;
import com.fj.android.mediamonkey.util.ResourceUtils;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jan - 2017
 */
public class VerticalViewerFragment extends AbstractViewerFragment
        implements ViewerDataManager.DataObserver {
    private ViewerDataManager dataMgr;
    private Adapter           viewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dataMgr = getViewerView().getDataManager();
    }

    @NonNull
    @Override
    public View getInflatedView(final LayoutInflater inflater,
                                final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_viewer_vertical, container, false);
        final RecyclerView listView = ButterKnife.findById(rootView, R.id.list_viewer_vertical);

        Context context = container.getContext();
        LinearLayoutManager llm = new LinearLayoutManager(context);
        DividerItemDecoration divider = new DividerItemDecoration(context, llm.getOrientation());
        listView.setLayoutManager(llm);
        listView.addItemDecoration(divider);
        this.viewAdapter = new Adapter(dataMgr, llm);
        listView.setAdapter(viewAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataMgr.registerDataObserver(this);
    }

    @Override
    public void onStop() {
        dataMgr.unregisterDataObserver(this);
        super.onStop();
    }

    @Override
    public void onDataChanged(final int oldSize) {
        // Defensive logic against wrong plugin implementation
        int lastPos = oldSize - 1;
        int newCount = dataMgr.getCount() - oldSize;
        if (newCount <= 0) {
            return;
        }

        viewAdapter.notifyItemRangeInserted(lastPos, newCount);
    }
}

class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final float PRELOAD_NEXT_BALANCE = 0.75f;

    private final ViewerDataManager dataMgr;
    private final LinearLayoutManager layoutMgr;

    Adapter(final ViewerDataManager dataMgr, final LinearLayoutManager llm) {
        this.dataMgr = dataMgr;
        this.layoutMgr = llm;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewTypeInt) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewerDataManager.ViewType viewType = ViewerDataManager.ViewType.byValue(viewTypeInt);
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case TEXT:
                viewHolder = new TextViewHolder(inflater.inflate(
                        R.layout.item_viewer_vertical_text, parent, false)
                );
                break;
            case IMAGE:
                viewHolder = new ImageViewHolder(inflater.inflate(
                        R.layout.item_viewer_vertical_image, parent, false)
                );
                break;
            case LOADING:
                viewHolder = new LoadingViewHolder(inflater.inflate(
                        R.layout.item_viewer_vertical_loading, parent, false)
                );
                break;
            default:
                viewHolder = new DummyViewHolder(inflater.inflate(
                        R.layout.item_viewer_vertical_dummy, parent, false)
                );
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        ViewerDataManager.ViewType viewType = dataMgr.getTypeAt(position);
        switch (viewType) {
            case TEXT: {
                TextViewHolder vh = (TextViewHolder) viewHolder;
                vh.bind(dataMgr.getViewerDataAt(position).getData().getPayload());
                break;
            }
            case IMAGE: {
                ImageViewHolder vh = (ImageViewHolder) viewHolder;
                vh.bind(dataMgr.getViewerDataAt(position).getData().getResourceUri());
                break;
            }
            case LOADING:
                break;
            default: {
                DummyViewHolder vh = (DummyViewHolder) viewHolder;
                vh.bind(viewType.name());
            }
        }

        final int currentItemPos = layoutMgr.findFirstVisibleItemPosition();
        if (RecyclerView.NO_POSITION == currentItemPos) {
            return;
        }

        // Post-drawing process
        dataMgr.setCurrentViewIndex(currentItemPos);
        if (dataMgr.canDisplayChapterEdge(position)) {
            dataMgr.setChapterEdgeDisplayed(position);
            ToastHelper.show(dataMgr.getChapterEdge(position), Toast.LENGTH_SHORT);
        }

        final long currentChapterId = dataMgr.getCurrentDisplayingChapter();
        canPreloadNext()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean canPreloadNext) throws Exception {
                        if (canPreloadNext) {
                            dataMgr.addPreloadViewAtLast();
                            notifyItemChanged(dataMgr.getCount() - 1);
                            dataMgr.loadNextChapter(currentChapterId);
                        }
                    }
                });
    }

    @Override
    public int getItemViewType(int position) {
        return dataMgr.getTypeAt(position).value;
    }

    @Override
    public int getItemCount() {
        return dataMgr.getCount();
    }

    private Observable<Boolean> canPreloadNext() {
        int threshold = Math.round(dataMgr.getCount() * PRELOAD_NEXT_BALANCE);
        final boolean isExceedThreshold = layoutMgr.findLastVisibleItemPosition() >= threshold;
        if (!isExceedThreshold) {
            return Observable.just(false);
        }

        return dataMgr.nextChapterPreloadable();
    }
}

class TextViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    TextViewHolder(final View itemView) {
        super(itemView);
        this.textView = ButterKnife.findById(itemView, R.id.tv_item_viewer_text_vert);
    }

    void bind(final CharSequence data) {
        textView.setText(data);
    }
}

class ImageViewHolder extends RecyclerView.ViewHolder {
    private final SimpleDraweeView draweeView;

    ImageViewHolder(final View itemView) {
        super(itemView);
        this.draweeView = ButterKnife.findById(itemView, R.id.tv_item_viewer_image_vert);
    }

    void bind(final Uri imageUri) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageUri)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);
    }
}

class LoadingViewHolder extends RecyclerView.ViewHolder {
    LoadingViewHolder(View itemView) {
        super(itemView);
    }
}

class DummyViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    DummyViewHolder(final View itemView) {
        super(itemView);
        this.textView = ButterKnife.findById(itemView, R.id.tv_item_viewer_dummy_vert);
    }

    void bind(final CharSequence data) {
        textView.setText(ResourceUtils.getString(R.string.err_type_not_supported, data));
    }
}