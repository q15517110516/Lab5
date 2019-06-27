# Lab5
## To build the projects
* Build RioBLL first, with ‘clean install’ goals
* Build RioJabber next, with ‘clean install’ goals
* Build RioClient last, with ‘jfx:jar’ goal.  This is how a JavaFx project is built with maven (jfx:jar will create a jar, jfx:run will build and then run the program)

This lab will handle the Sit/Leave function.  You’ll implement the code required to execute Sit/Leave.

To do this, you have to understand how the projects work together.  When the RioClient project starts, the first player will start a new PokerHub and create and attach a PokerClient to the PokerHub.  Each additional player will create a PokerClient and attach it to the running PokerHub.

If there are three players, there is one PokerHub and three PokerClients, and all three PokerClients are attached to the PokerHub.

## Here’s how the Client and Hub communicate:

MainApp has references to the PokerHub and PokerClient for each player.  This is the main class that’s always in memory.  When a button is pressed in one of the controllers, it will create an ‘Action’ instance, and send that instance to MainApp.messageSend(object).  MessageSend(object) will call the private messageSend() method in the PokerClient inner class.  messageSend() in the PokerClient will add that message to the PokerHub’s LBQ.

When the PokerHub is started, it’s monitoring its LBQ waiting for a message to be sent (in other words, it’s running the take() method which is a blocking method).  When a message is received, it’s your job to interrogate the message to determine what the message is (is it an ‘Action’?  What kind of Action?).  The purpose of the PokerHub is to keep the current state of the table and game in memory. 

After the hub does its work, the Hub will send a message back to one (or all) clients.  The client will receive a message and then update the UI to show the updated Table or Game status.  

## How to implement Sit/Leave:
1.	Implement the PokerTableController.btnSitLeave_Click method.  I gave you this implementation.  This shows you how to create an Action instance and how to send it to the mainApp application.
2.	Ensure mainApp.messageSend(object) is calling the message send in the inner client.  This has already been done.  This should *not* have to be changed when we add other Actions/messages.
3.	Ensure mainApp.PokerClient.messageSend(object) is implemented.  I gave you this implementation.  This should *not* have to be changed when we add other Actions/messages.
4.	Implement PokerHub.messageReceived.  This has NOT been done for you. 
    *	Check to see if the ‘message’ is an Action.
    *	If the type of Action is ‘Sit’, add the player to the HubPokerTable instance.  The method to add the player is already implemented in RioBLL.Table
    *	If the type of Action is ‘Leave’, remove the player from HubPokerTable.  The method to remove the player is already implemented in RioBLL.Table
    *	Send the HubPokerTable object back to the client using the ‘sendToAll(object)’ method.
5.	Implement mainApp.PokerClient.messageReceived(object) method to send the state of the Table back to the PokerTableController.  Check out the method PokerTableController. MessageFromMainApp- that’s how to send a String sent from the Hub to the controller.  
6.	Add a label in the UI for Player Name.  Make sure you have a label for each position.  A label for Position 1, Position 2, etc
7.	Implement Handle_TableState(Table) in PokerTableController.  This method will take an updated Table object from the Hub and update the UI.
    *	If ‘Joe’ sits in position 2, the following should be done:
        *	 the ‘Sit’ button in Position 2 should appear pressed in, and ‘Leave’ should be the text in the button.
        *	Joe should not be able to click any of the other ‘Sit’ buttons.  Those buttons should not be visible to Joe
        *	Every other client should not be able to click the ‘Sit’ button for Position 2.  This button should not be visible to the other clients
    *	If ‘Joe’ leaves position 2, the following should be done
        *	The ‘Sit’ button in Position 2 should appear to be not pressed in, and ‘Sit’ should be the text of the button.
        *	Joe should be able to ‘Sit’ at any other position
        *	The ‘Sit’ button should be visible and clickable to the other clients that aren’t already seated.
