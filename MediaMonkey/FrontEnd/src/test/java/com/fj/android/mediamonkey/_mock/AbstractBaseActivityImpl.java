/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.mediamonkey._mock;

import com.fj.android.mediamonkey.ui._base.AbstractBaseActivity;
import com.fj.android.mediamonkey.ui._base.BasePresenter;

import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 8 - Nov - 2016
 */
public class AbstractBaseActivityImpl extends AbstractBaseActivity {
    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return null;
    }
}
