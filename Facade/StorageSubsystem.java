package Facade;

public class StorageSubsystem {
    private final double capacityMb;
    private double usedMb=0;

    public StorageSubsystem(double capacityMb) {
        this.capacityMb=capacityMb;
    }

    public boolean checkDiskSpace(double requiredMb) {
        boolean enough=usedMb+requiredMb<=capacityMb;
        System.out.println("[Storage]Checking disk space:need"+requiredMb+"MB,free"
                +getFreeSpaceMb()+"MB->"+(enough?"OK":"NOT ENOUGH"));
        return enough;
    }

    public void saveToDisk(String fileName,double sizeMb) {
        usedMb+= sizeMb;
        System.out.println("[Storage]Saving file'"+fileName+"'("+sizeMb+"MB) to persistent local storage.");
    }

    public double getFreeSpaceMb() {
        return capacityMb-usedMb;
    }
}