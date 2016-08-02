package workflowGen;

//import edu.indiana.d2i.komadu.axis2.client.ActivityEntityType;
//import edu.indiana.d2i.komadu.axis2.client.ActivityType;
//import edu.indiana.d2i.komadu.axis2.client.AddActivityEntityRelationshipDocument;
//import edu.indiana.d2i.komadu.axis2.client.AddAgentActivityRelationshipDocument;
//import edu.indiana.d2i.komadu.axis2.client.AddAgentEntityRelationshipDocument;
//import edu.indiana.d2i.komadu.axis2.client.AddAttributesDocument;
//import edu.indiana.d2i.komadu.axis2.client.AddAttributesType;
//import edu.indiana.d2i.komadu.axis2.client.AddEntityEntityRelationshipDocument;
//import edu.indiana.d2i.komadu.axis2.client.AgentActivityType;
//import edu.indiana.d2i.komadu.axis2.client.AgentEntityType;
//import edu.indiana.d2i.komadu.axis2.client.AgentEnumType;
//import edu.indiana.d2i.komadu.axis2.client.AgentType;
//import edu.indiana.d2i.komadu.axis2.client.AssociationType;
//import edu.indiana.d2i.komadu.axis2.client.AttributeType;
//import edu.indiana.d2i.komadu.axis2.client.AttributesType;
//import edu.indiana.d2i.komadu.axis2.client.AttributionType;
//import edu.indiana.d2i.komadu.axis2.client.DerivationType;
//import edu.indiana.d2i.komadu.axis2.client.EntityEntityType;
//import edu.indiana.d2i.komadu.axis2.client.EntityEnumType;
//import edu.indiana.d2i.komadu.axis2.client.EntityType;
//import edu.indiana.d2i.komadu.axis2.client.FileType;
//import edu.indiana.d2i.komadu.axis2.client.GenerationType;
//import edu.indiana.d2i.komadu.axis2.client.InstanceOfType;
//import edu.indiana.d2i.komadu.axis2.client.KomaduServiceStub;
//import edu.indiana.d2i.komadu.axis2.client.ObjectEnumType;
//import edu.indiana.d2i.komadu.axis2.client.ServiceInformationType;
//import edu.indiana.d2i.komadu.axis2.client.UsageType;
//import edu.indiana.d2i.komadu.axis2.client.UserAgentType;
//import edu.indiana.d2i.komadu.axis2.client.WorkflowInformationType;
//import edu.indiana.d2i.komadu.query.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.isi.pegasus.planner.dax.ADAG;
import edu.isi.pegasus.planner.dax.File;
import edu.isi.pegasus.planner.dax.Job;
//import edu.uci.ics.jung.algorithms.layout.CircleLayout;
//import edu.uci.ics.jung.algorithms.layout.Layout;
//import edu.uci.ics.jung.algorithms.layout.SpringLayout;
//import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
//import edu.uci.ics.jung.graph.DirectedSparseGraph;
//import edu.uci.ics.jung.graph.Graph;
//import edu.uci.ics.jung.graph.SparseMultigraph;
//import edu.uci.ics.jung.io.GraphMLWriter;
//import edu.uci.ics.jung.visualization.RenderContext;
//import edu.uci.ics.jung.visualization.VisualizationImageServer;
//import edu.uci.ics.jung.visualization.VisualizationViewer;
//import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
//import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
//import edu.uci.ics.jung.visualization.renderers.Renderer;
//import edu.uci.ics.jung.visualization.renderers.Renderer.Vertex;
//import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
//import komaduTests.DataIngestor;
//import edu.uci.ics.jung.visualization.control.*;

//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Rectangle;
//import java.awt.Shape;
//import java.awt.geom.Ellipse2D;
//import java.awt.geom.Point2D;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.rmi.RemoteException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Random;
//
//import javax.swing.JFrame;

public class WorkflowGen {

	// private static KomaduServiceStub stub = null;
	// private static String workflowGraphId = null;
	// private static String agentGraphId = null;
	// private static String entityGraphURI = null;
	// private static String service1Id = null;

	public WorkflowGen() {
	}

	public static void main(String[] agrs) throws Exception {
		WorkflowGen ingestor = new WorkflowGen();
		ingestor.createSocialWorkflow(100, 1, "100");
	}

	public void createSocialWorkflow(int numberOfIterations, int iteration, String id) {
		int entityIndex = 1;
		int agentIndex = 1;
		int activityIndex = 1;
		Map<String, String> successors = new HashMap<String, String>();
		Map<String, String> tweetsList = new HashMap<String, String>();

		try {
			// start with new tweet
			// *********************
			// creating new agent

			String agentID = "agent_" + iteration + "_" + id + "_" + agentIndex;

			// creating main workflow
			ADAG dax = new ADAG("SocialWorkflow");

			File tweet = new File("http://twitter.com/status_1_100_1");

			Job mainTweet = new Job("workflow_main_" + iteration + "_" + id, "post");
			mainTweet.addArgument("-o").addArgument(tweet);
			mainTweet.addArgument("http://pegasus.isi.edu");
			mainTweet.addArgument(tweet);
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
					agentIndex = agentIndex + 1;
					activityIndex = activityIndex + 1;
					String likeActivityID = "activity_" + iteration + "_" + id + "_" + activityIndex;
					String temptweetID = "http://twitter.com/status_" + iteration + "_" + id + "_"
							+ generateRand(entityIndex, 1);
					String affectedBy = (String) successors.get(temptweetID);

					Job like = new Job(likeActivityID, "like");
					File likedTweet = new File(temptweetID);

					// adding relationships
					successors.put(temptweetID, likeActivityID);
					like.addArgument(likedTweet);
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

					String temptweetID2 = "http://twitter.com/status_" + iteration + "_" + id + "_"
							+ generateRand(entityIndex, 1);
					String affectedBy2 = (String) successors.get(temptweetID2);
					activityIndex = activityIndex + 1;
					String retweetActivityID = "activity_" + iteration + "_" + id + "_" + activityIndex;

					// incrementEntityIndex();
					entityIndex = entityIndex + 1;
					String retweetID = "http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex;

					Job retweet = new Job(retweetActivityID, "retweet");
					File retweetedTweet = new File(temptweetID2);
					File theRetweet = new File(retweetID);
					retweet.uses(retweetedTweet, File.LINK.INPUT, File.TRANSFER.TRUE, true);
					retweet.uses(theRetweet, File.LINK.OUTPUT, File.TRANSFER.TRUE, true);
					retweet.addArgument(retweetedTweet);
					dax.addJob(retweet);
					// dax.addDependency(split, wc);
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

					agentIndex = agentIndex + 1;

					String temptweetID3 = "http://twitter.com/status_" + iteration + "_" + id + "_"
							+ generateRand(entityIndex, 1);
					// AddEntityAttributes(temptweetID3);
					String affectedBy3 = (String) successors.get(temptweetID3);
					activityIndex = activityIndex + 1;
					String replyActivityID = "activity_" + iteration + "_" + id + "_" + activityIndex;

					// incrementEntityIndex();
					entityIndex = entityIndex + 1;
					String replyID = "http://twitter.com/status_" + iteration + "_" + id + "_" + entityIndex;

					Job reply = new Job(replyActivityID, "reply");
					File repliedTweet = new File(temptweetID3);
					File theReply = new File(replyID);
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
				// long stopTime = System.currentTimeMillis();
				// long elapsedTime = stopTime - startTime;
				// Date resultdate = new Date(System.currentTimeMillis());
				// System.out.println(elapsedTime);
				i++;
			}
			dax.writeToSTDOUT();
			dax.writeToFile("/home/jihad/Desktop/myExample.dax");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	//
	// public int[] randomDrops(int numberOfIterations)
	// {
	// int onePercentDrops = numberOfIterations/100;
	// if(onePercentDrops==0) onePercentDrops++;
	// System.out.println("1% ration count is: "+onePercentDrops);
	// int[] drops = new int[onePercentDrops];
	// for(int i =0; i< onePercentDrops;i++)
	// {
	// drops[i]= generateRand(numberOfIterations,0);
	// System.out.println(drops[i]);
	//
	// System.out.println(contains(drops,drops[i]));
	// }
	// System.out.println("Length"+drops.length);
	//
	// Arrays.sort(drops);
	//
	// return drops;
	//
	// }
	// public boolean contains(final int[] array,final int key) {
	// //System.out.println("does it contain "+array[key]);
	// return Arrays.binarySearch(array, key) >= 0;
	// }
	//
	//
	//
	//
	// public void createFaultySocialWorkflow(int numberOfIterations,int
	// iteration, String id)
	// {
	// int entityIndex = 1;
	// int agentIndex = 1;
	// int activityIndex=1;
	//
	// Map successors = new HashMap<String,String>();
	// int[] drops = randomDrops(numberOfIterations-1);
	//
	// try {
	// // start with new tweet
	// //*********************
	// //creating new agent
	//
	// String agentID = "agent_"+iteration+"_"+id+"_"+agentIndex;
	// AgentType main = createAgent(agentID);
	//
	//
	// //creating main workflow
	//
	// ActivityType mainWorkflow =
	// createWorkflowActivity("workflow_main_"+iteration+"_"+id, "Tweet"); //
	// this has to be reconsidered
	//
	//
	// //creating post activity
	// //String activityID = "activity_"+activityIndex;
	// //ActivityType serviceActivity =
	// createServiceActivity(activityID,"Tweet","workflow_main");
	//
	// //creating tweet entity
	//
	// String tweetMainID
	// ="http://twitter.com/status_"+iteration+"_"+id+"_"+entityIndex;
	// EntityType mainEntity = createFileEntity(tweetMainID,agentID);
	// AddEntityAttributes(tweetMainID);
	//
	//
	//
	// //adding relationships
	// addGeneration(stub,mainWorkflow,mainEntity,"workflow_main_"+iteration+"_"+id,tweetMainID);
	// addAttribution(stub,main,mainEntity,agentID,tweetMainID);
	// addAssociation(stub,main,mainWorkflow,agentID,"workflow_main_"+iteration+"_"+id);
	// //end of main social post
	// successors.put(tweetMainID, "workflow_main_"+iteration+"_"+id);
	//
	//
	// //writing code for visualization
	//// nodeWriter.println("{ data: { id: '"+agentID+"', name: '"+agentID+"',
	// weight: 65, faveColor: '#6FB1FC', faveShape: 'octagon' } },");
	//// nodeWriter.println("{ data: { id: 'workflow_main', name:
	// 'workflow_main', weight: 65, faveColor: '#F5A45D', faveShape: 'rectangle'
	// } },");
	//// nodeWriter.println("{ data: { id: '"+tweetMainID+"', name:
	// '"+tweetMainID+"', weight: 65, faveColor: '#EDA1ED', faveShape: 'ellipse'
	// } },");
	////
	//// edgeWriter.println("{ data: { source: '"+tweetMainID+"', target:
	// 'workflow_main', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasGenerateBy' } },");
	//// edgeWriter.println("{ data: { source: '"+tweetMainID+"', target:
	// '"+agentID+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasAttributedTo' } },");
	//// edgeWriter.println("{ data: { source: 'workflow_main', target:
	// '"+agentID+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasAssociatedBy' } },");
	////
	////
	//// nodeWriter.println("{ data: { id: '"+agentID+"_info', name: 'Followers:
	// 50, Following:10', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// edgeWriter.println("{ data: { source: '"+agentID+"', target:
	// '"+agentID+"_info', faveColor: '#6FB1FC', strength: 70, label: '' } },");
	////
	//// nodeWriter.println("{ data: { id: '"+tweetMainID+"_info', name: 'info
	// about tweet', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// edgeWriter.println("{ data: { source: '"+tweetMainID+"', target:
	// '"+tweetMainID+"_info', faveColor: '#6FB1FC', strength: 70, label: '' }
	// },");
	////
	//// nodeWriter.println("{ data: { id: 'workflow_main_info', name: 'Post
	// operation', weight: 65, faveColor: '#6FB1FC', faveShape: 'roundrectangle'
	// }, classes: 'questionable' },");
	//// edgeWriter.println("{ data: { source: 'workflow_main', target:
	// 'workflow_main_info', faveColor: '#6FB1FC', strength: 70, label: '' }
	// },");
	////
	// //executing random social operations for the specified number of
	// iterations
	//
	// int i =0;
	// while (i<numberOfIterations)
	// {
	//
	// // generate random number between 1-3 and adds social operations
	// depending on the result of this random
	// // where:
	// // 1 = Like operation
	// // 2 = Retweet Operation
	// // 3 = Reply Operation
	// int result = generateRand(3, 1);
	// switch (result) {
	//
	// case 1:
	// //Like operation
	// // a NEW AGENT does a LIKE on a an EXISTING TWEET
	// agentIndex = agentIndex + 1;
	// String agentIDlike = "agent_"+iteration+"_"+id+"_"+agentIndex;
	// AgentType newAgent = createAgent(agentIDlike);
	//
	// String temptweetID
	// ="http://twitter.com/status_"+iteration+"_"+id+"_"+generateRand(entityIndex,1);
	// EntityType tempEntity = createFileEntity(temptweetID,agentIDlike);
	// //AddEntityAttributes(temptweetID);
	//
	//
	// String affectedBy = (String)successors.get(temptweetID);
	//
	// activityIndex = activityIndex + 1;
	// String likeActivityID = "activity_"+iteration+"_"+id+"_"+activityIndex;
	// ActivityType likeActivity = createServiceActivity(likeActivityID,"Like",
	// affectedBy);
	//
	//
	//
	// // adding relationships
	//// nodeWriter.println("{ data: { id: '"+agentIDlike+"', name:
	// '"+agentIDlike+"', weight: 65, faveColor: '#6FB1FC', faveShape: 'octagon'
	// } },");
	//// nodeWriter.println("{ data: { id: '"+likeActivityID+"', name:
	// '"+likeActivityID+"', weight: 65, faveColor: '#F5A45D', faveShape:
	// 'rectangle' } },");
	//// nodeWriter.println("{ data: { id: '"+agentIDlike+"_info', name:
	// 'Followers: X, Following:Y', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// nodeWriter.println("{ data: { id: '"+likeActivityID+"_info', name:
	// 'Like operation', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// edgeWriter.println("{ data: { source: '"+likeActivityID+"', target:
	// '"+likeActivityID+"_info', faveColor: '#6FB1FC', strength: 70, label: ''
	// } },");
	//// edgeWriter.println("{ data: { source: '"+agentIDlike+"', target:
	// '"+agentIDlike+"_info', faveColor: '#6FB1FC', strength: 70, label: '' }
	// },");
	//
	// if(! contains(drops,i))
	// {
	// //System.out.println("Record!");
	//
	// addAssociation(stub,newAgent,likeActivity,agentIDlike,likeActivityID);
	// addUsage(stub,likeActivity,tempEntity,likeActivityID,temptweetID);
	// //writing code for visualization
	//
	//// edgeWriter.println("{ data: { source: '"+likeActivityID+"', target:
	// '"+temptweetID+"', faveColor: '#6FB1FC', strength: 70, label: 'Used' }
	// },");
	//// edgeWriter.println("{ data: { source: '"+likeActivityID+"', target:
	// '"+agentIDlike+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasAssociatedBy' } },");
	////
	////
	//
	//
	// }
	// else
	// {
	// System.out.println("Hey one Skip!");
	// System.out.println(drops.toString()+" "+i);
	// }
	//
	// successors.put(temptweetID, likeActivityID);
	//
	// System.out.println("User "+ agentIDlike +" has liked Status" +
	// temptweetID+" which was an effect of " +affectedBy);
	//
	//
	// break;
	//
	// case 2:
	// // Retweet operation
	// // a New AGENT RETWEETS an already EXISTING ENTITY, GENERATING NEW
	// ENTITY.
	//
	//
	// agentIndex = agentIndex + 1;
	// String agentIDRetweet = "agent_"+iteration+"_"+id+"_"+agentIndex;
	// AgentType newAgent2 = createAgent(agentIDRetweet);
	//
	// String temptweetID2
	// ="http://twitter.com/status_"+iteration+"_"+id+"_"+generateRand(entityIndex,1);
	// EntityType tempEntity2 = createFileEntity(temptweetID2,agentIDRetweet);
	// //AddEntityAttributes(temptweetID2);
	//
	// String affectedBy2 = (String)successors.get(temptweetID2);
	//
	// activityIndex = activityIndex + 1;
	// String retweetActivityID =
	// "activity_"+iteration+"_"+id+"_"+activityIndex;
	// ActivityType retweetActivity =
	// createWorkflowActivity(retweetActivityID,"Retweet");
	//
	//
	//
	// //incrementEntityIndex();
	// entityIndex = entityIndex + 1;
	// String retweetID
	// ="http://twitter.com/status_"+iteration+"_"+id+"_"+entityIndex;
	// EntityType retweetEntity = createFileEntity(retweetID,agentIDRetweet);
	// AddEntityAttributes(retweetID);
	//
	// // adding relationships
	//// nodeWriter.println("{ data: { id: '"+agentIDRetweet+"', name:
	// '"+agentIDRetweet+"', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'octagon' } },");
	//// nodeWriter.println("{ data: { id: '"+retweetActivityID+"', name:
	// '"+retweetActivityID+"', weight: 65, faveColor: '#F5A45D', faveShape:
	// 'rectangle' } },");
	//// nodeWriter.println("{ data: { id: '"+retweetID+"', name:
	// '"+retweetID+"', weight: 65, faveColor: '#EDA1ED', faveShape: 'ellipse' }
	// },");
	//// nodeWriter.println("{ data: { id: '"+agentIDRetweet+"_info', name:
	// 'Followers: X, Following:Y', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// nodeWriter.println("{ data: { id: '"+retweetID+"_info', name: 'info
	// about Retweet', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// nodeWriter.println("{ data: { id: '"+retweetActivityID+"_info', name:
	// 'Retweet operation', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// edgeWriter.println("{ data: { source: '"+retweetActivityID+"', target:
	// '"+retweetActivityID+"_info', faveColor: '#6FB1FC', strength: 70, label:
	// '' } },");
	//// edgeWriter.println("{ data: { source: '"+retweetID+"', target:
	// '"+retweetID+"_info', faveColor: '#6FB1FC', strength: 70, label: '' }
	// },");
	//// edgeWriter.println("{ data: { source: '"+agentIDRetweet+"', target:
	// '"+agentIDRetweet+"_info', faveColor: '#6FB1FC', strength: 70, label: ''
	// } },");
	//
	// if(! contains(drops,i))
	// {
	// //System.out.println("Record!");
	//
	// addAssociation(stub,newAgent2,retweetActivity,agentIDRetweet,retweetActivityID
	// );
	// addGeneration(stub,retweetActivity,retweetEntity,retweetActivityID,retweetID);
	// addUsage(stub,retweetActivity,tempEntity2,retweetActivityID,temptweetID2);
	// addAttribution(stub,newAgent2,retweetEntity,agentIDRetweet,retweetID);
	// addDerivation(stub,tempEntity2,retweetEntity,temptweetID2,retweetID);
	// //writing code for visualization
	//
	//// edgeWriter.println("{ data: { source: '"+retweetActivityID+"', target:
	// '"+agentIDRetweet+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasAssociatedBy' } },");
	//// edgeWriter.println("{ data: { source: '"+retweetID+"', target:
	// '"+retweetActivityID+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasGeneratedBy' } },");
	//// edgeWriter.println("{ data: { source: '"+retweetActivityID+"', target:
	// '"+temptweetID2+"', faveColor: '#6FB1FC', strength: 70, label: 'Used' }
	// },");
	//// edgeWriter.println("{ data: { source: '"+retweetID+"', target:
	// '"+temptweetID2+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasDerivedFrom' } },");
	////
	//
	//
	//
	//
	// }
	// else
	// {
	// System.out.println("Hey one Skip!");
	// System.out.println(drops.toString()+" "+i);
	//
	// }
	// successors.put(temptweetID2, retweetActivityID);
	// successors.put(retweetID, retweetActivityID);
	//
	//
	//
	// System.out.println("User "+ agentIDRetweet +" has Retweeted Status" +
	// temptweetID2+" which was an effect of "+ affectedBy2);
	//
	//
	//
	//
	// break;
	//
	// case 3:
	// // ReplyOperation
	// // a New AGENT makes a REPLY on an already EXISTING TWEET
	//
	// agentIndex = agentIndex + 1;
	// String agentIDReply = "agent_"+iteration+"_"+id+"_"+agentIndex;
	// AgentType newAgent3 = createAgent(agentIDReply);
	//
	// String temptweetID3
	// ="http://twitter.com/status_"+iteration+"_"+id+"_"+generateRand(entityIndex,1);
	// EntityType tempEntity3 = createFileEntity(temptweetID3,agentIDReply);
	// //AddEntityAttributes(temptweetID3);
	//
	// String affectedBy3 = (String)successors.get(temptweetID3);
	//
	// activityIndex = activityIndex + 1;
	// String replyActivityID = "activity_"+iteration+"_"+id+"_"+activityIndex;
	// ActivityType replyActivity =
	// createWorkflowActivity(replyActivityID,"Reply");
	//
	//
	//
	// //incrementEntityIndex();
	// entityIndex = entityIndex + 1;
	// String replyID
	// ="http://twitter.com/status_"+iteration+"_"+id+"_"+entityIndex;
	// EntityType replyEntity = createFileEntity(replyID,agentIDReply);
	// AddEntityAttributes(replyID);
	//
	//
	// //adding relationships
	//// nodeWriter.println("{ data: { id: '"+agentIDReply+"', name:
	// '"+agentIDReply+"', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'octagon' } },");
	//// nodeWriter.println("{ data: { id: '"+replyActivityID+"', name:
	// '"+replyActivityID+"', weight: 65, faveColor: '#F5A45D', faveShape:
	// 'rectangle' } },");
	//// nodeWriter.println("{ data: { id: '"+replyID+"', name: '"+replyID+"',
	// weight: 65, faveColor: '#EDA1ED', faveShape: 'ellipse' } },");
	//// nodeWriter.println("{ data: { id: '"+agentIDReply+"_info', name:
	// 'Followers: X, Following:Y', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// edgeWriter.println("{ data: { source: '"+agentIDReply+"', target:
	// '"+agentIDReply+"_info', faveColor: '#6FB1FC', strength: 70, label: '' }
	// },");
	////
	//// nodeWriter.println("{ data: { id: '"+replyID+"_info', name: 'info about
	// Reply', weight: 65, faveColor: '#6FB1FC', faveShape: 'roundrectangle' },
	// classes: 'questionable' },");
	//// edgeWriter.println("{ data: { source: '"+replyID+"', target:
	// '"+replyID+"_info', faveColor: '#6FB1FC', strength: 70, label: '' } },");
	////
	//// nodeWriter.println("{ data: { id: '"+replyActivityID+"_info', name:
	// 'Reply operation', weight: 65, faveColor: '#6FB1FC', faveShape:
	// 'roundrectangle' }, classes: 'questionable' },");
	//// edgeWriter.println("{ data: { source: '"+replyActivityID+"', target:
	// '"+replyActivityID+"_info', faveColor: '#6FB1FC', strength: 70, label: ''
	// } },");
	////
	// if(! contains(drops,i))
	// {
	// //System.out.println("Record!");
	//
	// addAssociation(stub,newAgent3,replyActivity,agentIDReply,replyActivityID
	// );
	// addGeneration(stub,replyActivity,replyEntity,replyActivityID,replyID);
	// addUsage(stub,replyActivity,tempEntity3,replyActivityID,temptweetID3);
	// addAttribution(stub,newAgent3,replyEntity,agentIDReply,replyID);
	// //writing code for visualization
	//
	//// edgeWriter.println("{ data: { source: '"+replyActivityID+"', target:
	// '"+agentIDReply+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasAssociatedBy' } },");
	//// edgeWriter.println("{ data: { source: '"+replyID+"', target:
	// '"+replyActivityID+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasGeneratedBy' } },");
	//// edgeWriter.println("{ data: { source: '"+replyActivityID+"', target:
	// '"+temptweetID3+"', faveColor: '#6FB1FC', strength: 70, label: 'Used' }
	// },");
	//// edgeWriter.println("{ data: { source: '"+replyID+"', target:
	// '"+agentIDReply+"', faveColor: '#6FB1FC', strength: 70, label:
	// 'WasAttributedTo' } },");
	////
	////
	// }
	// else
	// {
	// System.out.println("Hey one Skip!");
	// System.out.println(drops.toString()+" "+i);
	//
	// }
	// successors.put(temptweetID3, replyActivityID);
	// successors.put(replyID, replyActivityID);
	//
	//
	// System.out.println("User "+ agentIDReply +" has Replied to Status " +
	// temptweetID3+" which was an effect of "+ affectedBy3);
	//
	//
	// break;
	//
	// default:
	// // ...
	// break;
	//
	// }
	// i++;
	// }
	//// nodeWriter.println("],");
	//// edgeWriter.println(" ] },");
	//// nodeWriter.close();
	//// edgeWriter.close();
	//
	//
	//// System.out.println("Giving 5 seconds for processing of
	// notifications...");
	//// Thread.sleep(5000);
	//// System.out.println("Resuming...");
	////
	//// System.out.println("\n\n Context workflow graph \n\n");
	////
	//// GetContextWorkflowGraphRequestDocument workflowRequest =
	// GetContextWorkflowGraphRequestDocument.Factory
	//// .newInstance();
	//// GetContextWorkflowGraphRequestType requestType =
	// GetContextWorkflowGraphRequestType.Factory.newInstance();
	//// requestType.setInformationDetailLevel(DetailEnumType.FINE);
	//// requestType.setContextWorkflowURI("workflow_main");
	//// workflowRequest.setGetContextWorkflowGraphRequest(requestType);
	//// GetContextWorkflowGraphResponseDocument response =
	// stub.getContextWorkflowGraph(workflowRequest);
	//// System.out.println(response.getGetContextWorkflowGraphResponse().getDocument());
	////
	//
	//
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	//
	// }
	//
	//
	//
	// }
	// //*********************************
	//
	//
	//
	//
	// // ********************************
	// private static int generateID() {
	// Random num = new Random();
	// return num.nextInt(10000);
	// }
	//
	// private AgentType createAgent(String agentId) throws Exception {
	// // Delegate Agent
	// AgentType agent = AgentType.Factory.newInstance();
	// // User Agent
	// UserAgentType userAgent = createUserAgent("Username_" + generateID(),
	// "Test", "email@yildiz.edu.tr", agentId);
	// // Attributes
	// AttributesType attributes = AttributesType.Factory.newInstance();
	// AttributeType[] attributesArr = new AttributeType[2];
	// // Attribute 1
	// Random rand = new Random();
	// String reliability = "" + rand.nextInt(10 - 1 + 1) + 1;
	//
	// AttributeType att1 = createAttribute("Reliability", reliability);
	// attributesArr[0] = att1;
	//
	// userAgent.setAttributes(attributes);
	// agent.setUserAgent(userAgent);
	// agent.setType(AgentEnumType.PERSON);
	// return agent;
	// }
	//
	// private UserAgentType createUserAgent(String name, String aff, String
	// email, String id) {
	// UserAgentType userAgent = UserAgentType.Factory.newInstance();
	// userAgent.setFullName(name);
	// userAgent.setAffiliation(aff);
	// userAgent.setEmail(email);
	// userAgent.setAgentID(id);
	// return userAgent;
	// }
	//
	// private static AttributeType createAttribute(String name, String val)
	// throws Exception {
	// AttributeType att = AttributeType.Factory.newInstance();
	// att.setProperty(name);
	// att.setValue(val);
	// return att;
	// }
	//
	// private static ActivityType createWorkflowActivity(String workflowId,
	// String operation) throws Exception {
	// ActivityType activity = ActivityType.Factory.newInstance();
	// WorkflowInformationType workflow =
	// WorkflowInformationType.Factory.newInstance();
	// workflow.setWorkflowID(workflowId);
	// workflow.setWorkflowNodeID("node1");
	// workflow.setTimestep(20);
	//
	// // Attributes
	// AttributesType attributes = AttributesType.Factory.newInstance();
	// AttributeType[] attributesArr = new AttributeType[1];
	// // Attribute 1
	// AttributeType att1 = createAttribute("Social Operation", operation);
	// attributesArr[0] = att1;
	// attributes.setAttributeArray(attributesArr);
	// workflow.setAttributes(attributes);
	//
	// activity.setWorkflowInformation(workflow);
	// activity.setLocation("Twitter");
	// return activity;
	// }
	//
	// private static void AddEntityAttributes(String entityURI) throws
	// Exception {
	// AddAttributesDocument addAttributesDoc =
	// AddAttributesDocument.Factory.newInstance();
	// AddAttributesType addAttributesType =
	// AddAttributesType.Factory.newInstance();
	// addAttributesType.setObjectType(ObjectEnumType.ENTITY);
	// addAttributesType.setEntityType(EntityEnumType.FILE);
	// addAttributesType.setObjectID(entityURI);
	// addAttributesType.setAddAttributeTimestamp(Calendar.getInstance());
	// addAttributesType.setNotificationTimestamp(Calendar.getInstance());
	// // Attributes
	// AttributesType attributes = AttributesType.Factory.newInstance();
	// AttributeType[] attributesArr = new AttributeType[1];
	// // Attribute 1
	// AttributeType att1 = createAttribute("UserMentions", "Randomly select one
	// of the users");
	// attributesArr[0] = att1;
	//
	// attributes.setAttributeArray(attributesArr);
	// addAttributesType.setAttributes(attributes);
	// addAttributesDoc.setAddAttributes(addAttributesType);
	// stub.addAttributes(addAttributesDoc);
	// }
	//
	// private static ActivityType createServiceActivity(String serviceId,
	// String operation, String workflowID) throws Exception {
	// ActivityType activity = ActivityType.Factory.newInstance();
	// ServiceInformationType service =
	// ServiceInformationType.Factory.newInstance();
	// service.setWorkflowID(workflowID);
	// service.setWorkflowNodeID("node1");
	// service.setServiceID(serviceId);
	// service.setTimestep(20);
	//
	// // Instance of
	// InstanceOfType instanceOf = InstanceOfType.Factory.newInstance();
	// instanceOf.setInstanceOfID("slosh_workflow");
	// instanceOf.setVersion("1.0.0");
	// instanceOf.setCreationTime(Calendar.getInstance());
	// service.setInstanceOf(instanceOf);
	//
	// // Attributes
	// AttributesType attributes = AttributesType.Factory.newInstance();
	// AttributeType[] attributesArr = new AttributeType[1];
	// // Attribute 1
	// AttributeType att1 = createAttribute("Social Operation", operation);
	// attributesArr[0] = att1;
	// attributes.setAttributeArray(attributesArr);
	// service.setAttributes(attributes);
	//
	// activity.setServiceInformation(service);
	// activity.setLocation("Twitter");
	//
	// return activity;
	// }
	//
	// private static EntityType createFileEntity(String fileURI,String
	// ownerURI) throws Exception {
	// EntityType entity = EntityType.Factory.newInstance();
	// FileType file = FileType.Factory.newInstance();
	// file.setFileName(fileURI);
	// file.setFileURI(fileURI);
	// file.setCreateDate(Calendar.getInstance());
	// file.setMd5Sum("dummy_md5_" + fileURI);
	// file.setOwnerDN(ownerURI);
	// file.setSize(500);
	//
	// entity.setFile(file);
	// return entity;
	// }

	public static int generateRand(int max, int min) {
		Random rand = new Random();

		return rand.nextInt(max - min + 1) + min;

	}
	//
	// private static void addAssociation(KomaduServiceStub stub, AgentType
	// agent, ActivityType activity,
	// String agentId, String activityId) throws Exception {
	// AddAgentActivityRelationshipDocument agentActivity =
	// AddAgentActivityRelationshipDocument.Factory.newInstance();
	// AgentActivityType agentActivityType =
	// AgentActivityType.Factory.newInstance();
	//
	// AssociationType association1 = createAssociation1(agentId, activityId);
	// agentActivityType.setActivity(activity);
	// agentActivityType.setAgent(agent);
	// agentActivityType.setAssociation(association1);
	// // execute
	// agentActivity.setAddAgentActivityRelationship(agentActivityType);
	// stub.addAgentActivityRelationship(agentActivity);
	// }
	//
	// private static AssociationType createAssociation1(String agentId, String
	// activityId) throws Exception {
	// AssociationType association = AssociationType.Factory.newInstance();
	// association.setAgentID(agentId);
	// association.setActivityID(activityId);
	//
	// return association;
	// }
	//
	// private static void addAttribution(KomaduServiceStub stub, AgentType
	// agent, EntityType entity,
	// String agentId, String entityId) throws Exception {
	// AddAgentEntityRelationshipDocument activityEntity =
	// AddAgentEntityRelationshipDocument.Factory.newInstance();
	// AgentEntityType agentEntityType = AgentEntityType.Factory.newInstance();
	//
	// AttributionType attribution = AttributionType.Factory.newInstance();
	// attribution.setAgentID(agentId);
	// attribution.setEntityID(entityId);
	// agentEntityType.setAgent(agent);
	// agentEntityType.setEntity(entity);
	// agentEntityType.setAttribution(attribution);
	// activityEntity.setAddAgentEntityRelationship(agentEntityType);
	// // invoke
	// stub.addAgentEntityRelationship(activityEntity);
	// }
	//
	// private static void addGeneration(KomaduServiceStub stub, ActivityType
	// activity, EntityType entity,
	// String activityId, String entityId) throws Exception {
	// AddActivityEntityRelationshipDocument activityEntity =
	// AddActivityEntityRelationshipDocument.Factory.newInstance();
	// ActivityEntityType activityEntityType =
	// ActivityEntityType.Factory.newInstance();
	//
	// GenerationType generation = GenerationType.Factory.newInstance();
	// generation.setActivityID(activityId);
	// generation.setEntityID(entityId);
	// generation.setLocation("Location1");
	// generation.setTimestamp(Calendar.getInstance());
	//
	// activityEntityType.setActivity(activity);
	// activityEntityType.setEntity(entity);
	// activityEntityType.setGeneration(generation);
	// activityEntity.setAddActivityEntityRelationship(activityEntityType);
	// // invoke
	// stub.addActivityEntityRelationship(activityEntity);
	// }
	//
	// private static void addUsage(KomaduServiceStub stub, ActivityType
	// activity, EntityType entity,
	// String activityId, String entityId) throws Exception {
	// AddActivityEntityRelationshipDocument activityEntity =
	// AddActivityEntityRelationshipDocument.Factory.newInstance();
	// ActivityEntityType activityEntityType =
	// ActivityEntityType.Factory.newInstance();
	//
	// UsageType usage = UsageType.Factory.newInstance();
	// usage.setActivityID(activityId);
	// usage.setEntityID(entityId);
	// usage.setLocation("Location2");
	// usage.setTimestamp(Calendar.getInstance());
	//
	// activityEntityType.setActivity(activity);
	// activityEntityType.setEntity(entity);
	// activityEntityType.setUsage(usage);
	// activityEntity.setAddActivityEntityRelationship(activityEntityType);
	// // invoke
	// stub.addActivityEntityRelationship(activityEntity);
	// }
	//
	//
	// private static void addDerivation(KomaduServiceStub stub, EntityType
	// usedEntity, EntityType generatedEntity,
	// String usedId, String generatedId) throws Exception {
	// AddEntityEntityRelationshipDocument activityEntity =
	// AddEntityEntityRelationshipDocument.Factory.newInstance();
	// EntityEntityType entityEntityType =
	// EntityEntityType.Factory.newInstance();
	//
	// DerivationType derivation = DerivationType.Factory.newInstance();
	// derivation.setUsedEntityID(usedId);
	// derivation.setGeneratedEntityID(generatedId);
	//
	//
	// entityEntityType.setEntity1(usedEntity);
	// entityEntityType.setEntity2(generatedEntity);
	// entityEntityType.setDerivation(derivation);
	// activityEntity.setAddEntityEntityRelationship(entityEntityType);
	// // invoke
	// stub.addEntityEntityRelationship(activityEntity);
	// }

	// public void incrementEntityIndex() {
	// entityIndex = entityIndex + 1;
	// }
	//
	// public void incrementAgentIndex() {
	// agentIndex = agentIndex + 1;
	// }
	//
	// public void incrementActivityIndex() {
	// activityIndex = activityIndex + 1;
	// }

}
