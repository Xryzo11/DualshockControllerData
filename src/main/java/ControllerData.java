import org.hid4java.*;

public class ControllerData implements HIDController {
    private HidDevice device;
    private int lastBatteryLevel = -1;
    private int batteryLevel = -1;
    private SinkingIntArray batteryHistory;

    public ControllerData(HidDevice device) {
        this.device = device;
        this.device.open();
        batteryHistory = new SinkingIntArray(5);
    }

    @Override
    public String getManufacturer() {
        return device.getManufacturer();
    }

    @Override
    public String getProductName() {
        return device.getProduct();
    }

    @Override
    public int getVendorId() {
        return device.getVendorId();
    }

    @Override
    public int getProductId() {
        return device.getProductId();
    }

    @Override
    public boolean isConnected() {
        batteryLevel = -1;
        byte[] data = new byte[64];
        int bytesRead = device.read(data, 1000);
        if (bytesRead > 12) {
            int raw = data[12] & 0xFF;
            if (raw <= 15) {
                batteryLevel = (int) ((raw / 15.0) * 100);
            } else {
                batteryLevel = (int) ((raw / 255.0) * 100);
            }
        }
        if (device == null) return false;
        if (getBatteryLevel() == -1) return false;
        if (device.isClosed()) return false;
        return true;
    }

    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }

    public int getLastBatteryLevel() {
        return lastBatteryLevel;
    }

    public boolean isBatteryLower() {
        if (lastBatteryLevel == -1) return false;
        if (batteryLevel == -1) return false;
        return batteryLevel < lastBatteryLevel;
    }

    public int getAverageBatteryLevel() {
        return batteryHistory.average();
    }

    @Override
    public void update() {
        if (!isConnected()) return;
        lastBatteryLevel = batteryLevel;
        batteryLevel = -1;
        device.open();
        device.setNonBlocking(false);
        byte[] data = new byte[64];
        int bytesRead = device.read(data, 1000);
        if (bytesRead > 12) {
            int raw = data[12] & 0xFF;
            if (raw == 0x0A || raw == 0x0B) {
                batteryLevel = 100;
            } else if (raw <= 0x0A){
                batteryLevel = (int) ((raw / 10.0) * 100);
            } else {
                batteryLevel = (int) ((raw / 255.0) * 100);
            }
            batteryHistory.add(batteryLevel);
        }
        System.out.println("Raw report:");
        for (int i = 0; i < bytesRead; i++) {
            System.out.printf("[%02d] = 0x%02X\n", i, data[i]);
        }
    }

    @Override
    public void close() {
        device.close();
    }

    @Override
    public String toString() {
        String conStatus = "Disconnected";
        if (isConnected()) {
            conStatus = "Connected";
        }
        return getProductName() + " | " + conStatus + " | " + getAverageBatteryLevel() + "%";
    }
}
