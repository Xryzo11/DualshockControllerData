public class SinkingIntArray {
    int size;
    int[] array;
    int index = 0;

    public SinkingIntArray(int size) {
        this.size = size;
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = -1;
        }
    }

    public void add(int value) {
        array[index] = value;
        index++;
        if (index >= size) {
            index = 0;
        }
    }
    public int remove(int index) {
        int value = array[index];
        array[index] = -1;
        return value;
    }
    public int get(int index) {
        return array[index];
    }
    public int getSize() {
        return size;
    }
    public int average() {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (array[i] == -1) continue;
            sum += array[i];
            count++;
        }
        double average = sum / count;
        return (int) average;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
