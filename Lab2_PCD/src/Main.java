 class Variant5 {
    public static void main(String[] args) {
        // Главная группа (Main создаётся автоматически)
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();

        // Потоки в главной группе
        Thread th1 = new Thread("Th1");
        th1.setPriority(3);

        Thread th2 = new Thread("Th2");
        th2.setPriority(7);

        // --- Подгруппа GE ---
        ThreadGroup ge = new ThreadGroup(mainGroup, "GE");

        // Подгруппа GH внутри GE
        ThreadGroup gh = new ThreadGroup(ge, "GH");

        Thread tha = new Thread(gh, "Tha");
        tha.setPriority(4);
        Thread thb = new Thread(gh, "Thb");
        thb.setPriority(3);
        Thread thc = new Thread(gh, "Thc");
        thc.setPriority(2);
        Thread thd = new Thread(gh, "Thd");
        thd.setPriority(1);

        // Поток ThA в GE
        Thread thA = new Thread(ge, "ThA");
        thA.setPriority(3);

        // --- Подгруппа GK ---
        ThreadGroup gk = new ThreadGroup(mainGroup, "GK");
        Thread gkTh1 = new Thread(gk, "Th1-GK");
        gkTh1.setPriority(3);
        Thread gkTh2 = new Thread(gk, "Th2-GK");
        gkTh2.setPriority(6);
        Thread gkTh3 = new Thread(gk, "Th3-GK");
        gkTh3.setPriority(3);

        // --- Вывод информации о потоках ---
        System.out.println("=== Список потоков и их групп ===");
        listThreads(mainGroup);
        listThreads(ge);
        listThreads(gh);
        listThreads(gk);
    }

    private static void listThreads(ThreadGroup group) {
        int num = group.activeCount();
        Thread[] threads = new Thread[num];
        group.enumerate(threads, false);

        System.out.println("\nГруппа: " + group.getName());
        for (Thread t : threads) {
            if (t != null)
                System.out.println("Поток: " + t.getName() +
                        " | Группа: " + t.getThreadGroup().getName() +
                        " | Приоритет: " + t.getPriority());
        }
    }
}
