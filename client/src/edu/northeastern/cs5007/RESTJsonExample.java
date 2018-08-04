package edu.northeastern.cs5007;

import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Represents Tic Tac Toe game client.
 */
public class RESTJsonExample {

  private static String URL = "http://localhost:8085/tictactoe";
  private static RESTJsonExample self = new RESTJsonExample();

  /**
   * Main function for the Tic Tac Toe client.
   * @param args command line arguments.
   * @throws IOException upon problem in closing client program.
   * @throws Exception upon problem in reading server response.
   */
  public static void main(String[] args) {
    char[] board = new char[9];
    board[0] = '0';
    board[1] = '1';
    board[2] = '2';
    board[3] = '3';
    board[4] = '4';
    board[5] = '5';
    board[6] = '6';
    board[7] = '7';
    board[8] = '8';
    

    Random rand = new Random();
    int n = rand.nextInt(10);
    System.out.println("Tic Tac Toe\nGame Start\n");
    self.printBoard(board);
    if(n < 5) {
      System.out.println("You make the first move!");
      self.userMakeMove(board);
      self.printBoard(board);
    } else {
      System.out.println("Your opponent makes the first move!");
    }
    
    GameBoard currentBoard = new GameBoard(new String(board));


    while(true) {
      CloseableHttpClient httpClient = HttpClientBuilder.create().build();
      Gson gson = new GsonBuilder().create();
      String request = gson.toJson(currentBoard);

      String completePayload = "";
      try {
        HttpPost httpPostRequest = new HttpPost(URL);
        httpPostRequest.setEntity(new StringEntity(request));
        httpPostRequest.setHeader("Accept", "application/json");
        httpPostRequest.setHeader("Content-type", "application/json");
      
      

        HttpResponse httpResponse = httpClient.execute(httpPostRequest);
        System.out.println("----------------------------------------");
        System.out.println(httpResponse.getStatusLine());
        System.out.println("----------------------------------------");

        HttpEntity entity = httpResponse.getEntity();

        byte[] buffer = new byte[1024];

        if (entity != null) {
          InputStream inputStream = entity.getContent();
          try {
            int bytesRead = 0;
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            while ((bytesRead = bis.read(buffer)) != -1) {
              String chunk = new String(buffer, 0, bytesRead);
              System.out.println(chunk);
              completePayload += chunk;
            }
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            try { inputStream.close(); } catch (Exception ignore) {}
          }
        }

        System.out.println("Entire paylaod: " + completePayload);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
        httpClient.close();
        } catch (IOException e) {
        System.out.println("Eating IOException on http client close. " +
                           e.toString());
        }
      } 

      currentBoard = gson.fromJson(completePayload, GameBoard.class);
      char[] newBoard = currentBoard.board.toCharArray();
      self.printBoard(newBoard);
      if(!self.gameOver(newBoard)) {
        self.userMakeMove(newBoard);
        currentBoard.board = new String(newBoard);
        self.printBoard(newBoard);
      } 
      if(self.gameOver(newBoard)) {
        char winner = self.whoWins(newBoard);
        if(winner == '0') {
          System.out.println("Game over! It is a draw. No winner!");
        } else if (winner == 'o') {
          System.out.println("Game over! You Win!");
        } else {
          System.out.println("Game over! You lose.");
        }
         break;
      }  
    }
  }


  /**
  * Represents a game board.
  */
  static class GameBoard {
    String board;
    /**
     * Creates a new game board with the given board string representation.
     * @param board the given board string representation.
     */
    public GameBoard(String board) {
      this.board = board;
    }
  }


  /**
   * Prints out the game board.
   * @param board the char array representation of the game board. 
   */
  public void printBoard(char[] board) {
    System.out.println("\n\nTic Tac Toe\n\n");
    System.out.println("Player Computer (X)  -  Player User (O)\n\n\n");
    System.out.println("     |     |     ");
    System.out.println("  "+ board[0] + "  |  " + board[1] + "  |  " 
      + board[2] + "  ");
    System.out.println("_____|_____|_____");
    System.out.println("     |     |     ");
    System.out.println("  "+ board[3] + "  |  " + board[4] + "  |  " 
      + board[5] + "  ");
    System.out.println("_____|_____|_____");
    System.out.println("     |     |     ");
    System.out.println("  "+ board[6] + "  |  " + board[7] + "  |  " 
      + board[8] + "  ");
    System.out.println("     |     |     \n");
  }


  /**
   * User makes one move.
   * @param board the char array representation of the game board. 
   */
  public void userMakeMove(char[] board) {
    System.out.println("Please make a move: type in the corresponding" 
      + " position which is one of the number shown on the board.\n");
    boolean valid = false;
    String s = null;
    while(!valid) {
      try{
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
          System.in));
        s = bufferRead.readLine();
      }
      catch(IOException e)
      {
        e.printStackTrace();
      } 
      if (s.length() != 1 || s.charAt(0) < '0' || s.charAt(0) > '8') {
        System.out.println("Invalid input. Please type in a legal position"
        + "which is one of the number shown on the board.\n");
      } else {
        int n = Integer.parseInt(s);
        if(n >= 0 && n <= 8 && board[n] >= '0' && board[n] <= '8') {
          valid = true;
          board[n] = 'o';
        } else {
          System.out.println("Please type in a legal position which is one"
          + " of the number shown on the board.\n");
        }
      }
    }
  }


  /**
   * Checks if the game is over.
   * @param board the char array representation of the game board. 
   * @return true if one player wins the game or there is no more 
   *         unvisited position, false otherwise.
   */
  public boolean gameOver(char[] board) {
    return 
      ((board[0] == board[1]) && (board[2] == board[1])) ||
      ((board[3] == board[4]) && (board[5] == board[4])) ||
      ((board[6] == board[7]) && (board[8] == board[7])) ||
      ((board[0] == board[3]) && (board[6] == board[3])) ||
      ((board[1] == board[4]) && (board[4] == board[7])) ||
      ((board[2] == board[5]) && (board[8] == board[5])) ||
      ((board[0] == board[4]) && (board[8] == board[4])) ||
      ((board[2] == board[4]) && (board[6] == board[4])) || 
      isDraw(board);
  }

  /**
   * Checks whether the given game board is a draw result.
   * @param board the char array representation of the game board. 
   * @return true if the given game board is a draw result, 
   *        false otherwise.
   */
  public boolean isDraw(char[] board) {
    return ((board[0] == 'o') || (board[0] == 'x')) &&
           ((board[1] == 'o') || (board[1] == 'x')) &&
           ((board[2] == 'o') || (board[2] == 'x')) &&
           ((board[3] == 'o') || (board[3] == 'x')) &&
           ((board[4] == 'o') || (board[4] == 'x')) &&
           ((board[5] == 'o') || (board[5] == 'x')) &&
           ((board[6] == 'o') || (board[6] == 'x')) &&
           ((board[7] == 'o') || (board[7] == 'x')) &&
           ((board[8] == 'o') || (board[8] == 'x'));
  }


  /**
   * Determins the winner of the game.
   * @param board the char array representation of the game board. 
   * @retun 'x' if computer player wins the game,
   *        'o' if the user wins the game.
   *        '0' if it is a draw.
   */
  public char whoWins(char[] board) {
    if ((board[0] == board[1]) && (board[2] == board[1])) {
      return board[0];
    }
    if ((board[3] == board[4]) && (board[5] == board[4])) {
      return board[3];
    }
    if ((board[6] == board[7]) && (board[8] == board[7])) {
      return board[6];
    }
    if ((board[1] == board[4]) && (board[4] == board[7])) {
      return board[1];
    }
    if ((board[2] == board[5]) && (board[8] == board[5])) {
      return board[2];
    }
    if ((board[0] == board[3]) && (board[3] == board[6])) {
      return board[0];
    }
    if ((board[0] == board[4]) && (board[8] == board[4])) {
      return board[0];
    }
    if((board[2] == board[4]) && (board[6] == board[4])) {
      return board[2];
    }
    return '0';
  }
}
