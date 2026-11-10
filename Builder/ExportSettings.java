package Builder;

public class ExportSettings {
    private final String resolution;
    private final String format;
    private final boolean includeWatermark;
    private final int compressionQuality; // percentage 1-100
    private final String destinationPath;

    ExportSettings(ExportSettingsBuilder builder) {
        this.resolution=builder.resolution;
        this.format=builder.format;
        this.includeWatermark=builder.includeWatermark;
        this.compressionQuality=builder.compressionQuality;
        this.destinationPath=builder.destinationPath;
    }

    public String getResolution() {
        return resolution;
    }

    public String getFormat() {
        return format;
    }

    public boolean isIncludeWatermark() {
        return includeWatermark;
    }

    public int getCompressionQuality() {
        return compressionQuality;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    @Override
    public String toString() {
        return "ExportSettings{resolution='"+resolution+"',format='"+format+
                "',watermark="+includeWatermark+",quality="+compressionQuality+
                "%,destination='"+destinationPath+"'}";
    }
}