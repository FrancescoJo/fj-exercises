/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.main;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fj.android.mediamonkey.ui.common.ViewHolderClickListener;
import com.fj.android.mediamonkey.util.lang.CollectionUtils;

import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Dec - 2016
 */
abstract class AbstractMainUiAdapterTemplate<T, VH extends AbstractMainUiAdapterTemplate.ViewHolder<T>> extends RecyclerView.Adapter<VH> {
    private ViewHolderClickListener<VH, T> itemViewClickListener;

    private List<T> items;

    AbstractMainUiAdapterTemplate(final List<T> items) {
        this.items = items;
    }

    abstract VH onNewViewHolder(final ViewGroup parent, final int viewType);

    @Override
    public final VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final VH viewHolder = onNewViewHolder(parent, viewType);
        viewHolder.setViewRootClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View rootView) {
                @SuppressWarnings("unchecked")
                VH vh = (VH) rootView.getTag();
                T data = vh.getDrawingData();

                itemViewClickListener.onClick(viewHolder, data);
            }
        });

        return viewHolder;
    }

    @CallSuper
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(items);
    }

    List<T> getItems() {
        return items;
    }

    void setItemClickListener(final ViewHolderClickListener<VH, T> listener) {
        this.itemViewClickListener = listener;
    }

    abstract static class ViewHolder<T> extends RecyclerView.ViewHolder {
        ViewHolder(final View rootView) {
            super(rootView);
        }

        void setViewRootClickListener(final View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }

        public abstract T getDrawingData();

        public abstract void bind(final T data);
    }
}
