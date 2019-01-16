import java.util.ArrayList;
import java.util.List;

class CIP extends Connection
{

    public CIP (final String[] range)
    {
        super(range);
    }

    @Override
    public void connect ()
    {

    }

    @Override
    public void send (final byte[] message)
    {

    }

    @Override
    public List<Byte> receive ()
    {
        ArrayList<Byte> msg = new ArrayList<>();
        return msg;
    }
}