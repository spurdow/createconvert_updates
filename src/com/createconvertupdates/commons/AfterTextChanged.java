package com.createconvertupdates.commons;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class AfterTextChanged implements TextWatcher{

	
	public abstract void abstract_afterTextChanged(Editable s);
	
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		abstract_afterTextChanged(s);
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}
