package wicket;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * Homepage
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public HomePage(final PageParameters parameters) {

        // Add the simplest type of label
        add(new Label("message", "If you see this message wicket is properly configured and running. Modify this message in HomePage, then" +
        		" reload the module 'wicket'. See your changes reflected."));

        // TODO Add your page's components here
    }

	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		System.out.println("Just rendered the page");
	}
    
    
}
