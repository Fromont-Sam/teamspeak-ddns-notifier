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
  private static void start(String[] args) throws IOException, InterruptedException {
    //init
    Engine engine = Engine.getInstance();
    String newIpAdress;

    while(true)
      {
      try
        {
        //get ip
        newIpAdress = engine.getPublicIp();
        if(newIpAdress!= null && !ipAdress.equals(newIpAdress))
          {
          LOG.info("A new IP has been found {}", newIpAdress);
          ipAdress = newIpAdress;
          engine.sendToDynu(ipAdress);
          }
        }
      catch (UnknownHostException e)
        {
        LOG.error("Connection lost", e);
        }
        catch (IOException e)
        {
        LOG.error("An error occurred", e);
        }
      finally
        {
        Thread.sleep(WAITING_TIME);
        }
      }
    }
  }
