/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.filepicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fj.android.mediamonkey.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2016
 */
class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {
    private final List<PluginPkgVo>    items;
    private final View.OnClickListener clickListener;

    FileListAdapter(View.OnClickListener itemClickListener) {
        this.items = new ArrayList<>();
        this.clickListener = itemClickListener;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.item_file_list, parent, false
        );
        rootView.setClickable(true);
        rootView.setOnClickListener(clickListener);

        return new FileViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        holder.draw(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    PluginPkgVo getBoundPkgInfo(View view) {
        Object fileViewHolder = view.getTag();
        if (fileViewHolder instanceof FileViewHolder) {
            return ((FileViewHolder) fileViewHolder).data;
        } else {
            return null;
        }
    }

    void add(PluginPkgVo pkgVo) {
        items.add(pkgVo);
        notifyItemInserted(items.size() - 1);
    }

    void addAll(List<PluginPkgVo> pkgVoCollection) {
        for (PluginPkgVo pkgVo : pkgVoCollection) {
            items.add(pkgVo);
        }
        notifyItemRangeInserted(items.size() - 1, pkgVoCollection.size());
    }

    void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        private final View      rootView;
        private final ImageView imgIcon;
        private final TextView  tvFileName;
        private final TextView  tvDate;
        PluginPkgVo             data;

        FileViewHolder(View rootView) {
            super(rootView);
            this.rootView   = rootView;
            this.imgIcon    = ButterKnife.findById(rootView, R.id.img_file_list_icon);
            this.tvFileName = ButterKnife.findById(rootView, R.id.txt_file_list_name);
            this.tvDate     = ButterKnife.findById(rootView, R.id.txt_file_list_date);
        }

        void draw(PluginPkgVo pkgVo) {
            this.data = pkgVo;
            rootView.setTag(this);

            imgIcon.setImageResource(getIconResources(pkgVo.getType()));
            tvFileName.setText(pkgVo.getFileName());
            tvDate.setText(pkgVo.getDateAsText());
        }

        private int getIconResources(@PluginPkgVo.Type int type) {
            switch (type) {
                case PluginPkgVo.Type.DIRECTORY:
                    return R.drawable.ico_folder;
                case PluginPkgVo.Type.DIRECTORY_PARENT:
                    return R.drawable.ico_folder_parent;
                case PluginPkgVo.Type.DIRECTORY_EXTERNAL:
                    return R.drawable.ico_file_external;
                case PluginPkgVo.Type.CONTAINER:
                default:
                    return R.drawable.ico_file_mmplugin;
            }
        }
    }
}
