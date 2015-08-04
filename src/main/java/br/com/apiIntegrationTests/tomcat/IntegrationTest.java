package br.com.apiIntegrationTests.tomcat;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class IntegrationTest {

	private static TomcatTestLaucher tomcatTestLaucher;

	@BeforeClass
	public static void setUpClass() {
		//hack to run integration tests in eclipse and maven. When env vars are null, then we are running from eclipse
		if(isRunningFromTerminal()) {
			tomcatTestLaucher = new TomcatTestLaucher();
			tomcatTestLaucher.start();
		}
	}
	
	@AfterClass
	public static void tearDownClass() {
		if(tomcatTestLaucher != null) {
			tomcatTestLaucher.stop();
		}
	}

	private static boolean isRunningFromTerminal() {
		String isRunningFromTerminal = System.getProperty("api.integration.tests");
		if(isRunningFromTerminal == null && Boolean.parseBoolean(isRunningFromTerminal)) {
			return true;
		} else {
			return false;
		}
	}

}
