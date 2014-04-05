README for TICTACTOE

Protocol:
REGISTER - allows client to register with the server
JOIN - tell the server that you want to join a game
GAMESTART - server notification to clients telling them that the server has started a game and gives the opponent ID number
PLAY - play your move
ENDGAME - send opponent notification that the game is over. Individual client figures out loss/tie/win
