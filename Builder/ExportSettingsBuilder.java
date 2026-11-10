package Builder;

import java.util.Set;

public class ExportSettingsBuilder {
    private static final Set<String> SUPPORTED_FORMATS = Set.of("JPEG","PNG","WEBP","TIFF");

    String resolution="1920x1080";
    String format="JPEG";
    boolean includeWatermark=false;
    int compressionQuality=90;
    String destinationPath="/gallery/exports";

    public ExportSettingsBuilder setResolution(String resolution) {
        if (resolution==null || !resolution.matches("\\d+x\\d+")) {
            throw new IllegalArgumentException("Resolution must look like 1920x1080.");
        }
        this.resolution=resolution;
        return this;
    }

    public ExportSettingsBuilder setFormat(String format) {
        if (format==null || !SUPPORTED_FORMATS.contains(format.toUpperCase())) {
            throw new IllegalArgumentException("Unsupported format.Allowed:"+SUPPORTED_FORMATS);
        }
        this.format=format.toUpperCase();
        return this;
    }

    public ExportSettingsBuilder setIncludeWatermark(boolean includeWatermark) {
        this.includeWatermark=includeWatermark;
        return this;
    }

    public ExportSettingsBuilder setCompressionQuality(int compressionQuality) {
        if (compressionQuality< 1 || compressionQuality>100) {
            throw new IllegalArgumentException("Compression quality must be between 1 and 100.");
        }
        this.compressionQuality=compressionQuality;
        return this;
    }

    public ExportSettingsBuilder setDestinationPath(String destinationPath) {
        if (destinationPath==null || destinationPath.isBlank()) {
            throw new IllegalArgumentException("Destination path cannot be empty.");
        }
        this.destinationPath=destinationPath;
        return this;
    }

    public ExportSettings build() {
        return new ExportSettings(this);
    }
}