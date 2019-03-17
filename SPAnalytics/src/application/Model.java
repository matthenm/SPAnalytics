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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javafx.concurrent.Task;

public class Model {
	private DocumentReference docRef;
	private DocumentReference docTeamRef;
	private Firestore db;
	//The database data is found here: https://console.firebase.google.com/u/0/project/discovery-8d956/database/discovery-8d956/data
		public void makeDatabase() {
			FileInputStream serviceAccount;
			try {
				serviceAccount = new FileInputStream("/Users/Valeria/capstoneProject/discovery-8d956-firebase-adminsdk-wib15-b501c5de29.json");
				
				// Initialize the app with a custom auth variable, limiting the server's access
				GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
				
				FirestoreOptions firestoreOptions = 
						FirestoreOptions.newBuilder().setTimestampsInSnapshotsEnabled(true).build();

				FirebaseOptions options = new FirebaseOptions.Builder()
				    .setCredentials(credentials).setFirestoreOptions(firestoreOptions)
				    .build();
				FirebaseApp.initializeApp(options);
			
				this.db = FirestoreClient.getFirestore();

				FirebaseApp.initializeApp(options,"data");
				this.docRef = db.collection("information").document("players");
				this.docTeamRef = db.collection("information").document("team");
				//getPlayer(db, "6");
				//getPlayers(db);
				//getGameStats("Miami vs. Omaha");
				//addMember(docRef);
				//addGoalie();
				//deletePlayerGoalie(docRef);
				// getPlayerStats("6");
				//createTeam(docRefTeam);
				ArrayList<Object> map = getGameStats();
				System.out.println(map);
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}


		//example of adding a team
		public void createTeam() {
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
		addTeam(team);
		
	}

		/**
		 * Example method of deleting a goalie
		 * @param docRef
		 */
		public void deletePlayerGoalie() {
			Position position = new Position("goalie");
			 Goalie goalie = new Goalie(4, 3, 2);
			  position.goalie(goalie);
			  PlayerStats stats1 = new PlayerStats("16-17", 6, position);	 
			  Map<String, PlayerStats> stats = new HashMap<String, PlayerStats>();
			  stats.put(stats1.season, stats1);
			  Player player = new Player(31, "Ryan Larkin", "6'1", "201 lbs", "6/10/97", "Clarkson, MI", stats);
			 
			deletePlayer(player);
		}
		/**
		 * Example method of adding a goalie
		 * @param docRef
		 */
		public void addGoalie() {
			Position position = new Position("goalie");
			 Goalie goalie = new Goalie(4, 3, 2);
			 position.goalie(goalie);
			  PlayerStats stats1 = new PlayerStats("17-17", 6, position);
			  Map<String, PlayerStats> stats = new HashMap<String, PlayerStats>();
			  stats.put(stats1.season, stats1);
			  Player player = new Player(31, "Ryan Larkin", "6'1", "201 lbs", "6/10/97", "Clarkson, MI", stats);
			 
			addPlayer(player);
		}
		/**
		 * Example method of adding a player
		 * @param docRef
		 */
		public void addMember() {
			Position position = new Position("defense");
			 Member defense = new Member(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
			  position.member(defense);
			  PlayerStats stats1 = new PlayerStats("16-17", 8, position);
			  Map<String, PlayerStats> stats = new HashMap<String, PlayerStats>();
			  stats.put(stats1.season, stats1);
			  Player player = new Player(6, "Alec Mahalak", "5'9", "161 lbs", "9/14/98", "Monroe, MI", stats);
			 
			addPlayer(player);
		}

		/**
		 * add player to the database (does not override the database only appends to it)
		 * @param docRef
		 * @param player
		 */
		public void addPlayer(Player player) {

			  Map<String, Player> players = new HashMap<>();
			 
			  players.put(""+player.jerseyNo, player);
			  
			  ApiFuture<WriteResult> result = this.docRef.set(players, SetOptions.merge());
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
		public void addPlayers(Map<String, Player> players) {

			  
			  ApiFuture<WriteResult> result = this.docRef.set(players, SetOptions.merge());
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
		public void removeAllPlayers() {

			  ApiFuture<WriteResult> result = this.docRef.delete();
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
		public void deletePlayer(Player player) {

			  Map<String, Object> players = new HashMap<>();
			 
			  players.put(""+player.jerseyNo, FieldValue.delete());
			  
			  ApiFuture<WriteResult> result = this.docRef.set(players, SetOptions.merge());
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
		public void addTeam(Team team) {
			  Map<String, Team> teams = new HashMap<>();
				 
			  teams.put(team.teamName, team);
			  
			  ApiFuture<WriteResult> result = this.docRef.set(teams, SetOptions.merge());
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
		public HashMap getPlayer(String jerseyNo) {
			//asynchronously retrieve all documents
			//asynchronously retrieve multiple documents
			 DocumentReference docRef = this.db.collection("information").document("players");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			 
			try {
				document = future.get();
				HashMap map = (HashMap) document.get(jerseyNo);
				
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
		 * Get the player stats on the player based on jerseyNo. It creates a String which is the jerseyNo and the rest of the
		 *  stats is a hashmap of all the seasons for that player
		 * @param jerseyNo
		 * @return String season and the rest of the stats
		 */
		public HashMap<String, HashMap> getPlayerStats(String jerseyNo) {
			//asynchronously retrieve all documents
			//asynchronously retrieve multiple documents
			 DocumentReference docRef = this.db.collection("information").document("players");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			 HashMap<String, HashMap> allStats = new HashMap<String, HashMap>();
			 
			try {
				document = future.get();
				HashMap map = (HashMap) document.get(jerseyNo+".stats");
				
				if (map.size() > 0) {
					for (Object s: map.keySet()) { //for each game
						Object detailsOnGame = map.get(s);
					
						if (detailsOnGame instanceof HashMap) {
							HashMap details = (HashMap) detailsOnGame;
						
							String season = (String) details.get("season");
						
							Long GP = (Long) details.get("GP");
							HashMap position = (HashMap) details.get("position");
							
							if (position instanceof HashMap) {
								HashMap positionG = (HashMap) position;
								if (positionG.get("goalie") != null) {
									//the player is a goalie
								
									Object posInfo = positionG.get("goalie");
									
									if (posInfo instanceof HashMap) {
										HashMap member = (HashMap) posInfo;
										
										//Create a hashmap for one season
										HashMap<String, Object> info = new HashMap<String, Object>();
										
										info.put("season", season);
										info.put("GP", GP);
										info.put("W", member.get("W"));
										info.put("L", member.get("L"));
										info.put("T", member.get("T"));
										info.put("GA", member.get("GA"));
										info.put("GAA", member.get("GAA"));
										info.put("SA", member.get("SA"));
										info.put("SV", member.get("SV"));
										info.put("SVpercent", member.get("SVpercent"));
										info.put("SO", member.get("SO"));
										allStats.put(jerseyNo, info);
									}	
									
								} else {
									//the player cis a member
									Object posInfo = positionG.get("member");
									if (posInfo instanceof HashMap) {
										HashMap member = (HashMap) posInfo;
										
										//Create a hashmap for one season
										HashMap<String, Object> info = new HashMap<String, Object>();
										info.put("season", season);
										info.put("GP", GP);
										info.put("PPA", member.get("PPA"));
										info.put("A", member.get("A"));
										info.put("PPG", member.get("PPG"));
										info.put("G", member.get("G"));
										info.put("SOG", member.get("SOG"));
										info.put("percent", member.get("percent"));
										info.put("PTS", member.get("PTS"));
										info.put("PROD", member.get("PROD"));
										info.put("SHG", member.get("SHG"));
										info.put("GWG", member.get("GWG"));
										info.put("winsOrLosses", member.get("winsOrLosses"));
										info.put("GTG", member.get("GTG"));
										info.put("TOIG", member.get("TOIG"));
										allStats.put(jerseyNo, info);
									}
									
								}
										
								
							}
						}
					}
				}
				return allStats;
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}
		/**
		 * get hashmap of all player stats (string being the jerseyNo and object being the rest of the player stats
		 * @return a map of all the players that exist
		 */
		public Map<String, Object> getPlayers() {
			
			 DocumentReference docRef = this.db.collection("information").document("players");
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
		 * @return all the statistics about the team Miami
		 */
		public Map<String, Object> getTeam() {
			
			 DocumentReference docRef = this.db.collection("information").document("team");
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
		 * @param game
		 * @return details about that game
		 */
		public HashMap getGameStats(String game) {
			//asynchronously retrieve all documents
			//asynchronously retrieve multiple documents
			 DocumentReference docRef = this.db.collection("information").document("team");
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

		}
		
		/**
		 * An ArrayList of Objects for all the games (The Objects are Strings)
		 */
		public ArrayList<Object> getGameStats() {
			ArrayList<Object> list;
			//asynchronously retrieve all documents
			//asynchronously retrieve multiple documents
			 DocumentReference docRef = this.db.collection("information").document("team");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			 
			try {
				document = future.get();

				HashMap map = (HashMap) document.get("Miami.games");
				if (map.size() > 0) {
				list = new ArrayList<Object>(Arrays.asList(map.keySet().toArray()));
					return list;
				}
				// future.get() blocks on response
				
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}
		/**
		 * Gives the position of the player (goalie, or member) by inputting a jersey no.
		 */
		public String getPosition(String jerseyNo) {
			String posName = null;
			 DocumentReference docRef = db.collection("information").document("players");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			 
			try {
				document = future.get();
				HashMap map = (HashMap) document.get(jerseyNo+".stats");
				
				System.out.println(map);
				if (map.size() > 0) {
					
				for (Object s: map.keySet()) { //for each game
					Object detailsOnGame = map.get(s);
					if (detailsOnGame instanceof HashMap && posName == null) {
						HashMap details = (HashMap) detailsOnGame;
						HashMap position = (HashMap) details.get("position");
						posName = (String) position.get("namePosition");
						}
					}
				}
				
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			return posName;
		
		
		} //end getPosition
		
		/**
		 * Get the jerseyNo of a player by inputting their name
		 * @param playerName
		 * @return jerseyNo
		 */
		public String getJerseyNo(String playerName) {
			String jerseyNo = null;
			 DocumentReference docRef = db.collection("information").document("players");
			 ApiFuture<DocumentSnapshot> future = docRef.get();
			 DocumentSnapshot document;
			 
			try {
				document = future.get();
				Map<String, Object> players = getPlayers();
				
				if (players.size() > 0) {
					
				for (Object s: players.keySet()) { //for each game
					Object detailsOfPlayer = players.get(s);
					if (detailsOfPlayer instanceof HashMap && jerseyNo == null) {
						HashMap details = (HashMap) detailsOfPlayer;

						
						if (details.get("name").equals(playerName)) {
						jerseyNo = details.get("jerseyNo").toString();
						}
					}
					}
				}
				
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return jerseyNo;
		}
}
