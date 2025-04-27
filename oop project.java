class ElectricAppliance {
    private String brand;
    private int voltage;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getVoltage() {
        return voltage;
    }

    public void on() {
        System.out.println("Appliance is on");
    }

    public void off() {
        System.out.println("Appliance is off");
    }
}

class Motor {
    public void start() {
        System.out.println("Motor is running");
    }
}

class ElectricFan extends ElectricAppliance {
    private int speed;
    private final int MAX_SPEED = 5;
    private final Motor motor = new Motor();
    private boolean isSwinging = false;
    private int timerInSeconds = 0;
    private boolean isOn = false;

    public void setSpeed(int speed) {
        if (speed >= 1 && speed <= MAX_SPEED) {
            this.speed = speed;
            System.out.println("Fan speed set to: " + speed);
        } else {
            System.out.println("Speed must be between 1 and 5.");
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void enableSwing(boolean swing) {
        this.isSwinging = swing;
        System.out.println("Swing mode: " + (swing ? "ON" : "OFF"));
    }

    public boolean isSwinging() {
        return isSwinging;
    }

    public void setTimer(int seconds) {
        this.timerInSeconds = seconds;
        System.out.println("Fan timer set: " + seconds + " seconds");
    }

    public int getTimer() {
        return timerInSeconds;
    }

    public boolean isOn() {
        return isOn;
    }

    public void startFan() {
        if (!isOn) {
            on();
            isOn = true;
        }
        motor.start();
        System.out.println("Fan is running");
    }

    public void stopFan() {
        isOn = false;
        off();
    }

    public void decreaseTimer() {
        if (timerInSeconds > 0) {
            timerInSeconds--;
        }
    }

    public void displayStatus() {
        System.out.println("======== Fan Status ========");
        System.out.println("Brand: " + getBrand());
        System.out.println("Voltage: " + getVoltage() + "V");
        System.out.println("Fan is " + (isOn ? "ON" : "OFF"));
        System.out.println("Speed: " + speed);
        System.out.println("Swing Mode: " + (isSwinging ? "ON" : "OFF"));
        System.out.println("Timer: " + (timerInSeconds > 0 ? timerInSeconds + "s left" : "Off"));
        System.out.println("============================");
    }
}

class Remote {
    public void speedUp(ElectricFan fan) {
        fan.setSpeed(fan.getSpeed() + 1);
    }

    public void turnOffFan(ElectricFan fan) {
        fan.stopFan();
    }

    public void turnOnFan(ElectricFan fan) {
        fan.startFan();
    }

    public void toggleSwing(ElectricFan fan) {
        fan.enableSwing(!fan.isSwinging());
    }

    public void setTimer(ElectricFan fan, int seconds) {
        fan.setTimer(seconds);
    }

    public void showStatus(ElectricFan fan) {
        fan.displayStatus();
    }
}

class FanThread extends Thread {
    private final ElectricFan fan;

    public FanThread(ElectricFan fan) {
        this.fan = fan;
    }

    public void run() {
        while (!isInterrupted()) {
            if (fan.isOn()) {
                fan.displayStatus();
                if (fan.getTimer() > 0) {
                    fan.decreaseTimer();
                    if (fan.getTimer() == 0) {
                        System.out.println("Timer ended: Fan is turning off");
                        fan.stopFan();
                    }
                }
            }
            try {
                Thread.sleep(1000); // 1 second update interval
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Fan turned off (from thread)");
    }
}

public class Main {
    public static void main(String[] args) {
        ElectricFan myFan = new ElectricFan();
        myFan.setBrand("PakFan");
        myFan.setVoltage(220);
        myFan.setSpeed(2);

        Remote remote = new Remote();
        remote.turnOnFan(myFan);
        remote.setTimer(myFan, 5); // Auto stop in 5 seconds
        remote.toggleSwing(myFan);
        remote.speedUp(myFan);

        FanThread fanThread = new FanThread(myFan);
        fanThread.start();

        try {
            Thread.sleep(8000); // Let fan run for 8 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fanThread.interrupt(); // Stop the thread
        remote.turnOffFan(myFan);
    }
}
