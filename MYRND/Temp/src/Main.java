import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Main {

public static void main(String[] args) {

SwingUtilities.invokeLater(new Runnable() {
public void run() {
final JFrame frame = new JFrame();
JMenuBar bar = new JMenuBar();
JMenu menu = new JMenu("mainmenu");

Action action = new MyAction(frame);
action.putValue(Action.NAME, "GO");
JMenuItem item = new JMenuItem(action);
menu.add(item);
bar.add(menu);
frame.setJMenuBar(bar);

frame.setLocationRelativeTo(null);
frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
frame.pack();
frame.setVisible(true);
}
});

}
private static class ProgressShower {
private final JDialog progressDialog;
private State state;
private final Frame owner;

private static enum State {
NOT_YET_STARTED,
SHOWING_PROGRESS,
STOPPED;
}

public ProgressShower(Frame owner, String title, String message) {
super();

this.owner = owner;
this.state = ProgressShower.State.NOT_YET_STARTED;
this.progressDialog = constructDialog(title, message);
}

private JDialog constructDialog(String title, String message) {
JPanel panel = new JPanel();
panel.add(new JLabel("This is a label"));
panel.add(new JLabel(message));

final JDialog dialog = new JDialog(this.owner, title, true);
dialog.add(panel);
dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
dialog.pack();

return dialog;
}

public void start() {
if (this.state == ProgressShower.State.NOT_YET_STARTED) {
this.state = ProgressShower.State.SHOWING_PROGRESS;
this.progressDialog.setLocationRelativeTo(this.owner);
this.progressDialog.setVisible(true);
}
}

public void stop() {
if (this.state == ProgressShower.State.SHOWING_PROGRESS) {
this.state = ProgressShower.State.STOPPED;
this.progressDialog.setVisible(false);
//this.progressDialog.dispose();
}
}
}

public static class ClosingThread extends Thread {
public void run() {
SwingUtilities.invokeLater(new Runnable() {
public void run() {
Main.stopShower();
}
});
}
}

public static class MyAction extends AbstractAction {
private final JFrame frame;
public MyAction(JFrame frame) {
this.frame = frame;
}
public void actionPerformed(ActionEvent event) {

initShower(this.frame);

final Thread th = new ClosingThread();
th.start();

startShower();
}
}

private static ProgressShower shower;
public static void initShower(JFrame frame) {
shower = new ProgressShower(frame, "dialog title", "dialog message");
}
public static void startShower() {
shower.start();
}
public static void stopShower() {
shower.stop();
}
} 