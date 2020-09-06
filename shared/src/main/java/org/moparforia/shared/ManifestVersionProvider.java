package org.moparforia.shared;

import picocli.CommandLine;

public class ManifestVersionProvider implements CommandLine.IVersionProvider {
    @Override
    public String[] getVersion() {
        String version = ManifestVersionProvider.class.getPackage().getImplementationVersion();
        return new String[] {"${COMMAND-FULL-NAME} " + version};
    }
}
