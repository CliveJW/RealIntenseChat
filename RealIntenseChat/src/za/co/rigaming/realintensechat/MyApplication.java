package za.co.rigaming.realintensechat;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "dFJNcVdBTm4tQzUzbncwb3FUbUQzUWc6MQ") 

public class MyApplication extends Application {
	
	 @Override
	  public void onCreate() {
	      super.onCreate();

	      // The following line triggers the initialization of ACRA
	      ACRA.init(this);
	  }

	
}
