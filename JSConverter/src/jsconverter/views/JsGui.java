package jsconverter.views;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.json.JSONException;

public class JsGui extends Composite {
	private Text modelsText;
	private Text jsText;
	private Button btnNewButton;
	private Button btnRun;
	private Label lblUrl;
	private Text urlAddress;
	private Button btnExecute;
	private Label lblJavascriptInput;
	private Label lblJavaModelsResult;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public JsGui(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(4, true));

		lblUrl = new Label(this, SWT.NONE);
		lblUrl.setText("JavaScript Url:");

		urlAddress = new Text(this, SWT.BORDER);
		urlAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));
		urlAddress
				.setText("http://maps.google.com/maps/geo?q=50.414915,30.523249&output=json&sensor=false");

		btnExecute = new Button(this, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(urlAddress.getText());
					HttpResponse execute = client.execute(get);

					String string = EntityUtils.toString(execute.getEntity());
					jsText.setText(string);
				} catch (Exception e1) {
					e1.printStackTrace();
					jsText.setText(e1.getMessage());
				}

			}
		});
		btnExecute.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnExecute.setText("GET");

		lblJavascriptInput = new Label(this, SWT.NONE);
		lblJavascriptInput.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 2, 1));
		lblJavascriptInput.setText("JavaScript Input");

		lblJavaModelsResult = new Label(this, SWT.NONE);
		lblJavaModelsResult.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 2, 1));
		lblJavaModelsResult.setText("Java Models Result");

		jsText = new Text(this, SWT.BORDER | SWT.MULTI);
		GridData gd_jsText = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_jsText.horizontalSpan = 2;
		jsText.setLayoutData(gd_jsText);

		modelsText = new Text(this, SWT.BORDER | SWT.MULTI);
		GridData gd_modelsText = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_modelsText.horizontalSpan = 2;
		modelsText.setLayoutData(gd_modelsText);

		btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				jsText.setText("");
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 2, 1));
		btnNewButton.setText("Clear");

		btnRun = new Button(this, SWT.NONE);
		btnRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String res;
				try {
					res = new JSConverter().toString(jsText.getText());
					modelsText.setText(res);
				} catch (JSONException e1) {
					e1.printStackTrace();
					modelsText.setText(e1.getMessage());
				}

			}
		});
		btnRun.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2,
				1));
		btnRun.setText("Convert!");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
