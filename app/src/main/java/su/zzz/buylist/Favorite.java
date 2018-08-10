package su.zzz.buylist;

public class Favorite {
    private Long id;
    private String name;
    private Boolean need;

    public Favorite(Long id, String name, boolean need){
        this.id = id;
        this.name = name;
        this.need = need;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Boolean getNeed() {
        return need;
    }
}
