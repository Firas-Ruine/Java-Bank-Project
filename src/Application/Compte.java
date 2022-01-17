package Application;

public class Compte {
    private int idcompte ;
    private int idclient;
    private double solde;
    private double decouvert;
    private double MaxDecouvert;
    private double MaxRetrait;

    public Compte() {
    }

    public Compte(int idcompte, int idclient, double solde, double decouvert, double maxDecouvert, double maxRetrait) {
        this.idcompte = idcompte;
        this.idclient = idclient;
        this.solde = solde;
        this.decouvert = decouvert;
        MaxDecouvert = maxDecouvert;
        MaxRetrait = maxRetrait;
    }

   public Compte(double solde, double maxdecouvert) {
    }


    public int getidcompte() {
            return idcompte;
        }

        public void setidcompte(int idcompte) {
            this.idcompte = idcompte;
        }

        public int getidclient() {
            return idclient;
        }

        public void setidclient(int idclient) {
            this.idclient = idclient;
        }

        public double getSolde() {
            return solde;
        }

        public void setSolde(double solde) {
            this.solde = solde;
        }

        public double getDecouvert() {
            return decouvert;
        }

        public void setDecouvert(double decouvert) {
            this.decouvert = decouvert;
        }

        public double getMaxDecouvert() {
            return MaxDecouvert;
        }

        public void setMaxDecouvert(double maxDecouvert) {
            MaxDecouvert = maxDecouvert;
        }

        public double getMaxRetrait() {
            return MaxRetrait;
        }

        public void setMaxRetrait(double maxRetrait) {
            MaxRetrait = maxRetrait;
        }

    @Override
    public String toString() {
        return "Compte{" +
                "idcompte=" + idcompte +
                ", idclient=" + idclient +
                ", solde=" + solde +
                ", decouvert=" + decouvert +
                ", MaxDecouvert=" + MaxDecouvert +
                ", MaxRetrait=" + MaxRetrait +
                '}';
    }
}
