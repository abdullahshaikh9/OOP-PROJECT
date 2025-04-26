// Ye project Electric Fan ka hai jisme OOP concepts use kiye gaye hain

class ElectricAppliance {
    String brand;
    int voltage;

    void on() {
        System.out.println("Appliance chalu hogaya");
    }

    void off() {
        System.out.println("Appliance band hogaya");
    }
}

// Composition: Fan ke andar Motor hai
class Motor {
    void start() {
        System.out.println("Motor ghoom rahi hai");
    }
}

// Inheritance: Fan ElectricAppliance ka child hai
class ElectricFan extends ElectricAppliance {
    int speed;
    Motor motor = new Motor(); // Composition

    void setSpeed(int s) {
        speed = s;
        System.out.println("Fan ki speed set hui: " + speed);
    }

    void startFan() {
        on();
        motor.start();
        System.out.println("Fan chal gaya");
    }
}

// Aggregation: Remote fan ko control karta hai (ek alag class hai)
class Remote {
    void speedUp(ElectricFan fan) {
        fan.setSpeed(fan.speed + 1);
    }

    void turnOffFan(ElectricFan fan) {
        fan.off();
    }
}

// Threading: Fan jab chalta hai to background mein loop chalega
class FanThread extends Thread {
    ElectricFan fan;

    FanThread(ElectricFan f) {
        fan = f;
    }

    public void run() {
        while (!isInterrupted()) {
            System.out.println("Fan speed: " + fan.speed + " (chal raha hai)");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Fan band hogaya (Thread se)");
    }
}

// Main class (Program yahan se start hota hai)
public class Main {
    public static void main(String[] args) {
        ElectricFan myFan = new ElectricFan();
        myFan.brand = "PakFan";
        myFan.voltage = 220;
        myFan.setSpeed(1);

        Remote remote = new Remote();
        remote.speedUp(myFan);

        FanThread fanThread = new FanThread(myFan);
        fanThread.start();

        try {
            Thread.sleep(7000); // 7 second fan chalega
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fanThread.interrupt(); // Fan band karna
        remote.turnOffFan(myFan);
    }
}
