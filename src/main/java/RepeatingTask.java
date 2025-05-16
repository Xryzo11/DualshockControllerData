public class RepeatingTask {
    static boolean repeat = false;

    void run() {
        while (repeat) {
            try {
                Thread.sleep(1000);
                Main.updateControllerList();
                Main.updateInfoDisplay();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
