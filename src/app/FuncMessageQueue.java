package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import java.util.concurrent.TimeoutException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AuthenticationFailureException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import externallib.IniReader;

public class FuncMessageQueue {
	public static String LOG_TYPE_OPEN = "OPEN CONNECTION";
	public static String LOG_TYPE_CLOSE = "CLOSE CONNECTION";
	public static String LOG_TYPE_RECEIVE = "FROM MQ SERVER";
	public static String LOG_TYPE_TO = "TO MQ SERVER";
	public static String LOG_TYPE_CREATE_QUEUE = "CREATE QUEUE";
	public static String LOG_TYPE_CREATE_EXCHANGE = "CREATE EXCHANGE";
	public static String LOG_TYPE_ERROR = "ERROR";
	public static String LOG_TYPE_CONNECTION_ERROR = "CONNECION ERROR";
	private String m_sMqServer;
	private int m_iMqPort;
	private String m_sLogin;
	private String m_sPassword;
	
	private ConnectionFactory m_oMqFactory;
	private Connection m_oMqConnection;
	private Channel m_oMqChannel;
	private Consumer m_oMqConsumer;
	
	private String m_sResponseQueue;
	private String m_sRequestContent;
	
	private boolean m_bConnected;
	private boolean m_bLoginFail;
	public final static int CONNECTION_MAX_RETRY_COUNT = 10;
	public final static int RETRY_CONNECTION_INTERVAL = 30 * 1000;
	
	// message queue name
	public final static String QUEUE_NAME_ALERT_MESSAGE = "POS_alert_message";
	
	public FuncMessageQueue() {
		// Connect to HERO Cloud MQ server
		m_sMqServer = "mq.hero-cloud.com";
		m_iMqPort = 5672;
		
		// Read config from setup file
		HashMap<String, String> oMessageQueueSetupList = loadMessageQueueSetup();
		m_sMqServer = (oMessageQueueSetupList.containsKey("path")) ? oMessageQueueSetupList.get("path") : "mq.hero-cloud.com";
		
		m_sLogin = (oMessageQueueSetupList.containsKey("login")) ? oMessageQueueSetupList.get("login") : "";
		m_sPassword = (oMessageQueueSetupList.containsKey("password")) ? oMessageQueueSetupList.get("password") : "";
		
		m_bConnected = false;
	}
	
	public FuncMessageQueue(String sMqServer, int iMqPort, String sLogin, String sPassword) {
		m_sMqServer = sMqServer;
		m_iMqPort = iMqPort;
		m_sLogin = sLogin;
		m_sPassword = sPassword;
		
		m_bConnected = false;
		m_bLoginFail = false;
	}
	
	// Connect to HERO Cloud MQ server and read login from setup file
	public boolean initConnection() {
		try {
			m_bConnected = false;
			
			// Connect to MQ server
			m_oMqFactory = new ConnectionFactory();
			m_oMqFactory.setHost(m_sMqServer);
			m_oMqFactory.setPort(m_iMqPort);
			m_oMqFactory.setUsername(m_sLogin);
			m_oMqFactory.setPassword(m_sPassword);
			
			// Set the heartbeat timeout to 60s
			m_oMqFactory.setRequestedHeartbeat(60);
			
			// Enable auto recovery
			m_oMqFactory.setAutomaticRecoveryEnabled(true);
			
			boolean bRetry = true;
			int iConnectionCnt = 0;
			while (bRetry && iConnectionCnt<CONNECTION_MAX_RETRY_COUNT) {
				try {
					m_oMqConnection = m_oMqFactory.newConnection();
					bRetry = false;
				}
				catch (IOException e) {
					bRetry = true;
					if (iConnectionCnt > 0)
						writePacketLog(FuncMessageQueue.LOG_TYPE_CONNECTION_ERROR, "Retry " +iConnectionCnt+ ", " +e.getMessage());
					else
						writePacketLog(FuncMessageQueue.LOG_TYPE_CONNECTION_ERROR, e.getMessage());
					AppGlobal.stack2Log(e);
					Thread.sleep(RETRY_CONNECTION_INTERVAL);
				}
				iConnectionCnt ++;
			}
			
			if (bRetry)
				return false;
			
			m_oMqChannel = m_oMqConnection.createChannel();
			
			// Connect successfully
			m_bConnected = true;
			
			writePacketLog(FuncMessageQueue.LOG_TYPE_OPEN, m_sMqServer);
			
			return true;
		} catch (TimeoutException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Connect MQ server timeout");
			AppGlobal.stack2Log(e);
			return false;
		} catch (AuthenticationFailureException e) {
			// Login fail
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Fail to login MQ server");
			AppGlobal.stack2Log(e);
			
			// Set the flag for login failure
			m_bLoginFail = true;
			
			return false;
		} catch (IOException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Connect MQ server error");
			AppGlobal.stack2Log(e);
			return false;
		} catch (Exception e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Connect MQ internal error");
			AppGlobal.stack2Log(e);
			return false;
		}
	}
	
	// Connect to HERO Cloud MQ server through SSL and read login from setup file
	public boolean initSSLConnection() {
		try {
			m_bConnected = false;
			
			// Connect to MQ server
			m_oMqFactory = new ConnectionFactory();
			m_oMqFactory.setHost(m_sMqServer);
			m_oMqFactory.setPort(m_iMqPort);
			m_oMqFactory.setUsername(m_sLogin);
			m_oMqFactory.setPassword(m_sPassword);
			
			// Support SSL
			m_oMqFactory.useSslProtocol();
			
			// Set the heartbeat timeout to 60s
			m_oMqFactory.setRequestedHeartbeat(60);
			
			// Enable auto recovery
			m_oMqFactory.setAutomaticRecoveryEnabled(true);
			
			boolean bRetry = true;
			int iConnectionCnt = 0;
			while (bRetry && iConnectionCnt<CONNECTION_MAX_RETRY_COUNT) {
				try {
					m_oMqConnection = m_oMqFactory.newConnection();
					bRetry = false;
				}
				catch (IOException e) {
					bRetry = true;
					if (iConnectionCnt > 0)
						writePacketLog(FuncMessageQueue.LOG_TYPE_CONNECTION_ERROR, "Retry " +iConnectionCnt+ ", " +e.getMessage());
					else
						writePacketLog(FuncMessageQueue.LOG_TYPE_CONNECTION_ERROR, e.getMessage());
					AppGlobal.stack2Log(e);
					Thread.sleep(RETRY_CONNECTION_INTERVAL);
				}
				iConnectionCnt ++;
			}
			
			if (bRetry)
				return false;
			
			m_oMqChannel = m_oMqConnection.createChannel();
			
			// Connect successfully
			m_bConnected = true;
			
			writePacketLog(FuncMessageQueue.LOG_TYPE_OPEN, m_sMqServer);
			
			return true;
		} catch (TimeoutException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Connect MQ server timeout");
			AppGlobal.stack2Log(e);
			return false;
		} catch (AuthenticationFailureException e) {
			// Login fail
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Fail to login MQ server");
			AppGlobal.stack2Log(e);
			
			// Set the flag for login failure
			m_bLoginFail = true;
			
			return false;
		} catch (IOException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Connect MQ server error");
			AppGlobal.stack2Log(e);
			return false;
		} catch (Exception e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Connect MQ internal error");
			AppGlobal.stack2Log(e);
			return false;
		}
	}
	
	// Create exchange
	public boolean declareExchange(String sExchangeName, String sExchangeType) {
		try {
			m_oMqChannel.exchangeDeclare(sExchangeName, sExchangeType);
			
			writePacketLog(FuncMessageQueue.LOG_TYPE_CREATE_EXCHANGE, sExchangeName);
			
			return true;
		} catch (IOException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Unable to create exchange - "+sExchangeName);
			AppGlobal.stack2Log(e);
			return false;
		}
	}
	
	// Create queue with provided queue name
	public String createQueue(String sQueueName, String sExchangeName, String sRoutingKey, boolean bAutoDelete) {
		try {
			sQueueName = m_oMqChannel.queueDeclare(sQueueName, true, false, bAutoDelete, null).getQueue();
			
			if (!sExchangeName.isEmpty())
				m_oMqChannel.queueBind(sQueueName, sExchangeName, sRoutingKey);
			
			writePacketLog(FuncMessageQueue.LOG_TYPE_CREATE_QUEUE, sQueueName);
			
			return sQueueName;
		} catch (IOException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Unable to create queue - "+sQueueName);
			AppGlobal.stack2Log(e);
			return "";
		}
	}
	
	// Consume queue with provided queue name
	public boolean consumeQueue(String sQueueName, final AppThread oRunnable) {
		try {
			m_oMqConsumer = new DefaultConsumer(m_oMqChannel) {
				@Override
				public void handleDelivery(String sConsumerTag, Envelope oEnvelope, AMQP.BasicProperties properties, byte[] bBody) {
					try {
						m_oMqChannel.basicAck(oEnvelope.getDeliveryTag(), false);
						
						m_sResponseQueue = m_sRequestContent = "";
						m_sResponseQueue = properties.getReplyTo();
						m_sRequestContent = new String(bBody, "UTF-8");
						
						writePacketLog(LOG_TYPE_RECEIVE, "[Response Queue:"+m_sResponseQueue+"] "+m_sRequestContent);
						
						oRunnable.run();
					}catch (IOException e) {
						//write log
					}
				}
			};
			
			m_oMqChannel.basicConsume(sQueueName, false, m_oMqConsumer);
			return true;
		} catch (IOException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Unable to comsume queue ("+sQueueName+")");
			AppGlobal.stack2Log(e);
			return false;
		}
	}
	
	// Publish message to provided queue
	public void publishMessage(String sExchangeName, String sQueueNameOrRoutingKey, String sMessage, String sExpiration) {
		try {
			AMQP.BasicProperties.Builder propertiesBuilder = new AMQP.BasicProperties.Builder();
			
			propertiesBuilder.contentType("text/plain");
			if (sExpiration != null)
				propertiesBuilder.expiration(sExpiration);
			
			m_oMqChannel.basicPublish(sExchangeName, sQueueNameOrRoutingKey, propertiesBuilder.build(), sMessage.getBytes());
			
			if (sExchangeName.isEmpty())
				writePacketLog(FuncMessageQueue.LOG_TYPE_TO, "[Publish Queue:"+sQueueNameOrRoutingKey+"] "+sMessage);
			else
				writePacketLog(FuncMessageQueue.LOG_TYPE_TO, "[Publish Routing Key:"+sQueueNameOrRoutingKey+"] "+sMessage);
		} catch (IOException e) {
			if (sExchangeName.isEmpty())
				writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Unable to publish message to queue ("+sQueueNameOrRoutingKey+") - "+sMessage);
			else
				writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Unable to publish message to routing key ("+sQueueNameOrRoutingKey+") - "+sMessage);
			AppGlobal.stack2Log(e);
		}
	}
	
	// Close connection
	public void closeConnection() {
		try {
			m_oMqChannel.close();
			m_oMqConnection.close();
			
			writePacketLog(FuncMessageQueue.LOG_TYPE_CLOSE, m_sMqServer);
		} catch (TimeoutException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Fail to close connection");
		} catch (IOException e) {
			writePacketLog(FuncMessageQueue.LOG_TYPE_ERROR, "Fail to close connection");
		}
	}
	
	public boolean isAlive() {
		return m_oMqConnection.isOpen();
	}
	
	public boolean isConnected() {
		// Server is connected and the connection is keeping alive
		return (m_bConnected && m_oMqConnection.isOpen());
	}
	
	public String getResponseQueue() {
		return m_sResponseQueue;
	}
	
	public String getRequest() {
		return m_sRequestContent;
	}
	
	public boolean isLocalServer() {
		return m_sMqServer.contains("localhost") || m_sMqServer.contains("127.0.0.1");
	}
	
	/**** Private Function ****/
	/**************************/
	private HashMap<String, String> loadMessageQueueSetup() {
		HashMap<String, String> oMessageQueueSetupList = new HashMap<String, String>();
		
		// Load the internal ID and password from config.ini
		// Read setup from the setup file
		IniReader iniReader = null;
		try {
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
			String sTmp = iniReader.getValue("message queue", "login");
			if(sTmp != null) {
				String sLoginId = sTmp;
				oMessageQueueSetupList.put("login", sLoginId);
			}
			
			sTmp = iniReader.getValue("message queue", "password");
			if(sTmp != null) {
				String sPassword = sTmp;
				oMessageQueueSetupList.put("password", sPassword);
			}
			
			sTmp = iniReader.getValue("message queue", "path");
			if(sTmp != null) {
				String sPath = sTmp;
				oMessageQueueSetupList.put("path", sPath);
			}
			
			sTmp = iniReader.getValue("message queue", "node");
			if(sTmp != null) {
				String sNode = sTmp;
				oMessageQueueSetupList.put("node", sNode);
			}
			
			sTmp = iniReader.getValue("message queue", "support_https");
			if(sTmp != null) {
				String sSupportHttps = sTmp;
				oMessageQueueSetupList.put("supportHttps", sSupportHttps);
			}
		} catch (IOException e) {
			// Fail to read config.ini
			AppGlobal.stack2Log(e);
		}
		
		return oMessageQueueSetupList;
	}
	
	public void writePacketLog(String sLogType, String sLog) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/message_queue_packet_log." + sCurrentMonth;
		
		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append("[server:");
			sContent.append(m_sMqServer);
			sContent.append("] ");
			if(sLogType.equals(FuncMessageQueue.LOG_TYPE_OPEN) ||
					sLogType.equals(FuncMessageQueue.LOG_TYPE_CLOSE) ||
					sLogType.equals(FuncMessageQueue.LOG_TYPE_CREATE_QUEUE) ||
					sLogType.equals(FuncMessageQueue.LOG_TYPE_CREATE_EXCHANGE))
				sContent.append(sLogType+" ----- ");
			else if(sLogType.equals(FuncMessageQueue.LOG_TYPE_TO))
				sContent.append(FuncMessageQueue.LOG_TYPE_TO+" >>>>> ");
			else if(sLogType.equals(FuncMessageQueue.LOG_TYPE_RECEIVE))
				sContent.append(FuncMessageQueue.LOG_TYPE_RECEIVE+" <<<<< ");
			else if(sLogType.equals(FuncMessageQueue.LOG_TYPE_ERROR))
				sContent.append(" !!!!! "+FuncMessageQueue.LOG_TYPE_ERROR+" !!!!! ");
			else if(sLogType.equals(FuncMessageQueue.LOG_TYPE_CONNECTION_ERROR))
				sContent.append(" !!!!! "+FuncMessageQueue.LOG_TYPE_CONNECTION_ERROR+" !!!!! ");
			
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			out.close();
			fstream.close();
		}catch (Exception e){}
	}
}