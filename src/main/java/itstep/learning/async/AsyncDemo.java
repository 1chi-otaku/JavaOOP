package itstep.learning.async;

import java.util.*;
import java.util.concurrent.*;

public class AsyncDemo {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private StringBuilder digitString = new StringBuilder();
    private long startTime;

    public void run() {
        System.out.println( "AsyncDemo: make choice" );
        System.out.println( "1 - Thread demo" );
        System.out.println( "2 - Percent (thread) demo" );
        System.out.println( "3 - Task demo" );
        System.out.println( "4 - Percent (task) demo" );
        System.out.println( "5 - Digit String option 1" );
        System.out.println( "6 - Digit String option 2" );
        System.out.println( "0 - Quit" );

        Scanner kbScanner = new Scanner( System.in );
        int choice = kbScanner.nextInt();
        switch( choice ) {
            case 1: threadDemo(); break;
            case 2: percentDemo(); break;
            case 3: taskDemo(); break;
            case 4: taskPercentDemo(); break;
            case 5: digitStringDemo(); break;
            case 6: digitStringMultitask(); break;
        }
    }

    private void digitStringMultitask() {
        startTime = System.currentTimeMillis();

        digitString = new StringBuilder();
        Future[] tasks = new Future[10];
        List<String> digits = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        Collections.shuffle(digits);

        for( int i = 0; i <= 9; i++ ) {
            tasks[i] = threadPool.submit( new DigitTask(digits.get(i)) );
        }

        try {
            for (int i = 9; i >= 0; i--) {
                digitString.append(tasks[i].get());
                System.out.println(  "added : " + digits.get(i) + " : " + digitString);
            }
        }
        catch( Exception ex ) {
            System.err.println( ex.getMessage() );
        }
        stopExecutor();
    }

    private class DigitTask implements Callable<String> {
        private final String str;

        public DigitTask(String str) {
            this.str = str;
        }

        @Override
        public String call() throws Exception {
            System.out.println(
                    System.currentTimeMillis() - startTime +
                            " DigitTask " + str + " started" );
            return str;
        }
    }

    private void taskPercentDemo() {
        startTime = System.currentTimeMillis();
        try {
            sum = 100;
            for( int i = 1; i <= 12; i++ ) {
                sum *= threadPool.submit( new RateTask(i) ).get();
                System.out.println(
                        System.currentTimeMillis() - startTime +
                                " Rate " + i + " sum = " + sum);
            }
        }
        catch( Exception ex ) {
            System.err.println( ex.getMessage() );
        }
        stopExecutor();
    }

    private class RateTask implements Callable<Double> {
        private final int month;

        public RateTask(int month) {
            this.month = month;
        }

        @Override
        public Double call() throws Exception {
            System.out.println(
                    System.currentTimeMillis() - startTime +
                            " RateTask " + month + " started" );
            double percent;
            Thread.sleep( 500 );  // імітація запиту
            percent = 10.0;
            return  (1 + percent / 100.0);
        }
    }

    private class DigitString implements Runnable {
        private String digit;

        public DigitString(String Digit){
            this.digit = Digit;
        }
//
        @Override
        public void run() {
            try{
                Thread.sleep(200);
            }
            catch( Exception ex ) {
                System.err.println( ex.getMessage() );
                return;
            }
            synchronized (sumLock){
                digitString.append(digit);
                System.out.println("added: " + digit + " - " + digitString.toString() + " finished");
            }
        }

    }

    private void digitStringDemo() {
        startTime = System.currentTimeMillis();

        List<String> digits = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        Collections.shuffle(digits);

        List<Future<?>> futures = new ArrayList<>();
        for (String digit : digits) {
            futures.add(threadPool.submit(new DigitString(digit)));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println("Error waiting for task: " + ex.getMessage());
            }
        }

        stopExecutor();
    }

    private void taskDemo() {
        startTime = System.currentTimeMillis();
        // Багатозадачність. Особливості:
        // - задачі беруться на виконання спеціалізованим "виконавцем"
        //    який треба початково створити
        // - задачі стартують одразу після передачі до виконавця
        // - у кінці програми виконавця необхідно зупиняти,
        //    інакше програма не завершується
        Future<?> task1 = threadPool.submit( new Rate(2) );
        // - очікування виконання задачі - .get()
        try {                                                      // Цей блок є розтлумаченням
            task1.get();                                           // "цукрової" конструкції
            System.out.println(                                    // await
                    System.currentTimeMillis() - startTime +       //
                            " Task 1 got");                        //
        }                                                          //
        catch( InterruptedException | ExecutionException ex ) {    //
            System.err.println( ex.getMessage() );                 //
        }                                                          //
        // - задачі можуть приймати на виконання інші функціональні
        //    інтерфейси, зокрема, Callable
        Future<String> task2 = threadPool.submit(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {   // метод повертає значення
                        TimeUnit.MILLISECONDS.sleep( 500 );   // а також містить Exception
                        return "Hello Callable";              // у сигнатурі (у тілі немає потреби
                    }                                         // try-catch)
                });
        try {
            String res = task2.get();   // res = await task2
            System.out.println( System.currentTimeMillis() - startTime + " Task 2 finished with result: " + res );
        }
        catch( InterruptedException | ExecutionException ex ) {
            System.err.println( System.currentTimeMillis() - startTime + " Task 2 finished with exception: " + ex.getMessage() );
        }
        stopExecutor();
    }

    private void stopExecutor() {
        threadPool.shutdown();  // припиняємо прийом нових задач
        try {
            boolean isDone = threadPool.awaitTermination( 300, TimeUnit.MILLISECONDS );
            if ( !isDone ) {
                List<Runnable> cancelledTasks = threadPool.shutdownNow();   // "жорстка" зупинка
                if ( !cancelledTasks.isEmpty() ) {
                    System.err.println( System.currentTimeMillis() - startTime + " Tasks cancelled:" );
                    for ( Runnable task : cancelledTasks ) {
                        System.err.println( task.toString() );
                    }
                }
            }
        }
        catch( InterruptedException ignored ) { }
    }

    private void threadDemo() {
        /*
        Багатопоточність - програмування з використанням об'єктів
        системного типу - Thread.
        Об'єкти приймають у конструктор інші об'єкти функціональних
        інтерфейсів.
        (У Java функціональними інтерфейсами називають інтерфейси, у
         яких декларовано лише один метод)
         */
        Thread thread = new Thread(
                new Runnable() {           // Анонімний тип, що імплементує Runnable
                    @Override              // переозначає його метод
                    public void run() {    // та інстанціюється (стає об'єктом)
                        System.out.println( "Hello Thread" );
                    }                      // Традиційно для Java, створення
                }                          // нового об'єкту (thread) не створює
        );                             // сам потік, а лише програмну сутність
        thread.start();   // асинхронний запуск
        // thread.run();  // синхронний запуск
        System.out.println( "1 Hello Main" );
        System.out.println( "2 Hello Main" );
        System.out.println( "3 Hello Main" );
        System.out.println( "4 Hello Main" );
        System.out.println( "5 Hello Main" );
    }

    private double sum;
    private final Object sumLock = new Object();

    private void percentDemo() {
        sum = 100.0;
        for( int i = 1; i <= 12; i++ ) {
            new Thread( new Rate(i) ).start();
        }
    }

    private class Rate implements Runnable {
        private final int month;

        public Rate(int month) {
            this.month = month;
        }

        @Override
        public void run() {
            System.out.println( "Rate " + month + " started" );
            double percent;
            try {
                Thread.sleep( 500 );  // імітація запиту
                percent = 10.0;
            }
            catch( InterruptedException ex ) {
                System.err.println( ex.getMessage() );
                return;
            }
            synchronized( sumLock ) {
                sum = sum * (1 + percent / 100.0);
                System.out.println( "Rate " + month + " finished with sum " + sum );
            }
        }
    }
}
/*
Асинхронне програмування.

Синхронність - послідовне у часі виконання частин коду.
  ----- =====

Асинхронність - будь-яке відхилення від синхронності.
  -----     - - - - -       -- -- -
  =====      = = = = =        =  = ===

Реалізації:
 - багатозадачність: використання об'єктів рівня мови програмування / платформи
    (як-то Promise, Task, Future, Coroutine тощо)
 - багатопоточність: використання системних ресурсів - потоків (якщо вони
    існують у системі)
 - багатопроцесність: використання системних ресурсів - процесів
 - мережні технології
    = grid
    = network

Задачі, які вигідно вирішувати в асинхронному режимі, це "переставні"
задачі, в яких порядок врахування їх частин не грає ролі. Наприклад, задачі
додавання чи множення чисел.
Приклад:
Нац.банк публікує відсоткові значення інфляції кожен місяць. Необхідно
визначити річну інфляцію.
? чи можна враховувати відсотки у довільному порядку?
(100 + 10%) + 20%  =?= (100 + 20%) + 10%
(100 x 1.1) x 1.2  =?= (100 x 1.2) x 1.1
100 x 1.1 x 1.2 =!= 100 x 1.2 x 1.1
Так, можна. Зауваження - при врахуванні відсотків 5-го місяця ми не
гарантуємо, що це інфляція НА 5-й місяць, гарантується лише загальний
результат після врахування всіх складових.

Д.З. Згенерувати число (рядок), що містить всі цифри від 0 до 9
один раз кожну в довільному порядку. Використати асинхронний підхід,
за якого кожну цифру додає окремий потік. Вивести всі проміжні
стани при формуванні числа
added 7: 7
added 3: 73
added 1: 731
added 8: 7318
...
added 5: 7318249605


 */