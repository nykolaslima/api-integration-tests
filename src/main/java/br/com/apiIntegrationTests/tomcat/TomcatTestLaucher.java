package br.com.apiIntegrationTests.tomcat;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class TomcatTestLaucher {

	private static final String WEBAPP_DIR_LOCATION = "src/main/webapp/";
	private Tomcat tomcat;
	
	public void start() {
		try {
			tomcat = new Tomcat();
			tomcat.setPort(8080);
			
			Context context = tomcat.addWebapp("/", new File(WEBAPP_DIR_LOCATION).getAbsolutePath());
			context.getServletContext();
			context.setAllowCasualMultipartParsing(true);
			StandardContext standardContext = (StandardContext) context;
			standardContext.setDelegate(true);
			
			tomcat.start();
		} catch(Exception e) {
			throw new RuntimeException("Error starting tomcat server to run tests");
		}
	}
	
	public void stop() {
		try {
			tomcat.stop();
			tomcat.destroy();
		} catch (LifecycleException e) {
			throw new RuntimeException("Error stoping tomcat server to run tests");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void setEnv(Map<String, String> newenv)
	{
	  try
	    {
	        Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
	        Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
	        theEnvironmentField.setAccessible(true);
	        Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
	        env.putAll(newenv);
	        Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
	        theCaseInsensitiveEnvironmentField.setAccessible(true);
	        Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
	        cienv.putAll(newenv);
	    }
	    catch (NoSuchFieldException e)
	    {
	      try {
	        Class[] classes = Collections.class.getDeclaredClasses();
	        Map<String, String> env = System.getenv();
	        for(Class cl : classes) {
	            if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
	                Field field = cl.getDeclaredField("m");
	                field.setAccessible(true);
	                Object obj = field.get(env);
	                Map<String, String> map = (Map<String, String>) obj;
	                map.clear();
	                map.putAll(newenv);
	            }
	        }
	      } catch (Exception e2) {
	        e2.printStackTrace();
	      }
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    } 
	}
	
}
