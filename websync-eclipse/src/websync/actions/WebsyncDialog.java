package websync.actions;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WebsyncDialog extends TitleAreaDialog {

	private Text hostAddress;
	private Text secretKey;

	private String host;
	private String secret;
	private Button stopButton;
	private boolean isClose = false;
	private int port = 8000;

	public WebsyncDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Eclipse WebSync plug-in activation.");
		setMessage("Please, select HOST and secret key for session:", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);

		createHostField(container);
		createKeyField(container);

		return area;
	}

	private void createHostField(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Host ");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		hostAddress = new Text(container, SWT.BORDER);
		hostAddress.setText("localhost:8000");
		hostAddress.setLayoutData(dataFirstName);
	}

	private void createKeyField(Composite container) {
		Label lbtLastName = new Label(container, SWT.NONE);
		lbtLastName.setText("Last Name");

		GridData dataLastName = new GridData();
		dataLastName.grabExcessHorizontalSpace = true;
		dataLastName.horizontalAlignment = GridData.FILL;
		secretKey = new Text(container, SWT.BORDER);
		secretKey.setText("123123123sdfsfffdfassdfsdfsdfsdfsdfas234123");
		secretKey.setLayoutData(dataLastName);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Start", true);
		stopButton = createButton(parent, IDialogConstants.CLOSE_ID, "Stop", false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case IDialogConstants.CLOSE_ID:
			closeButtonPressed();
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}

	private void closeButtonPressed() {
		isClose  = true;
		super.close();
	}
	
	public boolean isClosePressed() {
		return isClose;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		String hap = hostAddress.getText();
		String[] hp = hap.split(":");
		host = hp[0];
		port = Integer.parseInt(hp[1]);
		secret = secretKey.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public String getHost() {
		return host;
	}

	public String getSecret() {
		return secret;
	}

	public int getPort() {
		return port;
	}
}