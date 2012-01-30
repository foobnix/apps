package jsconverter.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "jsconverter.views.SampleView";

	@Override
	public void createPartControl(Composite parent) {
		new JsGui(parent, SWT.FILL);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
