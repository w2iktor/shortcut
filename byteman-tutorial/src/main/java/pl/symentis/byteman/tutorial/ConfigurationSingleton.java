package pl.symentis.byteman.tutorial;

public class ConfigurationSingleton
{
    private static ConfigurationSingleton instance;

    public static ConfigurationSingleton getInstance()
    {
        if ( instance == null )
        {
            instance = new ConfigurationSingleton();
        }
        return instance;
    }

    public String getProperty()
    {
        return "symentis";
    }

    public String getOrgCode(String name){
        String val = name;
        return "ACC:" + val;
    }
}
