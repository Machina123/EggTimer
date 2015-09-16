package net.machina.eggtimer.common;

import android.util.Log;

public class Egg {

    public enum Size {

        S(0, 50), M(1, 58), L(2, 68), XL(3, 78);

        private int id;
        private int weight;

        Size(int id, int weight) {
            this.id = id;
            this.weight = weight;
        }

        public static Size fromId(int _id) {
            switch(_id) {
                case 0: return Size.S;
                case 1: return Size.M;
                case 2: return Size.L;
                case 3: return Size.XL;
                default: throw new IllegalArgumentException("Niewłaściwy identyfikator pola Size!");
            }
        }

        public int getWeight() {
            return weight;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Size{" +
                    "weight=" + weight +
                    ", id=" + id +
                    '}';
        }
    }

    public enum Doneness {
        Liquid(0,62),
        PartlySet(1,64),
        Set(2,66),
        Waxy(3,70),
        Hard(4,80),
        Crumbly(5,85);

        private int id;
        private int temp;

        Doneness(int id, int temp) {
            this.id = id;
            this.temp = temp;
        }

        public int getId() {
            return id;
        }

        public int getTemp() {
            return temp;
        }

        public static Doneness fromId(int _id) {
            switch(_id) {
                case 0: return Doneness.Liquid;
                case 1: return Doneness.PartlySet;
                case 2: return Doneness.Set;
                case 3: return Doneness.Waxy;
                case 4: return Doneness.Hard;
                case 5: return Doneness.Crumbly;
                default: throw new IllegalArgumentException("Nieprawidłowy identyfikator pola Doneness!");
            }
        }

        @Override
        public String toString() {
            return "Doneness{" +
                    "id=" + id +
                    ", temp=" + temp +
                    '}';
        }
    }

    public enum BeginTemp {
        Fridge(0,4),
        Room(1,20);

        private int id;
        private int temp;

        BeginTemp(int id, int temp) {
            this.id = id;
            this.temp = temp;
        }

        public static BeginTemp fromId(int _id) {
            switch(_id) {
                case 0: return BeginTemp.Fridge;
                case 1: return BeginTemp.Room;
                default: throw new IllegalArgumentException("Nieprawidłowy identyfikator pola BeginTemp");
            }
        }

        public int getId() {
            return id;
        }

        public int getTemp() {
            return temp;
        }

        @Override
        public String toString() {
            return "BeginTemp = " + getTemp();
        }
    }

    public static double calculateTime(Size size, Doneness doneness, BeginTemp temp) {
        double returned = Math.round(((Constants.MASS_MULTIPLIER * Math.pow((double)size.getWeight(), Constants.TWO_THIRDS)) * Math.log(Constants.TEMP_MULTIPLIER * ((temp.getTemp() - Constants.WATER_BOIL_POINT) / (doneness.getTemp() - Constants.WATER_BOIL_POINT))))* 60 );
        Log.i(Constants.LOGGER_TAG, "Wyliczony czas: " + returned);
        return returned;
    }

}
