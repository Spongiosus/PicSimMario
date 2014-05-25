package PicSimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IOPanel extends JPanel
{
	JCheckBox rb0 = new JCheckBox("RB0");
	Processor proc;
	
	
	public IOPanel(Processor proc)
	{
		add(rb0);
		this.proc = proc;
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
	}

	public void repaint(Processor myProcessor)
	{
		rb0.setSelected((Register.PORTB & 0x01) != 0);
	}
}
