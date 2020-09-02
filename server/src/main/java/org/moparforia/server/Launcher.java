package org.moparforia.server;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        description = "Starts Minigolf Server",
        name = "server", mixinStandardHelpOptions = true
)
public class Launcher implements Callable<Void> {

    @CommandLine.Option(names = {"--hostname", "-ip"},
            description = "Sets server hostname",
            defaultValue = "0.0.0.0")
    private String host;

    @CommandLine.Option(names = {"--port", "-p"},
            description = "Sets server port",
            defaultValue = "4242")
    private int port;

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
        new Server(host, port).start();
        return null;
    }
}
