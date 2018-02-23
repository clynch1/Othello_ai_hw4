//Program:      Othello
//Course:       COSC470
//Description:  Permits two programs, each using this control structure (but each with additional
//              customized classes and/or methods)to play Othello (i.e, against each other).
//Author:       Connor Lynch   
//Revised:      

import java.io.*;
import java.util.LinkedList;
//***************************************************************************************************
//***************************************************************************************************
//Class:        Othello
//Description:  Main class for the program. Allows set-up and plays one side.

public class lynch4 {

    public static char myColor = '?';           //B (black) or W (white) - ? means not yet selected
    public static char opponentColor = '?';     //ditto but opposite

    //INSERT ANY ADDITIONAL GLOBAL VARIABLES HERE
    //===========================================
    //===========================================           
    //===========================================
    //===========================================
    //***************************************************************************************************
    //Method:		main
    //Description:	Calls routines to play Othello
    //Parameters:	none
    //Returns:		nothing
    //Calls:            loadBoard, saveBoard, showBoard, constructor in Board class
    //                  getCharacter, getInteger, getKeyboardInput, constructor in KeyboardInputClass
    public static void main(String args[]) {
        //INSERT ANY ADDITIONAL CONTROL VARIABLES HERE
        //============================================
        //============================================

        //============================================
        //============================================
        KeyboardInputClass keyboardInput = new KeyboardInputClass();
        int pollDelay = 250;
        long moveStartTime, moveEndTime, moveGraceTime = 10000;     //times in milliseconds
        Board currentBoard = Board.loadBoard();
        String myMove = "", myColorText = "";
        System.out.println("--- Othello ---");
        System.out.println("Player: Connor\n");
        if (currentBoard != null) {                                 //board found, make sure it can be used
            if (currentBoard.status == 1) {                          //is a game in progress?   
                if (keyboardInput.getCharacter(true, 'Y', "YN", 1, "A game appears to be in progress. Abort it? (Y/N (default = Y)") == 'Y') {
                    currentBoard = null;
                } else {
                    System.out.println("Exiting program. Try again later...");
                    System.exit(0);
                }
            }
        }
        if ((currentBoard == null) || (currentBoard.status == 2)) {   //create a board for a new game
            int rows = 8;
            int cols = 8;
            if (keyboardInput.getCharacter(true, 'Y', "YN", 1, "Use standard board? (Y/N: default = Y):") == 'N') {
                rows = keyboardInput.getInteger(true, rows, 4, 26, "Specify the number of rows for the board (default = " + rows + "):");
                cols = keyboardInput.getInteger(true, cols, 4, 26, "Specify the number of columns for the board (default = " + cols + "):");
            }
            int maxTime = 60;
            maxTime = keyboardInput.getInteger(true, maxTime, 10, 600, "Max time (seconds) allowed per move (Default = " + maxTime + "):");
            currentBoard = new Board(rows, cols, maxTime);
            while (currentBoard.saveBoard() == false) {
            }               //try until board is saved (necessary in case of access conflict)
        }

        //INSERT CODE HERE FOR ANY ADDITIONAL SET-UP OPTIONS
        //==================================================
        //==================================================     
        int gameType = keyboardInput.getInteger(true, 1, 1, 3, "Please Choose Game Play: \n1: Normally (Default) \n"
                + "2: Randomly \n"
                + "3: Manually ");
        switch (gameType) {
            case 1: {
                System.out.println("You Are Playing Normally\n");
                break;
            }//end of case 1
            case 2: {
                System.out.println("You Are Playing Randomly\n");
                break;
            }//end of case 2
            case 3: {
                System.out.println("You Are Playing Manually\n");
                break;
            }//end of case 3
        }//end of game type

        //==================================================
        //==================================================
        //At this point set-up must be in progress so colors can be assigned
        if (currentBoard.colorSelected == '?') {                    //if no one has chosen a color yet, choose one (player #1)
            myColor = keyboardInput.getCharacter(true, 'B', "BW", 1, "Select color: B=Black; W=White (Default = Black):");
            currentBoard.colorSelected = myColor;

            while (currentBoard.saveBoard() == false) {
            }               //try until the board is saved
            System.out.println("You may now start the opponent's program...");
            while (currentBoard.status == 0) {                      //wait for other player to join in
                currentBoard = null;                                //get the updated board
                while (currentBoard == null) {
                    currentBoard = Board.loadBoard();
                }
            }
        } else {                                                      //otherwise take the other color (this is player #2)
            if (currentBoard.colorSelected == 'B') {
                myColor = 'W';
            } else {
                myColor = 'B';
            }
            currentBoard.status = 1;                                //by now, both players are engaged and play can begin
            while (currentBoard.saveBoard() == false) {
            }               //try until the board is saved
        }

        if (myColor == 'B') {
            myColorText = "Black";
            opponentColor = 'W';
        } else {
            myColorText = "White";
            opponentColor = 'B';
        }
        System.out.println("This player will be " + myColorText + "\n");

        //INSERT CODE HERE FOR ANY ADDITIONAL OUTPUT OPTIONS
        //==================================================
        //==================================================
        //==================================================
        //==================================================
        //Now play can begin. (At this point each player should have an identical copy of currentBoard.)
        while (currentBoard.status == 1) {
            if (currentBoard.whoseTurn == myColor) {
                if (currentBoard.whoseTurn == 'B') {
                    System.out.println("Black's turn to move...");
                } else {
                    System.out.println("White's turn to move");
                }
                currentBoard.showBoard();
                String previousMove = currentBoard.move;
                moveStartTime = System.currentTimeMillis();

                //CALL METHOD(S) HERE TO SELECT AND MAKE A VALID MOVE
                //===================================================
                //===================================================
                switch (gameType) {
                    case 1: {//Normally
                        myMove = makeMoves(getMoves(currentBoard, myColor, opponentColor), currentBoard, gameType);
                        break;
                    }//end of case 1
                    case 2: {//Randomly
                        myMove = makeMoves(getMoves(currentBoard, myColor, opponentColor), currentBoard, gameType);
                        break;
                    }//end of case 2
                    case 3: {//Manually
                        myMove = playManually(currentBoard);
                        break;
                    }//end of case 3
                }//end of switch                
                //===================================================
                //===================================================
                //YOU MAY ADD NEW CLASSES AND/OR METHODS BUT DO NOT
                //CHANGE ANY EXISTING CODE BELOW THIS POINT
                moveEndTime = System.currentTimeMillis();
                if ((moveEndTime - moveStartTime) > (currentBoard.maxMoveTime * 1000 + moveGraceTime)) {
                    System.out.println("\nMaximum allotted move time exceeded--Opponent wins by default...\n");
                    keyboardInput.getKeyboardInput("\nPress ENTER to exit...");
                    currentBoard.status = 2;
                    while (currentBoard.saveBoard() == false) {
                    }       //try until the board is saved
                    System.exit(0);
                }

                if (myMove.length() != 0) {
                    System.out.println(myColorText + " chooses " + myMove + "\n");
                    currentBoard.showBoard();
                    System.out.println("Waiting for opponent's move...\n");
                } else {
                    if (previousMove.length() == 0) {               //neither player can move
                        currentBoard.status = 2;                    //game over...
                        System.out.println("\nGame over!");
                        int blackScore = 0;
                        int whiteScore = 0;
                        for (int r = 0; r < currentBoard.boardRows; r++) {
                            for (int c = 0; c < currentBoard.boardCols; c++) {
                                if (currentBoard.board[r][c] == 'B') {
                                    blackScore++;
                                } else if (currentBoard.board[r][c] == 'W') {
                                    whiteScore++;
                                }
                            }
                        }
                        if (blackScore > whiteScore) {
                            System.out.println("Blacks wins " + blackScore + " to " + whiteScore);
                        } else if (whiteScore > blackScore) {
                            System.out.println("White wins " + whiteScore + " to " + blackScore);
                        } else {
                            System.out.println("Black and White tie with scores of " + blackScore + " each");
                        }
                    } else {
                        System.out.println("No move available. Opponent gets to move again...");
                    }
                }
                currentBoard.move = myMove;
                currentBoard.whoseTurn = opponentColor;
                while (currentBoard.saveBoard() == false) {
                }           //try until the board is saved
            } else {                                                   //wait a moment then poll again
                try {
                    Thread.sleep(pollDelay);
                } catch (Exception e) {
                }
            }
            currentBoard = null;                                    //get the updated board
            while (currentBoard == null) {
                currentBoard = Board.loadBoard();
            }
        }
        keyboardInput.getKeyboardInput("\nPress ENTER to exit...");
    }

    //***************************************************************************************************
    //***************************************************************************************************
    //Method:           playManually
    //Description:      This method allows the user to play manually.  It prompts the user to enter row
    //                      and col cordinates for their move.  It then checks to see if the move is 
    //                      a valid move.  if it is not a valid move, it prompts the user to select a 
    //                      new move.
    //Parameters:	Board currentBoard  -this is the current board object
    //Calls:		KeyboardInput(Object), Move(Object), getIndex(), checkAround(), makeMoves()
    //Returns:		String myMove       -this is the string containing my current move.
    public static String playManually(Board currentBoard) {
        KeyboardInputClass keyboardInput = new KeyboardInputClass();
        LinkedList<Move> possibleMoves = new LinkedList<>();
        Move currentMove = new Move();
        boolean validMove = false;
        String myMove = "";
        while (!validMove) {
            char charRow = keyboardInput.getCharacter(true, '!', "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1, "Please Select A Row: (Press Enter If There Are No Valid Moves)");
            char charCol = keyboardInput.getCharacter(true, '!', "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1, "Please Select A Col: (Press Enter If There Are No Valid Moves)");
            if (charRow == '!' || charCol == '!') {
                return myMove;
            }//end of if
            int row = getIndex(1, charRow, 2);
            int col = getIndex(1, charCol, 2);

            currentMove = checkAround(currentBoard, row, col, currentMove, myColor, opponentColor);
            if (currentMove.hasMove) {
                validMove = true;
                possibleMoves.add(currentMove);
                myMove = makeMoves(possibleMoves, currentBoard, 3);
            }//end of if             
            else {
                System.out.println("Please Enter A Valid Move");
            }//end of else
        }//end of while      
        return myMove;
    }//end of playManually
    //***************************************************************************************************
    //Method:           makeMoves
    //Description:      This method makes the move and returns the move that it made to the shell.  It 
    //                      takes the possible moves, the currentBoard and the game play type and exicutes 
    //                      the appropriate actions.  
    //Parameters:	LinkedList<Move> possibleMoves  -this is all of the possible moves
    //                  Board currentBoard              -this is the current board object
    //                  int gameType                    -this is the type of game that it is playing
    //Calls:		Board(Object), getBestMove(), getIndex(), Math.random()
    //Returns:		String myMove                   -this is the string containg the contents of the current move
    public static String makeMoves(LinkedList<Move> possileMoves, Board currentBoard, int gameType) {
        String myMove = "";
        if (possileMoves.size() != 0) {
            switch (gameType) {
                case 1: {//Normally
                    int index = getBestMove(possileMoves);
                    currentBoard.board = possileMoves.get(index).board;
                    String row = "" + getIndex(possileMoves.get(index).row, ' ', 1);
                    String col = "" + getIndex(possileMoves.get(index).col, ' ', 1);
                    return row + col;
                }//end of case 1
                case 2: {//Randomly
                    int index = (int) (Math.random() * (possileMoves.size() - 0)) + 0;
                    currentBoard.board = possileMoves.get(index).board;
                    String row = "" + getIndex(possileMoves.get(index).row, ' ', 1);
                    String col = "" + getIndex(possileMoves.get(index).col, ' ', 1);
                    return row + col;
                }//end of case 2
                case 3: {//manually
                    currentBoard.board = possileMoves.get(0).board;
                    String row = "" + getIndex(possileMoves.get(0).row, ' ', 1);
                    String col = "" + getIndex(possileMoves.get(0).col, ' ', 1);
                    return row + col;
                }//end of case 3
            }//end of switch
        }//end of if
        return myMove;
    }//end of make moves
    //***************************************************************************************************
    //Method:           getBestMove
    //Description:      This method looks for the best move out of the LinkedList of possible moves.
    //                      It saves the best score and updates the score and location of score as 
    //                      it looks at the possible moves.  It also considers if a move is a corner 
    //                      or touching a corner.  It the index location of the best move.
    //Parameters:	LinkedList<Move> possibleMoves  -these are all of the possible moves
    //Calls:		Move(Object)
    //Returns:		int indexOfBestMove             -this is the index of the best move
    public static int getBestMove(LinkedList<Move> possibleMoves) {
        int bestScore = 0, indexOfBestScore = 0;
        int bestCornerScore = 0, indexOfBestCorner = 0;
        boolean hasGoodMovies = false;
        for (int i = 0; i < possibleMoves.size(); i++) {
            Move currentMove = possibleMoves.get(i);
            if (currentMove.isACorner) {
                if (currentMove.score > bestCornerScore) {
                    bestCornerScore = currentMove.score;
                    indexOfBestCorner = i;
                    hasGoodMovies = true;
                }//end of if
            }//end of isACorner            
            else {
                if (currentMove.isNearACorner) {
                }//end of if
                else {
                    if (currentMove.score > bestScore) {
                        bestScore = currentMove.score;
                        indexOfBestScore = i;
                        hasGoodMovies = true;
                    }//end of if
                }//end of else
            }//end of else
        }//end of for
        if (!hasGoodMovies) {
            for (int i = 0; i < possibleMoves.size(); i++) {
                Move currentMove = possibleMoves.get(i);
                if (currentMove.score > bestScore) {
                    bestScore = currentMove.score;
                    indexOfBestScore = i;
                }//end of if
            }//end of for
        }//end of if
        if (bestCornerScore > 0) {
            return indexOfBestCorner;
        }//end of if 
        return indexOfBestScore;
    }//end of get best move 
    //***************************************************************************************************
    //Method:           getindex 
    //Description:      This method takes a int and returns a char of takes a char and returns the int.
    //                      If the method is looking for a char then it takes the int that it was passed 
    //                      and gets the letter at that location in the alphabet string.  If the method is
    //                      looking for an in then it takes the char that it was passed and find the index
    //                      of that letter in the alphabet string.  It returns a char containing the result.
    //Parameters:	int passedIdex  -this is the int to be converted to a char
    //                  char passedChar -this is the char to be converted to an int
    //                  int type        -this is the search that the method should exicute
    //Calls:		nothing
    //Returns:		char            -this is the value found by the appropriate search.
    public static char getIndex(int passedIndex, char passedChar, int type) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        switch (type) {
            case 1: {
                char character = alphabet.charAt(passedIndex);
                return character;
            }//end of case 1
            case 2: {
                int index = alphabet.indexOf(passedChar);
                return (char) index;
            }//end of case 2
        }//end of type                
        return ' ';
    }//end of getNumber    
    //***************************************************************************************************
    //Method:           getMoves
    //Description:      This method loops through and finds blank spaces.  Once it finds a blank space,
    //                      it checks to see if it is a valid move.  If it is a valid move, then it adds 
    //                      the move to the possibleMoves linked list.  Once it has searched the whole tree
    //                      it returns the possibleMoves.
    //Parameters:	Board currentBoard          -this is the current board
    //                  char myCurrentColor         -this is my current color 
    //                  char oppCurrentColor        -this is my opponents current color
    //Calls:		Board(Object), Move(Object), checkAround()
    //Returns:		LinkedList possibleMoves    -this is all of the possible moves
    public static LinkedList getMoves(Board currentBoard, char myCurrentColor, char oppCurrentColor) {
        LinkedList<Move> possibleMoves = new LinkedList<Move>();
        for (int row = 0; row < currentBoard.boardRows; row++) {
            for (int col = 0; col < currentBoard.boardCols; col++) {
                Move currentMove = new Move();
                if (currentBoard.board[row][col] == ' ') {
                    currentMove = checkAround(currentBoard, row, col, currentMove, myCurrentColor, oppCurrentColor);
                    if (currentMove.hasMove) {
                        possibleMoves.add(currentMove);
                    }//end of if
                }//end of if
            }//end of for(col)
        }//end of for(Row)
        return possibleMoves;
    }//end of getMove
    //***************************************************************************************************
    //Method:           checkAround 
    //Description:      This method looks to find valid move for the location passed into it.  It first checks 
    //                      basic things such as if the piece is a corner or touching a corner piece.  It then
    //                      checks all direction for valid moves.  If it finds a valid move it sets loops through
    //                      and finds the start and end point for the move.  Once it has run all of the checks it 
    //                      returns the Move.
    //Parameters:	Board crrentBoard   -this is the current board
    //                  int row             -this is the current row
    //                  int col             -this is the current col
    //                  Move currentMove    -this is the current move
    //                  char myCurrentColor -this is my current color 
    //                  char oppCurrentColor-this is the opponents color
    //Calls:		Move(Object), Board(Object), flipPieces()
    //Returns:		Move currentMove    -this is the current move
    public static Move checkAround(Board currentBoard, int row, int col, Move currentMove, char myCurrentColor, char oppCurrentColor) {
        currentMove.row = row;
        currentMove.col = col;
        currentMove.hasMove = false;

        if ((row == 0 && col == 0) || (row == 0 && col == currentBoard.boardCols - 1)
                || (row == currentBoard.boardRows - 1 && col == 0) || (row == currentBoard.boardRows - 1 && col == currentBoard.boardCols - 1)) {
            currentMove.isACorner = true;
        }//end of if 

        if ((row == 0 && col == 1) || (row == 1 && col == 0) || (row == 1 && col == 1)
                || (row == 0 && col == currentBoard.boardCols - 2) || (row == 1 && col == currentBoard.boardCols - 2) || (row == 1 && col == currentBoard.boardCols - 1)
                || (row == currentBoard.boardRows - 2 && col == 0) || (row == currentBoard.boardRows - 2 && col == 1) || (row == currentBoard.boardRows - 1 && col == 1)
                || (row == currentBoard.boardRows - 2 && col == currentBoard.boardCols - 2) || (row == currentBoard.boardRows - 2 && col == currentBoard.boardCols - 1) 
                || (row == currentBoard.boardRows - 1 && col == currentBoard.boardCols - 2)) {
            currentMove.isNearACorner = true;
        }//end of if 

        if (row > 0) {
            //N-----------------------------------------------------------------
            if (currentBoard.board[row - 1][col] == oppCurrentColor) {
                int score = 0;
                for (int newRow = row - 2; newRow >= 0; newRow--) {               //checking row -2 because i have already looked at -1 ^^ so go to next
                    score++;
                    if (currentBoard.board[newRow][col] == myCurrentColor) {
                        currentMove.hasN = true;
                        currentMove.setLocationOfN(row, col, newRow, col);
                        currentMove.hasMove = true;
                        currentMove.score = score;
                        break;
                    }//end of if
                    if (currentBoard.board[newRow][col] == ' ') {
                        break;  //not a move
                    }//end of if ' '
                    //if not either of those two cases then the tile in that position
                    //is opponents tile and the loop will move to the next tile
                }//end of for                
            }//end of if
            //NW----------------------------------------------------------------
            if (col > 0) { //NW
                if (currentBoard.board[row - 1][col - 1] == oppCurrentColor) {
                    int score = 0;
                    for (int newRow = row - 2, newCol = col - 2; newRow >= 0 && newCol >= 0; newRow--, newCol--) {
                        score++;
                        if (currentBoard.board[newRow][newCol] == myCurrentColor) {
                            currentMove.hasNW = true;
                            currentMove.setLocationOfNW(row, col, newRow, newCol);
                            currentMove.hasMove = true;
                            currentMove.score = score;
                            break;
                        }//end of if 
                        if (currentBoard.board[newRow][newCol] == ' ') {
                            break;  //not a move
                        }//end of if ' '
                    }//end of for
                }//end of if
            }//end of NW
            //NE----------------------------------------------------------------
            if (col < currentBoard.boardCols - 1) {  //NE
                if (currentBoard.board[row - 1][col + 1] == oppCurrentColor) {
                    int score = 0;
                    for (int newRow = row - 2, newCol = col + 2; newRow >= 0 && newCol < currentBoard.boardCols; newRow--, newCol++) {
                        score++;
                        if (currentBoard.board[newRow][newCol] == myCurrentColor) {
                            currentMove.hasNE = true;
                            currentMove.setLocationOfNE(row, col, newRow, newCol);
                            currentMove.hasMove = true;
                            currentMove.score = score;
                            break;
                        }//end of if
                        if (currentBoard.board[newRow][newCol] == ' ') {
                            break;  //not a move
                        }//end of if ' '
                    }//end of for                    
                }//end of if 
            }//end NE
        }//end of if row > 0  
        if (row < currentBoard.boardRows - 1) {
            //S-----------------------------------------------------------------
            if (currentBoard.board[row + 1][col] == oppCurrentColor) {
                int score = 0;
                for (int newRow = row + 2; newRow < currentBoard.boardRows; newRow++) {
                    score++;
                    if (currentBoard.board[newRow][col] == myCurrentColor) {
                        currentMove.hasS = true;
                        currentMove.setLocationOfS(row, col, newRow, col);
                        currentMove.hasMove = true;
                        currentMove.score = score;
                        break;
                    }//end of if
                    if (currentBoard.board[newRow][col] == ' ') {
                        break;  //not a move
                    }//end of if ' '                    
                }//end of for                
            }//end of if
            //SE----------------------------------------------------------------           
            if (col < currentBoard.boardCols - 1) {
                if (currentBoard.board[row + 1][col + 1] == oppCurrentColor) {
                    int score = 0;
                    for (int newRow = row + 2, newCol = col + 2; newRow < currentBoard.boardRows && newCol < currentBoard.boardCols; newRow++, newCol++) {
                        score++;
                        if (currentBoard.board[newRow][newCol] == myCurrentColor) {
                            currentMove.hasSE = true;
                            currentMove.setLocationOfSE(row, col, newRow, newCol);
                            currentMove.hasMove = true;
                            currentMove.score = score;
                            break;
                        }//end if 
                        if (currentBoard.board[newRow][newCol] == ' ') {
                            break;  //not a move
                        }//end of if ' '                         
                    }//end of for
                }//end of if
            }//end of SE
            //SW----------------------------------------------------------------            
            if (col > 0) {
                if (currentBoard.board[row + 1][col - 1] == oppCurrentColor) {
                    int score = 0;
                    for (int newRow = row + 2, newCol = col - 2; newRow < currentBoard.boardRows && newCol >= 0; newRow++, newCol--) {
                        score++;
                        if (currentBoard.board[newRow][newCol] == myCurrentColor) {
                            currentMove.hasSW = true;
                            currentMove.setLocationOfSW(row, col, newRow, newCol);
                            currentMove.hasMove = true;
                            currentMove.score = score;
                            break;
                        }//end of if
                        if (currentBoard.board[newRow][newCol] == ' ') {
                            break;  //not a move
                        }//end of if ' '                        
                    }//end of for
                }//end of if
            }//end of SW
        }//end of if for < boardRows
        //E-----------------------------------------------------------------
        if (col < currentBoard.boardCols - 1) {
            if (currentBoard.board[row][col + 1] == oppCurrentColor) {
                int score = 0;
                for (int newCol = col + 2; newCol < currentBoard.boardCols; newCol++) {
                    score++;
                    if (currentBoard.board[row][newCol] == myCurrentColor) {
                        currentMove.hasE = true;
                        currentMove.setLocationOfE(row, col, row, newCol);
                        currentMove.hasMove = true;
                        currentMove.score = score;
                        break;
                    }//end of if
                    if (currentBoard.board[row][newCol] == ' ') {
                        break;  //not a move
                    }//end of if ' '
                }//end of for                    
            }//end of if
        }//end of E
        //W-----------------------------------------------------------------
        if (col > 0) {
            if (currentBoard.board[row][col - 1] == oppCurrentColor) {
                int score = 0;
                for (int newCol = col - 2; newCol >= 0; newCol--) {
                    score++;
                    if (currentBoard.board[row][newCol] == myCurrentColor) {
                        currentMove.hasW = true;
                        currentMove.setLocationOfW(row, col, row, newCol);
                        currentMove.hasMove = true;
                        currentMove.score = score;
                        break;
                    }//end of if 
                    if (currentBoard.board[row][newCol] == ' ') {
                        break;  //not a move
                    }//end of if ' '
                }//end of for
            }//end of if
        }//end of W
        if (currentMove.hasMove == true) {
            currentMove.board = flipPieces(currentMove, currentBoard, myCurrentColor);
        }//end of if
        return currentMove;
    }//end of check around
    //***************************************************************************************************
    //Method:           flipPieces
    //Description:      This method checks to see what valid moves the current move has and then goes through and 
    //                      flips all of the pieces between the beguinning and end point for each direction.
    //Parameters:	Move currentMove    -this is the current Move object 
    //                  Board currentBoard  -this is the current Board object 
    //                  char pieceColor     -this is the current piece color
    //Calls:		Move(Object), createBoard()
    //Returns:		char[][]            -Move.currentBoard
    public static char[][] flipPieces(Move currentMove, Board currentBoard, char pieceColor) {
        currentMove.board = createBoard(currentBoard);
        int row = currentMove.row;
        int col = currentMove.col;
        //NW------------------------------------------------------------------------
        if (currentMove.hasNW) {
            int endRow = currentMove.locationOfNW.get(2);
            int endCol = currentMove.locationOfNW.get(3);
            for (int newRow = row, newCol = col; newRow != endRow && newCol != endCol; newRow--, newCol--) {
                currentMove.board[newRow][newCol] = pieceColor;
            }//end of for
        }//end of NW        
        //N-------------------------------------------------------------------------
        if (currentMove.hasN) {
            int endRow = currentMove.locationOfN.get(2);
            for (int newRow = row; newRow != endRow; newRow--) {
                currentMove.board[newRow][col] = pieceColor;
            }//end of for
        }//end of if
        //NE------------------------------------------------------------------------
        if (currentMove.hasNE) {
            int endRow = currentMove.locationOfNE.get(2);
            int endCol = currentMove.locationOfNE.get(3);
            for (int newRow = row, newCol = col; newRow != endRow && newCol != endCol; newRow--, newCol++) {
                currentMove.board[newRow][newCol] = pieceColor;
            }//end of for
        }//end of if
        //E-------------------------------------------------------------------------
        if (currentMove.hasE) {
            int endCol = currentMove.locationOfE.get(3);
            for (int newCol = col; newCol != endCol; newCol++) {
                currentMove.board[row][newCol] = pieceColor;
            }//end of for
        }//end of if
        //SE------------------------------------------------------------------------
        if (currentMove.hasSE) {
            int endRow = currentMove.locationOfSE.get(2);
            int endCol = currentMove.locationOfSE.get(3);
            for (int newRow = row, newCol = col; newRow != endRow && newCol != endCol; newRow++, newCol++) {
                currentMove.board[newRow][newCol] = pieceColor;
            }//end of for            
        }//end of if 
        //S-------------------------------------------------------------------------
        if (currentMove.hasS) {
            int endRow = currentMove.locationOfS.get(2);
            for (int newRow = row; newRow != endRow; newRow++) {
                currentMove.board[newRow][col] = pieceColor;
            }//end of for
        }//end of if
        //SW------------------------------------------------------------------------
        if (currentMove.hasSW) {
            int endRow = currentMove.locationOfSW.get(2);
            int endCol = currentMove.locationOfSW.get(3);
            for (int newRow = row, newCol = col; newRow != endRow && newCol != endCol; newRow++, newCol--) {
                currentMove.board[newRow][newCol] = pieceColor;
            }//end of for
        }//end of if
        //W-------------------------------------------------------------------------
        if (currentMove.hasW) {
            int endCol = currentMove.locationOfW.get(3);
            for (int newCol = col; newCol != endCol; newCol--) {
                currentMove.board[row][newCol] = pieceColor;
            }//end of for
        }//end of if
        return currentMove.board;
    }//end of flipPieces
    //***************************************************************************************************
    //Method:           createBoard
    //Description:      This method takes creates a deep copy of the board char[][] 
    //Parameters:	Board currentBoard  -this is the current board object
    //Calls:		Board(Object)
    //Returns:		char[][] board      -this is the copy of the board
    public static char[][] createBoard(Board currentBoard) {
        char[][] board = new char[currentBoard.boardRows][currentBoard.boardCols];
        for (int row = 0; row < currentBoard.boardRows; row++) {
            for (int col = 0; col < currentBoard.boardCols; col++) {
                char fromCurrentBoard = currentBoard.board[row][col];
                board[row][col] = fromCurrentBoard;
            }//end of col
        }//end of row
        return board;
    }//end of createBoard

}//end of lynch4
//*******************************************************************************************************
//Class:        Move
//Description:  All of the options of the current move 
class Move {

    char[][] board;                                             //this is a copy of the actual board
    int score;                                                  //this is the total score of this move
    int row;                                                    //this is the row value for this move
    int col;                                                    //this is the col value for this move 
    boolean isACorner;                                          //this move is a corner
    boolean isNearACorner;                                      //this move is touching a corner piece
    boolean hasMove;                                            //this move is valid

    boolean hasNW;                                              //this move has a move in the NW direction
    LinkedList<Integer> locationOfNW = new LinkedList<>();      //this contains the location of the start and end of that move
    boolean hasN;                                               //this move has a move in the N direction
    LinkedList<Integer> locationOfN = new LinkedList<>();       //this contains the location of the start and end of that move
    boolean hasNE;                                              //this move has a move in the NE direction
    LinkedList<Integer> locationOfNE = new LinkedList<>();      //this contains the location of the start and end of that move
    boolean hasE;                                               //this move has a move in the E direction
    LinkedList<Integer> locationOfE = new LinkedList<>();       //this contains the location of the start and end of that move
    boolean hasSE;                                              //this move has a move in the SE direction
    LinkedList<Integer> locationOfSE = new LinkedList<>();      //this contains the location of the start and end of that move
    boolean hasS;                                               //this move has a move in the S direction
    LinkedList<Integer> locationOfS = new LinkedList<>();       //this contains the location of the start and end of that move
    boolean hasSW;                                              //this move has a move in the SW direction
    LinkedList<Integer> locationOfSW = new LinkedList<>();      //this contains the location of the start and end of that move
    boolean hasW;                                               //this move has a move in the W direction
    LinkedList<Integer> locationOfW = new LinkedList<>();       //this contains the location of the start and end of that move

    //***************************************************************************************************
    //Method:           setLocationOfNW
    //Description:      Constructor to create create the linked list containing the full position of a move. 
    //Parameters:	int startRow    -this is the row value for the start
    //                  int startCol    -this is the col value for the start 
    //                  int endRow      -this is the row value for the end
    //                  int endCol      -this is the col value for the end
    //Calls:		locationOfNW(Global)
    //Returns:		nothing
    public void setLocationOfNW(int startRow, int startCol, int endRow, int endCol) {
        locationOfNW.add(startRow);
        locationOfNW.add(startCol);
        locationOfNW.add(endRow);
        locationOfNW.add(endCol);
    }//end of setLocationOfNW
    //***************************************************************************************************
    //Method:           setLocationOfN
    //Description:      Constructor to create create the linked list containing the full position of a move. 
    //Parameters:	int startRow    -this is the row value for the start
    //                  int startCol    -this is the col value for the start 
    //                  int endRow      -this is the row value for the end
    //                  int endCol      -this is the col value for the end
    //Calls:		locationOfN(Global)
    //Returns:		nothing
    public void setLocationOfN(int startRow, int startCol, int endRow, int endCol) {
        locationOfN.add(startRow);
        locationOfN.add(startCol);
        locationOfN.add(endRow);
        locationOfN.add(endCol);
    }//end of setLocationOfN
    //***************************************************************************************************
    //Method:           setLocationOfNE
    //Description:      Constructor to create create the linked list containing the full position of a move. 
    //Parameters:	int startRow    -this is the row value for the start
    //                  int startCol    -this is the col value for the start 
    //                  int endRow      -this is the row value for the end
    //                  int endCol      -this is the col value for the end
    //Calls:		locationOfNE(Global)
    //Returns:		nothing
    public void setLocationOfNE(int startRow, int startCol, int endRow, int endCol) {
        locationOfNE.add(startRow);
        locationOfNE.add(startCol);
        locationOfNE.add(endRow);
        locationOfNE.add(endCol);
    }//end of setLocationOfNE
    //***************************************************************************************************
    //Method:           setLocationOfE
    //Description:      Constructor to create create the linked list containing the full position of a move. 
    //Parameters:	int startRow    -this is the row value for the start
    //                  int startCol    -this is the col value for the start 
    //                  int endRow      -this is the row value for the end
    //                  int endCol      -this is the col value for the end
    //Calls:		locationOfE(Global)
    //Returns:		nothing
    public void setLocationOfE(int startRow, int startCol, int endRow, int endCol) {
        locationOfE.add(startRow);
        locationOfE.add(startCol);
        locationOfE.add(endRow);
        locationOfE.add(endCol);
    }//end of setLocationOfE
    //***************************************************************************************************
    //Method:           setLocationOfSE
    //Description:      Constructor to create create the linked list containing the full position of a move. 
    //Parameters:	int startRow    -this is the row value for the start
    //                  int startCol    -this is the col value for the start 
    //                  int endRow      -this is the row value for the end
    //                  int endCol      -this is the col value for the end
    //Calls:		locationOfSE(Global)
    //Returns:		nothing
    public void setLocationOfSE(int startRow, int startCol, int endRow, int endCol) {
        locationOfSE.add(startRow);
        locationOfSE.add(startCol);
        locationOfSE.add(endRow);
        locationOfSE.add(endCol);
    }//end of setLocationOfSE
    //***************************************************************************************************
    //Method:           setLocationOfS
    //Description:      Constructor to create create the linked list containing the full position of a move. 
    //Parameters:	int startRow    -this is the row value for the start
    //                  int startCol    -this is the col value for the start 
    //                  int endRow      -this is the row value for the end
    //                  int endCol      -this is the col value for the end
    //Calls:		locationOfS(Global)
    //Returns:		nothing
    public void setLocationOfS(int startRow, int startCol, int endRow, int endCol) {
        locationOfS.add(startRow);
        locationOfS.add(startCol);
        locationOfS.add(endRow);
        locationOfS.add(endCol);
    }//end of setLocationOfS
    //***************************************************************************************************
    //Method:           setLocationOfSW
    //Description:      Constructor to create create the linked list containing the full position of a move. 
    //Parameters:	int startRow    -this is the row value for the start
    //                  int startCol    -this is the col value for the start 
    //                  int endRow      -this is the row value for the end
    //                  int endCol      -this is the col value for the end
    //Calls:		locationOfSW(Global)
    //Returns:		nothing
    public void setLocationOfSW(int startRow, int startCol, int endRow, int endCol) {
        locationOfSW.add(startRow);
        locationOfSW.add(startCol);
        locationOfSW.add(endRow);
        locationOfSW.add(endCol);
    }//end of setLocationOfSW
    //***************************************************************************************************
    //Method:           setLocationOfW
    //Description:      Constructor to create create the linked list containing the full position of a move. 
    //Parameters:	int startRow    -this is the row value for the start
    //                  int startCol    -this is the col value for the start 
    //                  int endRow      -this is the row value for the end
    //                  int endCol      -this is the col value for the end
    //Calls:		locationOfW(Global)
    //Returns:		nothing
    public void setLocationOfW(int startRow, int startCol, int endRow, int endCol) {
        locationOfW.add(startRow);
        locationOfW.add(startCol);
        locationOfW.add(endRow);
        locationOfW.add(endCol);
    }//end of setLocationOfW
}//end of move
//*******************************************************************************************************
//*******************************************************************************************************
//Class:        Board
//Description:  Othello board and related parms
class Board implements Serializable {

    char status;        //0=set-up for a new game is in progress; 1=a game is in progress; 2=game is over
    char whoseTurn;     //'?'=no one's turn yet--game has not begun; 'B'=black; 'W'=white
    String move;        //the move selected by the current player (as indicated by whoseTurn)
    char colorSelected; //'B' or 'W' indicating the color chosen by the first player to access the file
    //for a new game ('?' if neither player has yet chosen a color)
    //Note: this may or may not be the color for the player accessing the file
    int maxMoveTime;    //maximum time allotted for a move (in seconds)
    int boardRows;      //size of the board (allows for variations on the standard 8x8 board)
    int boardCols;
    char board[][];     //the board. Positions are filled with: blank = no piece; 'B'=black; 'W'=white
    //***************************************************************************************************
    //Method:       Board
    //Description:  Constructor to create a new board object
    //Parameters:	rows - size of the board
    //              cols
    //              time - maximum time (in seconds) allowed per move
    //Calls:		nothing
    //Returns:		nothing

    Board(int rows, int cols, int time) {
        int r, c;
        status = 0;
        whoseTurn = 'B';        //Black always makes the first move
        move = "*";
        colorSelected = '?';
        maxMoveTime = time;
        boardRows = rows;
        boardCols = cols;
        board = new char[boardRows][boardCols];
        for (r = 0; r < boardRows; r++) {
            for (c = 0; c < boardCols; c++) {
                board[r][c] = ' ';
            }
        }
        r = boardRows / 2 - 1;
        c = boardCols / 2 - 1;
        board[r][c] = 'W';
        board[r][c + 1] = 'B';
        board[r + 1][c] = 'B';
        board[r + 1][c + 1] = 'W';
    }

    //***************************************************************************************************
    //Method:       saveBoard
    //Description:  Saves the current board to disk as a binary file named "OthelloBoard"
    //Parameters:	none
    //Calls:		nothing
    //Returns:		true if successful; false otherwise
    public boolean saveBoard() {
        try {
            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("OthelloBoard"));
            outStream.writeObject(this);
            outStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //***************************************************************************************************
    //Method:       loadBoard
    //Description:  Loads the current Othello board and data from a binary file
    //Parameters:   none
    //Calls:        nothing
    //Returns:      a Board object (or null if routine is unsuccessful)
    public static Board loadBoard() {
        try {
            ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("OthelloBoard"));
            Board boardObject = (Board) inStream.readObject();
            inStream.close();
            return boardObject;
        } catch (Exception e) {
        }
        return null;
    }

    //***************************************************************************************************
    //Method:       showBoard
    //Description:  Displays the current Othello board using extended Unicode characters. Looks fine
    //               in a command window but may not display well in the NetBeans IDE...
    //Parameters:   none
    //Calls:        nothing
    //Returns:      nothing
    public void showBoard() {
        int r, c;
        System.out.print("  ");                         //column identifiers
        for (c = 0; c < boardCols; c++) {
            System.out.print(" " + (char) (c + 65));
        }
        System.out.println();

        //top border
        System.out.print("  " + (char) 9484);                   //top left corner \u250C
        for (c = 0; c < boardCols - 1; c++) {
            System.out.print((char) 9472);               //horizontal \u2500
            System.out.print((char) 9516);               //vertical T \u252C
        }
        System.out.print((char) 9472);                   //horizontal \u2500
        System.out.println((char) 9488);                 //top right corner \u2510

        //board rows
        for (r = 0; r < boardRows; r++) {
            System.out.print(" " + (char) (r + 65));         //row identifier
            System.out.print((char) 9474);               //vertical \u2502
            for (c = 0; c < boardCols; c++) {
                System.out.print(board[r][c]);
                System.out.print((char) 9474);           //vertical \u2502
            }
            System.out.println();

            //insert row separators
            if (r < boardRows - 1) {
                System.out.print("  " + (char) 9500);           //left T \u251C
                for (c = 0; c < boardCols - 1; c++) {
                    System.out.print((char) 9472);       //horizontal \u2500
                    System.out.print((char) 9532);       //+ (cross) \u253C
                }
                System.out.print((char) 9472);           //horizontal \u2500
                System.out.println((char) 9508);         //right T \u2524
            }
        }

        //bottom border
        System.out.print("  " + (char) 9492);                   //lower left corner \u2514
        for (c = 0; c < boardCols - 1; c++) {
            System.out.print((char) 9472);               //horizontal \u2500
            System.out.print((char) 9524);               //upside down T \u2534
        }
        System.out.print((char) 9472);                   //horizontal \u2500
        System.out.println((char) 9496);                 //lower right corner \u2518

        return;
    }
    //***************************************************************************************************
}
//*******************************************************************************************************
//*******************************************************************************************************