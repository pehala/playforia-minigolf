package org.moparforia.server;

import org.moparforia.shared.ManifestVersionProvider;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        description = "Starts Minigolf Server",
        name = "server",
        mixinStandardHelpOptions = true,
        versionProvider = ManifestVersionProvider.class
)
public class Launcher implements Callable<Void> {

    public static final String DEFAULT_HOST = "0.0.0.0";
    public static final int DEFAULT_PORT = 4242;

    @CommandLine.Option(names = {"--hostname", "-ip"},
            description = "Sets server hostname")
    private String host = DEFAULT_HOST;

    @CommandLine.Option(names = {"--port", "-p"},
            description = "Sets server port")
    private int port = DEFAULT_PORT;

    public static void main(String... args) {
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

    @Override
    public Void call() {
        getServer(host, port).start();
        return null;
    }

    public Server getServer(String host, int port) {
        return new Server(host, port);
    }
}
