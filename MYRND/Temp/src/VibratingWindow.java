/* * To change this template, choose Tools | Templates * and open the template in the editor. */import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/** * * @author Arthur */
public class VibratingWindow extends JFrame {
	public VibratingWindow() {
		setTitle("Vibrating Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 300);
		setVisible(true);
		setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		JButton vibrateButton = new JButton("VIBRATE");
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(vibrateButton);
		add(buttonPanel, BorderLayout.SOUTH);
		vibrateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vibrate();
			}
		});
	}

	private void vibrate() {
		final int originalX = this.getLocationOnScreen().x;
		final int originalY = this.getLocationOnScreen().y;
		try {
			for (int i = 0; i < 20; i++) {
				Thread.sleep(10);
				setLocation(this.getLocationOnScreen().x, this
						.getLocationOnScreen().y + 10);
				Thread.sleep(10);
				setLocation(this.getLocationOnScreen().x, this
						.getLocationOnScreen().y - 10);
				Thread.sleep(10);
				setLocation(this.getLocationOnScreen().x - 10, this
						.getLocationOnScreen().y);
				Thread.sleep(10);
				setLocation(originalX, originalY);
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		VibratingWindow vibrator = new VibratingWindow();
		vibrator.setLocation(300, 300);
	}
}