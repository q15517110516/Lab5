package poker.app.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import exceptions.DeckException;
import netgame.common.Hub;
import pokerBase.Action;
import pokerBase.Card;
import pokerBase.Deck;
import pokerBase.GamePlay;
import pokerBase.GamePlayPlayerHand;
import pokerBase.Player;
import pokerBase.Rule;
import pokerBase.Table;
import pokerEnums.eAction;
import pokerEnums.eGame;
import pokerEnums.eGameState;

public class PokerHub extends Hub {

	
	private Table HubPokerTable = new Table();
	private GamePlay HubGamePlay;
	private int iDealNbr = 0;
	// private PokerGameState state;
	private eGameState eGameState;

	public PokerHub(int port) throws IOException {
		super(port);
	}

	protected void playerConnected(int playerID) {

		if (playerID == 2) {
			shutdownServerSocket();
		}
	}

	protected void playerDisconnected(int playerID) {
		shutDownHub();
	}

	protected void messageReceived(int ClientID, Object message) {

		if (message instanceof Action) {
			Action act = (Action) message;
			switch (act.getAction()) {
			case GameState:
				sendToAll(HubPokerTable);
				break;
			case TableState:
				resetOutput();
				sendToAll(HubPokerTable);
				break;
			case Sit:
				resetOutput();
				HubPokerTable.AddPlayerToTable(act.getPlayer());
				sendToAll(HubPokerTable);
				break;
			case Leave:
				resetOutput();
				HubPokerTable.RemovePlayerFromTable(act.getPlayer());
				sendToAll(HubPokerTable);
				break;

			case StartGame:
				System.out.println("Starting Game!");
				resetOutput();

				// TODO - Lab #5 Do all the things you need to do to start a
				// game!!

				// Determine which game is selected (from RootTableController)
				// 1 line of code
				//#1 We saw that there was a method to get the game selected and used it
                                  eGame game = act.geteGame();

				// Get the Rule based on the game selected
				// 1 line of code
                                //#2 The solution to this was explained in piazza.
				Rule rule = new Rule(game);

				// The table should eventually allow multiple instances of
				// 'GamePlay'...
				// Each game played is an instance of 'GamePlay'...
				// For the first instance of GamePlay, pick a random player to
				// be the
				// 'Dealer'...
				// < 5 lines of code to pick random player
                                //#3 Using the same logic from the previous steps, we assumed that
                                //a method with a name that tells us it will pick a random player to assign a dealer
				Player dealer = HubPokerTable.PickRandomPlayerAtTable();

				// Start a new instance of GamePlay, based on rule set and
				// Dealer (Player.PlayerID)
				// 1 line of code
                                //#4 This step is similar to the previous steps: using the literal names of methods to put 
                                //together a new instance of gameplay based on the previously set rules and dealer
				HubGamePlay = new GamePlay(rule,dealer.getPlayerID());
				
				// There are 1+ players seated at the table... add these players
				// to the game
				// < 5 lines of code
                                //#5 We used the methods that get players and set them in order to add players to the game
				HubGamePlay.setGamePlayers(HubPokerTable.getHashPlayers());
			
				// GamePlay has a deck... create the deck based on the game's
				// rules (the rule
				// will have number of jokers... wild cards...
				// 1 line of code
                                //#6 Similar logic as previous step, but now for a deck
				HubGamePlay.setGameDeck(new Deck());

				// Determine the order of players and add each player in turn to
				// GamePlay.lnkPlayerOrder
				// Example... four players playing... seated in Position 1, 2,
				// 3, 4
				// Dealer = Position 2
				// Order should be 3, 4, 1, 2
				// Example... three players playing... seated in Position 1, 2,
				// 4
				// Dealer = Position 4
				// Order should be 1, 2, 4
				// < 10 lines of code
                                //#8 Found a method that sets the order and used it
				HubGamePlay.setiActOrder(GamePlay.GetOrder(dealer.getiPlayerPosition()));

				// Set PlayerID_NextToAct in GamePlay (next player after Dealer)
				// 1 line of code
                                //#9 Found a method that sets the next player and used it
				HubGamePlay.setPlayerNextToAct(HubGamePlay.getPlayerByPosition(HubGamePlay.getiActOrder()[0]));

				// Send the state of the game back to the players
				sendToAll(HubGamePlay);
				break;
			case Deal:

				/*
				 * int iCardstoDraw[] = HubGamePlay.getRule().getiCardsToDraw();
				 * int iDrawCount = iCardstoDraw[iDealNbr];
				 * 
				 * for (int i = 0; i<iDrawCount; i++) { try { Card c =
				 * HubGamePlay.getGameDeck().Draw(); } catch (DeckException e) {
				 * e.printStackTrace(); } }
				 */
				break;
			}
		}

		// System.out.println("Message Received by Hub");
	}

}