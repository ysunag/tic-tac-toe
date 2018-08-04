code layout:
code being in two directories, named "client" and "server": 2/2pts

client:

Makefile, one each for the targets "all", "clean" and "run" or something including run that "all" calls : 3/3pts

clean target actually cleans all non-source files: 1/1pts

code compiles cleanly (no errors, warnings can be ignored) (off 2 points if compilation depends on a jar that is not included): 5/5pts

client runs without throwing an exception: 3/3pts

Java style guidelines: 5/5pts 

17/17 points subtotal

server:

Makefile for the targets "all", "clean", "run", "run_docker": 4/4pts

clean deletes all the non-source file: 1/1pts

clean deletes the docker image from the local repository: 1/1pts

if the code compiles cleanly: 5/5pts

C style guide: 5/5pts

server runs on Ubuntu correctly (doesn't error out): 3/3pts

server runs in docker correcting (doesn't error out): 3/3pts

docker container is named "ttt-server": 1/1pts

server shutdows cleanly when the user enters a key (if this doesn't work, check whether they used the "-i -t" flags in their "docker run" command.: 2/2pts

JSON is used for passing data between client and server: 3/3pts

REST calls are used for communicating between client and server: 3/3ts

31/31 points subtotal

playing the game:

first player to go when starting a new game is random (each player should go first at least once in 5 games started): 2/2pts

server doesn't lose (plays to a draw or wins). Play 5 games or so and that'll suffice: 8/8pts

prompt for a player to move tells the player how to enter their move: 2/2pts

an invalid move is noted as such (user should be reprompted to move): 2/2pts

two games can be interleaved (start one client, play a move, start a second client in a new terminal window): 5/5pts

19/19 points subtotal

total: 70/70pts(69 with 1 free point)

