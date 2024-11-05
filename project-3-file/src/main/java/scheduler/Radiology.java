package scheduler;

/**
 * This enum represents the different Radiology types.
 * It defines a method to validate if a given string is a valid radiology service.
 * Services include CATSCAN, ULTRASOUND, and XRAY.
 * @author Keshav Dave, Daniel Watson
 */
public enum Radiology {
    CATSCAN,ULTRASOUND,XRAY;

    /**
     * finds out if a service is a valid imaging service
     * @param service service to check
     */
    public static void isValidImaging(String service) throws Exception{
        boolean foundService = false;
        for(Radiology imagingService : Radiology.values()){
            if (imagingService.name().equalsIgnoreCase(service)) {
                foundService = true;
                break;
            }
        }
        if(!foundService)
            throw new Exception(service + " - imaging service not provided.");
    }
}