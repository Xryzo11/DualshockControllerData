import org.hid4java.*;

import java.util.ArrayList;

public class Main {
    static AppFront app = new AppFront();
    static ArrayList<HIDController> controllers = new ArrayList<>();

    public static void main(String[] args) {
        app.infoDisplay.setText(""); // Remove placeholder text
        app.setVisible(true);
        RepeatingTask task = new RepeatingTask();
        Thread thread = new Thread(task::run);
        thread.start();
        RepeatingTask.repeat = true;
    }

    public static void updateControllerList() {
        HidServicesSpecification spec = new HidServicesSpecification();
        HidServices services = HidManager.getHidServices(spec);
//        Main.controllers.clear();
        controllers.removeIf(controller -> !controller.isConnected());
        for (HidDevice dev : services.getAttachedHidDevices()) {
//            System.out.println("Device: " + dev.getProduct() + " (" + dev.getVendorId() + ":" + dev.getProductId() + ")");
            // 0x054C - Sony, 0x45E - Microsoft, 0x057E - Nintendo
            if (dev.getVendorId() == 0x054C) { // Sony VID HEX
                System.out.println("Sony VID hex found");
                if (!controllers.contains(dev)) {
                    boolean alreadyAdded = controllers.stream()
                            .anyMatch(c -> c.getVendorId() == dev.getVendorId() && c.getProductId() == dev.getProductId());
                    if (!alreadyAdded) {
                        controllers.add(new ControllerData(dev));
                    }
                }
            }
        }
    }

    public static void updateInfoDisplay() {
        StringBuilder text = new StringBuilder("");
        int batteryLevel = -1;
        for (HIDController controller : controllers) {
            if (controller == null) continue;
            if (!controller.isConnected()) continue;
            controller.update();
            System.out.println("------ " + controller.getProductName() + " (" + controller.getVendorId() + ":" + controller.getProductId() + ") ------");
            System.out.println("Connected: " + controller.isConnected());
            System.out.println("Battery Level: " + controller.getAverageBatteryLevel() + "%");
            System.out.println("Last Battery Level: " + controller.getLastBatteryLevel() + "%");
            System.out.println("Battery Lower: " + controller.isBatteryLower());
            if (controller.isConnected()) batteryLevel = controller.getAverageBatteryLevel();
            if (controller.isBatteryLower()) {
                System.out.println("Battery level is lower than last check");
                if (batteryLevel <= 15) {
                    System.out.println("Battery level is lower than 10%");
                    app.sendNotification("Battery level is low", controller.getProductName() + " battery level is " + controller.getAverageBatteryLevel() + "%");
                }
            }
            text.append(controller).append("\n");
//            controller.close();
        }
        if (controllers.isEmpty()) text = new StringBuilder("No controllers found");
        app.setIcon(IconGenerator.genIcon(batteryLevel));
        app.infoDisplay.setText(text.toString());
    }
}
