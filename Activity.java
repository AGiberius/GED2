package ged;

/**
 *
 * Anouchka Giberius
 */
public enum Activity {
   
    A("THINK_AW","Serious thought"),
    B("BUSPLNAW","Began business plan "),
    C("DFNMKTAW","Define markets to enter"),
    D("FINPRJAW","Financial projections"),
    E("ASKFNDAW","Asked for formal funding"),
    F("GETFNDAW","Got initial formal financing"),
    G("FTWK_AW1","MBR 1: Full time start-up work"),
    H("HIRE__AW","Hired employee"),
    I("LEASE_AW","Leased, acquired major assets"),
    J("MODEL_AW","Developed model, prototype"),
    K("ONINVAW1","MBR 1: Invest own money"),
    L("PATENTAW","Patent, copyright, trademark filing"),
    M("PHLISTAW","Phone book listing for firm"),
    N("PROMOTAW","Promote products or services"),
    O("PURCHAAW","Purchased materials, supplies, parts"),
    P("SUTEAMAW","Began to organized start-up team"),
    Q("SALES_AW","Sales, income, or revenue"),
    R("SUPCRDAW","Obtaining supplied credit"),
    S("EIN___AW","Acquired registration number");

    public final String label;
    public final String description;

    private Activity(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public static Activity valueOfLabel(String label) {
        for (Activity e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
    
}
