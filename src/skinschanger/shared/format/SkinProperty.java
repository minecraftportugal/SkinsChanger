package skinschanger.shared.format;

public class SkinProperty
{
    private String name;
    private String value;
    private String signature;
    
    public SkinProperty(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    public SkinProperty(final String name, final String value, final String signature) {
        this(name, value);
        this.signature = signature;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public boolean hasSignature() {
        return this.signature != null;
    }
    
    public String getSignature() {
        return this.signature;
    }
}
