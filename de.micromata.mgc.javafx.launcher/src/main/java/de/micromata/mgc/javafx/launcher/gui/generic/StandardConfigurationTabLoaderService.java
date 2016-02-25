package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.HibernateSchemaConfigModel;
import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.MailSessionLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.TabConfig;
import de.micromata.mgc.javafx.launcher.gui.jetty.JettyConfigTabController;
import de.micromata.mgc.jettystarter.JettyConfigModel;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class StandardConfigurationTabLoaderService implements ConfigurationTabLoaderService
{

  @Override
  public List<TabConfig> getTabsByConfiguration(CastableLocalSettingsConfigModel configModel)
  {
    List<TabConfig> ret = new ArrayList<>();
    JettyConfigModel jettyConfig = configModel.castToForConfigDialog(JettyConfigModel.class);
    if (jettyConfig != null) {
      ret.add(new TabConfig(JettyConfigTabController.class, jettyConfig));
    }

    LauncherLocalSettingsConfigModel launcherConfig = configModel
        .castToForConfigDialog(LauncherLocalSettingsConfigModel.class);
    if (launcherConfig != null) {
      ret.add(new TabConfig(LauncherConfigTabController.class, launcherConfig));
    }

    MailSessionLocalSettingsConfigModel emailConfig = configModel
        .castToForConfigDialog(MailSessionLocalSettingsConfigModel.class);
    if (emailConfig != null) {
      ret.add(new TabConfig(MailSessionConfigTabController.class, emailConfig));
    }
    JdbcLocalSettingsConfigModel jdbc = configModel.castToForConfigDialog(JdbcLocalSettingsConfigModel.class);
    if (jdbc != null) {
      ret.add(new TabConfig(JdbcConfigTabController.class, jdbc));
    }
    HibernateSchemaConfigModel hibernateConfig = configModel.castToForConfigDialog(HibernateSchemaConfigModel.class);
    if (hibernateConfig != null) {
      ret.add(new TabConfig(HibernateSchemaConfigTabController.class, hibernateConfig));
    }

    return ret;
  }

}