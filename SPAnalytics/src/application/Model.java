package application;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javafx.concurrent.Task;

public class Model {
	//The database data is found here: https://console.firebase.google.com/u/0/project/discovery-8d956/database/discovery-8d956/data
		public void makeDatabase() {
			FileInputStream serviceAccount;
			try {
				serviceAccount = new FileInputStream("/Users/nicolematthews/Desktop/discovery-8d956-firebase-adminsdk-wib15-1d3864509e.json");
				
				// Initialize the app with a custom auth variable, limiting the server's access
				GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
				
				FirestoreOptions firestoreOptions = 
						FirestoreOptions.newBuilder().setTimestampsInSnapshotsEnabled(true).build();

				FirebaseOptions options = new FirebaseOptions.Builder()
				    .setCredentials(credentials).setFirestoreOptions(firestoreOptions)
				    .build();
				FirebaseApp.initializeApp(options);
			
				Firestore db = FirestoreClient.getFirestore();

				FirebaseApp.initializeApp(options,"data");
				DocumentReference docRef = db.collection("information").document("players");
				
				//getPlayer(db, "6");
				//getPlayers(db);
				getGameStats(db, "Miami vs. Omaha");
				//addMember(docRef);
				//addGoalie(docRef);
				//deletePlayerGoalie(docRef);
				DocumentReference docRefTeam = db.collection("information").document("team");
				//createTeam(docRefTeam);
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		//example of adding a team
		public void createTeam(DocumentReference docRefTeam) {
		// TODO Auto-generated method stub
		Team team = new Team("Miami", 14, 44, 35, 468, 405,
					"9-3-1-1");
		Map<String, Game> games = new HashMap<String, Game>();
		Game game1 = new Game();
		game1.gameName = "Miami vs. Omaha";
		game1.finalScore = "3-1";
	
		TeamScore offense = new TeamScore("Miami", 4, 3, 6, 7);
		TeamScore defense = new TeamScore("Omaha", 5, 2, 7, 1);
		
		game1.offense = offense;
		game1.defense = defense;
		
		games.put(game1.gameName, game1);
		team.games = games;
		
		//add to database
		addTeam(docRefTeam, team);
		
	}

		/**
		 * Example method of deleting a goalie
		 * @param docRef
		 */
		public void deletePlayerGoalie(DocumentReference docRef) {
			Position position = new Position("goalie");
			 Goalie goalie = new Goalie(4, 3, 2);
			  position.goalie(goalie);
			  PlayerStats stats1 = new PlayerStats("16-17", 6, position);	 
			  Map<String, PlayerStats> stats = new HashMap<String, PlayerStats>();
			  stats.put(stats1.season, stats1);
			  Player player = new Player(31, "Ryan Larkin", "6'1", "201 lbs", "6/10/97", "Clarkson, MI", stats);
			 
			deletePlayer(docRef, player);
		}
		/**
		 * Example method of adding a goalie
		 * @param docRef
		 */
		public void addGoalie(DocumentReference docRef) {
			Position position = new Position("goalie");
			 Goalie goalie = new Goalie(4, 3, 2);
			 position.goalie(goalie);
			  PlayerStats stats1 = new PlayerStats("16-17", 6, position);
			  Map<String, PlayerStats> stats = new HashMap<String, PlayerStats>();
			  stats.put(stats1.season, stats1);
			  Player player = new Player(31, "Ryan Larkin", "6'1", "201 lbs", "6/10/97", "Clarkson, MI", stats);
			 
			addPlayer(docRef, player);
		}
		/**
		 * Example method of adding a player
		 * @param docRef
		 */
		public void addMember(DocumentReference docRef) {
			Position position = new Position("defense");
			 Member defense = new Member(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
			  position.member(defense);
			  PlayerStats stats1 = new PlayerStats("16-17", 8, position);
			  Map<String, PlayerStats> stats = new HashMap<String, PlayerStats>();
			  stats.put(stats1.season, stats1);
			  Player player = new Player(6, "Alec Mahalak", "5'9", "161 lbs", "9/14/98", "Monroe, MI", stats);
			 
			addPlayer(docRef, player);
		}

		/**
		 * add player to the database (does not override the database only appends to it)
		 * @param docRef
		 * @param player
		 */
		public void addPlayer(DocumentReference docRef, Player player) {

			  Map<String, Player> players = new HashMap<>();
			 
			  players.put(""+player.jerseyNo, player);
			  
			  ApiFuture<WriteResult> result = docRef.set(players, SetOptions.merge());
			// ...
			// result.get() blocks on response
			try {
				System.out.println("Update time : " + result.get().getUpdateTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		
		/**
		 * adds an array of players to the database (does not override the database only appends to it)
		 * @param docRef
		 * @param player
		 */
		public void addPlayers(DocumentReference docRef, Map<String, Player> players) {

			  
			  ApiFuture<WriteResult> result = docRef.set(players, SetOptions.merge());
			// ...
			// result.get() blocks on response
			try {
				System.out.println("Update time : " + result.get().getUpdateTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		/**
		 * Removes all players from the document (essentially deleting the document players)
		 * @param docRef
		 */
		public void removeAllPlayers(DocumentReference docRef) {

			  ApiFuture<WriteResult> result = docRef.delete();
			// ...
			// result.get() blocks on response
			try {
				System.out.println("Update time : " + result.get().getUpdateTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		
		/**
		 * Remove existing player from database
		 */
		public void deletePlayer(DocumentReference docRef, Player player) {

			  Map<String, Object> players = new HashMap<>();
			 
			  players.put(""+player.jerseyNo, FieldValue.delete());
			  
			  ApiFuture<WriteResult> result = docRef.set(players, SetOptions.merge());
			// ...
			// result.get() blocks on response
			try {
				System.out.println("Update time : " + result.get().getUpdateTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		
		/**
		 * Add a team to the database
		 * @param docRef
		 * @param team
		 */
		public void addTeam(DocumentReference docRef, Team team) {
			  Map<String, Team> teams = new HashMap<>();
				 
			  teams.put(team.teamName, team);
			  
			  ApiFuture<WriteResult> result = docRef.set(teams, SetOptions.merge());
			// ...
			// result.get() blocks on response
			try {
				System.out.println("Update time : " + result.get().getUpdateTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		 * get a specific player based on their jersey no
		 * @param db
		 * @param jerseyNo
		 * @return
		 */
		public HashMap getPlayer(Firestore db, String jerseyNo) {
			//asynchronously retrieve all documents
			//asynchronously retrieve multiple documents
			 DocumentReference docRef = db.collection("information").document("players");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			 
			try {
				document = future.get();
				HashMap map = (HashMap) document.get(jerseyNo);
				
				System.out.println(map.get("stats").getClass());
				if (map.size() > 0) {
					return map;
				}
				// future.get() blocks on response
				
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
			

		//get the hashmap that contains the player stats
		
		

		}
		/**
		 * get hashmap of all player stats (string being the jerseyNo and object being the rest of the player stats
		 * @param db
		 * @return
		 */
		public Map<String, Object> getPlayers(Firestore db) {
			
			 DocumentReference docRef = db.collection("information").document("players");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			try {
				document = future.get();
			
				 if (document.exists()) {
				
					  System.out.println("Document data: " + document.getData());
					  return document.getData();
					} else {
					  System.out.println("No such document!");
					  return null;
					}

			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
			return null;
	}
		
		/**
		 * get hashmap of all team stats (string being the jerseyNo and object being the rest of the player stats
		 * @param db
		 * @return
		 */
		public Map<String, Object> getTeam(Firestore db) {
			
			 DocumentReference docRef = db.collection("information").document("team");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			try {
				document = future.get();
			
				 if (document.exists()) {
				
					  System.out.println("Document data: " + document.getData());
					  return document.getData();
					} else {
					  System.out.println("No such document!");
					  return null;
					}

			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
			return null;
	}
		
		/**
		 * get information about a specific game
		 * @param db
		 * @param jerseyNo
		 * @return
		 */
		public HashMap getGameStats(Firestore db, String game) {
			//asynchronously retrieve all documents
			//asynchronously retrieve multiple documents
			 DocumentReference docRef = db.collection("information").document("team");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			 
			try {
				document = future.get();

				HashMap map = (HashMap) document.get("Miami.games");
				//Returns about a specific game
				System.out.println(map.get(game).toString());
				if (map.size() > 0) {
					return map;
				}
				// future.get() blocks on response
				
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
			

		//get the hashmap that contains the player stats
		
		

		}
		
		
		
}
