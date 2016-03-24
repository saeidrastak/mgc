package de.micromata.mgc.email.launcher;

import java.util.List;

import javax.mail.Provider;

import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValState;
import de.micromata.mgc.email.MailReceiveService;
import de.micromata.mgc.email.MailReceiverLocalSettingsConfigModel;
import de.micromata.mgc.email.MailReceiverServiceManager;
import de.micromata.mgc.javafx.ModelGuiField;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * GUI controller for MailReceiverLocalSettingsConfigModel.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmailReceiverController extends AbstractConfigTabController<MailReceiverLocalSettingsConfigModel>
{

  @FXML
  Button testButton;
  @FXML
  @ModelGuiField
  ChoiceBox<String> protocol;
  @FXML
  @ModelGuiField
  private CheckBox enableTLS;
  @FXML
  @ModelGuiField
  TextField host;
  @FXML
  @ModelGuiField
  TextField port;

  @FXML
  @ModelGuiField
  TextField user;
  @FXML
  @ModelGuiField
  TextField password;

  @FXML
  private CheckBox extended;

  @FXML
  @ModelGuiField
  private CheckBox enableSelfSignedCerts;
  @FXML
  @ModelGuiField
  ComboBox<String> defaultFolder;

  @Override
  public void initializeWithModel()
  {
    protocol.setItems(FXCollections
        .observableList(MailReceiverServiceManager.get().getMailReceiveService().getProviders(Provider.Type.STORE)));
    super.initializeWithModel();
    testButton.setOnAction(event -> testConnection());
    defaultFolder.setItems(FXCollections.observableArrayList("<Please click test to get folder list>"));
  }

  private void testConnection()
  {
    toModel();
    ValContext ctx = new ValContext().createSubContext(model, null);

    model.validate(ctx);

    if (ctx.hasErrors() == true) {
      getConfigDialog().mapValidationMessagesToGui(ctx);
      return;
    }
    MailReceiveService service = MailReceiverServiceManager.get().getMailReceiveService();
    List<String> folderlist = service.testConnection(model, ctx);
    if (ctx.hasErrors() == false) {
      ctx.addDirect(ValState.Ok, model, null, "Successfull connected", null);
    }
    getConfigDialog().mapValidationMessagesToGui(ctx);
    defaultFolder.setItems(FXCollections.observableList(folderlist));

  }

  @Override
  public String getTabTitle()
  {
    return "Mailbox";
  }

}