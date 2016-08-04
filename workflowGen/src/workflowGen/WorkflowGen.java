package workflowGen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.fluttercode.datafactory.impl.DataFactory;
import edu.isi.pegasus.planner.dax.ADAG;
import edu.isi.pegasus.planner.dax.File;
import edu.isi.pegasus.planner.dax.Job;


public class WorkflowGen {

	public WorkflowGen() {
	}

	public static void main(String[] agrs) throws Exception {
		WorkflowGen ingestor = new WorkflowGen();
		ingestor.createSocialWorkflow(10, 1, "100");
	}

	public Tweep[] createTweepList(int size,double[]cred)
	{

		Tweep[] tweep = new Tweep[size];
        DataFactory df = new DataFactory();

		for(int i=0;i<size;i++)
		{
			Tweep temp = new Tweep();
			temp.setUsername("@"+df.getFirstName() + ""+ df.getLastName());
			temp.setName(df.getFirstName() + " "+ df.getLastName());
			temp.setBio(df.getEmailAddress());
			temp.setCredibility(cred[i]);
			System.out.println("Created");
			System.out.println(temp.toString());
			tweep[i]= temp;
		}
		return tweep;

	}

	public void createSocialWorkflow(int numberOfIterations, int iteration, String id) {
		int entityIndex = 1;
		int agentIndex = 1;
		int activityIndex = 1;
		Date timeStamp = new Date(2016,8,4,14,0,0);

		SimpleDateFormat ft =
			      new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");

		Map<String, String> successors = new HashMap<String, String>();
		Map<String, String> tweetsList = new HashMap<String, String>();
		double[] credibility =Util.createNDDataset(5);
		Tweep[] users =createTweepList(5,Util.normalize(credibility));
		ArrayList <Double>tweetVisibility= new ArrayList<Double>();



		try {
			// start with new tweet
			// *********************
			// selecting user from pool
			System.out.println("list of users credibility "+credibility.length);
			int userIndex =generateRand(credibility.length-1,0);
			String agentID = "agent_" + iteration + "_" + id + "_" + agentIndex;
			tweetVisibility.add(credibility[userIndex]);//confusing naming

			// creating main workflow
			ADAG dax = new ADAG("SocialWorkflow");
			File tweet = new File("http://twitter.com/status_1_100_1");
			Job mainTweet = new Job("workflow_main_" + iteration + "_" + id, "post");
			mainTweet.addArgument("-o").addArgument(tweet);
			mainTweet.addArgument("http://pegasus.isi.edu");
			mainTweet.addArgument(tweet);
			mainTweet.addArgument("userID", users[userIndex].getUsername());
			mainTweet.addArgument("User Name", users[userIndex].getName());
			mainTweet.addArgument("Credibility score", ""+users[userIndex].getCredibility());
			mainTweet.addMetaData("timeStamp", ft.format(timeStamp));
			mainTweet.uses(tweet, File.LINK.OUTPUT, File.TRANSFER.TRUE, true);
			dax.addJob(mainTweet);
			successors.put("http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex,
					"workflow_main_" + iteration + "_" + id);

			int i = 0;
			while (i < numberOfIterations) {
				// long startTime = System.currentTimeMillis();

				// generate random number between 1-3 and adds social operations
				// depending on the result of this random
				// where:
				// 1 = Like operation
				// 2 = Retweet Operation
				// 3 = Reply Operation
				int result = generateRand(3, 1);
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
					userIndex =generateRand(credibility.length-1,0);
					String affectedBy = (String) successors.get(temptweetID);
					Job like = new Job(likeActivityID, "like");
					File likedTweet = new File(temptweetID);

					// adding relationships
					successors.put(temptweetID, likeActivityID);
					like.addArgument(likedTweet);
					like.addArgument("userID", users[userIndex].getUsername());
					like.addMetaData("timeStamp", ft.format(timeStamp));
					like.addArgument("User Name", users[userIndex].getName());
					like.addArgument("Credibility score", ""+users[userIndex].getCredibility());
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
					String affectedBy2 = (String) successors.get(temptweetID2);
					activityIndex = activityIndex + 1;
					String retweetActivityID = "activity_" + iteration + "_" + id + "_" + activityIndex;

					// incrementEntityIndex();
					entityIndex = entityIndex + 1;
					String retweetID = "http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex;

					userIndex =generateRand(credibility.length-1,0);
					tweetVisibility.add(credibility[userIndex]);//confusing naming
					Job retweet = new Job(retweetActivityID, "retweet");
					File retweetedTweet = new File(temptweetID2);
					File theRetweet = new File(retweetID);
					retweet.addArgument("userID", users[userIndex].getUsername());
					retweet.addArgument("User Name", users[userIndex].getName());
					retweet.addArgument("Credibility score", ""+users[userIndex].getCredibility());
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
					String affectedBy3 = (String) successors.get(temptweetID3);
					activityIndex = activityIndex + 1;
					String replyActivityID = "activity_" + iteration + "_" + id + "_" + activityIndex;

					// incrementEntityIndex();
					entityIndex = entityIndex + 1;
					String replyID = "http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex;
					userIndex =generateRand(credibility.length-1,0);
					tweetVisibility.add(credibility[userIndex]);//confusing naming

					Job reply = new Job(replyActivityID, "reply");
					File repliedTweet = new File(temptweetID3);
					File theReply = new File(replyID);
					reply.addArgument("userID", users[userIndex].getUsername());
					reply.addArgument("User Name", users[userIndex].getName());
					reply.addArgument("Credibility score", ""+users[userIndex].getCredibility());
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
			dax.writeToSTDOUT();
			dax.writeToFile("/home/jihad/Desktop/myExample.dax");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static int generateRand(int max, int min) {
		Random rand = new Random();

		return rand.nextInt(max - min + 1) + min;

	}

}
