import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestModal extends JApplet {
	public void init() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}
				setLayout(new FlowLayout());
				JButton button = new JButton("click for dialog");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new ModalDialog(SwingUtilities
								.getWindowAncestor(TestModal.this));
					}
				});
				getContentPane().add(button);
			}
		});
		super.init();
	}

	static class ModalDialog extends JDialog {
		public ModalDialog(Window owner) {
			super(owner);
			setModal(true);
			setSize(400, 300);
			setLayout(new FlowLayout());
			final JComboBox combo = new JComboBox(
					new Object[] { "1", "2", "3" });
			combo.setPreferredSize(new Dimension(80, 30));
			getContentPane().add(combo);
			JButton button = new JButton("close");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			getContentPane().add(button);
			setLocationRelativeTo(owner);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});
			setVisible(true);
		}
	}

	/** * Entry point for testing stand alone app */
	public static void main(String[] argv) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}
				final JFrame jf = new JFrame();
				jf.setLayout(new FlowLayout());
				JButton button = new JButton("click for dialog");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new ModalDialog(jf);
					}
				});
				jf.getContentPane().add(button);
				jf.setSize(800, 600);
				jf.setLocationRelativeTo(null);
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.setVisible(true);
			}
		});
	}
}