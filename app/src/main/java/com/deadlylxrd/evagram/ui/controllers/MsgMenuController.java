package com.deadlylxrd.evagram.ui.controllers;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;

import com.deadlylxrd.evagram.EvaSettings;

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

public class MsgMenuController extends RecyclerViewController<Void> implements View.OnClickListener, ViewController.SettingsIntDelegate {
  private SettingsAdapter adapter;

  public MsgMenuController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public CharSequence getName () {
    return Lang.getString(R.string.MsgMenuSettings);
  }

  @Override public void onClick (View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_messageReply:
        EvaSettings.instance().toggleMsgReply();
        adapter.updateValuedSettingById(R.id.btn_messageReply);
        break;
      case R.id.btn_messageShare:
        EvaSettings.instance().toggleMsgRepost();
        adapter.updateValuedSettingById(R.id.btn_messageShare);
        break;
      case R.id.btn_messageRepeat:
        EvaSettings.instance().toggleMsgRepeat();
        adapter.updateValuedSettingById(R.id.btn_messageRepeat);
        break;
      case R.id.btn_messagePin:
        EvaSettings.instance().toggleMsgPin();
        adapter.updateValuedSettingById(R.id.btn_messagePin);
        break;
      case R.id.btn_messageEdit:
        EvaSettings.instance().toggleMsgEdit();
        adapter.updateValuedSettingById(R.id.btn_messageEdit);
        break;
      case R.id.btn_messageCopyLink:
        EvaSettings.instance().toggleMsgLink();
        adapter.updateValuedSettingById(R.id.btn_messageCopyLink);
        break;
      case R.id.btn_messageCopy:
        EvaSettings.instance().toggleMsgCopy();
        adapter.updateValuedSettingById(R.id.btn_messageCopy);
        break;
      case R.id.btn_messageDelete:
        EvaSettings.instance().toggleMsgDel();
        adapter.updateValuedSettingById(R.id.btn_messageDelete);
        break;
      case R.id.btn_saveFile:
        EvaSettings.instance().toggleMsgSave();
        adapter.updateValuedSettingById(R.id.btn_saveFile);
        break;
      case R.id.btn_viewStatistics:
        EvaSettings.instance().toggleMsgStats();
        adapter.updateValuedSettingById(R.id.btn_viewStatistics);
        break;
      case R.id.btn_messageMore:
        EvaSettings.instance().toggleMsgOther();
        adapter.updateValuedSettingById(R.id.btn_messageMore);
    }
  }

  @Override public void onApplySettings (int id, SparseIntArray result) {
    // Do nothing.
  }

  @Override public int getId () {
    return R.id.controller_msgMenuSettings;
  }

  @Override protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override protected void setValuedSetting (ListItem item, SettingView view, boolean isUpdate) {
        view.setDrawModifier(item.getDrawModifier());
        switch (item.getId()) {
          case R.id.btn_messageReply:
            view.setChecked(EvaSettings.instance().isMsgReply(), isUpdate);
            break;
          case R.id.btn_messageShare:
            view.setChecked(EvaSettings.instance().isMsgRepost(), isUpdate);
            break;
          case R.id.btn_messageRepeat:
            view.setChecked(EvaSettings.instance().isMsgRepeat(), isUpdate);
            break;
          case R.id.btn_messagePin:
            view.setChecked(EvaSettings.instance().isMsgPin(), isUpdate);
            break;
          case R.id.btn_messageEdit:
            view.setChecked(EvaSettings.instance().isMsgEdit(), isUpdate);
            break;
          case R.id.btn_messageCopyLink:
            view.setChecked(EvaSettings.instance().isMsgLink(), isUpdate);
            break;
          case R.id.btn_messageCopy:
            view.setChecked(EvaSettings.instance().isMsgCopy(), isUpdate);
            break;
          case R.id.btn_messageDelete:
            view.setChecked(EvaSettings.instance().isMsgDel(), isUpdate);
            break;
          case R.id.btn_saveFile:
            view.setChecked(EvaSettings.instance().isMsgSave(), isUpdate);
            break;
          case R.id.btn_viewStatistics:
            view.setChecked(EvaSettings.instance().isMsgStats(), isUpdate);
            break;
          case R.id.btn_messageMore:
            view.setChecked(EvaSettings.instance().isMsgOther(), isUpdate);
            break;
        }
      }
    };

    ArrayList<ListItem> items = new ArrayList<>();

    items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));
    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.MsgMenuSettingsDesc));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messageReply, 0, R.string.Reply));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messageRepeat, 0, R.string.MessageRepeat));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messageShare, 0, R.string.Share));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messagePin, 0, R.string.MessagePin));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messageEdit, 0, R.string.edit));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messageCopyLink, 0, R.string.CopyLink));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messageCopy, 0, R.string.Copy));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messageDelete, 0, R.string.Delete));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_saveFile, 0, R.string.Save));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_viewStatistics, 0, R.string.ViewStats));
    items.add(new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_messageMore, 0, R.string.MoreMessageOptions));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    adapter.setItems(items, true);
    recyclerView.setAdapter(adapter);
  }
}
