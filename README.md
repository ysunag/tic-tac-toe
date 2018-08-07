# tic-tac-toe
The project contains a client and a server for playing tic-tac-toe.

## Client 
* Written in Java.
* Build and run via a Makefile ('make run' will run the client)
* Use Gson for parsing JSON and HTTPClient for making REST calls
Client program propmts users for their moves and let them know if it was not a valid move and repeat the prompt until getting a valid move

## Server
* Written in C.
* Build and run in a docker container via a Makefile
* Expose REST endpoints
* Use Jansson for our JSON library
* Use ulfius for the REST server library

## General 
* Who goes first (server or client) is random
* The server never loses (a draw is possible)
* All states are passed in the JSON, the server should not maintain state
* Can support more than one game at a time
