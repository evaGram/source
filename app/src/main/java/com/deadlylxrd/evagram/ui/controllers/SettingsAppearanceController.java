package com.deadlylxrd.evagram.ui.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.SparseIntArray;
import android.view.View;

import com.deadlylxrd.evagram.EvaSettings;
import com.deadlylxrd.evagram.ui.controllers.CustomDrawerController;
import com.deadlylxrd.evagram.utils.LifecycleUtils;

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

public class SettingsAppearanceController extends RecyclerViewController<Void> implements View.OnClickListener, ViewController.SettingsIntDelegate {
  private SettingsAdapter adapter;

  public SettingsAppearanceController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public CharSequence getName () {
    return Lang.getString(R.string.AppearanceSettings);
  }

  @Override public void onClick (View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_hideNumber:
        EvaSettings.instance().toggleHideNumber();
        adapter.updateValuedSettingById(R.id.btn_hideNumber);
        LifecycleUtils.restartApp();
        break;
      case R.id.btn_enableComments:
        EvaSettings.instance().toggleEnableComments();
        adapter.updateValuedSettingById(R.id.btn_enableComments);
        break;
      case R.id.btn_drawerSettings:
        navigateTo(new CustomDrawerController(context, tdlib));
        break;
    }
  }

  @Override public void onApplySettings (int id, SparseIntArray result) {
    // Do nothing.
  }

  @Override public int getId () {
    return R.id.controller_appearanceSettings;
  }

  @Override protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override protected void setValuedSetting (ListItem item, SettingView view, boolean isUpdate) {
        view.setDrawModifier(item.getDrawModifier());
        switch (item.getId()) {
          case R.id.btn_hideNumber:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isNumberHidden(), isUpdate);
            break;
          case R.id.btn_enableComments:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isCommentsEnabled(), isUpdate);
            break;
        }
      }
    };

    ArrayList<ListItem> items = new ArrayList<>();

    items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideNumber, 0, R.string.HideNumber));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.HideNumberDesc));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_enableComments, 0, R.string.EnableComments));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.EnableCommentsDesc));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_drawerSettings, 0, R.string.DrawerSettings));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    adapter.setItems(items, true);
    recyclerView.setAdapter(adapter);
  }
}
