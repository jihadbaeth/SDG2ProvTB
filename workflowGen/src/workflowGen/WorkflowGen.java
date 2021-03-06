package workflowGen;

import edu.isi.pegasus.planner.dax.ADAG;
import edu.isi.pegasus.planner.dax.File;
import edu.isi.pegasus.planner.dax.Job;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.fluttercode.datafactory.impl.DataFactory;

import java.text.SimpleDateFormat;
import java.util.*;



public class WorkflowGen {

	public WorkflowGen() {
	}

	public static void main(String[] agrs) throws Exception {
		WorkflowGen ingestor = new WorkflowGen();
		for(int i =401; i<=500; i++)
		{
			ingestor.createSocialWorkflow(1000, i, "1K");
            Util.addMissingAtt("C:\\Users\\jehad\\Desktop\\Dax\\dax_" + i + "_1K_");

		}


	}

    public String convertToCommaDelimetedString(Tweep[] tweep) {
        if (tweep.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (Tweep n : tweep) {
                nameBuilder.append(n.getUsername()).append(",");
                // can also do the following
                // nameBuilder.append("'").append(n.replace("'", "''")).append("',");
            }

            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }
    }

    public Item chooseOnWeight(List<Item> items) {
	        double completeWeight = 0.0;
	        for (Item item : items)
	            completeWeight += item.getWeight();
	        double r = Math.random() * completeWeight;
	        double countWeight = 0.0;
	        for (Item item : items) {
	            countWeight += item.getWeight();
	            if (countWeight >= r)
	                return item;
	        }
	        throw new RuntimeException("Should never be shown.");
	    }

	public Tweep[] createTweepList(int size,double[]popu, double[]avail, double[]legit)
	{

		Tweep[] tweep = new Tweep[size];
        DataFactory df = new DataFactory();

        for (int i = 0; i < size; i++) {
            Tweep temp = new Tweep();
            temp.setUsername(("@" + df.getFirstName() + "" + df.getLastName()).replace("'", ""));
            temp.setName((df.getFirstName() + " " + df.getLastName()).replace("'", ""));
            temp.setBio(df.getEmailAddress());
            temp.setPopularity(popu[i]);
            temp.setAvailability(avail[i]);
            temp.setLegitimacy(legit[i]);
            System.out.println("Created");
            System.out.println(temp.toString());
            tweep[i] = temp;
        }
        for (int currentUser = 0; currentUser < tweep.length; currentUser++) {
            Tweep t = tweep[currentUser];
            int numberOfFollowers = (int) (t.getPopularity() * 100);
            System.out.println("Number of Followers for user " + t.getUsername() + " is" + numberOfFollowers);
            Tweep[] followingList = new Tweep[numberOfFollowers];
            int currentFollower = 0;
            ArrayList<Integer> keepTrack = new ArrayList<Integer>();
            while (currentFollower < numberOfFollowers) {
                Random rand = new Random();
                int n = rand.nextInt(tweep.length);
                if (currentUser != n && !keepTrack.contains(n)) {
                    followingList[currentFollower] = tweep[n];
                    keepTrack.add(n);
                    currentFollower++;

                }
            }

            tweep[currentUser].setFollowingList(followingList);

        }
        for (Tweep temp : tweep) {
            System.out.println("User ID:" + temp.getUsername() + " Popularity Score: " + temp.getPopularity() + " Number of Followers: " + temp.getFollowingList().length);
        }
        return tweep;

	}

	public void createSocialWorkflow(int numberOfIterations, int iteration, String id) {
		int entityIndex = 1;
		int agentIndex = 1;
		int activityIndex = 1;
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 8, 7, 14, 0, 0);
		Date timeStamp = new Date(cal.getTimeInMillis());

		SimpleDateFormat ft =
			      new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");

		Map<String, String> successors = new HashMap<String, String>();
		Map<String, String> tweetsList = new HashMap<String, String>();
		double[] popularity =Util.createNDDataset(500);
		double[] availability =Util.createNDDataset(500);
		double[] legitimacy =Util.createNDDataset(500);
		Tweep[] users =createTweepList(500,Util.normalize(popularity),Util.normalize(availability),Util.normalize(legitimacy));
        //

		ArrayList <Double>tweetVisibility= new ArrayList<Double>();



		try {
			// start with new tweet
			// *********************
			// selecting user from pool
			System.out.println("list of users popularity "+popularity.length);
			int userIndex =generateRand(popularity.length-1,0);
			String agentID = "agent_" + iteration + "_" + id + "_" + agentIndex;
			tweetVisibility.add(popularity[userIndex]);//confusing naming

			// creating main workflow
			ADAG dax = new ADAG("SocialWorkflow");
			File tweet = new File("http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex);
			Job mainTweet = new Job("workflow_main_" + iteration + "_" + id, "post");
			//mainTweet.addArgument("-o").addArgument(tweet);
			//mainTweet.addArgument("http://pronaliz.yildiz.edu.tr");
			mainTweet.addArgument(tweet);
			mainTweet.addArgument("userID", users[userIndex].getUsername()+",");
			mainTweet.addArgument("Username", users[userIndex].getName()+",");
			mainTweet.addArgument("popularity_score", ""+users[userIndex].getPopularity()+",");
			mainTweet.addArgument("availability_score", ""+users[userIndex].getAvailability()+",");
            mainTweet.addArgument("legitimacy_score", "" + users[userIndex].getLegitimacy());
            mainTweet.addArgument("list_of_followers", "" + convertToCommaDelimetedString(users[userIndex].getFollowingList()));
            mainTweet.addMetaData("timeStamp", ft.format(timeStamp));
            mainTweet.uses(tweet, File.LINK.OUTPUT, File.TRANSFER.TRUE, true);
            String mainUserCredi = users[userIndex].getPopularity() + "";
            dax.addJob(mainTweet);
			successors.put("http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex,
					"workflow_main_" + iteration + "_" + id);

			int i = 0;
			int randomness = (int) (users[userIndex].getPopularity()*100);
			RandomItemChooser ric = new RandomItemChooser();
			ArrayList<Item> items = new ArrayList<Item>();
			items.add(new Operation(randomness,"1"));
			items.add(new Operation(randomness/2,"2"));
			items.add(new Operation(randomness/2,"3"));
			while (i < numberOfIterations) {
				// long startTime = System.currentTimeMillis();

				// generate random number between 1-3 and adds social operations
				// depending on the result of this random
				// where:
				// 1 = Like operation
				// 2 = Retweet Operation
				// 3 = Reply Operation
//				int result = Integer.parseInt(ric.chooseOnWeight(items).getOperation());
				int result = generateRand(3,1);
				switch (result) {

				case 1:
					// Like operation
					// a NEW AGENT does a LIKE on a an EXISTING TWEET
					timeStamp = DateUtils.addSeconds(timeStamp, 30);
					agentIndex = agentIndex + 1;
					activityIndex = activityIndex + 1;
					String likeActivityID = "activity_" + iteration + "_" + id + "_" + activityIndex;
					String temptweetID = "http://twitter.com/status_" + iteration + "_" + id + "_"
							+ (int)Util.chooseWithChance(Util.probabilityTable(Util.normalize(
									ArrayUtils.toPrimitive(tweetVisibility.toArray(new Double[tweetVisibility.size()])))));
					userIndex =generateRand(popularity.length-1,0);
                    String affectedBy = successors.get(temptweetID);
                    Job like = new Job(likeActivityID, "like");
                    File likedTweet = new File(temptweetID);

					// adding relationships
					successors.put(temptweetID, likeActivityID);
					like.addArgument(likedTweet);
					like.addArgument("userID", users[userIndex].getUsername()+",");
					like.addMetaData("timeStamp", ft.format(timeStamp));
					like.addArgument("Username", users[userIndex].getName()+",");
					like.addArgument("popularity_score", ""+users[userIndex].getPopularity()+",");
					like.addArgument("availability_score", ""+users[userIndex].getAvailability()+",");
                    like.addArgument("legitimacy_score", "" + users[userIndex].getLegitimacy());
                    like.addArgument("list_of_followers", "" + convertToCommaDelimetedString(users[userIndex].getFollowingList()));
                    like.uses(likedTweet, File.LINK.INPUT, File.TRANSFER.TRUE, true);


					// like.uses(likedTweet, File.LINK.OUTPUT,
					// File.TRANSFER.FALSE, false);

					dax.addJob(like);

					// need to add the effect
					// Job tempJob = new Job(affectedBy,"");
					// if(i==0)
					// {
					// dax.addDependency("workflow_main_"+iteration+"_"+id,
					// likeActivityID);
					// } else {
					dax.addDependency(affectedBy, likeActivityID);
					// }

					System.out.println("The operation  " + likeActivityID + " is a like Status upon tweet with ID "
							+ temptweetID + " which was an effect of " + affectedBy);
					Util.listOfAffected(tweetsList, temptweetID);


					break;

				case 2:
					// Retweet operation
					// a New AGENT RETWEETS an already EXISTING ENTITY,
					// GENERATING NEW ENTITY.

					agentIndex = agentIndex + 1;
					timeStamp = DateUtils.addSeconds(timeStamp, 30);

					String temptweetID2 = "http://twitter.com/status_" + iteration + "_" + id + "_"
							+ (int)Util.chooseWithChance(Util.probabilityTable(Util.normalize(
									ArrayUtils.toPrimitive(tweetVisibility.toArray(new Double[tweetVisibility.size()])))));
                    String affectedBy2 = successors.get(temptweetID2);
                    activityIndex = activityIndex + 1;
                    String retweetActivityID = "activity_" + iteration + "_" + id + "_" + activityIndex;

					// incrementEntityIndex();
					entityIndex = entityIndex + 1;
					String retweetID = "http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex;

					userIndex =generateRand(popularity.length-1,0);
					tweetVisibility.add(popularity[userIndex]);//confusing naming
					Job retweet = new Job(retweetActivityID, "retweet");
					File retweetedTweet = new File(temptweetID2);
					File theRetweet = new File(retweetID);
					retweet.addArgument("userID", users[userIndex].getUsername()+",");
					retweet.addArgument("Username", users[userIndex].getName()+",");
					retweet.addArgument("popularity_score", ""+users[userIndex].getPopularity()+",");
					retweet.addArgument("availability_score", ""+users[userIndex].getAvailability()+",");
                    retweet.addArgument("legitimacy_score", "" + users[userIndex].getLegitimacy());
                    retweet.addArgument("list_of_followers", "" + convertToCommaDelimetedString(users[userIndex].getFollowingList()));
                    retweet.uses(retweetedTweet, File.LINK.INPUT, File.TRANSFER.TRUE, true);
                    retweet.uses(theRetweet, File.LINK.OUTPUT, File.TRANSFER.TRUE, true);
                    retweet.addMetaData("timeStamp", ft.format(timeStamp));
                    retweet.addArgument(retweetedTweet);
					dax.addJob(retweet);
					dax.addDependency(affectedBy2, retweetActivityID);

					successors.put(temptweetID2, retweetActivityID);
					successors.put(retweetID, retweetActivityID);


					tweetsList.put(retweetID, temptweetID2);
					System.out.println("The operation   " + retweetActivityID + " has Retweeted Status" + temptweetID2
							+ " which was an effect of " + affectedBy2);
					Util.listOfAffected(tweetsList, temptweetID2);

					break;

				case 3:
					// ReplyOperation
					// a New AGENT makes a REPLY on an already EXISTING TWEET
					timeStamp = DateUtils.addSeconds(timeStamp, 30);

					agentIndex = agentIndex + 1;

					String temptweetID3 = "http://twitter.com/status_" + iteration + "_" + id + "_"
							+ (int)Util.chooseWithChance(Util.probabilityTable(Util.normalize(
									ArrayUtils.toPrimitive(tweetVisibility.toArray(new Double[tweetVisibility.size()])))));
					// AddEntityAttributes(temptweetID3);
                    String affectedBy3 = successors.get(temptweetID3);
                    activityIndex = activityIndex + 1;
                    String replyActivityID = "activity_" + iteration + "_" + id + "_" + activityIndex;

					// incrementEntityIndex();
					entityIndex = entityIndex + 1;
					String replyID = "http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex;
					userIndex =generateRand(popularity.length-1,0);
					tweetVisibility.add(popularity[userIndex]);//confusing naming

					Job reply = new Job(replyActivityID, "reply");
					File repliedTweet = new File(temptweetID3);
					File theReply = new File(replyID);
					reply.addArgument("userID", users[userIndex].getUsername()+",");
					reply.addArgument("Username", users[userIndex].getName()+",");
					reply.addArgument("popularity_score", ""+users[userIndex].getPopularity()+",");
					reply.addArgument("availability_score", ""+users[userIndex].getAvailability()+",");
                    reply.addArgument("legitimacy_score", "" + users[userIndex].getLegitimacy());
                    reply.addArgument("list_of_followers", "" + convertToCommaDelimetedString(users[userIndex].getFollowingList()));
                    reply.addMetaData("timeStamp", ft.format(timeStamp));
                    reply.uses(repliedTweet, File.LINK.INPUT, File.TRANSFER.TRUE, true);
                    reply.uses(theReply, File.LINK.OUTPUT, File.TRANSFER.TRUE, true);
                    reply.addArgument(repliedTweet);
					dax.addJob(reply);
					// dax.addDependency(split, wc);
					dax.addDependency(affectedBy3, replyActivityID);

					successors.put(temptweetID3, replyActivityID);
					successors.put(replyID, replyActivityID);

					tweetsList.put(replyID, temptweetID3);

					System.out.println("The operation    " + replyActivityID + " has Replied to Status " + temptweetID3
							+ " which was an effect of " + affectedBy3);
					Util.listOfAffected(tweetsList, temptweetID3);

					break;

				default:
					// ...
					break;

				}

				i++;
			}
            //dax.writeToSTDOUT();
            dax.writeToFile("C:\\Users\\jehad\\Desktop\\Dax\\dax_" + iteration + "_1K_");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static int generateRand(int max, int min) {
		Random rand = new Random();

		return rand.nextInt(max - min + 1) + min;

	}

}
