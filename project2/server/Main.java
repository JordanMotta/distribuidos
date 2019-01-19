import java.util.List;

class Main
{   
    /**
     * Valid args:
     *  -p -> it's gonna use ports. After of '-p', specify the range of ports to use.
     *  -i -> it's gonna use ips. After of '-i', specify the range of ips to use.
     *  range for ports: xxx-xxx
     *  range for ips: xxx.xxx.xxx.xxx-xxx
     *  You cannot use both at the same time
     */

    public final static String P_PARAM = "-p";
    public final static String I_PARAM = "-i";

    /**
     * Main verifica que se envian los parametros correctos y crea una instancia del servidor.
     */
    public static void main(String[] args) 
    {

        if (args.length == 0)
        {
            printHelp ();
        }

        if (isParamRepeated(P_PARAM, args))
        {
            System.out.println ("You are repeating -p param");
            return;
        }

        if (isParamRepeated(I_PARAM, args))
        {
            System.out.println ("You are repeating -i param");
            return;
        }

        if (existsPAndI(args))
        {
            System.out.println ("It cannot be both parameters");
            return;
        }

        
        String[] range = null;
        String PARAM_TYPE = null;
        for (int index = 0; index < args.length; ++index)
        {
            if (args[index].equals(P_PARAM))
            {
                
                range = getPorts (args[index + 1]);
                if (range == null)
                {
                    System.out.println ("Invalid value for -p");
                    return;
                }
                // System.out.println ("Ports: ");
                // for (String p : ports)
                // {
                //     System.out.println (p);
                // }
                ++index;
                PARAM_TYPE = Main.P_PARAM;
            }
            else if (args[index].equals(I_PARAM))
            {
                range = getIPs (args[index + 1]);
                if (range == null)
                {
                    System.out.println ("Invalid value for -i");
                    return;
                }
                // System.out.println ("Ips: ");
                // for (String ip : range)
                // {
                //     System.out.println (ip);
                // }
                ++index;
                PARAM_TYPE = Main.I_PARAM;
            }
            else
            {
                System.out.println ("Unknow command: " + args[index]);
                return;
            }
        }

        final Server s = new Server (PARAM_TYPE, range);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable () {

            @Override
            public void run ()
            {
                s.close ();
            }
        }));
    }

    //DONE
    private static String[] getPorts (final String portsRange)
    {
        String[] baseEnd = portsRange.split("-");
        String[] ports = null;
        if (baseEnd.length == 1) return null;
        // System.out.println ("--------------------------");
        try
        {
            int base = Integer.parseInt(baseEnd[0]);
            int end = Integer.parseInt(baseEnd[1]);
            if (base < 0) return null;
            if (end < base) return null;
            ports = new String[end - base + 1];
            for (int port = base, index = 0; port <= end; ++port, ++index)
            {
                ports[index] = String.valueOf(port);
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return ports;
    }

    //DONE
    private static String[] getIPs (final String ipsRange)
    {
        // System.out.println ("ips range: " + ipsRange);
        // System.out.println ("ips range split length: " + ipsRange.split("\\.").length);
        String[] ips = null;
        String[] baseEnd = ipsRange.split("\\.")[3].split("-");
        if (baseEnd.length == 1) return null;

        String[] ipSplited = ipsRange.split ("\\.");
        String threeOctates = ipSplited[0] + "." + ipSplited[1] + "." + ipSplited[2] + ".";
        try
        {
            int base = Integer.parseInt(baseEnd[0]);
            int end = Integer.parseInt(baseEnd[1]);
            if (base < 0) return null;
            if (end < base) return null;
            ips = new String[end - base + 1];
            for (int ip = base, index = 0; ip <= end; ++ip, ++index)
            {
                ips[index] = threeOctates + String.valueOf(ip);
            }
        }
        catch (Exception e)
        {
            return null;
        }
        
        for (String str : baseEnd)
        {
            System.out.println (str);
        }
        return ips;
    }

    //DONE
    public static boolean isParamRepeated (final String param, final String[] params)
    {
        int n = 0;
        for (String p : params)
        {
            if (param.equals(p))
            {
                n++;
            }
        }
        return n > 1;
    }

    //DONE
    private static boolean existsPAndI (final String[] params)
    {
        int n = 0;
        for (String param : params)
        {
            if (P_PARAM.equals(param)) ++n;
            if (I_PARAM.equals(param)) ++n;
        }

        return n == 2;
    }

    //DONE
    private static void printParams (final String[] params)
    {
        for (String param : params)
        {
            System.out.println (param);
        }
    }

    public static String bytesToString (final List<Byte> bytes)
    {
        StringBuilder str = new StringBuilder();
        for (Byte b : bytes)
        {
            str.append (b);
        }

        return str.toString();
    }

    //DONE
    /**
     * Imprime los comandos disponibles en el servidor.
     */
    private static void printHelp ()
    {
        System.out.println ("DGit Server commands");
        System.out.println ("-p <port range> - Use the same machine and use a range of ports to comunicate among them. port's range example: -p 3000-3010");
        System.out.println ("-i <ip range> - The servers are located in a LAN. Use a range of ips in order to comunicate with them. Ip range example: -i 192.168.1.1-254");
        System.out.println ("");
        System.out.println ("DGit server");
    }
}