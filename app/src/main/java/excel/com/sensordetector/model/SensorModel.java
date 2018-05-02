package excel.com.sensordetector.model;

public class SensorModel {
    //private long date;
    //private long time;
    //private long humidity;
    private String motion;
    private String distance;
    private String temperature;



    public SensorModel(){

    }
    public SensorModel(String distance,String motion, String temperature){
        this.distance=distance;
        //this.date =date;
        this.motion = motion;
        this.temperature = temperature;
        //this.humidity = humidity;
        //this.time = time;
    }

//    public long getDate() {
//        return date;
//    }
//
//    public void setDate(long date) {
//        this.date = date;
//    }
//
//    public long getTime() {
//        return time;
//    }
//
//    public void setTime(long time) {
//        this.time = time;
//    }
//
//    public long getHumidity() {
//        return humidity;
//    }
//
//    public void setHumidity(long humidity) {
//        this.humidity = humidity;
//    }

    public String getMotion() {
        if (motion == null || motion.isEmpty()) {
            return "no"; // we return 0 by default if we have nothing else to return
        }
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    public String getDistance() {
        if (distance == null || distance.isEmpty()) {
            return "0"; // we return 0 by default if we have nothing else to return
        }
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}