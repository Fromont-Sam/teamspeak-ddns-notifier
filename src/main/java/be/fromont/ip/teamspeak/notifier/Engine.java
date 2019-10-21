package be.fromont.ip.teamspeak.notifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

@SuppressWarnings({"squid:CallToDeprecatedMethod"})
public class Engine
  {

  private static final Logger LOG = LogManager.getLogger(Engine.class);

  private static Engine instance;

  private String username;
  private String password;

  private Engine(String username, String password)
    {
    this.username = username;
    this.password = password;
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
      String username = authProp.getProperty("username");
      String password = authProp.getProperty("password");
      instance = new Engine(username, password);
      }
    return instance;
    }

  /**
   * Update DNS IP
   *
   * @param ip
   * @throws IOException
   */
  public void sendToDynu(String ip) throws IOException
    {
    LOG.info("Try to send to dynu the new ip...");
    String url = "http://api.dynu.com/nic/update?myip=" + ip + "&username=" + username + "&password=" + password;
    HttpURLConnection con = null;

    try
      {
      URL myurl = new URL(url);
      con = (HttpURLConnection) myurl.openConnection();
      con.setRequestMethod("GET");

      StringBuilder content;

      try (BufferedReader in = new BufferedReader(
          new InputStreamReader(con.getInputStream())))
        {
        String line;
        content = new StringBuilder();
        while ((line = in.readLine()) != null)
          {
          content.append(line);
          content.append(System.lineSeparator());
          }
        }
      LOG.info("Response : " + content.toString());

      }
    finally
      {
      con.disconnect();
      }
    }

  /**
   * Retrieve the public IP adress of your machine
   *
   * @return the public IP adress
   * @throws UnknownHostException
   */
  public String getPublicIp() throws IOException
    {
    String ip;
    URL url_name = new URL("http://bot.whatismyipaddress.com");

    BufferedReader sc =
        new BufferedReader(new InputStreamReader(url_name.openStream()));

    // reads system IPAddress
    ip = sc.readLine().trim();
    return addZeroToIp(ip);
    }

    /**
     * Transform a ip adress to fill it with zero
     *
     * @param ipAdress the ip adress to transform
     * @return the ip address filled with zero
     */
    public String addZeroToIp(String ipAdress)
    {
      try
      {
        String[] parts = ipAdress.split("\\.");
        StringBuilder res = new StringBuilder();
        for(String part : parts)
        {
          res.append(String.format("%02d", Integer.parseInt(part)));
          res.append(".");
        }
        res.deleteCharAt(res.length()-1);
        return res.toString();
      }
      catch(Exception e)
      {
        LOG.error("An error occurred while trying to fill IP adress with zero", e);
        return null;
      }
    }

  }
