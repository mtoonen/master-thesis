package nl.meine.master.method;

public class Even {
    public static int countEven(int [] values)
    {
        int count = 0;
        for (int i = 0; i < values.length; i++)
        {
            if (values[i] % 2 == 0)
            {
                count++;
            }
            else
            {
                return count;
            }
        }
        return count;
    }
}
