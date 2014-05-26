package PicSimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IOPanel extends JPanel
{
	JCheckBox rb0 = new JCheckBox("RB0");
	JCheckBox ra4 = new JCheckBox("RA4");
	
	public IOPanel()
	{
		add(rb0);
		add(ra4);
		rb0.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(rb0.isSelected())
					Register.PORTB |= 0x01;
				else
					Register.PORTB &= 0xFE;
			}
		});
		ra4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(ra4.isSelected())
					Register.PORTA |= 0x10;
				else
					Register.PORTA &= 0xEF;
			}
		});
	}

	public void repaint(Processor myProcessor)
	{
		//rb0.setSelected((Register.PORTB & 0x01) != 0);
		//ra4.setSelected((Register.PORTA & 0x10) != 0);
	}
}
