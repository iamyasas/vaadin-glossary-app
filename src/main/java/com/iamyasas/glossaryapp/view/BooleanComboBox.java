package com.iamyasas.glossaryapp.view;

import com.vaadin.flow.component.combobox.ComboBox;

public class BooleanComboBox extends ComboBox<Boolean>{

	private static final long serialVersionUID = 3145900248722021323L;
	
	public BooleanComboBox(String label) {
		super(label, true, false);
		this.setWidth("100px");
	}

};
