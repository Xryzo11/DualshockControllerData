public interface HIDController {
    String getManufacturer();
    String getProductName();
    int getVendorId();
    int getProductId();
    boolean isConnected();
    int getBatteryLevel();
    int getLastBatteryLevel();
    int getAverageBatteryLevel();
    boolean isBatteryLower();

    void update();
    void close();

    String toString();

}
