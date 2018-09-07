package tfg.main.vistas;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

//Clase para poner un texto de ayuda en las etiquetas de los JText
public class HintTextField extends JTextField implements FocusListener{
	private static final long serialVersionUID = -5666636468969979944L;
	
	private final String hint;
	private boolean showingHint;

	public HintTextField(final String hint) {
		super(hint);
		super.setForeground(Color.GRAY);
		this.hint = hint;
		this.showingHint = true;
		super.addFocusListener(this);
	}

	public void focusGained(FocusEvent e) {
		if(this.getText().isEmpty()) {
			super.setForeground(Color.BLACK);
		    super.setText("");
		    showingHint = false;
		}
	}
		  
	public void focusLost(FocusEvent e) {
		if(this.getText().isEmpty()) {
			super.setForeground(Color.GRAY);
		    super.setText(hint);
		    showingHint = true;
		}
	}

	public String getText() {
		return showingHint ? "" : super.getText();
	}

}
