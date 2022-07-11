public class Flight {
    private final String source;
    private final String destination;
    private final String distanceInKm;
    private final String flightTime;
    private final String airFare;
    public Flight(String source, String destination, String distanceInKm, String flightTime, String airFare)
    {
        this.source = source;
        this.destination = destination;
        this.distanceInKm = distanceInKm;
        this.flightTime = flightTime;
        this.airFare = airFare;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDistanceInKm() {
        return distanceInKm;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public String getAirFare() {
        return airFare;
    }
    public String toString()
    {

        return String.format("%13s%15s%12s%24s%32s\n",getSource(),getDestination(),getDistanceInKm(),getFlightTime(),getAirFare());
    }
    public String data()
    {
        return getSource()+","+getDestination()+","+getDistanceInKm()+","+getFlightTime()+","+getAirFare();
    }
}
