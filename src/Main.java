import java.util.Scanner;

interface ICostCalculationStrategy {
    double calculateCost(BookingContext context);
}

enum ServiceClass {
    ECONOMY, BUSINESS
}

class AirplaneCostCalculationStrategy implements ICostCalculationStrategy {
    @Override
    public double calculateCost(BookingContext context) {
        double baseCost = 100;
        double distanceCost = context.getDistance() * 0.5;
        double classCost = context.getServiceClass() == ServiceClass.BUSINESS ? 50 : 0;
        return baseCost + distanceCost + classCost;
    }
}

class TrainCostCalculationStrategy implements ICostCalculationStrategy {
    @Override
    public double calculateCost(BookingContext context) {
        double baseCost = 50;
        double distanceCost = context.getDistance() * 0.3;
        return baseCost + distanceCost;
    }
}

class BusCostCalculationStrategy implements ICostCalculationStrategy {
    @Override
    public double calculateCost(BookingContext context) {
        double baseCost = 30;
        double distanceCost = context.getDistance() * 0.2;
        return baseCost + distanceCost;
    }
}

class Discount {
    private double amount;
    private boolean isForChildren;
    private boolean isForSeniors;

    public Discount(double amount, boolean isForChildren, boolean isForSeniors) {
        this.amount = amount;
        this.isForChildren = isForChildren;
        this.isForSeniors = isForSeniors;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isForChildren() {
        return isForChildren;
    }

    public boolean isForSeniors() {
        return isForSeniors;
    }
}

class BookingContext {
    private ICostCalculationStrategy costCalculationStrategy;
    private double distance;
    private ServiceClass serviceClass;
    private Discount discount;

    public BookingContext(double distance, ServiceClass serviceClass) {
        this.distance = distance;
        this.serviceClass = serviceClass;
    }

    public void setCostCalculationStrategy(ICostCalculationStrategy strategy) {
        this.costCalculationStrategy = strategy;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public double getDistance() {
        return distance;
    }

    public ServiceClass getServiceClass() {
        return serviceClass;
    }

    public double calculateCost() {
        if (costCalculationStrategy == null) {
            throw new IllegalStateException("Strategy not set.");
        }

        double cost = costCalculationStrategy.calculateCost(this);

        if (discount != null) {
            cost -= discount.getAmount();
        }

        return cost;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите расстояние (в км): ");
        double distance = scanner.nextDouble();
        System.out.print("Выберите класс обслуживания (1 - Economy, 2 - Business): ");
        int classChoice = scanner.nextInt();
        ServiceClass serviceClass = classChoice == 2 ? ServiceClass.BUSINESS : ServiceClass.ECONOMY;
        System.out.println("Выберите транспортное средство: ");
        System.out.println("1 - Самолет");
        System.out.println("2 - Поезд");
        System.out.println("3 - Автобус");
        int transportChoice = scanner.nextInt();
        BookingContext booking = new BookingContext(distance, serviceClass);
        switch (transportChoice) {
            case 1 -> booking.setCostCalculationStrategy(new AirplaneCostCalculationStrategy());
            case 2 -> booking.setCostCalculationStrategy(new TrainCostCalculationStrategy());
            case 3 -> booking.setCostCalculationStrategy(new BusCostCalculationStrategy());
            default -> {
                System.out.println("Неверный выбор транспорта!");
                return;
            }
        }
        System.out.print("Введите сумму скидки: ");
        double discountAmount = scanner.nextDouble();
        booking.setDiscount(new Discount(discountAmount, false, false));
        double totalCost = booking.calculateCost();
        System.out.println("Общая стоимость: " + totalCost);
    }
}
