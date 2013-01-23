package za.co.rigaming.realintensechat;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(formKey = "dFJNcVdBTm4tQzUzbncwb3FUbUQzUWc6MQ") 

public class MyApplication extends Application {
	
	 @Override
	  public void onCreate() {
	      super.onCreate();

	      // The following line triggers the initialization of ACRA
	      ACRA.init(this);
	  }

	
}
