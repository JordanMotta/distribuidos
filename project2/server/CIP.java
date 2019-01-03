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
    public byte[] receive ()
    {
        byte[] message = null;
        return message;
    }
}