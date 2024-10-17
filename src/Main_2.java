import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

interface IObserver {
    void update(String stockSymbol, double newPrice);
}

interface ISubject {
    void attach(IObserver observer);
    void detach(IObserver observer);
    void notifyObservers(String stockSymbol, double newPrice);
}

class StockExchange implements ISubject {
    private final Map<String, Double> stockPrices;
    private final List<IObserver> observers;

    public StockExchange() {
        stockPrices = new HashMap<>();
        observers = new ArrayList<>();
    }

    @Override
    public void attach(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String stockSymbol, double newPrice) {
        for (IObserver observer : observers) {
            observer.update(stockSymbol, newPrice);
        }
    }

    public void updateStockPrice(String stockSymbol, double newPrice) {
        stockPrices.put(stockSymbol, newPrice);
        notifyObservers(stockSymbol, newPrice);
    }

    public double getStockPrice(String stockSymbol) {
        return stockPrices.getOrDefault(stockSymbol, 0.0);
    }
}

class Trader implements IObserver {
    @Override
    public void update(String stockSymbol, double newPrice) {
        System.out.println("Трейдер: Цена акции " + stockSymbol + " изменена на " + newPrice + ".");
    }
}

class TradingRobot implements IObserver {
    private final double buyThreshold;
    private final double sellThreshold;

    public TradingRobot(double buyThreshold, double sellThreshold) {
        this.buyThreshold = buyThreshold;
        this.sellThreshold = sellThreshold;
    }

    @Override
    public void update(String stockSymbol, double newPrice) {
        if (newPrice < buyThreshold) {
            System.out.println("Торговый робот: Покупаем акцию " + stockSymbol + " по цене " + newPrice + ".");
        } else if (newPrice > sellThreshold) {
            System.out.println("Торговый робот: Продаем акцию " + stockSymbol + " по цене " + newPrice + ".");
        } else {
            System.out.println("Торговый робот: Ожидаем для акции " + stockSymbol + ". Текущая цена " + newPrice + ".");
        }
    }
}


public class Main_2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StockExchange stockExchange = new StockExchange();

        while (true) {
            System.out.println("Выберите наблюдателя:");
            System.out.println("1 - Трейдер");
            System.out.println("2 - Торговый робот");
            System.out.println("0 - Выход");
            int choice = scanner.nextInt();

            if (choice == 0) {
                break;
            }

            if (choice == 1) {
                Trader trader = new Trader();
                stockExchange.attach(trader);
                System.out.println("Трейдер добавлен.");
            } else if (choice == 2) {
                System.out.print("Введите порог покупки: ");
                double buyThreshold = scanner.nextDouble();
                System.out.print("Введите порог продажи: ");
                double sellThreshold = scanner.nextDouble();
                TradingRobot robot = new TradingRobot(buyThreshold, sellThreshold);
                stockExchange.attach(robot);
                System.out.println("Торговый робот добавлен.");
            } else {
                System.out.println("Неверный выбор, попробуйте снова.");
            }

            System.out.print("Введите символ акции: ");
            String stockSymbol = scanner.next();
            System.out.print("Введите новую цену акции: ");
            double newPrice = scanner.nextDouble();
            stockExchange.updateStockPrice(stockSymbol, newPrice);
        }
    }
}
