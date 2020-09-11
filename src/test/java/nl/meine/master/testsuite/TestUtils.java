package nl.meine.master.testsuite;

public class TestUtils {

    public final static String FUNCTION_FOREACHBUTINDEX = "public static int countEven(int [] values)\n" +
            "    {\n" +
            "        int count = 0;\n" +
            "        for (int i = 0; i < values.length; i++)\n" +
            "        {\n" +
            "            if (values[i] % 2 == 0)\n" +
            "            {\n" +
            "                count++;\n" +
            "            }\n" +
            "            else\n" +
            "            {\n" +
            "                return count;\n" +
            "            }\n" +
            "        }\n" +
            "        return count;\n" +
            "    }";
    public final static String FUNCTION_UNCOMPILABLE = "public static int countEven(int [] values)\n" +
            "    {\n" +
            "        int count = 0;\n" +
            "        for (int i = 0; i < vaslues.length; i++)\n" +
            "        {\n" +
            "            if (values[i] % 2 == 0)\n" +
            "            {\n" +
            "                count++;\n" +
            "            }\n" +
            "            else\n" +
            "            {\n" +
            "                return count;\n" +
            "            }\n" +
            "        }\n" +
            "        return count;\n" +
            "    }";
}
