import java.util.Random;

class Worker extends Thread {
    private int[] mas;
    private boolean fromStart; // true - условие 1, false - условие 2

    public Worker(int[] mas, boolean fromStart) {
        this.mas = mas;
        this.fromStart = fromStart;
    }

    @Override
    public void run() {
        int sum = 0;

        if (fromStart) {
            // Условие 1: по два подряд с начала
            for (int i = 0; i < mas.length - 1; i += 2) {
                int product = mas[i] * mas[i + 1];
                sum += product;
                System.out.println(getName() + " Пара (" + i + "," + (i + 1) + ") => " +
                        mas[i] + "*" + mas[i + 1] + " = " + product);
            }
        } else {
            // Условие 2: по два с четных позиций, начиная с конца
            for (int i = mas.length - 2; i >= 2; i -= 4) {
                int product = mas[i] * mas[i - 2];
                sum += product;
                System.out.println(getName() + " Пара (" + i + "," + (i - 2) + ") => " +
                        mas[i] + "*" + mas[i - 2] + " = " + product);
            }
        }

        System.out.println(getName() + " Итоговая сумма = " + sum + "\n");
    }
}

javac Main.java
java Main


 {
    public static void main(String[] args) {
        int[] mas = new int[100];
        Random rand = new Random();

        // Генерация массива
        for (int i = 0; i < mas.length; i++) {
            mas[i] = rand.nextInt(100) + 1; // от 1 до 100
            System.out.print(mas[i] + " ");
        }
        System.out.println("\n");

        // Поток Th1: условие 1
        Worker th1 = new Worker(mas, true);
        th1.setName("Th1");

        // Поток Th2: условие 2
        Worker th2 = new Worker(mas, false);
        th2.setName("Th2");

        // Запуск потоков
        th1.start();
        th2.start();

        try {
            // Ожидание завершения обоих потоков
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Главный поток: информация о студентах (с задержкой 100 мс)
        String info = "Лабораторную работу выполнил студент группы ХХХ Иванов И.И.";
        for (char c : info.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(100); // задержка 100 мс
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }
}
