package be.fromont.ip.teamspeak.notifier;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main
  {

  private static final Integer WAITING_TIME = 300000;

  private static String ipAdress = "";
  private static String newIpAdress = null;
  private static Reason reason = Reason.UNKNOWN_REASON;

  /**
   * Main process
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException
    {
    //init
    Engine engine = Engine.getInstance();
    if(args.length>=1 && args[0].toUpperCase().equals("RESTART"))
      {
      reason = Reason.RESTART_SERVER;
      }

    //start scanning ip thread
    Thread t = new Thread(() ->
      {
      while(true)
        {
        try
          {
          //get ip
          newIpAdress = engine.getPublicIp();
          if(!ipAdress.equals(newIpAdress))
            {
            ipAdress = newIpAdress;
//            engine.sendToFacebook(generateMessage());
            System.out.println(generateMessage());
            }
          Thread.sleep(WAITING_TIME);
          }
        catch (InterruptedException e)
          {
          System.out.println("InterruptedException");
          e.printStackTrace();
          }
        catch (UnknownHostException e)
          {
          System.out.println("UnknownHostException");
          e.printStackTrace();
          }
        }
      });
    t.start();
    }

  /**
   * Generate the message to send to the facebook group
   *
   * @return the message to send
   */
  private static String generateMessage()
    {
    StringBuilder sb = new StringBuilder();
    sb.append("Nouvelle adresse IP teamspeak : ");
    sb.append(ipAdress);
    sb.append("\n");
    sb.append("\n");
    sb.append("Raison : ");
    sb.append(reason.getReasonDescription());
    return sb.toString();
    }
  }
