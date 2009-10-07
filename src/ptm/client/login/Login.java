package ptm.client.login;

import com.google.gwt.core.client.EntryPoint;

public class Login implements EntryPoint {
	public LoginManager lm = new LoginManager();
	@Override
	public void onModuleLoad() {
		lm.initialize();
	}

}
