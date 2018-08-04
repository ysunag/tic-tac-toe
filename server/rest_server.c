/**
 * Represents Tic Tac Toe game server
 */
#include <stdio.h>
#include <ulfius.h>
#include <jansson.h>

#define PORT 8085

#define COMPUTER 'x'
#define USER 'o'

//function prototypes.
void print_board(char* board);
int check_winner(char* board, char c);
void server_move(char* board);
int getScore(char* board, char player);
char invert(char player);


/**  
 * Callback function for the web application on /tictactoe url call  
 */ 
int callback_tictactoe (const struct _u_request * request, struct _u_response *
response, void * user_data) {
  json_auto_t * root = ulfius_get_json_body_request(request, NULL);
  if(!root) {
    printf("root is null, exiting\n");   
  }
  json_auto_t *str = json_object_get(root, "board");
  if (json_is_string(str)) {
    printf("received a json_string\n");
  } else {
    printf("It is NOT a json_string\n");
  }
  char* board = json_string_value(str);
  server_move(board);
  json_auto_t *obj = json_object(); 
  json_auto_t *board_obj = json_string(board);
  json_object_set(obj,"board", board_obj);
  char* json_dump = json_dumps(obj, JSON_ENCODE_ANY);
  printf("sending jason: %s", json_dump);
  ulfius_set_json_body_response(response, 200, obj);
  return U_CALLBACK_CONTINUE;
}

/**
 * main function for tic tac toe server.
 */
int main(void) {
  struct _u_instance instance;

  // Initialize instance with the port number
  if (ulfius_init_instance(&instance, PORT, NULL, NULL) != U_OK) {
    fprintf(stderr, "Error ulfius_init_instance, abort\n");
    return(1);
  }

  // Endpoint list declaration
  ulfius_add_endpoint_by_val(&instance, "POST", "/tictactoe", NULL, 0, 
    &callback_tictactoe, NULL);

  // Start the framework
  if (ulfius_start_framework(&instance) == U_OK) {
    printf("Start framework on port %d\n", instance.port);

    // Wait for the user to press <enter> on the console to quit the 
    // application
    getchar();
  } else {
    fprintf(stderr, "Error starting framework\n");
  }
  printf("End framework\n");

  ulfius_stop_framework(&instance);
  ulfius_clean_instance(&instance);

  return 0;
}


/**
 * Prints out the current game board.
 * @param board char array representation of the game board.
 */
void print_board(char* board) {
  printf("\n\nTic Tac Toe\n\n");
  printf("Player Computer (X)  -  Player User (O)\n\n\n");
  printf("     |     |     \n");
  printf("  %c  |  %c  |  %c \n", board[0], board[1], board[2]);
  printf("_____|_____|_____\n");
  printf("     |     |     \n");
  printf("  %c  |  %c  |  %c \n", board[3], board[4], board[5]);
  printf("_____|_____|_____\n");
  printf("     |     |     \n");
  printf("  %c  |  %c  |  %c \n", board[6], board[7], board[8]);
  printf("     |     |     \n\n");
}


/**
 * Check if the given player wins the game represented by the given 
 * board.
 * @param board char array representation of the game board.
 * @param c the char reprentation of the given player. 'x' for 
        computer player and 'o' for user player
 */
int check_winner(char* board, char c) {
  if ((board[0] == c && board[1] == c && board[2] == c) ||
    (board[3] == c && board[4] == c && board[5] == c) ||   
    (board[6] == c && board[7] == c && board[8] == c) ||
    (board[0] == c && board[3] == c && board[6] == c) ||
    (board[1] == c && board[4] == c && board[7] == c) ||
    (board[2] == c && board[5] == c && board[8] == c) ||
    (board[0] == c && board[4] == c && board[8] == c) ||
    (board[2] == c && board[4] == c && board[6] == c)) {
    return 1;
    } else {
      return 0;
    }
  }


/**
 * Server makes one move.
 * @param board char array representation of the game board.
 */
void server_move(char* board) {
  print_board(board);
  printf("%s\n", "Server makes a move:" );
  int current_best_score = -10000;
  int current_best_move;
  int i;
  int score;
  for(i = 0; i < 9; i++) {
    if(board[i] != COMPUTER && board[i] != USER) {
      board[i] = COMPUTER;
      score = getScore(board,USER);
      if(score > current_best_score) {
        current_best_score = score;
        current_best_move = i;
      }
      board[i] = '0' + i;
    }
  }
  printf("%d\n", current_best_move);
  board[current_best_move] = COMPUTER;
  print_board(board);
}


/**
 * Gets the opposite side of the given player.
 * @param player the char representation of the given player.'x' for 
 *      computer player and 'o' for user player
 * @return compauter player if given user player, user player if 
 *     given compauter player.
 */
char invert(char player) {
  if (player == COMPUTER) {
    return USER;
  } else {
    return COMPUTER;
  }
}



/**
 * Gets the score if the given player makes a move on the given 
 * board.
 * @param player the char representation of the player to make move
 * 'x' for computer player and 'o' for user player
 * @param board char array representation of the current game board.
 * @return the score of the given game board for the given player..
 */
int getScore(char* board, char player) {
  int current_best_score;
  int legal_move_num = 0;
  int i;
  int computer_win  = check_winner(board, COMPUTER);
  int user_win = check_winner(board, USER);

  if (computer_win > 0) {
      return 1;
  }

  if (user_win > 0) {
      return -1;
  }

  for (i = 0; i < 9; i++) {
    if((board[i] != COMPUTER) && (board[i] != USER)) {
      legal_move_num += 1;
    }
  }

  if(legal_move_num == 0) {
      return 0;
  }

  if(player == COMPUTER) {
    current_best_score = -10000;
  } else {
    current_best_score = 10000;
  }

  int score;
  for(int i = 0; i < 9; i++) {
    if((board[i] != COMPUTER) && (board[i] != USER)) {
      board[i] = player;
      score = getScore(board, invert(player));
      board[i] = '0' + i;
      if((player == COMPUTER) && (score > current_best_score)) {
        current_best_score = score;
      } 
      if((player == USER) && (score < current_best_score)) {
        current_best_score = score;
      }

    }
  }
  return current_best_score;
}