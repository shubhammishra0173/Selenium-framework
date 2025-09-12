package utilites.exeptions;

public class PropertyFileNotFoundException extends RuntimeException{
    public static final long serailVersionUID= 1L;
    public PropertyFileNotFoundException(){

    }
    public PropertyFileNotFoundException(String arg0){
        super(arg0);
    }
}
