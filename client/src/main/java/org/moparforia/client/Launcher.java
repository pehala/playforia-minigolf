package org.moparforia.client;

import picocli.CommandLine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.concurrent.Callable;

/**
 * Playforia
 * 28.5.2013
 */
@CommandLine.Command(
        description = "Starts Minigolf Client",
        name = "client", mixinStandardHelpOptions = true
)
public class Launcher implements Callable<Void> {

    private static final String DEFAULT_SERVER = "127.0.0.1";
    private static final int DEFAULT_PORT = 4242;

    // CLI options
    @CommandLine.Option(names = {"--hostname", "-ip"},
            description = "Sets server hostname",
            defaultValue = "")
    private String hostname;

    @CommandLine.Option(names = {"--port", "-p"},
            description = "Sets server port",
            defaultValue = "0")
    private int port;

    @CommandLine.Option(names = {"--lang", "-l"},
            description = "Sets language of the game, available values: ${COMPLETION-CANDIDATES}",
            defaultValue = "EN_US")
    private Language lang;

    @CommandLine.Option(names = {"--verbose", "-v"}, description = "Set if you want verbose information")
    private static boolean verbose = false;

    public static boolean debug() {
        return verbose;
    }

    public static boolean isUsingCustomServer() {
        return true;//instance.serverBox.isSelected();
    }

    public static void main(String... args) throws Exception {
        Launcher launcher = new Launcher();
        try {
            CommandLine.ParseResult parseResult = new CommandLine(launcher).parseArgs(args);
            if (!CommandLine.printHelpIfRequested(parseResult)) {
                launcher.call();
            }
        } catch (CommandLine.ParameterException ex) { // command line arguments could not be parsed
            System.err.println(ex.getMessage());
            ex.getCommandLine().usage(System.err);
        }
    }

    private boolean showSettingDialog(JFrame frame, String server, int port) throws ParseException {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(4, 1));

        final JTextField serverField = new JTextField();
        JLabel serverLabel = new JLabel("Hostname:");
        serverField.setText(server);
        pane.add(serverLabel);
        pane.add(serverField);

        JSpinner portSpinner = new JSpinner();
        portSpinner.setModel(new SpinnerNumberModel(port,1, Integer.MAX_VALUE,1));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(portSpinner,"#");
        // Align spinner values to the left
        editor.getTextField().setHorizontalAlignment(JTextField.LEFT);
        portSpinner.setEditor(editor);
        JLabel portLabel = new JLabel("Port:");
        pane.add(portLabel);
        pane.add(portSpinner);

        int result = JOptionPane.showConfirmDialog(frame, pane, "Choose a server", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            this.hostname = serverField.getText();
            portSpinner.commitEdit();
            this.port = (Integer) portSpinner.getValue();
            return true;
        } else {
            return false;
        }
    }
    private String[] login(JFrame frame) {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(2, 2));
        JLabel userLabel = new JLabel("user");
        JLabel passLabel = new JLabel("pass");
        final JTextField userField = new JTextField();
        final JPasswordField passField = new JPasswordField();

        pane.add(userLabel);
        pane.add(userField);
        pane.add(passLabel);
        pane.add(passField);

        int option = JOptionPane.showConfirmDialog(frame, pane, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            return new String[]{user, pass};
        } else {
            return new String[]{null, null};
        }

    }

    @Override
    public Void call() throws Exception{
        JFrame frame = new JFrame();
        frame.setTitle("Minigolf");
        Image img = ImageIO.read(getClass().getResource("/icons/playforia.png"));
        frame.setIconImage(img);
        if (hostname.isEmpty() || port == 0) {
            // Determine which of these was actually false
            String temp_hostname = hostname.isEmpty() ? DEFAULT_SERVER : hostname;
            int temp_port = port == 0 ? DEFAULT_PORT : port;
            if (!showSettingDialog(frame, temp_hostname, temp_port)) {
                System.err.println("Server needs to be specified for minigolf to run");
            }
        }

        new Game(frame, hostname, port, lang.toString(), verbose);
        return null;
    }


    enum Language {
        EN_US("en_US"),
        FI_FI("fi_FI"),
        SV_SE("sv_SE");

        private final String custom_name;

        Language(String name) {
            this.custom_name = name;
        }

        @Override
        public String toString() {
            return this.custom_name;
        }
    }
}
