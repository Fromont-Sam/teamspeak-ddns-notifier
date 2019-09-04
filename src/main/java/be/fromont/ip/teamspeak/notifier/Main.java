package be.fromont.ip.teamspeak.notifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.UnknownHostException;

@SuppressWarnings({"squid:S2142"})
public class Main
  {

  private static final Logger LOG = LogManager.getLogger(Main.class);

  private static final Integer WAITING_TIME = 420000;

  private static String ipAdress = "";
  private static Reason reason = Reason.PROXIMUS_DYNAMIC_IP;

  /**
   * Main process that manage all unexpected cases
   *
   * @param args the arguments provided while launching the process
   */
  public static void main(String[] args)
    {
    try
      {
      LOG.info("Starting the process");
      start(args);
      }
    catch(InterruptedException e)
      {
      LOG.error("An error occurred while waiting the next IP scan", e);
      System.exit(-1);
      }
    catch(IOException e)
      {
      LOG.error("An error occurred while scanning the public IP", e);
      System.exit(-1);
      }
    }

  /**
   * Logical process that manage all expected cases
   *
   * @param args the arguments provided while launching the process
   * @throws InterruptedException
   * @throws IOException
   */
  private static void start(String[] args) throws InterruptedException, IOException
    {
    //init
    Engine engine = Engine.getInstance();
    String newIpAdress;

    //Launch args
    if(args.length>=1 && args[0].equalsIgnoreCase("RESTART"))
      {
      reason = Reason.RESTART_SERVER;
      }
    else
      {
      reason = Reason.POWER_OUTRAGE;
      }

    while(true)
      {
      try
        {
        //get ip
        newIpAdress = engine.getPublicIp();
        if(!ipAdress.equals(newIpAdress))
          {
          LOG.info("A new IP has been found {}", newIpAdress);
          ipAdress = newIpAdress;
          engine.sendToFacebook(generateMessage());
          }
        }
      catch (UnknownHostException e)
        {
        reason = Reason.LOST_CONNECTION;
        }
      finally
        {
        Thread.sleep(WAITING_TIME);
        }
      }
    }

  /**
   * Generate the message to send to the facebook group
   *
   * @return the message to send
   */
  private static String generateMessage()
    {
    StringBuilder sb = new StringBuilder("Nouvelle adresse IP teamspeak : ");
    sb.append(ipAdress);
    sb.append("\n");
    sb.append("\n");
    sb.append("Raison : ");
    sb.append(reason.getReasonDescription());
    reason = Reason.PROXIMUS_DYNAMIC_IP;
    return sb.toString();
    }
  }
