/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.util.ResourceUtils;
import com.fj.android.mediamonkey.util.io.IOUtils;
import com.fj.android.mediamonkey.util.lang.EmptyCheckUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public final class DialogHelper {
    public static void dismiss(final Dialog dialog) {
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    public static boolean isShowing(final Dialog dialog) {
        return null != dialog && dialog.isShowing();
    }

    public static Dialog showFullScreenLoading(final Activity activity) {
        Dialog dlg = new Dialog(activity);
        @SuppressLint("InflateParams")
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.partial_loading_full, null);
        dlg.setContentView(rootView);
        if (null == dlg.getWindow()) {
            Timber.d("Activity %s is not displaying, not showing dialog", activity);
            return dlg;
        }
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ProgressBar progBar = (ProgressBar) rootView.getChildAt(0);
        progBar.setIndeterminate(true);
        // Lighter colour for colorAccent
        int pbColor = ResourcesCompat.getColor(activity.getResources(), R.color.colorAccentLight, null);
        progBar.getIndeterminateDrawable().setColorFilter(pbColor, android.graphics.PorterDuff.Mode.MULTIPLY);

        TextView loadingText = (TextView) rootView.getChildAt(1);
        int textColor = ResourcesCompat.getColor(activity.getResources(), R.color.white_ff, null);
        loadingText.setTextColor(textColor);
        dlg.show();
        return dlg;
    }

    public static Dialog showModalSpinner(final Context context, final int message) {
        final Dialog pg = ProgressDialog.show(context, null, context.getString(message));
        pg.setCancelable(false);

        return pg;
    }

    public static Dialog showConfirm(final Context context, final CharSequence title, final CharSequence message) {
        @SuppressLint("InflateParams")
        View contentView = LayoutInflater.from(context).inflate(R.layout.partial_dialogue_info_list, null);
        TextView tvTitle = ButterKnife.findById(contentView, R.id.txt_dialogue_info_title);
        View separator = ButterKnife.findById(contentView, R.id.txt_dialogue_info_separator);
        TextView tvMessage = ButterKnife.findById(contentView, R.id.txt_dialogue_info_msg);

        setTextResourceInternal(tvTitle, title);
        setSeparatorInternal(separator, 0, title);
        setTextResourceInternal(tvMessage, message);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(contentView)
                .setPositiveButton(R.string.text_confirm, null)
                .create();
        dialog.show();
        return dialog;
    }

    public static Dialog showSingleChoice(final Context context, final CharSequence title,
                                          final List<CharSequence> dataList, final int defaultSelection,
                                          final Consumer<Integer> onConfirmListener) {
        @SuppressLint("InflateParams")
        View contentView = LayoutInflater.from(context).inflate(R.layout.partial_dialogue_info_list, null);
        TextView tvTitle = ButterKnife.findById(contentView, R.id.txt_dialogue_info_title);
        View separator = ButterKnife.findById(contentView, R.id.txt_dialogue_info_separator);
        ListView lvTexts = ButterKnife.findById(contentView, R.id.list_dialogue_info_txtlist);
        RecyclerView lvItems = ButterKnife.findById(contentView, R.id.list_dialogue_info_itemlist);
        TextView tvMessage = ButterKnife.findById(contentView, R.id.txt_dialogue_info_msg);

        setTextResourceInternal(tvTitle, title);
        setSeparatorInternal(separator, 0, title);
        tvMessage.setVisibility(View.GONE);
        lvItems.setVisibility(View.VISIBLE);
        lvTexts.setVisibility(View.GONE);

        final SingleChoiceListAdapter lvAdapt = new SingleChoiceListAdapter(dataList, defaultSelection);
        LinearLayoutManager llMgr = new LinearLayoutManager(context);
        lvItems.setLayoutManager(llMgr);
        lvItems.addItemDecoration(new DividerItemDecoration(lvItems.getContext(), llMgr.getOrientation()));
        lvItems.setAdapter(lvAdapt);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(contentView)
                .setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                        try {
                            onConfirmListener.accept(lvAdapt.getCurrentSelection());
                        } catch (Exception e) {
                            DialogHelper.showException(context, e);
                        }
                    }
                })
                .setNegativeButton(R.string.text_cancel, null)
                .create();
        dialog.show();
        return dialog;
    }

    public static Dialog showYesNo(final Context context, final CharSequence title, final CharSequence message,
                                   DialogInterface.OnClickListener yesCallback) {
        return showYesNoInternal(context, 0, title, message,
                null, yesCallback, null);
    }

    public static Dialog showYesNo(final Context context, final int title, final int message,
                                   final CharSequence[] infoList,
                                   final DialogInterface.OnClickListener yesCallback) {
        return showYesNoInternal(context, 0, title, message, infoList, yesCallback, null);
    }

    private static Dialog showYesNoInternal(final Context context, final int icon, final int title,
                                            final int message, final CharSequence[] infoList,
                                            final DialogInterface.OnClickListener yesCallback,
                                            final DialogInterface.OnClickListener noCallback) {
        return showYesNoInternal(context, icon, context.getString(title), context.getString(message),
                infoList, yesCallback, noCallback);
    }

    private static Dialog showYesNoInternal(final Context context, int icon, CharSequence title,
                                            final CharSequence message, final CharSequence[] infoList,
                                            final DialogInterface.OnClickListener yesCallback,
                                            final DialogInterface.OnClickListener noCallback) {
        @SuppressLint("InflateParams")
        View contentView = LayoutInflater.from(context).inflate(R.layout.partial_dialogue_info_list, null);
        ImageView ivIcon = ButterKnife.findById(contentView, R.id.img_dialogue_info_title);
        TextView tvTitle = ButterKnife.findById(contentView, R.id.txt_dialogue_info_title);
        View separator = ButterKnife.findById(contentView, R.id.txt_dialogue_info_separator);
        TextView tvMessage = ButterKnife.findById(contentView, R.id.txt_dialogue_info_msg);
        ListView lvTexts = ButterKnife.findById(contentView, R.id.list_dialogue_info_txtlist);

        setImageResourceInternal(ivIcon, icon);
        setTextResourceInternal(tvTitle, title);
        setSeparatorInternal(separator, icon, title);
        setTextResourceInternal(tvMessage, message);
        setTextListInternal(lvTexts, infoList);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(contentView)
                .setPositiveButton(R.string.text_confirm, yesCallback)
                .setNegativeButton(R.string.text_cancel, noCallback)
                .create();
        dialog.show();
        return dialog;
    }

    public static Dialog showException(final Context context, final Throwable t) {
        return showExceptionInternal(context, t.getMessage(), t, null);
    }

    public static Dialog showException(final Context context, final Throwable t, final Runnable onClickButton) {
        return showExceptionInternal(context, t.getMessage(), t, onClickButton);
    }

    public static Dialog showPluginException(final Context context, final Throwable t) {
        return showExceptionInternal(context, ResourceUtils.getString(R.string.err_unexpected_exception_plugin), t, null);
    }

    public static Dialog showPluginException(final Context context, final Throwable t, final Runnable onClickButton) {
        return showExceptionInternal(context, ResourceUtils.getString(R.string.err_unexpected_exception_plugin), t, onClickButton);
    }

    private static Dialog showExceptionInternal(final Context context, final CharSequence message,
                                                final Throwable t, final Runnable onClickButton) {
        @SuppressLint("InflateParams")
        View contentView = LayoutInflater.from(context).inflate(R.layout.partial_dialogue_exception, null);
        ImageView ivIcon = ButterKnife.findById(contentView, R.id.img_dialogue_exception_title);
        TextView tvTitle = ButterKnife.findById(contentView, R.id.txt_dialogue_exception_title);
        TextView tvMessage = ButterKnife.findById(contentView, R.id.txt_dialogue_exception_msg);
        ListView lvTrace = ButterKnife.findById(contentView, R.id.list_dialogue_exception_trace);
        ivIcon.setImageResource(R.drawable.ico_dialog_alert);
        tvTitle.setText(R.string.msg_problem_occurred);
        if (TextUtils.isEmpty(message)) {
            tvMessage.setText(t.toString());
        } else {
            tvMessage.setText(message);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(contentView)
                .setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != onClickButton) {
                            onClickButton.run();
                        }
                    }
                });

        if (null == t) {
            lvTrace.setVisibility(View.GONE);
        } else {
            lvTrace.setVisibility(View.VISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            t.printStackTrace(ps);
            final String trace = new String(baos.toByteArray(), Charset.defaultCharset());
            lvTrace.setAdapter(new ArrayAdapter<>(context, R.layout.item_exception_trace_text, trace.split("\n")));
            IOUtils.closeQuietly(ps);
            IOUtils.closeQuietly(baos);

            dialogBuilder.setNeutralButton(R.string.text_copy_to_clipboard, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(ResourceUtils.getString(R.string.text_mediamonkey_exception_trace), trace);
                    clipboard.setPrimaryClip(clip);
                    ToastHelper.show(R.string.msg_copied_to_clipboard, Toast.LENGTH_SHORT);
                }
            });
        }

        Dialog dialog = dialogBuilder.create();
        dialog.show();
        return dialog;
    }

    private static void setImageResourceInternal(final ImageView iv, final int imgRes) {
        if (0 == imgRes) {
            iv.setVisibility(View.GONE);
        } else {
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(imgRes);
        }
    }

    private static void setTextResourceInternal(final TextView tv, final CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    private static void setSeparatorInternal(final View separatorView, final int iconRes, final CharSequence title) {
        if (0 == iconRes && TextUtils.isEmpty(title)) {
            separatorView.setVisibility(View.GONE);
        } else {
            separatorView.setVisibility(View.VISIBLE);
        }
    }

    private static void setTextListInternal(final ListView lv, final CharSequence[] infoList) {
        if (EmptyCheckUtils.isEmpty(infoList)) {
            lv.setVisibility(View.GONE);
        } else {
            lv.setVisibility(View.VISIBLE);
            lv.setAdapter(new ArrayAdapter<>(lv.getContext(),
                    android.R.layout.simple_list_item_1, infoList));
        }
    }

    private static class SingleChoiceListAdapter extends RecyclerView.Adapter<SingleChoiceListAdapter.ViewHolder>
            implements View.OnClickListener {
        private final List<CharSequence> dataList;
        private int currentSelection;

        SingleChoiceListAdapter(final List<CharSequence> dataList, final int defaultSelection) {
            this.dataList = dataList;
            this.currentSelection = defaultSelection;
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            Context ctx = parent.getContext();
            LayoutInflater li = LayoutInflater.from(ctx);
            View rootView = li.inflate(R.layout.item_dialogue_single_choice, parent, false);
            ViewHolder vh = new ViewHolder(rootView);
            vh.rootView.setOnClickListener(this);
            vh.btnRadio.setOnClickListener(this);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.draw(position, position == currentSelection, dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        int getCurrentSelection() {
            return currentSelection;
        }

        @Override
        public void onClick(final View v) {
            int oldPosition = currentSelection;
            ViewHolder vh = (ViewHolder) v.getTag();
            this.currentSelection = vh.position;
            notifyItemChanged(oldPosition);
            notifyItemChanged(currentSelection);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private final View        rootView;
            private final RadioButton btnRadio;
            private final TextView    tvText;

            int position;

            ViewHolder(final View rootView) {
                super(rootView);
                this.rootView = rootView;
                this.btnRadio = ButterKnife.findById(rootView, R.id.btn_item_dialogue_single_choice);
                this.tvText   = ButterKnife.findById(rootView, R.id.txt_item_dialogue_single_choice);
            }

            void draw(final int position, final boolean isSelected, final CharSequence text) {
                btnRadio.setTag(this);
                rootView.setTag(this);
                this.position = position;
                btnRadio.setChecked(isSelected);
                tvText.setText(text);
            }
        }
    }

    private DialogHelper() { }
}
