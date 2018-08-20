/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui.common.DialogHelper;
import com.fj.android.mediamonkey.ui.common.ToastHelper;
import com.fj.android.mediamonkey.util.io.FileUtils;
import com.fj.android.mediamonkey.util.lang.ArrayUtils;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2017
 */
public class EngineerModeFragment extends Fragment implements View.OnClickListener {
    private View exportDatabaseView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View rootView = inflater.inflate(R.layout.layout_engineering, null);
        this.exportDatabaseView = ButterKnife.findById(rootView, R.id.eng_export_database);

        exportDatabaseView.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(final View v) {
        if (v == exportDatabaseView) {
            onClickExportDatabaseView();
        }
    }

    private void onClickExportDatabaseView() {
        new AsyncTask<Void, Void, Integer>() {
            final String EXPORT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
            private int    dbFilesCount;
            private Dialog pg;
            private File   exportDir;

            @Override
            protected void onPreExecute() {
                this.pg = DialogHelper.showModalSpinner(getActivity(), R.string.text_please_wait);
            }

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            protected Integer doInBackground(final Void... params) {
                this.exportDir = new File(EXPORT_PATH, "MediaMonkey");
                exportDir.mkdirs();
                if (!exportDir.isDirectory()) {
                    return -1;
                }

                File dbFilesPath = getActivity().getDatabasePath(".");
                File[] dbFiles = dbFilesPath.listFiles();
                this.dbFilesCount = dbFiles.length;
                if (0 == ArrayUtils.length(dbFiles)) {
                    return 0;
                }

                int copiedFilesCount = 0;
                for (int i = 0; i < dbFilesCount; i++) {
                    File src  = dbFiles[i];
                    File dest = new File(exportDir  , src.getName());
                    try {
                        FileUtils.copy(src.getAbsolutePath(), dest.getAbsolutePath());
                    } catch (IOException e) {
                        return copiedFilesCount;
                    }
                    copiedFilesCount++;
                }
                return copiedFilesCount;
            }

            @Override
            protected void onPostExecute(final Integer copiedCount) {
                DialogHelper.dismiss(pg);
                if (-1 == copiedCount) {
                    ToastHelper.show("Unable to create directory " + exportDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }

                if (dbFilesCount == copiedCount) {
                    ToastHelper.show("Database files were exported to: " + exportDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } else {
                    ToastHelper.show("Some files were not exported: " + (copiedCount + 1) +
                            "/" + (dbFilesCount + 1), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}
