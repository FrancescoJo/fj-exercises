/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.fj.android.mediamonkey.event.RequestPluginSettingsActivityContextEvent;
import com.fj.android.mediamonkey.event.ResponsePluginSettingsActivityContextEvent;
import com.fj.android.mediamonkey.inject.objects.RxEventBus;
import com.fj.android.mediamonkey.ui.common.DialogHelper;
import com.fj.android.mediamonkey.util.RxUtils;
import com.mediamonkey.android.app.ui.ListItemSelectedListener;
import com.mediamonkey.android.app.ui.SettingsListDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Dec - 2016
 */
public class UiDialogServiceImpl implements UiDialogService {
    private RxEventBus eventBus;

    public UiDialogServiceImpl(RxEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public SettingsListDialog makeListDialog(final CharSequence title, final List<?> itemList, final int defaultSelection) {
        final List<CharSequence> dataList = new ArrayList<>();
        for (Object item : itemList) {
            if (item instanceof CharSequence) {
                dataList.add((CharSequence) item);
            } else {
                dataList.add(item.toString());
            }
        }

        return new SettingsListDialog() {
            private ListItemSelectedListener itemSelectedListener;
            private Disposable contextRequestSubscription;

            @Override
            public void show() {
                RxUtils.dispose(contextRequestSubscription);
                this.contextRequestSubscription =
                        eventBus.eventByType(ResponsePluginSettingsActivityContextEvent.class)
                                .subscribe(new Consumer<ResponsePluginSettingsActivityContextEvent>() {
                                    @Override
                                    public void accept(ResponsePluginSettingsActivityContextEvent responsePluginSettingsActivityContextEvent) throws Exception {
                                        RxUtils.dispose(contextRequestSubscription);
                                        Context ctx = responsePluginSettingsActivityContextEvent.getContext();
                                        if (null == ctx) {
                                            return;
                                        }
                                        showDialogInternal(ctx);
                                    }
                                });
                eventBus.post(new RequestPluginSettingsActivityContextEvent());
            }

            @Override
            public void setOnListItemSelectedListener(ListItemSelectedListener itemSelectedListener) {
                this.itemSelectedListener = itemSelectedListener;
            }

            void showDialogInternal(final Context context) {
                Dialog dialog = DialogHelper.showSingleChoice(context, title, dataList, defaultSelection, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        itemSelectedListener.onItemSelected(integer);
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        RxUtils.dispose(contextRequestSubscription);
                    }
                });
            }
        };
    }
}
