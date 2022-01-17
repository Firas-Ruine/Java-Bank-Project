package Application;

public class Client {

    private int idclient;
    private String Nom;
    private String Prenom;
    private String Adresse;


    public Client() {
    }

    public Client( int idClient,String nom, String prenom, String adresse) {
       this.idclient=idClient;
        this.Nom = nom;
        this.Prenom = prenom;
        this.Adresse = adresse;
    }

    public Client(String text, String text1, String text2) {
    }

    public int getidclient() {
        return idclient;
    }

    public void setidclient(int idClient) {
        this.idclient = idClient;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idclient=" + idclient +
                ", Nom='" + Nom + '\'' +
                ", Prenom='" + Prenom + '\'' +
                ", Adresse='" + Adresse + '\'' +
                '}';
    }
}
