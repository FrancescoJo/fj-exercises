/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.settings.plugin;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui.common.DialogHelper;
import com.fj.android.mediamonkey.util.PlatformUtils;
import com.mediamonkey.android.lib.dto.settings.MenuDisplay;
import com.mediamonkey.android.lib.dto.settings.MenuGroupDto;
import com.mediamonkey.android.lib.dto.settings.MenuItemDto;
import com.mediamonkey.android.lib.dto.settings.MenuValueDto;
import com.mediamonkey.android.plugin.PluginSettingsService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Dec - 2016
 */
class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.MenuViewHolder> {
    private final PluginSettingsPresenter        presenter;
    private final PluginSettingsService          pluginSettingsService;
    private final List<MenuDisplay>              menuDisplayList;
    private final Map<MenuItemDto, MenuValueDto> menuValueMap;

    SettingsListAdapter(final PluginSettingsPresenter presenter,
                        final PluginSettingsService pluginSettingsService) {
        this.presenter             = presenter;
        this.pluginSettingsService = pluginSettingsService;
        this.menuDisplayList       = new ArrayList<>();
        this.menuValueMap          = new HashMap<>();

        for (MenuDisplay group : pluginSettingsService.getMenus()) {
            menuDisplayList.add(group);
            for (MenuItemDto item : ((MenuGroupDto) group).getMenuItems()) {
                menuDisplayList.add(item);
                MenuValueDto value = pluginSettingsService.getMenuValueOf(item);
                menuValueMap.put(item, value);
            }
        }
    }

    @Override
    public int getItemViewType(final int position) {
        DisplayType type = DisplayType.byDisplayType(menuDisplayList.get(position));
        return type.viewType;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        DisplayType type = DisplayType.byViewType(viewType);
        return type.newViewHolder(presenter, parent);
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder holder, final int position) {
        holder.draw(pluginSettingsService, menuValueMap, menuDisplayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return menuDisplayList.size();
    }

    void notifyItemUpdated(final MenuItemDto menuItem) {
        for (int i = 0, limit = menuDisplayList.size(); i < limit; i++) {
            MenuDisplay menuDisplay = menuDisplayList.get(i);
            if (menuDisplay == menuItem) {
                notifyItemChanged(i);
            }
        }
    }

    enum DisplayType {
        GROUP(1) {
            @Override
            MenuViewHolder newViewHolder(final PluginSettingsPresenter presenter,
                                         final ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new GroupViewHolder(inflater.inflate(R.layout.item_settings_group, parent, false));
            }
        },
        ITEM(2) {
            @Override
            MenuViewHolder newViewHolder(final PluginSettingsPresenter presenter,
                                         final ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new ItemViewHolder(presenter, inflater.inflate(R.layout.item_settings_item, parent, false));
            }
        };

        final int viewType;

        DisplayType(final int i1) {
            this.viewType = i1;
        }

        static DisplayType byViewType(final int type) {
            if (GROUP.viewType == type) {
                return GROUP;
            } else {
                return ITEM;
            }
        }

        static DisplayType byDisplayType(final MenuDisplay display) {
            if (display instanceof MenuGroupDto) {
                return GROUP;
            } else {
                return ITEM;
            }
        }

        abstract MenuViewHolder newViewHolder(final PluginSettingsPresenter presenter,
                                              final ViewGroup parent);
    }

    static abstract class MenuViewHolder extends RecyclerView.ViewHolder {
        MenuViewHolder(final View itemView) {
            super(itemView);
        }

        abstract void draw(final PluginSettingsService pluginSettingsService,
                           final Map<MenuItemDto, MenuValueDto> menuValueMap,
                           final MenuDisplay display, final int position);
    }

    private static class GroupViewHolder extends MenuViewHolder {
        private final View     rootView;
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final Space    gap;

        GroupViewHolder(final View rootView) {
            super(rootView);
            this.rootView      = rootView;
            this.gap           = ButterKnife.findById(rootView, R.id.txt_setting_group_gap);
            this.tvTitle       = ButterKnife.findById(rootView, R.id.txt_setting_group_name);
            this.tvDescription = ButterKnife.findById(rootView, R.id.txt_setting_group_description);
            this.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuGroupDto group = (MenuGroupDto) GroupViewHolder.this.rootView.getTag();
                    DialogHelper.showConfirm(v.getContext(), group.getTitle(), group.getDescription());
                }
            });
        }

        @Override
        void draw(final PluginSettingsService pluginSettingsService,
                  final Map<MenuItemDto, MenuValueDto> menuValueMap,
                  final MenuDisplay display, final int position) {
            MenuGroupDto group = (MenuGroupDto) display;
            rootView.setTag(group);

            if (0 == position) {
                gap.setVisibility(View.GONE);
            } else {
                gap.setVisibility(View.VISIBLE);
            }

            tvTitle.setText(group.getTitle());
            tvDescription.setText(group.getDescription());
            boolean isEnabled = pluginSettingsService.isEnabled(display);
            rootView.setEnabled(isEnabled);
            tvTitle.setEnabled(isEnabled);
            tvDescription.setEnabled(isEnabled);
        }
    }

    private static class ItemViewHolder extends MenuViewHolder {
        private final PluginSettingsPresenter presenter;
        private final View                    rootView;
        private final TextView                tvTitle;
        private final TextView                tvDescription;
        private final Switch                  valueSwitch;
        private final TextView                valueText;

        ItemViewHolder(final PluginSettingsPresenter presenter, final View rootView) {
            super(rootView);
            this.presenter     = presenter;
            this.rootView      = rootView;
            this.tvTitle       = ButterKnife.findById(rootView, R.id.txt_setting_item_name);
            this.tvDescription = ButterKnife.findById(rootView, R.id.txt_setting_item_description);
            this.valueSwitch   = ButterKnife.findById(rootView, R.id.txt_setting_item_value_switch);
            this.valueText     = ButterKnife.findById(rootView, R.id.txt_setting_item_value_text);
            View.OnClickListener clickEventHandler = new View.OnClickListener() {
                @Override
                public void onClick(final View menuView) {
                    MenuItemDto item = (MenuItemDto) rootView.getTag();
                    ItemViewHolder.this.presenter.onClickMenuItem(item);
                }
            };
            this.rootView.setOnClickListener(clickEventHandler);
            valueSwitch.setOnClickListener(clickEventHandler);
        }

        @Override
        void draw(final PluginSettingsService pluginSettingsService,
                  final Map<MenuItemDto, MenuValueDto> menuValueMap,
                  final MenuDisplay display, final int position) {
            MenuItemDto item = (MenuItemDto) display;
            rootView.setTag(item);

            tvTitle.setText(item.getTitle());
            if (!TextUtils.isEmpty(item.getDescription())) {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(item.getDescription());
            } else {
                tvDescription.setVisibility(View.GONE);
            }

            MenuValueDto valueDto = menuValueMap.get(item);
            switch (valueDto.getType()) {
                case BOOLEAN:
                    valueSwitch.setVisibility(View.VISIBLE);
                    valueSwitch.setChecked((boolean) valueDto.getValue());
                    valueText.setVisibility(View.GONE);
                    break;
                case INTEGER:
                case DOUBLE:
                    valueSwitch.setVisibility(View.GONE);
                    valueText.setVisibility(View.VISIBLE);
                    Locale currentLocale = PlatformUtils.getCurrentLocale();
                    NumberFormat nf = NumberFormat.getNumberInstance(currentLocale);
                    valueText.setText(nf.format(valueDto.getValue()));
                    break;
                case STRING:
                    valueSwitch.setVisibility(View.GONE);
                    valueText.setVisibility(View.VISIBLE);
            }

            boolean isEnabled = pluginSettingsService.isEnabled(display);
            rootView.setEnabled(isEnabled);
            tvTitle.setEnabled(isEnabled);
            tvDescription.setEnabled(isEnabled);
            valueText.setEnabled(isEnabled);
        }
    }
}
