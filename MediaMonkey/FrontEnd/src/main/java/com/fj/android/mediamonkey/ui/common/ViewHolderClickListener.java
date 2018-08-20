/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.common;

import android.support.v7.widget.RecyclerView;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Dec - 2016
 */
public interface ViewHolderClickListener<VH extends RecyclerView.ViewHolder, T> {
    void onClick(VH viewHolder, T drawingItem);
}
