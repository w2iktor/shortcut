package pl.symentis.byteman.tutorial;

public class Main
{
    public static void main( String[] args )
    {
        new Main().run();
    }

    public void run()
    {
        System.out.println( "Hello " + ConfigurationSingleton.getInstance().getProperty() );
    }
}
