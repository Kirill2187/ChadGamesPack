package com.chadgames.gamespack.tictactoe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.chadgames.gamespack.games.GameType;
import com.chadgames.gamespack.games.MoveData;
import com.chadgames.gamespack.games.tictactoe.TicTacToeConstants;
import com.chadgames.gamespack.games.tictactoe.TicTacToeFactory;
import com.chadgames.gamespack.games.tictactoe.TicTacToeMoveData;
import com.chadgames.gamespack.games.tictactoe.TicTacToeState;
import com.chadgames.gamespack.utils.Constants;
import com.chadgames.gamespack.utils.Player;

public class TicTacToeLogicTest {

    private TicTacToeFactory factory;
    private TicTacToeState state;
    private final Player player1 = new Player();
    private final Player player2 = new Player();

    @Before
    public void setUp() {
        factory = (TicTacToeFactory) Constants.GAME_FACTORIES.get(GameType.TicTacToe);
        state = (TicTacToeState) factory.createState();
        player1.username = "player1";
        player2.username = "player2";
        player1.id = 0;
        player2.id = 1;
    }

    @Test
    public void simpleTest() {
        state.playerJoined(player1);
        state.playerLeft(player1);
        state.playerJoined(player2);
        state.playerJoined(player1);
        state.startGame();

        assertTrue(state.isGameStarted());

        int curMove = state.currentPlayerId;

        assertFalse(state.checkMove(generateMoveData((curMove + 1) % 2, 0, 0)));
        assertFalse(state.checkMove(generateMoveData(curMove, -1, 0)));
        assertFalse(state.checkMove(generateMoveData(curMove, 0, TicTacToeConstants.SIZE)));
        assertTrue(state.checkMove(generateMoveData(curMove, 0, 0)));

        state.makeMove(generateMoveData(curMove, 1, 1));

        assertFalse(state.checkMove(generateMoveData(curMove, 0, 1)));
        assertFalse(state.checkMove(generateMoveData((curMove + 1) % 2, 1, 1)));
        assertEquals(-1, state.getWinner());

        state.makeMove(generateMoveData((curMove + 1) % 2, 0, 0));
        state.makeMove(generateMoveData(curMove, 0, 1));
        state.makeMove(generateMoveData((curMove + 1) % 2, 0, 2));
        state.makeMove(generateMoveData(curMove, 2, 1));

        assertEquals(curMove, state.getWinner());

    }

    private TicTacToeMoveData generateMoveData(int id, int x, int y) {
        TicTacToeMoveData data = new TicTacToeMoveData(x, y);
        data.playerId = id;
        return data;
    }

}
