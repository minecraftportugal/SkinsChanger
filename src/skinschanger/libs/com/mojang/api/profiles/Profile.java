package skinschanger.libs.com.mojang.api.profiles;

public class Profile
{
    private String id;
    private String name;
    
    public Profile(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
}
