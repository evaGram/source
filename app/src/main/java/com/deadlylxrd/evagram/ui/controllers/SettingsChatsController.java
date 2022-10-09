package com.deadlylxrd.evagram.ui.controllers;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;

import com.deadlylxrd.evagram.EvaSettings;
import com.deadlylxrd.evagram.ui.controllers.MsgMenuController;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.navigation.ViewController;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.telegram.TdlibUi;
import org.thunderdog.challegram.ui.ListItem;
import org.thunderdog.challegram.ui.RecyclerViewController;
import org.thunderdog.challegram.ui.SettingsAdapter;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;

public class SettingsChatsController extends RecyclerViewController<Void> implements View.OnClickListener, ViewController.SettingsIntDelegate {
  private SettingsAdapter adapter;

  public SettingsChatsController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public CharSequence getName () {
    return Lang.getString(R.string.ChatsSettings);
  }

  @Override public void onClick (View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_disableCameraButton:
        EvaSettings.instance().toggleDisableCameraButton();
        adapter.updateValuedSettingById(R.id.btn_disableCameraButton);
        break;
      case R.id.btn_disableRecordButton:
        EvaSettings.instance().toggleDisableRecordButton();
        adapter.updateValuedSettingById(R.id.btn_disableRecordButton);
        break;
      case R.id.btn_rememberSendOptions:
        EvaSettings.instance().toggleRememberSendOptions();
        adapter.updateValuedSettingById(R.id.btn_rememberSendOptions);
        break;
      case R.id.btn_disableReactions:
        EvaSettings.instance().toggleDisableReactions();
        adapter.updateValuedSettingById(R.id.btn_disableReactions);
        break;
      case R.id.btn_msgMenuSettings:
        navigateTo(new MsgMenuController(context, tdlib));
        break;
    }
  }

  @Override public void onApplySettings (int id, SparseIntArray result) {
    // Do nothing.
  }

  @Override public int getId () {
    return R.id.controller_chatsSettings;
  }

  @Override protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override protected void setValuedSetting (ListItem item, SettingView view, boolean isUpdate) {
        view.setDrawModifier(item.getDrawModifier());
        switch (item.getId()) {
          case R.id.btn_disableCameraButton:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isDisableCameraButton(), isUpdate);
            break;
          case R.id.btn_disableRecordButton:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isDisableRecordButton(), isUpdate);
            break;
          case R.id.btn_rememberSendOptions:
	    view.getToggler().setRadioEnabled(EvaSettings.instance().isRememberSendOptions(), isUpdate);
	    break;
          case R.id.btn_disableReactions:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isReactionsDisabled(), isUpdate);
            break;
        }
      }
    };

    ArrayList<ListItem> items = new ArrayList<>();

    items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));
    // items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.ChatsSettingsDesc));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_disableCameraButton, 0, R.string.DisableCameraButton));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_disableRecordButton, 0, R.string.DisableRecordButton));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_rememberSendOptions, 0, R.string.RememberSendOptions));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.RememberSendOptionsDesc));
    
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_disableReactions, 0, R.string.DisableReactions));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_msgMenuSettings, 0, R.string.MsgMenuSettings));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    adapter.setItems(items, true);
    recyclerView.setAdapter(adapter);
  }
}

