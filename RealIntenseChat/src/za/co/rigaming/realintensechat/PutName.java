package za.co.rigaming.realintensechat;

import android.app.Activity;
import android.os.Bundle;

public class PutName extends Activity{
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		String uri = getIntent().getData().toString();
		
		super.onCreate(savedInstanceState);
	}
}
