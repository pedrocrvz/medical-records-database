package pt.ulisboa.tecnico.sirs;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        NHS.main(new String[]{
                "keys/NHS.jks",
                "NHS",
                "password123"
        });
    }
}
