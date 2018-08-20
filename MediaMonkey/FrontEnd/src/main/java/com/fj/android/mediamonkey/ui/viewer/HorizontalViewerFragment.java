/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fj.android.mediamonkey.R;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jan - 2017
 */
public class HorizontalViewerFragment extends AbstractViewerFragment {
    @NonNull
    @Override
    public View getInflatedView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewer_horizontal, container, false);
    }
}
