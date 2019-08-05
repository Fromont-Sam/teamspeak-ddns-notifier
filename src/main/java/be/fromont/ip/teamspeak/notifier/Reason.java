package be.fromont.ip.teamspeak.notifier;

public enum Reason
  {

  RESTART_SERVER(101,"Redémarrage manuel du serveur."),
  PROXIMUS_DYNAMIC_IP(102,"Proximus a modifié de manière dynamique l'IP du serveur."),
  POWER_OUTRAGE(103,"Panne de courant, le serveur a du se relancer et l'ip a reset."),
  LOST_CONNECTION(104,"Perte de connexion internet.");

  /**
   * Reason code
   */
  private Integer reasonCode;

  /**
   * Reason description
   */
  private String reasonDescription;

  Reason(final int reasonCode, final String reasonDescription)
    {
    this.reasonCode = reasonCode;
    this.reasonDescription = reasonDescription;
    }

  /**
   * Retrieve the reason code
   *
   * @return the reason code
   */
  public Integer getReasonCode()
    {
    return this.reasonCode;
    }

  /**
   * Retrieve the reason description
   *
   * @return the reason description
   */
  public String getReasonDescription()
    {
    return this.reasonDescription;
    }

  }
