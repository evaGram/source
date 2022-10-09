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

public class SettingsGeneralController extends RecyclerViewController<Void> implements View.OnClickListener, ViewController.SettingsIntDelegate {
  private SettingsAdapter adapter;

  public SettingsGeneralController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public CharSequence getName () {
    return Lang.getString(R.string.GeneralSettings);
  }

  @Override public void onClick (View v) {
    int id = v.getId();
    switch (id) {
        case R.id.btn_showChatId:
        EvaSettings.instance().toggleShowChatID();
        adapter.updateValuedSettingById(R.id.btn_showChatId);
        break;
    }
  }

  @Override public void onApplySettings (int id, SparseIntArray result) {
    // Do nothing.
  }

  @Override public int getId () {
    return R.id.controller_generalSettings;
  }

  @Override protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override protected void setValuedSetting (ListItem item, SettingView view, boolean isUpdate) {
        view.setDrawModifier(item.getDrawModifier());
        switch (item.getId()) {
            case R.id.btn_showChatId:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isChatIdShows(), isUpdate);
            break;
        }
      }
    };

    ArrayList<ListItem> items = new ArrayList<>();

    items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_showChatId, 0, R.string.ShowChatId));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    adapter.setItems(items, true);
    recyclerView.setAdapter(adapter);
  }
}
