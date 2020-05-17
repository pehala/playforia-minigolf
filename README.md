# Minigolf Server/Client/Editor (Playforia) [![Build Status](https://travis-ci.org/PhilippvK/playforia-minigolf.svg?branch=master)](https://travis-ci.org/PhilippvK/playforia-minigolf)

## UPDATE

Prebuild Standalone Clients have arrived: (no Java installation required anymore for Windows and MacOS)

Check out the latest [Release](https://github.com/PhilippvK/playforia-minigolf/releases)!

For benchmarking resons, a very low spec game server is currently hosting the minigolf server application. You can try it out using the IP `35.209.2.60` and leave some feedback on how the experience was. I apologize for any downtimes, as I can not check it very often.

## Screenshot

![Original Playforia Minigolf Main Menu](screenshot.png)

## Context

Playforia.net was an online game community created by Finnish game studio Playforia Inc. in 2002. As of the end of 2018, Playforia announced to close its web presence on January 7th, 2019. (Wikipedia: https://en.wikipedia.org/wiki/Playforia)
The gaming platform was also formerly known as Aapeli or Playray.

The Java Applet-based Minigolf Client was one of the most popular multiplayer games on the platform. When I found a partially working codebase for parts of the Playforia related Java-Projects on GitHub (https://github.com/WorldStarHipHopX/playforia) I got it running on my computer by implementing a few small changes, which are explained below.

## Features

### Original game
- 3718 Maps in 8 Categories
- Up to 4 players or Single Player mode
- Graphics quality options
- ...

### Reimplementation
- Commented out any communication with original Playforia.net servers
- Use local Map store instead of database
- Added ability to pass IP of server to client
- Ability to play on on a single computer and hosting a game for up to 4 players in your home network
- Removed bad words and custom tracks
- Added ability to choose a nickname freely

## Usage

### Prerequisites
- Clone this repo: `git clone git@github.com:PhilippvK/playforia-minigolf.git`
- Install Java Development Kit 8 (https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- OpenJDK1.8 should work as well!
- Install Apache `ant` for building: https://ant.apache.org/manual/install.html
- *Optional:* Install IntelliJ IDEA Java IDE (https://www.jetbrains.com/idea/download/) and import this reposiory as project 

### Building

There are 3 directories of interest: `client`, `server` and `editor`, which should compile quite easily.
Choose your prefered toolchain:
1. **IntelliJ IDEA**: Import Project from git and compile - Due to updates to the IDE, I unfortunately can not help you with this at the moment, as I have to figure it out, too.
2. **Ant Build**: Run `ant compile` in the repective directory

**Warning:** If you have multiple versions of the JDK installed, you have to make sure, that the environment variable `JAVA_HOME` points to the actual directory where version 1.8 is located. (For MacOS I had to run `JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home ant compile`)

### Running

First, the server application has to be started as it provides ressources like sounds, maps and textures which are required for "offline" modes, too.
As I cound not manage to include the tracks inside the compiled JAR archive, the `tracks` directory has to be located at the same folder where the `server.jar` is located! There is a symbolic link in the `server/` directory which will likely not work on Windows systems. Please remove it and copy the directory instead!
Assuming that all 3 tools have compiled sucessfully (or downloaded them them from the [Releases Page](https://github.com/PhilippvK/playforia-minigolf/releases)), you have 3 possible ways for running the server binary:
1. Using the IntelliJ IDE: Use the provides build artifacts or run the server by pressing the play button after compiling
2. Using the Ant tool: Run `ant run` in the `./server` directory
3. Use the exported JAR file: `java -jar server.jar`

The client can be started the same way (AFTER THE SERVER WAS STARTED) but you can also give launch options for server ip and game language in the following format

```bash
java -jar client.jar -server 192.168.1.7 -lang en_US # Replace IP with the one of your server (which you can find out by using for example `ifconfig`/`ipconfig`) and lang with en_US, fi_FI or sv_SE
```

If you want to enable debugging messages, add `-verbose` to the list of arguments.

After the Login screen, which can be skipped with an empty form, you should see your familiar Playforia Minigolf Menu!

**NEW:** You can now choose your nickname freely. However the input to the password field will still be ignored and there is no check for errors and so on...

Running the Editor is quite straightforward as it can be started like expected: `java -jar editor.jar`

## Compability

Tested:
- MacOS 10.14.5 Mojave with Java Version `1.8.0_152-ea` with (Open)JDK
- Raspbian Strech (9) with Java version 1.8.0\_65
- Windows (7/8/10)

Untested: (Will likely be compatible, too!)
- Ubuntu
- ...

## Problems
- Passwords and logins are ignored
- Ratings are not synced

## Notices

1. The code is neither written by me nor my property. I do NOT represent the same values as people who have worked on this code before. (Original Source: https://github.com/WorldStarHipHopX/playforia)
2. I am not responsible for any bug, problems, security flaws,...
3. Also, I do not intent to extend the current codebase very much.
4. The Java code you will find in the repository is pretty bad. Some parts even look like they where generated, for example by an converter tool
5. There is actually an aimbot implemented in the client code. Look for `allowCheating` in `GameCanvas.java` for trying it out. Use it wisely.

## Contributors

- [@PhilippvK](https://github.com/PhilippvK) (BuyMeACoffe: https://www.buymeacoff.ee/PhilippvK)
- [@maitovelkkis](https://github.com/maitovelkkis)
- [@eYeWoRRy](https://github.com/eYeWoRRy)

---

## Final Words

Have fun.

If you miss the good old times before Playforia.net went down, Minigolf probably was one of your favourite games. I hope you will have some fun in the single player or with friends with this  little crappy piece of oldschool software!
