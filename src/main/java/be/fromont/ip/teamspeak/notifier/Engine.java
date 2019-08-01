package be.fromont.ip.teamspeak.notifier;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.GraphResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

public class Engine
  {

  private static Engine instance;

  private String accessToken;
  private String groupId;

  private Engine(String accessToken, String groupId)
    {
    this.accessToken = accessToken;
    this.groupId = groupId;
    }

  /**
   * Generate a single instance of the engine class
   *
   * @return the single engine class instance
   * @throws IOException
   */
  public static Engine getInstance() throws IOException
    {
    if(instance==null)
      {
      Properties authProp = new Properties();
      InputStream input = Engine.class.getClassLoader().getResourceAsStream("auth.properties");
      authProp.load(input);
      String accessToken = authProp.getProperty("accessToken");
      String groupId = authProp.getProperty("groupId");
      instance = new Engine(accessToken, groupId);
      }
    return instance;
    }

  /**
   * Generate the message sending request and send it
   */
  public void sendToFacebook(String message)
    {
    FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
    GraphResponse response = facebookClient.publish(groupId + "/feed", GraphResponse.class, Parameter.with("message", message));
    }

  /**
   * Retrieve the public IP adress of your machine
   *
   * @return the public IP adress
   * @throws UnknownHostException
   */
  public String getPublicIp() throws UnknownHostException
    {
    String ip;
    try
      {
      URL url_name = new URL("http://bot.whatismyipaddress.com");

      BufferedReader sc =
          new BufferedReader(new InputStreamReader(url_name.openStream()));

      // reads system IPAddress
      ip = sc.readLine().trim();
      }
    catch (Exception e)
      {
      ip = "Cannot Execute Properly";
      }
    return ip;
    }

  }
