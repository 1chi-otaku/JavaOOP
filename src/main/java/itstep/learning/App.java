package itstep.learning;

import itstep.learning.async.AsyncDemo;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        AutoShop shop = new AutoShop();

        AsyncDemo asyncDemo = new AsyncDemo();

        asyncDemo.run();
    }
}
//test6