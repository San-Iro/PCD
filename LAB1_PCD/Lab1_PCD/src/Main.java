import java.util.*;

// ---------- Поток Th1 ----------
class Th1 extends Thread {
    private int[] mas;
    private List<String> results;

    public Th1(int[] mas, List<String> results) {
        this.mas = mas;
        this.results = results;
    }

    @Override
    public void run() {
        for (int i = 0; i < mas.length - 1; i += 2) {
            int prod = mas[i] * mas[i + 1];
            String line = String.format("(%02d,%02d) %3d*%3d=%4d", i, i + 1, mas[i], mas[i + 1], prod);
            synchronized (results) {
                results.add(line);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// ---------- Поток Th2 ----------
class Th2 extends Thread {
    private int[] mas;
    private List<String> results;

    public Th2(int[] mas, List<String> results) {
        this.mas = mas;
        this.results = results;
    }

    @Override
    public void run() {
        for (int i = mas.length - 2; i >= 0; i -= 2) {
            if (i % 2 == 0) {
                int prod = mas[i] * mas[i + 1];
                String line = String.format("(%02d,%02d) %3d*%3d=%4d", i, i + 1, mas[i], mas[i + 1], prod);
                synchronized (results) {
                    results.add(line);
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// ---------- Главный класс ----------
public class Main {
    public static void main(String[] args) {
        int[] mas = new int[100];
        Random rnd = new Random();

        // Генерация массива
        System.out.println("Массив случайных чисел:");
        for (int i = 0; i < mas.length; i++) {
            mas[i] = rnd.nextInt(100) + 1;
            System.out.print(mas[i] + " ");
        }
        System.out.println("\n-----------------------------------------");

        // Результаты потоков
        List<String> th1Results = Collections.synchronizedList(new ArrayList<>());
        List<String> th2Results = Collections.synchronizedList(new ArrayList<>());

        // Создание и запуск потоков
        Th1 t1 = new Th1(mas, th1Results);
        Th2 t2 = new Th2(mas, th2Results);
        t1.setName("Th1");
        t2.setName("Th2");
        t1.start();
        t2.start();

        // Ожидание завершения
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // --- Вывод в две колонки ---
        System.out.println("\nРезультаты потоков (в две колонки):");
        System.out.println("----------------------------------------------");
        System.out.printf("%-35s | %-35s%n", "Th1 (с начала)", "Th2 (с конца)");
        System.out.println("----------------------------------------------");

        int maxRows = Math.max(th1Results.size(), th2Results.size());
        for (int i = 0; i < maxRows; i++) {
            String left = (i < th1Results.size()) ? th1Results.get(i) : "";
            String right = (i < th2Results.size()) ? th2Results.get(i) : "";
            System.out.printf("%-35s | %-35s%n", left, right);
        }

        System.out.println("----------------------------------------------");

        // Вывод информации о студенте
        String studentInfo = "ФИО: Доду Георгий\nГруппа: CR-233\nВариант: 5\nТема: Потоки выполнения";
        for (char c : studentInfo.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n----------------------------------------------");
    }
}
