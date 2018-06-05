class RPQ{

    public enum type_rpq{
        Empty,  // element vide
        Reg,    // expression reguliere pour constituer un match A -> B
        Reg_inv,// expression inverse, traduit un chemins qui va de A <- B
        Or,     // Union de deux RPQ
        And,    // La concatenation de deux RPQ
        Star;   // Klene star
    }

    type_rpq type = null; // le Type de la RPQ, definie dans type_rpq
    RPQ gauche = null; // Si la RPQ est composer, alors gauche et droite sont remplie
    RPQ droite = null; // Par les sous-RPQ qui la compose
    String value = null; // Si la RPQ est seulement une

    // Constructeur de base, ne sera pas utiliser
    private RPQ (RPQ _gauche, type_rpq _type, RPQ _droite, String _value){
        this.type = _type;
        this.gauche = _gauche;
        this.droite = _droite;
        this.value = _value;
    }

    // Constructeur pour les RPQ composer, c'est a dire constituer de deux sous RPQ
    // dont on fait la concatenation ou l'union
    // RPQ(RPQ(), type_rpq.And, RPQ(Like))
    public RPQ( RPQ _gauche, type_rpq _type, RPQ _droite){
        this.gauche = _gauche;
        this.type = _type;
        this.droite = _droite;
    }

    // Constructeur pour les RPQ sous l'étoile
    // RPQ(RPQ(),type_rpq.Star)
    public RPQ(RPQ r, type_rpq star){
        this.gauche = r;
        this.type = star;
    }

    // Constructeur pour les RPQ simple qui prenne seulement un mot de l'alphabet A
    // RPQ(Like), avec Like qui appartient a A
    public RPQ (String regex){
        // On teste si le derniers char est un -, c'est a dire si l'on recherche
        // le chemins inverse, RPQ(Like-)
        if(regex.charAt(regex.length()-1)== '-'){
            //RPQ(Reg_inv,null,null,regex.substring(0,regex.length()-1));
            this.type = type_rpq.Reg_inv;
            this.value = regex.substring(0,regex.length()-1);// On enleve le - qui ne fait pas partie du pattern
        }
        else{
            //RPQ(Reg,null,null,regex);
            this.type = type_rpq.Reg;
            this.value = regex;
        }
    }
    // RPQ()
    public RPQ(){
        //RPQ(type_rpq.Empty,null,null,null);
        this.type = type_rpq.Empty;
    }

    // Methode toString recursive, qui affiche a la maniere de G-mark les requétes
    public String toString(){
        switch (this.type) {
            case Empty: return "";
            case Reg: return this.value;
            case Reg_inv: return this.value+"-";
            case And: return this.gauche.toString()+"+"+this.droite.toString();
            case Or: return this.gauche.toString()+"."+this.droite.toString();
            case Star: return "("+this.gauche.toString()+")*";
        }
        return "";
    }


    public static void main(String[] args){
        // La RPQ vide, qui match n'importe quel relation
        RPQ r = new RPQ();
        System.out.println(r);

        // (like.Comment-)
        RPQ r2 = new RPQ(
            new RPQ("like"),
            type_rpq.And,
            new RPQ("Comment-")
        );
        System.out.println(r2);

        // (Like+Comment-)
        RPQ r3 = new RPQ(
            new RPQ(
                new RPQ("Like"),
                type_rpq.Or,
                new RPQ("Comment-")
            ),
            type_rpq.Star
        );
        System.out.println(r3);

        // r2 sous l'étoile
        System.out.println(new RPQ(r2,type_rpq.Star));
    }
}
