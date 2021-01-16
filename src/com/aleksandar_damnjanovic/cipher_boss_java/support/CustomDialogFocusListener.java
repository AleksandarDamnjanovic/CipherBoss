package com.aleksandar_damnjanovic.cipher_boss_java.support;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class CustomDialogFocusListener implements WindowFocusListener {

	Frame dialog=null;
	
	public CustomDialogFocusListener(Frame frame) {
		dialog=frame;
	}
	
	@Override
	public void windowGainedFocus(WindowEvent arg0) {}

	@Override
	public void windowLostFocus(WindowEvent arg0) {
		dialog.setFocusable(true);
		dialog.setAutoRequestFocus(true);
		dialog.toFront();		
	}
}