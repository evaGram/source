package com.deadlylxrd.evagram.ui.controllers;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;

import com.deadlylxrd.evagram.EvaConfig;
import com.deadlylxrd.evagram.EvaSettings;
import com.deadlylxrd.evagram.ui.controllers.SettingsGeneralController;
import com.deadlylxrd.evagram.ui.controllers.SettingsAppearanceController;
import com.deadlylxrd.evagram.ui.controllers.SettingsChatsController;
import com.deadlylxrd.evagram.ui.controllers.SettingsExperimentalController;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.navigation.SettingsWrapBuilder;
import org.thunderdog.challegram.navigation.ViewController;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.telegram.TdlibUi;
import org.thunderdog.challegram.tool.Strings;
import org.thunderdog.challegram.ui.ListItem;
import org.thunderdog.challegram.ui.RecyclerViewController;
import org.thunderdog.challegram.ui.SettingsAdapter;
import org.thunderdog.challegram.unsorted.Settings;
import org.thunderdog.challegram.util.AppUpdater;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;

public class SettingsEvaController extends RecyclerViewController<Void> implements View.OnClickListener, ViewController.SettingsIntDelegate, AppUpdater.Listener {
  private SettingsAdapter adapter;

  public SettingsEvaController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override
  public void destroy () {
    super.destroy();
    context().appUpdater().removeListener(this);
  }

  @Override public CharSequence getName () {
    return Lang.getString(R.string.EvaSettings);
  }

  @Override
  public void onAppUpdateStateChanged (int state, int oldState, boolean isApk) {
    if (oldState == AppUpdater.State.CHECKING && state == AppUpdater.State.NONE) {
      // Slight delay
      runOnUiThread(() ->
        adapter.updateValuedSettingById(R.id.btn_checkUpdates),
        250
      );
    } else {
      adapter.updateValuedSettingById(R.id.btn_checkUpdates);
    }
  }

  @Override
  public void onAppUpdateDownloadProgress (long bytesDownloaded, long totalBytesToDownload) {
    adapter.updateValuedSettingById(R.id.btn_checkUpdates);
  }

  private void showUpdateOptions () {
    int autoUpdateMode = Settings.instance().getAutoUpdateMode();
    showSettings(new SettingsWrapBuilder(R.id.btn_updateAutomatically).setRawItems(new ListItem[] {
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_updateAutomaticallyPrompt, 0, R.string.AutoUpdatePrompt, R.id.btn_updateAutomatically, autoUpdateMode == Settings.AUTO_UPDATE_MODE_PROMPT),
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_updateAutomaticallyAlways, 0, R.string.AutoUpdateAlways, R.id.btn_updateAutomatically, autoUpdateMode == Settings.AUTO_UPDATE_MODE_ALWAYS),
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_updateAutomaticallyWiFi, 0, R.string.AutoUpdateWiFi, R.id.btn_updateAutomatically, autoUpdateMode == Settings.AUTO_UPDATE_MODE_WIFI_ONLY),
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_updateAutomaticallyNever, 0, R.string.AutoUpdateNever, R.id.btn_updateAutomatically, autoUpdateMode == Settings.AUTO_UPDATE_MODE_NEVER),
    }).setAllowResize(false).setIntDelegate((id, result) -> {
      int autoUpdateMode1 = Settings.instance().getAutoUpdateMode();
      int autoUpdateResult = result.get(R.id.btn_updateAutomatically);
      boolean shouldChangeUi = (autoUpdateMode1 == Settings.AUTO_UPDATE_MODE_NEVER && autoUpdateResult != R.id.btn_updateAutomaticallyNever) || (autoUpdateMode1 != Settings.AUTO_UPDATE_MODE_NEVER && autoUpdateResult == R.id.btn_updateAutomaticallyNever);
      switch (autoUpdateResult) {
        case R.id.btn_updateAutomaticallyAlways:
          autoUpdateMode1 = Settings.AUTO_UPDATE_MODE_ALWAYS;
          break;
        case R.id.btn_updateAutomaticallyWiFi:
          autoUpdateMode1 = Settings.AUTO_UPDATE_MODE_WIFI_ONLY;
          break;
        case R.id.btn_updateAutomaticallyPrompt:
          autoUpdateMode1 = Settings.AUTO_UPDATE_MODE_PROMPT;
          break;
        case R.id.btn_updateAutomaticallyNever:
          autoUpdateMode1 = Settings.AUTO_UPDATE_MODE_NEVER;
          break;
      }
      Settings.instance().setAutoUpdateMode(autoUpdateMode1);
      adapter.updateValuedSettingById(R.id.btn_checkUpdates);
    }));
  }

  @Override public void onClick (View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_generalSettings:
        navigateTo(new SettingsGeneralController(context, tdlib));
        break;
      case R.id.btn_appearanceSettings:
        navigateTo(new SettingsAppearanceController(context, tdlib));
        break;
      case R.id.btn_chatsSettings:
        navigateTo(new SettingsChatsController(context, tdlib));
        break;
      case R.id.btn_experimentalSettings:
        navigateTo(new SettingsExperimentalController(context, tdlib));
        break;
      case R.id.btn_tgChannel: {
        tdlib.ui().openUrl(this, EvaConfig.CHANNEL_LINK, new TdlibUi.UrlOpenParameters().forceInstantView());
        break;
      }
      case R.id.btn_sourceCode: {
        tdlib.ui().openUrl(this, EvaConfig.SOURCE_CODE_LINK, new TdlibUi.UrlOpenParameters().forceInstantView());
        break;
      }
      case R.id.btn_evaDev: {
        tdlib.ui().openUrl(this, EvaConfig.DEV_LINK, new TdlibUi.UrlOpenParameters().forceInstantView());
        break;
      }
      case R.id.btn_supportProject: {
        tdlib.ui().openUrl(this, EvaConfig.DONATE_LINK, new TdlibUi.UrlOpenParameters().forceInstantView());
        break;
      }
      case R.id.btn_checkUpdates: {
        switch (context().appUpdater().state()) {
          case AppUpdater.State.NONE: {
            context().appUpdater().checkForUpdates();
            break;
          }
          case AppUpdater.State.CHECKING:
          case AppUpdater.State.DOWNLOADING: {
            // Do nothing.
            break;
          }
          case AppUpdater.State.AVAILABLE: {
            context().appUpdater().downloadUpdate();
            break;
          }
          case AppUpdater.State.READY_TO_INSTALL: {
            context().appUpdater().installUpdate();
            break;
          }
        }
        break;
      }
      case R.id.btn_updateAutomatically: {
        showUpdateOptions();
        break;
      }
    }
  }

  @Override public void onApplySettings (int id, SparseIntArray result) {
    // Do nothing.
  }

  @Override public int getId () {
    return R.id.controller_evaSettings;
  }

  @Override protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override protected void setValuedSetting (ListItem item, SettingView view, boolean isUpdate) {
        view.setDrawModifier(item.getDrawModifier());
        switch (item.getId()) {
          case R.id.btn_tgChannel:
            view.setData(EvaConfig.CHANNEL_USERNAME);
            break;
          case R.id.btn_sourceCode:
            view.setData(EvaConfig.SOURCE_CODE_LINK);
            break;
          case R.id.btn_evaDev:
            view.setData(EvaConfig.EVA_DEV);
            break;
          case R.id.btn_supportProject:
            view.setData(R.string.SupportProjectDesc);
            break;
          case R.id.btn_updateAutomatically: {
            int mode = Settings.instance().getAutoUpdateMode();
            view.getToggler().setRadioEnabled(mode != Settings.AUTO_UPDATE_MODE_NEVER, isUpdate);
            switch (mode) {
              case Settings.AUTO_UPDATE_MODE_NEVER:
                view.setData(R.string.AutoUpdateNever);
                break;
              case Settings.AUTO_UPDATE_MODE_ALWAYS:
                view.setData(R.string.AutoUpdateAlways);
                break;
              case Settings.AUTO_UPDATE_MODE_WIFI_ONLY:
                view.setData(R.string.AutoUpdateWiFi);
                break;
              case Settings.AUTO_UPDATE_MODE_PROMPT:
                view.setData(R.string.AutoUpdatePrompt);
                break;
            }
            break;
          }
          case R.id.btn_checkUpdates: {
            switch (context().appUpdater().state()) {
              case AppUpdater.State.NONE: {
                view.setEnabledAnimated(true, isUpdate);
                view.setName(R.string.CheckForUpdates);
                break;
              }
              case AppUpdater.State.CHECKING: {
                view.setEnabledAnimated(false, isUpdate);
                view.setName(R.string.CheckingForUpdates);
                break;
              }
              case AppUpdater.State.AVAILABLE: {
                view.setEnabledAnimated(true, isUpdate);
                long bytesToDownload = context().appUpdater().totalBytesToDownload() - context().appUpdater().bytesDownloaded();
                if (bytesToDownload > 0) {
                  view.setName(Lang.getStringBold(R.string.DownloadUpdateSize, Strings.buildSize(bytesToDownload)));
                } else {
                  view.setName(R.string.DownloadUpdate);
                }
                break;
              }
              case AppUpdater.State.DOWNLOADING: {
                view.setEnabledAnimated(false, isUpdate);
                view.setName(Lang.getDownloadProgress(context().appUpdater().bytesDownloaded(), context().appUpdater().totalBytesToDownload(), true));
                break;
              }
              case AppUpdater.State.READY_TO_INSTALL: {
                view.setEnabledAnimated(true, isUpdate);
                view.setName(R.string.InstallUpdate);
                break;
              }
            }
            break;
          }
        }
      }
    };

    ArrayList<ListItem> items = new ArrayList<>();

    items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));
    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.EvaSettingsDesc));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_generalSettings, R.drawable.baseline_widgets_24, R.string.GeneralSettings));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_appearanceSettings, R.drawable.baseline_palette_24, R.string.AppearanceSettings));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_chatsSettings, R.drawable.baseline_chat_bubble_24, R.string.ChatsSettings));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_experimentalSettings, R.drawable.baseline_bug_report_24, R.string.ExperimentalSettings));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.EvaInfo));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_tgChannel, R.drawable.baseline_help_24, R.string.TgChannel));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_sourceCode, R.drawable.baseline_github_24, R.string.EvaSourceCode));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.EvaLinks));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_evaDev, R.drawable.baseline_code_24, R.string.EvaDev));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_supportProject, R.drawable.baseline_paid_24, R.string.SupportProject));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.AppUpdates));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT_WITH_TOGGLER, R.id.btn_updateAutomatically, 0, R.string.AutoUpdate));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_checkUpdates, 0, R.string.CheckForUpdates));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    adapter.setItems(items, true);
    recyclerView.setAdapter(adapter);
  }
}
