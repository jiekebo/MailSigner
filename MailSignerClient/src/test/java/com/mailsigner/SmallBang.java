package com.mailsigner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.mailsigner.control.discovery.MulticastClient;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Usersignature;

public class SmallBang {

	private static Object[][] allUsers;
	private static String[][] signatures;
	private static Object[] userDetails;
	private static Object[][] userSignatures;
	private static Object[][] userWorkStations;
	private static String userFileName;
	private static String userHome = System.getProperty("user.home");
	private static String signaturesPath = userHome + "\\Application Data\\Microsoft\\Signatures";
	private static String signaturesPathDK = userHome + "\\Application Data\\Microsoft\\Signaturer";
	private static String signaturesPathWin7 = userHome + "\\AppData\\Roaming\\Microsoft\\Signatures";
	private static String signaturesPathWin7DK = userHome + "\\AppData\\Roaming\\Microsoft\\Signaturer";
	private static String[] pathArray = new String[4];
	
	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static EntityTransaction entr;
	
	/**
	 * Finds the filename belonging to a username
	 * @param allUsers All users in the main directory
	 * @param user Single user id
	 * @return Filename corresponding to user id
	 */
	private static String findUserDetailsFileName(Object[][] allUsers, String user){
		for(int i = 0; i < allUsers[0].length; i++){
			if(allUsers[0][i].toString().equalsIgnoreCase(user))
				return allUsers[2][i].toString();
		}
		return null;
	}
	

	
	/**
	 * Deletes all files and subdirectories under dir
	 * @param dir Directory to be deleted
	 * @return True if deletion was successful or false if deletion failed
	 */
	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }
	    return dir.delete();
	}
	
	public static boolean deleteDirContent(File dir){
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            File child = new File(dir, children[i]);
	            Boolean deleted = child.delete();
	            if(!deleted){
	            	return false;
	            }
	        }
	    }
	    return true;
	}
	
	public static void writeSignatures (HashMap<String,String> hm, int signature, int workstation, String user, String userWorkStation){
//		for(int j = 0; j < signatures[0].length; j++){
//			// if user has current signature
//			if(userSignatures[0][signature].equals(signatures[0][j])){
//				String textVersion = signatures[2][j];
//				String htmlVersion = signatures[3][j];
//				String rtfVersion = signatures[4][j];
//				
//				String textVersionReplaced = replaceFields(textVersion, hm);
//				String htmlVersionReplaced = replaceFields(htmlVersion, hm);
//				String rtfVersionReplaced = replaceRTFFields(rtfVersion, hm);
//				
//				// stupid boolean used to make deployed true... If a path is not deployed and the path after is deployed, the signature will show as deployed for all paths :P
//				Boolean deployed = false;
//				// for each folder in pathArray
//				try{
//					for(int k = 0; k < pathArray.length; k++){
//						File folder = new File(pathArray[k]);
//						// If folder from pathArray exists, create signatures in that directory
//						if(folder.exists()){
//							BufferedWriter pw = new BufferedWriter(new FileWriter(pathArray[k] + "//" + signatures[0][j].toString() + ".txt"));
//							FileWriter htmlWriter = new FileWriter(pathArray[k] + "//" + signatures[0][j].toString() + ".htm");
//							FileWriter rtfWriter = new FileWriter(pathArray[k] + "//" + signatures[0][j].toString() + ".rtf");
//							
//							String[] text =  textVersionReplaced.split("\\n");
//							for(int l = 0; l < text.length; l++){
//								pw.write(text[l]);
//								pw.newLine();
//							}
//							pw.close();
//							
//							htmlWriter.write(htmlVersionReplaced);
//							htmlWriter.close();
//							
//							rtfWriter.write(rtfVersionReplaced);
//							rtfWriter.close();
//							deployed = true;
//						}
//					}
//					// if current signature has been deployed
//					if(deployed && workstation != -1){
//						// Set the signatures status on the current workstation as deployed
//						userWorkStations[1][workstation] = true;
//						userSignatures[2][signature] = userWorkStations;
//						userDetails[24] = userSignatures;
//						xml.saveUserSettings(userDetails, "", findUserDetailsFileName(allUsers, user));
//					} else if (deployed && workstation == -1) {
//						// If workstation does not exists create an entry with the workstations name and set deployed to true
//						int workStationsLength = userWorkStations[0].length;
//						Object[][] newUserWorkStations = new Object[2][(workStationsLength+1)];
//						// Copying old array into bigger array
//						for(int i = 0; i < userWorkStations.length; i++){
//							for(int n = 0; n < userWorkStations[i].length; n++){
//								newUserWorkStations[i][n] = userWorkStations[i][n];
//							}
//						}
//						newUserWorkStations[0][workStationsLength] = userWorkStation;
//						newUserWorkStations[1][workStationsLength] = true;
//						userSignatures[2][signature] = newUserWorkStations;
//						userDetails[24] = userSignatures;
//						xml.saveUserSettings(userDetails, "", findUserDetailsFileName(allUsers, user));
//					}
//				} catch (Exception e){
//					System.out.println(e);
//				}
//			}
//		}
	}
	
	public static Boolean isNewWorkstation(Object[][] userSignatures, String userWorkStation){
		Boolean isNew = true;
		for(int eachSignature = 0; eachSignature < userSignatures[0].length; eachSignature++){
			Object[][] workStationsInSignature = (Object[][]) userSignatures[2][eachSignature];
			for(int amountWorkStations = 0; amountWorkStations < workStationsInSignature[0].length; amountWorkStations++){
				if(workStationsInSignature[0][amountWorkStations].toString().equalsIgnoreCase(userWorkStation))
					isNew = false;
			}
		}
		return isNew;
	}
	
	public static Map<String,String> createDbProperties(String dbURL) {
		Map<String,String> dbProps = new HashMap<String,String>();
		dbProps.put("javax.persistence.jdbc.url", dbURL);
		dbProps.put("javax.persistence.jdbc.password", "");
		dbProps.put("javax.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
		dbProps.put("javax.persistence.jdbc.user", "SA");
		return dbProps;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		TrayMenu ti = new TrayMenu();
//		
//		Map<String,String> dbProps = createDbProperties("jdbc:hsqldb:hsql://localhost/mailsignerdb");
//		emf = Persistence.createEntityManagerFactory("MailSignerJPA", dbProps);
//		em = emf.createEntityManager();
//		entr = em.getTransaction();
//		
//		TypedQuery<User> userQuery = em.createNamedQuery("User.findUser", User.class);
//		userQuery.setParameter("value", "Test");
//		System.out.println("Users: " + userQuery.getResultList().size());
//		
//		User userPersistence = userQuery.getResultList().get(0);
//		
//		Set<Usersignature> signatures = userPersistence.getUsersignatures();
//		
//		
//		for (Usersignature usersignature : signatures) {
//			System.out.println(usersignature.getSignature().getTitle() + " : " + usersignature.getEnabled());
//		}

//		TestClient tc = new TestClient();
		
		String user = System.getProperty("user.name");
		String userWorkStation = null;
		try {
			userWorkStation = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("user " + user + " computer " + userWorkStation);
//		
//		pathArray[0] = signaturesPath;
//		pathArray[1] = signaturesPathDK;
//		pathArray[2] = signaturesPathWin7;
//		pathArray[3] = signaturesPathWin7DK;
//		
//		XMLInterface xml = new XMLInterface();
//		try {
//			allUsers = xml.loadAllUsers("Users.xml");
//			signatures = xml.loadSignatures("Signatures.xml");
//			userFileName = findUserDetailsFileName(allUsers, user);
//			if(userFileName != null){
//				userDetails = xml.loadUserSettings("", userFileName);
//				userSignatures = (Object[][]) userDetails[24];
//				
//				HashMap<String,String> hm = new HashMap<String,String>();
//				
//				hm.put("firstname", userDetails[0].toString());
//				hm.put("lastname", userDetails[1].toString());
//				hm.put("displayname", userDetails[2].toString());
//				hm.put("initials", userDetails[3].toString());
//				hm.put("description", userDetails[4].toString());
//				hm.put("office", userDetails[5].toString());
//				hm.put("telephonenumber", userDetails[6].toString());
//				hm.put("mail", userDetails[7].toString());
//				hm.put("wwwhomepage", userDetails[8].toString());
//				hm.put("street", userDetails[9].toString());
//				hm.put("pobox", userDetails[10].toString());
//				hm.put("city", userDetails[11].toString());
//				hm.put("state", userDetails[12].toString());
//				hm.put("zip", userDetails[13].toString());
//				hm.put("country", userDetails[14].toString());
//				hm.put("homephone", userDetails[15].toString());
//				hm.put("pager", userDetails[16].toString());
//				hm.put("mobile", userDetails[17].toString());
//				hm.put("fax", userDetails[18].toString());
//				hm.put("ipphone", userDetails[19].toString());
//				hm.put("title", userDetails[20].toString());
//				hm.put("department", userDetails[21].toString());
//				hm.put("company", userDetails[22].toString());
//				hm.put("manager", userDetails[23].toString());
//				
//				// Is the system completely new to mailsigner? if so clear the signature folders
//				if(isNewWorkstation(userSignatures, userWorkStation)){
//					for(int m = 0; m < pathArray.length; m++){
//						File folder = new File(pathArray[m]);
//						if(folder.exists()){
//							deleteDirContent(folder);
//						}
//					}
//				}
//				
//				// for each of the signatures
//				for(int i = 0; i < userSignatures[0].length; i++){
//					Boolean workStationExists = false;
//					userWorkStations = (Object[][]) userSignatures[2][i];
//					// if signature is enabled and not yet deployed
//					if(userSignatures[1][i].toString().equalsIgnoreCase("true") /*&& userSignatures[2][i].toString().equalsIgnoreCase("false")*/){
//						// check if workstation exists
//						for(int l = 0; l < userWorkStations[0].length; l++){
//							// if workstation exists
//							if(userWorkStations[0][l].toString().equalsIgnoreCase(userWorkStation)){
//								// renew the signature
//								if(userWorkStations[1][l].toString().equalsIgnoreCase("false")){
//									writeSignatures(hm, i, l, xml, user, userWorkStation);
//								}
//								workStationExists = true;
//							}
//						}
//						
//						// if no workstation was found, create a new entry
//						// old signatures will already have been deleted by isNewWorkStation function
//						if(!workStationExists){
//							writeSignatures(hm, i, -1, xml, user, userWorkStation);
//						}
//					}
//					// if signature has been deployed but later been disabled, delete the signature at user
//					if(userSignatures[1][i].toString().equalsIgnoreCase("false")){
//						// run through all workstations...
//						for(int n = 0; n < userWorkStations[0].length; n++){
//							// if the current workstation exists
//							if(userWorkStations[0][n].toString().equals(userWorkStation)){
//								// and if signature has been deployed
//								if(userWorkStations[1][n].toString().equals("true")){
//									for(int m = 0; m < pathArray.length; m++){
//										File folder = new File(pathArray[m]);
//										if(folder.exists()){
//											File textFile = new File(pathArray[m] + "//" + signatures[0][i].toString() + ".txt");
//											File htmlFile = new File(pathArray[m] + "//" + signatures[0][i].toString() + ".htm");
//											File rtfFile = new File(pathArray[m] + "//" + signatures[0][i].toString() + ".rtf");
//											textFile.delete();
//											htmlFile.delete();
//											rtfFile.delete();
//										}
//									}
//									userWorkStations[1][n] = false;
//									userSignatures[2][i] = userWorkStations;
//									userDetails[24] = userSignatures;
//									xml.saveUserSettings(userDetails, "", findUserDetailsFileName(allUsers, user));
//								}
//							}
//						}
//					}
//				}
//			// If user doesn't exist clear signature folders (to prevent more than allowed users!)
//			} else {
//				for(int i = 0; i < pathArray.length; i++){
//					File folder = new File(pathArray[i]);
//					if(folder.exists()){
//						deleteDirContent(folder);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}